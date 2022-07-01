package service;

import java.util.ArrayList;

import exception.UnboundedLinearProblemException;
import model.ElementMatrix;
import model.SparseMatrix;

/**
 * 
 * Ce simplex permet de gérer les problématique de type :
 * 
 *       maximize   f(x) = c . x
 *       ss contr.  Ax <= b
 * 
 * where 0 <= b
 */

public class SimplexService {

	public static final double EPSILON = 1.0E-10;
	/**
     * Number of variables, usually named N.
     */
    private int numberOfVariables;
	/**
	 * Number of constraints, usually named M.
	 */
	private int numberOfConstraints;
	/**
	 * tableau [M+1][N+M+1] de déroulement de l'algorithme se compose de 2 matrices + 2 vecteurs
	 * + valeur de la fonction coût 
	 * 
	 * (1) la matrice [1 ... M][1 ... N] représentant les variables en base (initialement la matrice A).
	 * (2) la matrice [1 ... M][1 ... M] représentant les variables hors base (initialement la matrice Identité).
	 * (3) le vecteur [M][1 ... N] représentant les coût réduits (initialement le vecteur b).
	 * (4) la dernière ligne [M][1 ... N+M] représente les coûts d'introduction (initialement le vecteur c).
	 * (5) la case [M+1][N+M+1] représente la valeur courante de la fonction coût que l'on cherche à maximiser.
	 */
	private SparseMatrix matrixA;
	/**
	 * Ce vecteur permet de connaître les variables courantes appartenant à la base (les autres étant nulles).
	 * 1 ... N : correspond à des variables de décisions.
	 * N+1 ... N+M : correspond à des variables d'écarts.
	 */
	private int[] variablesEnBase;    // basis[i] = basic variable corresponding to row i
	// only needed to print out solution, not book
	
	/**
	 * nombre pour gérer les inégalité
	 */
	public static final double BIG_M = 1e8;
	private double initialObjectiveValue = 0;

	/**
	 * Cette fonction permet d'initialiser l'algorithme du Simplexe et de lancer l'exécution du calcul.
	 * 
	 * @param A matrix
	 * @param b second member of equation
	 * @param c cost
	 */
	public SimplexService(SparseMatrix A, double[] b, double[] c, ArrayList<ElementMatrix> artificialVariable) {

		// récupération du nombre de contraintes.
		numberOfConstraints = b.length;
		// récupération du nombre de variables.
		numberOfVariables = c.length;
		// création du tableau permettant le déroulement de l'algorithme.
		matrixA = new SparseMatrix(); //new double[M+1][N+M+1];
		for (int i = 0; i < numberOfConstraints; i++) {
			ArrayList<ElementMatrix> line = A.cloneLine(i);
			matrixA.addLine(line);
			//            for (int j = 0; j < N; j++) {
			//                a[i][j] = A[i][j];
			//            }
		}
		// recopie de la matrice identité (création des variables d'écarts)
		for (int i = 0; i < numberOfConstraints; i++) {
			matrixA.add(new ElementMatrix(i, numberOfVariables+i, 1.0));
			//        	a[i][N+i] = 1.0;
		}
		// recopie des coûts de chaque variable
		ArrayList<ElementMatrix> listCosts = new ArrayList<ElementMatrix>();
		for (int j = 0; j < numberOfVariables; j++) {
			listCosts.add(new ElementMatrix(numberOfConstraints, j, c[j]));
			//        	a[M][j]   = c[j];
		}
		listCosts.add(new ElementMatrix(numberOfConstraints,numberOfConstraints+numberOfVariables,0.0D));
		matrixA.addLine(listCosts);

		// recopie du vecteur b
		for (int i = 0; i < numberOfConstraints; i++) {
			matrixA.add(new ElementMatrix(i, numberOfConstraints+numberOfVariables, b[i]));
			//        	a[i][M+N] = b[i];
		}
		//
		// initialisation des variables en base (les variables d'écart)
		//
		variablesEnBase = new int[numberOfConstraints];
		for (int i = 0; i < numberOfConstraints; i++) {
			variablesEnBase[i] = numberOfVariables + i;
		}
		//
		// prise en compte des variables artificielles ajoutées pour gérer les contraintes d'égalité et de geq
		//
		if (artificialVariable!=null)
		for (ElementMatrix artificialElement : artificialVariable) {
			int ligne = artificialElement.getLine();
			for (ElementMatrix elementInContrainte : matrixA.getLine(ligne)) {
				
				int colonne = elementInContrainte.getColumn();
				if (colonne<numberOfVariables || colonne==numberOfConstraints+numberOfVariables) {
					
					ElementMatrix elementLigneCost = matrixA.getElementMatrix(numberOfConstraints,colonne);
					double value = elementLigneCost.getValue()+BIG_M*elementInContrainte.getValue();
					elementLigneCost.setValue(value);
				}
			}
		}
		initialObjectiveValue = value();
	}

