package service.simplex;

import java.util.ArrayList;
import java.util.List;

import exception.UnboundedLinearProblemException;
import model.LinearModel;
import model.SimplexConstants;
import model.constraint.LinearConstraintType;
import model.constraint.LinearConstraint;
import model.matrix.ElementMatrix;
import model.matrix.SparseMatrix;

/**
 * Simplex algorithm solves linear problem modeled as : 
 * 
 *       maximize   f(x) = c . x
 *       ss contr.  Ax <= b
 * 
 *  where 0 <= b
 */

public class SimplexSolverService {
    /**
     * Number of variables, usually named N.
     */
    private int numberOfVariables;
    /**
     * Number of artificial variables
     */
    private int numberOfArtificialVariables;
    /**
     * Number of slack variables
     */
    private int numberOfSlackVariables;
    /**
     * Number of constraints, usually named M.
     */
    private int numberOfConstraints;
    /**
     * Linear costs [1...N].
     */
    private double[] costs;
    /**
     * Vector B = second member, maximum value for a constraint [1...N].
     */
    private double[] vectorB;
    /**
     * Constraint matrix A [1...M][1...N].
     */
    private SparseMatrix matrixA;
    /**
     * Tableau [M+1][N+M+1] where Simplex algorithm is executed. 
     * 
     * It contains 2 matrices, 2 vectors and the value of the cost function. 
     * 
     * (1) Matrix [1 ... M][1 ... N] represents the basis variables (initially matrix A).
     * (2) Matrix [M ... 2M][1 ... M] represents the out-basis slack variables (initially Identity matrix).
     * (3) Vector [M][1 ... N] represents the reduced costs (initially vector b).
     * (4) last line [M][1 ... N+M] represents the introduction costs (initially vector c).
     * (5) Cell [M+1][N+M+1] represents the current value of the cost function that we try to minimize.
     */
    private SparseMatrix tableau;
    /**
     * This vector lists the current variable indices in basis (variables not listed here are null).
     * In the tableau : 
     *     1 ... N : corresponds to problem decision variables.
     *   N+1 ... N+M : corresponds to slack variables.
     */
    private int[] variablesEnBase;   

    private ColumnSelection columnSelection = new BlandColumnSelection();

    private double initialObjectiveValue = 0;
    
    private List<Boolean> constraintHasAnArtificialVariable;

    /**
     * This function initializes the Simplex tableau.
     * 
     * @param A matrix 
     * @param b second member of equation
     * @param c cost
     */
    public SimplexSolverService(SparseMatrix A, double[] b, double[] c, ArrayList<ElementMatrix> artificialVariables) {

        numberOfConstraints = b.length;
        numberOfVariables = c.length;

        costs = c;
        vectorB = b;
        matrixA = A;
        
        constraintHasAnArtificialVariable = new ArrayList<>(); 
        for (int index=0 ; index<b.length ; index++) {
            constraintHasAnArtificialVariable.add(false);
        }

        // Simplex tableau initialization.
        tableau = new SparseMatrix();
        for (int i = 0; i < numberOfConstraints; i++) {
            ArrayList<ElementMatrix> line = A.cloneLine(i);
            tableau.addLine(line);
            //            for (int j = 0; j < N; j++) {
            //                a[i][j] = A[i][j];
            //            }
        }
        // Copy of identity matrix (=slack variables design)
        for (int i = 0; i < numberOfConstraints; i++) {
            tableau.add(new ElementMatrix(i, numberOfVariables+i, +1.0));    
            //        	a[i][N+i] = 1.0;
        }
        // Copy of vector c.
        ArrayList<ElementMatrix> listCosts = new ArrayList<ElementMatrix>();
        for (int j = 0; j < numberOfVariables; j++) {
            listCosts.add(new ElementMatrix(numberOfConstraints, j, c[j]));
            //        	a[M][j]   = c[j];
        }

        listCosts.add(new ElementMatrix(numberOfConstraints,numberOfConstraints+numberOfVariables,0.0D));
        tableau.addLine(listCosts);

        // Variables initialization in base (= slack variables).
        numberOfSlackVariables = numberOfConstraints;
        variablesEnBase = new int[numberOfSlackVariables];
        for (int i = 0; i < numberOfSlackVariables; i++) {
            variablesEnBase[i] = numberOfVariables + i;
        }

        // Add artificial variables to deal with "equality" and "greater than" constraints.
        numberOfArtificialVariables = 0;

        // Copy of vector b.
        int shiftIndex = numberOfVariables+numberOfArtificialVariables+numberOfSlackVariables;
        for (int i = 0; i < b.length; i++) {
            tableau.add(new ElementMatrix(i, shiftIndex, b[i]));
            //          a[i][M+N] = b[i];
        }

        initialObjectiveValue = costFunctionvalue();
    }

