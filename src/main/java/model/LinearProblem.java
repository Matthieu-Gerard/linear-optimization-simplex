package model;

import java.util.ArrayList;

import service.SimplexService;

public class LinearProblem {

	SparseMatrix matrixA = null;
	ArrayList<Double> vectorB = null;
	double[] vectorC = null;	// FIXME: mettre une liste
	Objective objectif = Objective.MAXIMIZE;
	
	ArrayList<Variable> listVariables = null;
	/**
	 * cette liste de variables d'ajouter des variables artificielles dans le modèle pour simuler les inéquations geq et eq. 
	 */
	ArrayList<Variable> listArtificialVariables = null;
	ArrayList<ElementMatrix> listArtificialLocation = null;

	private SimplexService simplex = null;

	public LinearProblem() {
		matrixA = new SparseMatrix();
		vectorB = new ArrayList<Double>();
		listVariables = new ArrayList<Variable>();
		listArtificialVariables = new ArrayList<Variable>();
		listArtificialLocation = new ArrayList<ElementMatrix>();
	}
	
	/**
	 * add constraint:  ax <= b 
	 * @param fValue
	 * @param variable
	 * @param boundMax
	 */
	public void addLinearConstraintLeq( double[] fValue , Variable[] variable , double boundMax ) {

		int indexLine = matrixA.getNbLines();
		
		ArrayList<ElementMatrix> line = new ArrayList<ElementMatrix>();
		for (int i=0 ; i<fValue.length ; i++) {
			int colonne = variable[i].getIndex();
			line.add(new ElementMatrix(indexLine, colonne, fValue[i]));
		}

		matrixA.addLine(line);
		vectorB.add(boundMax);
	}
	
	public void addLinearConstraintLeq( double[] fValue , Variable[] variable , Variable variableBoundMax ) {

		double[] fValueBis = new double[fValue.length+1];
		Variable[] variableBis = new Variable[variable.length+1];
		
		for (int i=0 ; i<fValue.length ; i++) {
			fValueBis[i] = fValue[i];
			variableBis[i] = variable[i];
		}
		fValueBis[fValue.length] = -1.0D;
		variableBis[variable.length] = variableBoundMax;
		
		addLinearConstraintLeq(fValueBis, variableBis, 0);
	}
	
	public void addLinearConstraintGeq( double[] fValue , Variable[] variable , Variable variableBoundMax ) {

		double[] fValueBis = new double[fValue.length+1];
		Variable[] variableBis = new Variable[variable.length+1];
		
		for (int i=0 ; i<fValue.length ; i++) {
			fValueBis[i] = fValue[i];
			variableBis[i] = variable[i];
		}
		fValueBis[fValue.length] = -1.0D;
		variableBis[variable.length] = variableBoundMax;
		
		addLinearConstraintGeq(fValueBis, variableBis, 0);
	}
	
	public void addLinearConstraintEq( double[] fValue , Variable[] variable , Variable variableBoundMax ) {

		double[] fValueBis = new double[fValue.length+1];
		Variable[] variableBis = new Variable[variable.length+1];
		
		for (int i=0 ; i<fValue.length ; i++) {
			fValueBis[i] = fValue[i];
			variableBis[i] = variable[i];
		}
		fValueBis[fValue.length] = -1.0D;
		variableBis[variable.length] = variableBoundMax;
		
		addLinearConstraintEq(fValueBis, variableBis, 0);
	}
	