	/**
	 * Cette procédure permet de lancer le simplexe à partir d'une solution réalisable (BFS: basic feasible solution).
	 */
	public void solve() throws Exception {

		while (true) {
			
			//
			// recherche de la colonne (variable) à faire entrer dans la base.
			//
			int q = bland();
			// si pas de colonne trouvée, alors la solution est optimale, on stoppe l'algorithme.
			if (q == -1) break;
			//
			// recherche de la colonne (variable) qui va quitter la base.
			//
			int p = minRatioRule(q);
			// si pas de colonne trouvée, alors c'est que le programme linéaire n'est pas borné
			// et ne peux donc pas être résolu.
			if (p == -1) throw new UnboundedLinearProblemException();
			//
			// on pivote les deux colonne dans le tableau en mettant à jour les coefficients
			//
			pivot(p, q);
			//
			// on met à jour la table des variables en base
			//
			variablesEnBase[p] = q;
		}
	}

	/**
	 * Cette fonction retourne la première colonne/variable (hors base) avec un coût courant positif. Cette
	 * sélection a été developpée par R. G Bland pour éviter les cycles lors de la résolution du Simplexe.
	 * @return
	 */
	private int bland() {
		for (ElementMatrix element : matrixA.getLine(numberOfConstraints)) {
			if (element.getColumn()<numberOfConstraints+numberOfVariables && 0<element.getValue()) {
				return element.getColumn();
			}
		}

		//    	for (int j = 0; j < M + N; j++) {
		//    		if (a[M][j] > 0) {
		//    			return j;
		//    		}
		//    	}
		// si on ne trouve de colonne avec un coût positif,
		// la solution optimale a été trouvée.
		return -1;  // optimal
	}

	/**
	 * Cette fonction retourne la colonne/variable (hors base) avec le coût courant positif le plus élevé. Cette
	 * sélection est l'originale développée par Dantzig.
	 * @return
	 */
	private int dantzig() {

		ElementMatrix q = matrixA.getElementMatrix(numberOfConstraints, 0);
		//    	q = 0;

		for (ElementMatrix element : matrixA.getLine(numberOfConstraints)) {
			if (element.getColumn()<numberOfConstraints+numberOfVariables && element.getValue()>q.getValue()) {
				q = element;
			}
		}
		//        for (int j = 1; j < M + N; j++) {
		//            if (a[M][j] > a[M][q]) q = j;
		//        }
		// si on ne trouve de colonne avec un coût positif,
		// la solution optimale a été trouvée.
		if (q.getValue() <= 0){
			return -1;
		}
		//        if (a[M][q] <= 0){
		//        	return -1;
		//        }
		else return q.getColumn();
	}