    /**
     * This function initializes the Simplex tableau.
     * 
     * @param A matrix 
     * @param b second member of equation
     * @param c cost
     */
    public SimplexSolverService(LinearModel linearModel) {

        numberOfConstraints = linearModel.getListOfConstraints().size();
        numberOfVariables = linearModel.getListOfVariables().size();

        costs = linearModel.generateVectorC();
        vectorB = linearModel.generateVectorB();
        matrixA = linearModel.generateMatrixA();
        
        constraintHasAnArtificialVariable = new ArrayList<>(); 
        for (int index=0 ; index<vectorB.length ; index++) {
            constraintHasAnArtificialVariable.add(false);
        }

        // Simplex tableau initialization.
        tableau = new SparseMatrix();
        for (int i = 0; i < numberOfConstraints; i++) {
            LinearConstraint constraint = linearModel.getListOfConstraints().get(i);

            ArrayList<ElementMatrix> line = new ArrayList<>();
            for (int index=0; index<constraint.getCoefficients().size() ; index++) {
                double coefficient = constraint.getCoefficients().get(index);
                int indexVariable = constraint.getVariables().get(index).getIndex();

                ElementMatrix element = new ElementMatrix(i, indexVariable, coefficient);
                line.add(element);
            }
            tableau.addLine(line);
            //            for (int j = 0; j < N; j++) {
            //                a[i][j] = A[i][j];
            //            }
        }

        // artificial variables
        numberOfArtificialVariables = 0;
        for (int i = 0; i < numberOfConstraints; i++) {
            LinearConstraint constraint = linearModel.getListOfConstraints().get(i);
            if (constraint.getSymbol()==LinearConstraintType.GEQ) {
                tableau.add(new ElementMatrix(i, numberOfVariables+numberOfArtificialVariables, 1.0));
                constraintHasAnArtificialVariable.set(i, true);
                numberOfArtificialVariables ++;
            }
//            else if (constraint.getSymbol()==LinearConstraintType.EQ) {
//                tableau.add(new ElementMatrix(i, numberOfVariables+numberOfArtificialVariables, 1.0));
//                numberOfArtificialVariables ++;
//            }
        }

        // Copy of identity matrix (=slack variables design)
        // a[i][N+i] = 1.0;
        numberOfSlackVariables = 0;
        for (int i = 0; i < numberOfConstraints; i++) {
            LinearConstraint constraint = linearModel.getListOfConstraints().get(i);
            if (constraint.getSymbol()==LinearConstraintType.LEQ) {
                tableau.add(new ElementMatrix(i, numberOfVariables+numberOfArtificialVariables+numberOfSlackVariables, +1.0));
                numberOfSlackVariables ++;
            }
            else if (constraint.getSymbol()==LinearConstraintType.GEQ) {
                tableau.add(new ElementMatrix(i, numberOfVariables+numberOfArtificialVariables+numberOfSlackVariables, -1.0));
                numberOfSlackVariables ++;
            }
//            else if (constraint.getSymbol()==LinearConstraintType.EQ) {
//                tableau.add(new ElementMatrix(i, numberOfVariables+numberOfArtificialVariables+numberOfSlackVariables, -1.0));
//                numberOfSlackVariables ++;
//            }
        }
        // Copy of vector c.
        ArrayList<ElementMatrix> listCosts = new ArrayList<ElementMatrix>();
        for (int j = 0; j < numberOfVariables; j++) {
            double cost = linearModel.getListOfCosts().get(j);
            listCosts.add(new ElementMatrix(numberOfConstraints, j, cost));
            //          a[M][j]   = c[j];
        }
        for (int j = 0; j < numberOfArtificialVariables; j++) {
            double cost = -SimplexConstants.BIG_M;
            listCosts.add(new ElementMatrix(numberOfConstraints, numberOfVariables+j, cost));
            //          a[M][j]   = c[j];
        }
        listCosts.add(new ElementMatrix(numberOfConstraints,numberOfVariables+numberOfArtificialVariables+numberOfSlackVariables,0.0D));
        tableau.addLine(listCosts);

        // Variables initialization in base (= slack variables indexes).
        variablesEnBase = new int[numberOfConstraints];
        for (int i = 0; i < numberOfConstraints; i++) {
            variablesEnBase[i] = numberOfVariables+numberOfArtificialVariables+ i;
        }

        // Copy of vector b.
        int variablesOffset = numberOfVariables+numberOfArtificialVariables+numberOfSlackVariables;
        for (int i = 0; i < numberOfConstraints; i++) {
            LinearConstraint constraint = linearModel.getListOfConstraints().get(i);
            double value = constraint.getValue();
            tableau.add(new ElementMatrix(i, variablesOffset, value));
            //          a[i][M+N] = b[i];
        }
        
        // taking into account the artificial variables in the objective function.
        for (int i = 0; i < numberOfConstraints; i++) {
            LinearConstraint constraint = linearModel.getListOfConstraints().get(i);
            if (constraint.getSymbol()==LinearConstraintType.GEQ /*|| constraint.getSymbol()==LinearConstraintType.EQ*/) {
                List<ElementMatrix> line = tableau.getLine(i);
                for (ElementMatrix element : line) {
                    int columnIndex = element.getColumnIndex();
                    double coefficient = element.getValue();
                    ElementMatrix target = tableau.getElementMatrix(numberOfConstraints, columnIndex);
                    if (target==null) {
                        tableau.getLine(numberOfConstraints).add(new ElementMatrix(numberOfConstraints, columnIndex, -coefficient*SimplexConstants.BIG_M));
                    }
                    else {
                        double value = target.getValue() + coefficient*SimplexConstants.BIG_M;
                        target.setValue(value);
                    }
                }
            }
        }

        initialObjectiveValue = costFunctionvalue();
    }