	/**
	 * add constraint ax >= b  
	 * @param fValue
	 * @param variable
	 * @param boundMin
	 */
	public void addLinearConstraintGeq( double[] fValue , Variable[] variable , double boundMin ) {
		//
		// this function is based on the equivalence -ax <= -b  <=>  ax >= b
		//
		int indexLine = matrixA.getNbLines();
		//
		// on crée la nouvelle variable pour avoir un degré de liberté
		//
		Variable slackVariable = newVariable("slack"+indexLine); 
		//
		// on crée une nouvelle variable avec un fort coefficient négatif
		//
		Variable artificialVariable = newVariable("leq"+indexLine);
		listArtificialVariables.add(artificialVariable);
		//
		// on crée la ligne normalement 
		//
		ArrayList<ElementMatrix> line = new ArrayList<ElementMatrix>();
		for (int i=0 ; i<fValue.length ; i++) {
			int colonne = variable[i].getIndex();
			line.add(new ElementMatrix(indexLine, colonne, fValue[i]));
		}
		//
		// on ajoute la variable de slack
		//
		int colonne = slackVariable.getIndex();
		ElementMatrix slackElement = new ElementMatrix(indexLine, colonne, -1.0);
		line.add(slackElement);
		
		//
		// on ajoute la variable artificielle
		//
		colonne = artificialVariable.getIndex();
		ElementMatrix artificialElement = new ElementMatrix(indexLine, colonne, 1.0);
		line.add(artificialElement);
		listArtificialLocation.add(artificialElement);
		
		matrixA.addLine(line);
		vectorB.add(boundMin);
	}
	/**
	 * add constraint equality Ax = b
	 * @param fValue
	 * @param variable
	 * @param sumValue
	 */
	public void addLinearConstraintEq( double[] fValue , Variable[] variable , double sumValue ) {
		//
		// this function is based on the equivalence -ax <= -b  <=>  ax >= b
		//
		int indexLine = matrixA.getNbLines();
		//
		// on crée une nouvelle variable avec un fort coefficient négatif
		//
		Variable artificialVariable = newVariable("leq"+indexLine);
		listArtificialVariables.add(artificialVariable);
		//
		// on crée la ligne normalement comme 
		//
		ArrayList<ElementMatrix> line = new ArrayList<ElementMatrix>();
		for (int i=0 ; i<fValue.length ; i++) {
			int colonne = variable[i].getIndex();
			line.add(new ElementMatrix(indexLine, colonne, fValue[i]));
		}
		int colonne = artificialVariable.getIndex();
		ElementMatrix artificialElement = new ElementMatrix(indexLine, colonne, 1.0);
		line.add(artificialElement);
		listArtificialLocation.add(artificialElement);
		
		matrixA.addLine(line);
		vectorB.add(sumValue);
	}

	public void setMinimizeObjective() {
		objectif = Objective.MINIMIZE;
	}

	public void setMaximizeObjective() {
		objectif = Objective.MAXIMIZE;
	}

	public void addLinearObjectif(double[] fValue , Variable[] variable) {
		vectorC = new double[listVariables.size()];
		for(int i=0 ; i<fValue.length ; i++) {
			vectorC[ variable[i].getIndex() ] = fValue[i];
		}
		for (int index=0 ; index<listArtificialVariables.size() ; index++) {
			Variable var = listArtificialVariables.get(index);
			vectorC[ var.getIndex() ] = -SimplexService.BIG_M;
		}
	}

	public void solve() {

		if (objectif==Objective.MINIMIZE) {
			for (int index=0 ; index<vectorC.length ; index++) {
				vectorC[index] = -vectorC[index];
			}
		}
		
		double[] b = new double[matrixA.getNbLines()];
		for (int i=0 ; i<matrixA.getNbLines() ; i++) {
			b[i] = vectorB.get(i);
		}
		simplex = new SimplexService(matrixA, b, vectorC, listArtificialLocation);

		try {
            simplex.solve();
        } catch (Exception e) {
            e.printStackTrace();
        }
		System.out.println("variable artificial = "+simplex.getPrimalValue(listArtificialVariables.get(0).getIndex()));
	}

	public Variable newVariable(String nom) {
		Variable variable = new Variable();
		variable.setName(nom);
		variable.setIndex(listVariables.size());
		listVariables.add(variable);
		return variable;
	}

	public boolean checkOptimality() {

		double[] b = new double[matrixA.getNbLines()];
		for (int i=0 ; i<matrixA.getNbLines() ; i++) {
			b[i] = vectorB.get(i);
		}
		return simplex.checkSolution(matrixA, b, vectorC);
	}

	public double getValue(Variable variable) {
		return simplex.getPrimalValue(variable.getIndex());
	}

	public double getObjectiveValue() {
		return simplex.value();
	}

	public ArrayList<Variable> getListVariables() {
		return listVariables;
	}
}