	/**
	 * sélection de la variable à faire sortir de la base (connaissant la variable à
	 * faire rentrer en base).
	 * @param q
	 * @return
	 */
	// find row p using min ratio rule (-1 if no such row)
	private int minRatioRule(int q) {

		ElementMatrix p = null;
		//  	int p = -1;

		// TODO: optimiser ce code en le séparant en deux parties : une initialisation + une recherche.
		for (int i = 0; i < numberOfConstraints; i++) {

			ElementMatrix element = matrixA.getElementMatrix(i, q);

			if (element!=null) {

				// si le coefficient est négatif, alors on ne sélectionne pas cette ligne.
				if (element.getValue() <= 0) {
					continue;
				}
				//      		if (a[i][q] <= 0) continue;
				// sinon on sélectionne cette ligne/variable par défaut (si aucune variable sélectionnée).
				else if (p==null) {
					p = element;
				}
				//        		else if (p == -1) p = i;
				//
				// on sélectionne la variable/ligne avec le ratio (coût réduit) positif le plus petit. 
				//
				else {
					// on recupere les elements
					ElementMatrix A_i_MplusN = matrixA.getElementMatrix(i,numberOfConstraints+numberOfVariables);
					ElementMatrix A_i_q = element;
					ElementMatrix A_p_MplusN = matrixA.getElementMatrix(p.getLine(),numberOfConstraints+numberOfVariables);
					ElementMatrix A_p_q = p;

					double value_p_MplusN = 0.0D;
					if (A_p_MplusN!=null) {
						value_p_MplusN = A_p_MplusN.getValue();
					}

					double value_i_MplusN = 0.0D;
					if (A_i_MplusN!=null) {
						value_i_MplusN = A_i_MplusN.getValue();
					}


					if ( (value_i_MplusN/A_i_q.getValue()) < (value_p_MplusN/A_p_q.getValue()) ) { 
						p = A_i_q;
					}
				}
				//        		else if ((a[i][M+N] / a[i][q]) < (a[p][M+N] / a[p][q])) p = i;
			}
		}
		if (p==null) {
			return -1;
		}
		return p.getLine();
		//		return p;
	}

	// pivot on entry (p, q) using Gauss-Jordan elimination
	private void pivot(int p, int q) {

		ElementMatrix A_p_q = matrixA.getElementMatrix(p, q);

		// everything but row p and column q
		for (int i = 0; i <= numberOfConstraints; i++) {

			ArrayList<ElementMatrix> ligne = new ArrayList<ElementMatrix>();
			ElementMatrix A_line_q = matrixA.getElementMatrix( i , q );
			if (A_line_q!=null) {
				for (int j = 0; j <= numberOfConstraints + numberOfVariables; j++) {
					if (i != p && j != q) {
						
						ElementMatrix A_p_j = matrixA.getElementMatrix( p , j );
						if (A_p_j!=null) {
							ElementMatrix A_i_j = matrixA.getElementMatrix( i , j );
							double dValue = 0.0D;
							if (A_i_j!=null) {
								dValue = A_i_j.getValue();
							}
							dValue -= A_p_j.getValue() * A_line_q.getValue() / A_p_q.getValue();

							// si la valeur n'est pas nulle, on l'ajoute à la ligne
							if (dValue!=0.0D || j==numberOfConstraints+numberOfVariables) {
								ligne.add(new ElementMatrix(i, j, dValue ));
							}
						} else {
							ElementMatrix A_line_j = matrixA.getElementMatrix( i , j );
							if (A_line_j!=null) {
								ligne.add( A_line_j );
							}
						}
					} else {

						if (i==p) {
							ligne = matrixA.cloneLine(i);
						} else if (j==q && i!=p) {

							ElementMatrix A_i_j = matrixA.getElementMatrix( i , j );
							if (A_i_j != null) {
								ligne.add(A_i_j);
							}
						} else if (j==q && i==p) {
							ligne.add(A_p_q);
						}
					}
				} 
			} else {
				ligne = matrixA.cloneLine(i);
			}
			matrixA.setLine(i, ligne);
		}
		//		for (int i = 0; i <= M; i++) {
		//			for (int j = 0; j <= M + N; j++) {
		//				if (i != p && j != q) {
		//					a[i][j] -= a[p][j] * a[i][q] / a[p][q];
		//				}
		//			}
		//		}

		// zero out column q
		for (int i = 0; i <= numberOfConstraints; i++) {
			if (i != p) {
				matrixA.deleteElementMatrix(i, q);
				//				a[i][q] = 0.0;
			}
		}

		// scale row p
		for (ElementMatrix element : matrixA.getLine(p)) {
			if (element.getColumn() != q) {
				element.setValue((element.getValue() / A_p_q.getValue()));
			}
		}

		//		for (int j = 0; j <= M + N; j++) {
		//			if (j != q) {
		//				a[p][j] /= a[p][q];
		//			}
		//		}

		//		A_p_q.setValue(1.0D);
		matrixA.getElementMatrix(p, q).setValue(1.0D);
		//		a[p][q] = 1.0;
	}