    /**
     * Execute the Simplex from a feasible solution (BFS: basic feasible solution).
     */
    public void solve() throws UnboundedLinearProblemException {
        
        while (true) {
            printTableau();
            // Search of the column / variable that goes in the base.
            int q = columnSelection.getColumnIndex(tableau,numberOfVariables,numberOfConstraints);

            // If no found column, then solution is optimal and we stop the algorithm.
            if (q == -1) break;

            // Search the column / variable that goes out of the base.
            int p = minRatioRule(q);

            // If no found column, it means that problem is unbounded (there is no solution).
            if (p == -1) throw new UnboundedLinearProblemException();

            // Pivot over the two columns in the tableau by updating the coefficients.
            pivot(p, q);
            // update the list of variables in the base.
            variablesEnBase[p] = q;
        }
        printTableau();
    }


    /**
     * Select the variable to remove in the base (knowing the one to enter in the base).
     * We find row index p using min ratio rule (-1 if no such row)
     * @param q index of variable to enter in the base.
     * @return p index of variable to remove in the base.
     */
    private int minRatioRule(int q) {

        ElementMatrix p = null;
        //  	int p = -1;

        // FIXME: optimiser ce code en le séparant en deux parties : une initialisation + une recherche.
        for (int i = 0; i < numberOfConstraints; i++) {

            ElementMatrix element = tableau.getElementMatrix(i, q);

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
                    ElementMatrix A_i_MplusN = tableau.getElementMatrix(i,numberOfVariables+numberOfArtificialVariables+numberOfSlackVariables);
                    ElementMatrix A_i_q = element;
                    ElementMatrix A_p_MplusN = tableau.getElementMatrix(p.getLineIndex(),numberOfVariables+numberOfArtificialVariables+numberOfSlackVariables);
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
        return p.getLineIndex();
        //		return p;
    }

    // pivot on entry (p, q) using Gauss-Jordan elimination
    private void pivot(int p, int q) {

        ElementMatrix A_p_q = tableau.getElementMatrix(p, q);

        // everything but row p and column q
        for (int i = 0; i <= numberOfConstraints; i++) {

            ArrayList<ElementMatrix> ligne = new ArrayList<ElementMatrix>();
            ElementMatrix A_line_q = tableau.getElementMatrix( i , q );
            if (A_line_q!=null) {
                for (int j = 0; j <= numberOfVariables+numberOfArtificialVariables+numberOfSlackVariables; j++) {
                    if (i != p && j != q) {
                        ElementMatrix A_p_j = tableau.getElementMatrix( p , j );
                        if (A_p_j!=null) {
                            ElementMatrix A_i_j = tableau.getElementMatrix( i , j );
                            double dValue = 0.0D;
                            if (A_i_j!=null) {
                                dValue = A_i_j.getValue();
                            }
                            dValue -= A_p_j.getValue() * A_line_q.getValue() / A_p_q.getValue();

                            // si la valeur n'est pas nulle, on l'ajoute à la ligne
                            if (dValue!=0.0D || j==numberOfVariables+numberOfArtificialVariables+numberOfSlackVariables) {
                                ligne.add(new ElementMatrix(i, j, dValue ));
                            }
                        } else {
                            ElementMatrix A_line_j = tableau.getElementMatrix( i , j );
                            if (A_line_j!=null) {
                                ligne.add( A_line_j );
                            }
                        }
                    } else {

                        if (i==p) {
                            ligne = tableau.cloneLine(i);
                        } else if (j==q && i!=p) {

                            ElementMatrix A_i_j = tableau.getElementMatrix( i , j );
                            if (A_i_j != null) {
                                ligne.add(A_i_j);
                            }
                        } else if (j==q && i==p) {
                            ligne.add(A_p_q);
                        }
                    }
                } 
            } else {
                ligne = tableau.cloneLine(i);
            }
            tableau.setLine(i, ligne);
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
                tableau.deleteElementMatrix(i, q);
                //				a[i][q] = 0.0;
            }
        }

        // scale row p
        for (ElementMatrix element : tableau.getLine(p)) {
            if (element.getColumnIndex() != q) {
                element.setValue((element.getValue() / A_p_q.getValue()));
            }
        }

        //		for (int j = 0; j <= M + N; j++) {
        //			if (j != q) {
        //				a[p][j] /= a[p][q];
        //			}
        //		}

        //		A_p_q.setValue(1.0D);
        tableau.getElementMatrix(p, q).setValue(1.0D);
        //		a[p][q] = 1.0;
    }