	/**
	 * cette fonction retourne la valeur de la fonction coût courante évoluant au fur 
	 * et à mesure du bon déroulement de l'algorithme du simplexe.
	 * @return
	 */
	public double value() {
		return -matrixA.getLastElementMatrixOfLine(numberOfConstraints).getValue();
		//		return -a[M][M+N];
	}

	/**
	 * retourne les valeurs des variables de décision du problème primal
	 * @return
	 */
	public double[] primal() {
		double[] x = new double[numberOfVariables];
		for (int i = 0; i < numberOfConstraints; i++) {
			// si la variable en base est une variable de décision (et non une variable d'écart).
			if (variablesEnBase[i] < numberOfVariables) {
				x[variablesEnBase[i]] = matrixA.getLastElementMatrixOfLine(i).getValue();
				//				x[variablesEnBase[i]] = a[i][M+N];
			}
		}
		return x;
	}

	// a réparer
	public double getPrimalValue(int index) {
		if (index<numberOfVariables) {
			for(int i=0 ; i<variablesEnBase.length ; i++) {
				if (index == variablesEnBase[i]) {
					return matrixA.getLastElementMatrixOfLine(i).getValue();
				}
			}
		}
		return 0;
	}

	/**
	 * retourne les valeurs des variables de décision du problème dual
	 * @return
	 */
	public double[] dual() {
		double[] y = new double[numberOfConstraints];
		for (int i = 0; i < numberOfConstraints; i++) {
			ElementMatrix A_M_Nplusi = matrixA.getElementMatrix(numberOfConstraints,numberOfVariables+i);
			if (A_M_Nplusi!=null) {
				y[i] = -A_M_Nplusi.getValue();	
			} else {
				y[i] = 0;
			}
			//			y[i] = -a[M][N+i];
		}
		return y;
	}

	/**
	 * fonction qui vérifie si la solution en cours du problème primal est réalisable 
	 * @param A
	 * @param b
	 * @return
	 */
	private boolean isPrimalFeasible(SparseMatrix A, double[] b) {
		double[] x = primal();
		//
		// on vérifie si x>=0
		//
		for (int j = 0; j < x.length; j++) {
			if (x[j] < 0.0) {
				System.out.println("x[" + j + "] = " + x[j] + " is negative");
				return false;
			}
		}
		//
		// on vérifie si Ax <= b
		//
		for (int i = 0; i < numberOfConstraints; i++) {
			double sum = 0.0;
			for (ElementMatrix element : A.getLine(i)) {
				sum += element.getValue() * x[element.getColumn()];
			}
			//			for (int j = 0; j < N; j++) {
			//				sum += A[i][j] * x[j];
			//			}
			if (sum > b[i] + EPSILON) {
				System.out.println("not primal feasible");
				System.out.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
				return false;
			}
		}
		return true;
	}

	/**
	 * fonction qui vérifie si la solution en cours du problème dual est réalisable 
	 * @param A
	 * @param b
	 * @return
	 */
	private boolean isDualFeasible(SparseMatrix A, double[] c) {

		double[] y = dual();
		//
		// on vérifie si y >= 0
		//
		for (int i = 0; i < y.length; i++) {
			if (y[i] < 0.0) {
				System.out.println("y[" + i + "] = " + y[i] + " is negative");
				return false;
			}
		}
		//
		// on vérifie si yA >= c
		//
		for (int j = 0; j < numberOfVariables; j++) {
			double sum = 0.0;
			for (int i = 0; i < numberOfConstraints; i++) {
				ElementMatrix element = A.getElementMatrix(i,j);
				if (element!=null) {
					sum += element.getValue() * y[i];
				}
			}
			sum -= initialObjectiveValue;
			//			for (int i = 0; i < M; i++) {
			//				sum += A[i][j] * y[i];
			//			}
			if (sum < c[j] - EPSILON) {
				System.out.println("not dual feasible");
				System.out.println("c[" + j + "] = " + c[j] + ", sum = " + sum);
				return false;
			}
		}
		return true;
	}

	/**
	 * Cette fonction vérifie si la solution obtenue est obtenue (condition si cx = yb)
	 * @param b
	 * @param c
	 * @return
	 */
	private boolean isOptimal(double[] b, double[] c) {
		double[] x = primal();
		double[] y = dual();
		// récupération de la valeur de la fonction coût obtenu après déroulement de l'algorithme
		double value = value();
		// calcul de cx
		double value1 = 0.0;
		for (int j = 0; j < x.length; j++) {
			value1 += c[j] * x[j];
		}
		// calcul de yb
		double value2 = initialObjectiveValue;
		for (int i = 0; i < y.length; i++) {
			value2 += y[i] * b[i];
		}
		
		// test d'optimalité
		if (Math.abs(value - value1) > EPSILON || Math.abs(value - value2) > EPSILON) {
			System.out.println("value = " + value + ", cx = " + value1 + ", yb = " + value2);
			return false;
		}

		return true;
	}

	/**
	 * Cette fonction vérifie si les conditions d'optimalité ont bien été respectée à savoir
	 * (1) la solution courante du problème primal est réalisable.
	 * (2) la solution courante du problème dual est réalisable.
	 * (3) l'égalite cx = yb.
	 * 
	 * @param A
	 * @param b
	 * @param c
	 * @return
	 */
	public boolean checkSolution(SparseMatrix A, double[] b, double[] c) {
		return isPrimalFeasible(A, b) && isDualFeasible(A, c) && isOptimal(b, c);
	}

	// print tableaux
	public void show() {
		System.out.println("M = " + numberOfConstraints);
		System.out.println("N = " + numberOfVariables);
		for (int i = 0; i <= numberOfConstraints; i++) {
			for (int j = 0; j <= numberOfConstraints + numberOfVariables; j++) {
				ElementMatrix A_i_j = matrixA.getElementMatrix(i, j);
				if (A_i_j==null) {
					System.out.printf("%7.1f- ", 0.0);
				} else {
					System.out.printf("%7.2f ", A_i_j.getValue());
				}
				//				System.out.printf("%7.2f ", a[i][j]);
			}
			System.out.println();
		}
		System.out.println("value = " + value());
//		for (int i = 0; i < M; i++)
//			if (variablesEnBase[i] < N) {
//				System.out.println("x_" + variablesEnBase[i] + " = " + a.getLastElementMatrixOfLine(i).getValue());
//				//				System.out.println("x_" + variablesEnBase[i] + " = " + a[i][M+N]);
//			}
//		System.out.println();
	}

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public int getNumberOfConstraints() {
        return numberOfConstraints;
    }

    public SparseMatrix getMatrixA() {
        return matrixA;
    }
}