    /**
     * Return the current value of the cost function. It evolves during Simplex resolution.
     */
    public double costFunctionvalue() {
        double costFunction = -tableau.getLastElementMatrixOfLine(numberOfConstraints).getValue();
        return costFunction;
        //		return -a[M][M+N];
    }

    /**
     * Return the current solution values of the primal problem (the solution we are looking for).
     */
    public double[] primalValues() {
        double[] x = new double[numberOfVariables];
        for (int i = 0; i < numberOfConstraints; i++) {
            // si la variable en base est une variable de décision (et non une variable d'écart).
            if (variablesEnBase[i] < numberOfVariables) {
                x[variablesEnBase[i]] = tableau.getLastElementMatrixOfLine(i).getValue();
                //				x[variablesEnBase[i]] = a[i][M+N];
            }
        }
        return x;
    }

    /**
     * Return the current value of decision variable in the primal problem.
     */
    public double primalValues(int index) {
        if (index<numberOfVariables) {
            for(int i=0 ; i<variablesEnBase.length ; i++) {
                if (index == variablesEnBase[i]) {
                    return tableau.getLastElementMatrixOfLine(i).getValue();
                }
            }
        }
        return 0;
    }

    /**
     * Return the current solution values of the dual problem.
     */
    public double[] dualValues() {
        double[] y = new double[numberOfConstraints];
        for (int i = 0; i < numberOfConstraints; i++) {
            ElementMatrix A_M_Nplusi = tableau.getElementMatrix(numberOfConstraints,numberOfVariables+i);
            if (A_M_Nplusi!=null) {
                y[i] = -A_M_Nplusi.getValue();
            } else {
                y[i] = 0;
            }
            //			y[i] = -a[M][N+i];
        }
        return y;
    }

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public int getNumberOfConstraints() {
        return numberOfConstraints;
    }

    public SparseMatrix getTableau() {
        return tableau;
    }

    public ColumnSelection getColumnSelection() {
        return columnSelection;
    }

    public void setColumnSelection(ColumnSelection columnSelection) {
        this.columnSelection = columnSelection;
    }

    public double[] getCosts() {
        return costs;
    }

    public void setCosts(double[] costs) {
        this.costs = costs;
    }

    public double[] getVectorB() {
        return vectorB;
    }

    public void setVectorB(double[] vectorB) {
        this.vectorB = vectorB;
    }

    public SparseMatrix getMatrixA() {
        return matrixA;
    }

    public void setMatrixA(SparseMatrix matrixA) {
        this.matrixA = matrixA;
    }

    public int[] getVariablesEnBase() {
        return variablesEnBase;
    }

    public void setVariablesEnBase(int[] variablesEnBase) {
        this.variablesEnBase = variablesEnBase;
    }

    public double getInitialObjectiveValue() {
        return initialObjectiveValue;
    }

    public void setInitialObjectiveValue(double initialObjectiveValue) {
        this.initialObjectiveValue = initialObjectiveValue;
    }

    public void setNumberOfVariables(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }

    public void setNumberOfConstraints(int numberOfConstraints) {
        this.numberOfConstraints = numberOfConstraints;
    }

    public void setTableau(SparseMatrix tableau) {
        this.tableau = tableau;
    }

    // print tableaux
    public void printTableau() {
        System.out.println("M = " + getNumberOfConstraints());
        System.out.println("N = " + getNumberOfVariables());
        for (int i = 0; i <= getNumberOfConstraints(); i++) {
            for (int j = 0; j <= numberOfVariables+numberOfArtificialVariables+numberOfSlackVariables ; j++) {
                ElementMatrix A_i_j = getTableau().getElementMatrix(i, j);
                if (A_i_j==null) {
                    System.out.printf("%7.1f ", 0.0);
                } else {
                    System.out.printf("%7.2f ", A_i_j.getValue());
                }
                //              System.out.printf("%7.2f ", a[i][j]);
            }
            System.out.println();
        }
        System.out.println("costFunctionvalue = " + costFunctionvalue());
        //      for (int i = 0; i < M; i++)
        //          if (variablesEnBase[i] < N) {
        //              System.out.println("x_" + variablesEnBase[i] + " = " + a.getLastElementMatrixOfLine(i).getValue());
        //              //              System.out.println("x_" + variablesEnBase[i] + " = " + a[i][M+N]);
        //          }
        //      System.out.println();
    }

    public int getNumberOfArtificialVariables() {
        return numberOfArtificialVariables;
    }

    public void setNumberOfArtificialVariables(int numberOfArtificialVariables) {
        this.numberOfArtificialVariables = numberOfArtificialVariables;
    }

    public List<Boolean> getConstraintHasAnArtificialVariable() {
        return constraintHasAnArtificialVariable;
    }

    public void setConstraintHasAnArtificialVariable(List<Boolean> constraintHasAnArtificialVariable) {
        this.constraintHasAnArtificialVariable = constraintHasAnArtificialVariable;
    }
}