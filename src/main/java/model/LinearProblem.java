package model;

import java.util.ArrayList;

import analyser.SimplexSolverAnalyser;
import exception.UnboundedLinearProblemException;
import model.matrix.ElementMatrix;
import model.matrix.SparseMatrix;
import service.SimplexSolverService;

public class LinearProblem {

    Objective objectif = Objective.MAXIMIZE;
    
    ArrayList<Double> vectorC = null;
    SparseMatrix matrixA = null;
    ArrayList<Double> vectorB = null;

    ArrayList<Variable> listVariables = null;
    ColumnSelectionStrategyEnum columnSelectionStrategy = ColumnSelectionStrategyEnum.BLAND;
    /**
     * This variables list contains artificial variables that we use for modelling inequality Geq and Eq. 
     */
    ArrayList<Variable> listArtificialVariables = null;
    ArrayList<ElementMatrix> listArtificialLocation = null;

    private SimplexSolverService simplex = null;

    public LinearProblem() {
        matrixA = new SparseMatrix();
        vectorB = new ArrayList<Double>();
        vectorC = new ArrayList<Double>();
        listVariables = new ArrayList<Variable>();
        listArtificialVariables = new ArrayList<Variable>();
        listArtificialLocation = new ArrayList<ElementMatrix>();
    }

    /**
     * Add constraint:  ax <= b 
     * @param coefficients
     * @param variables
     * @param maximumValue
     */
    public void addLinearConstraintLeq( double[] coefficients , Variable[] variables , double maximumValue ) {

        int indexLine = matrixA.getNumberofLines();

        ArrayList<ElementMatrix> line = new ArrayList<ElementMatrix>();
        for (int i=0 ; i<coefficients.length ; i++) {
            int colonne = variables[i].getIndex();
            line.add(new ElementMatrix(indexLine, colonne, coefficients[i]));
        }

        matrixA.addLine(line);
        vectorB.add(maximumValue);
    }

    /**
     * Add constraint:  somme_i [ a_i . x_i ] <= x_j
     * @param coefficients
     * @param variables
     * @param maximumValueBound
     */
    public void addLinearConstraintLeq( double[] coefficients , Variable[] variables , Variable maximumVariable ) {

        double[] fValueBis = new double[coefficients.length+1];
        Variable[] variableBis = new Variable[variables.length+1];

        for (int i=0 ; i<coefficients.length ; i++) {
            fValueBis[i] = coefficients[i];
            variableBis[i] = variables[i];
        }
        fValueBis[coefficients.length] = -1.0D;
        variableBis[variables.length] = maximumVariable;

        addLinearConstraintLeq(fValueBis, variableBis, 0);
    }
    
    /**
     * Add constraint ax >= b. 
     * This function is based on the equivalence ax >= b <=> -ax <= -b.
     *   
     * @param coefficients
     * @param variables
     * @param minimumValue
     */
    public void addLinearConstraintGeq( double[] coefficients , Variable[] variables , double minimumValue ) {
        
        /**/
        double maximumValue = -minimumValue;
        double[] bufferCoefficients = new double[coefficients.length+1];
        Variable[] bufferVariables = new Variable[coefficients.length+1];
        for (int coefficientIndex=0 ; coefficientIndex<coefficients.length ; coefficientIndex++) {
            bufferCoefficients[coefficientIndex] = -coefficients[coefficientIndex];
            bufferVariables[coefficientIndex] = variables[coefficientIndex];
        }
        bufferVariables[coefficients.length] = this.newVariable("Artificial");
        listVariables.add(bufferVariables[coefficients.length]);
        
        bufferCoefficients[coefficients.length] = ToleranceConstants.BIG_M;
        vectorC.add(bufferCoefficients[coefficients.length]);
        
        this.addLinearConstraintLeq(bufferCoefficients, bufferVariables, maximumValue);
        /**/
        
        /**/
        // Design of the constraint line as usual.
//        int indexLine = matrixA.getNumberofLines();
//        ArrayList<ElementMatrix> line = new ArrayList<ElementMatrix>();
//        for (int i=0 ; i<coefficients.length ; i++) {
//            int colonne = variables[i].getIndex();
//            line.add(new ElementMatrix(indexLine, colonne, -coefficients[i]));
//        }
//        vectorB.add(-minimumValue);
        
        // New slack variable to get a degree of freedom.
//        Variable slackVariable = newVariable("slack"+indexLine); 
//        int colonne = slackVariable.getIndex();
//        ElementMatrix slackElement = new ElementMatrix(indexLine, colonne, 1.0);
//        line.add(slackElement);
        
        /*
        // New artificial variable with zero value.
        int indexLine = matrixA.getNumberofLines()-1;
        Variable surplusVariable = newVariable("geq"+indexLine);
        listArtificialVariables.add(surplusVariable);
        listVariables.add(surplusVariable);
        int colonneIndex = surplusVariable.getIndex();
        ElementMatrix surplusElement = new ElementMatrix(indexLine, colonneIndex, 1.0);
        matrixA.add(surplusElement);
        listArtificialLocation.add(surplusElement);

        vectorC.add(0.0);
        
        // New artificial variable with a high negative value.
//        int indexLine = matrixA.getNumberofLines()-1;
        Variable artificialVariable = newVariable("geq"+indexLine);
        listArtificialVariables.add(artificialVariable);
        listVariables.add(artificialVariable);
        colonneIndex = artificialVariable.getIndex();
        ElementMatrix artificialElement = new ElementMatrix(indexLine, colonneIndex, -1.0);
        matrixA.add(artificialElement);
        listArtificialLocation.add(artificialElement);

        vectorC.add(ToleranceConstants.BIG_M);
        
//        matrixA.addLine(line);
        
        /**/
    }
    /**
     * Add constraint:  somme_i [ a_i . x_i ] >= x_j
     * @param coefficients
     * @param variables
     * @param maximumValueBound
     */
    public void addLinearConstraintGeq( double[] coefficients , Variable[] variables , Variable minimumVariable ) {

        double[] fValueBis = new double[coefficients.length+1];
        Variable[] variableBis = new Variable[variables.length+1];

        // Design of the constraint line as usual.
        for (int i=0 ; i<coefficients.length ; i++) {
            fValueBis[i] = coefficients[i];
            variableBis[i] = variables[i];
        }
        fValueBis[coefficients.length] = -1.0D;
        variableBis[variables.length] = minimumVariable;

        addLinearConstraintGeq(fValueBis, variableBis, 0);
    }
    /**
     * Add constraint:  somme_i [ a_i . x_i ] = x_j
     * @param coefficients
     * @param variables
     * @param exactVariable
     */
    public void addLinearConstraintEq( double[] coefficient , Variable[] variables , Variable exactVariable ) {

        double[] fValueBis = new double[coefficient.length+1];
        Variable[] variableBis = new Variable[variables.length+1];

        for (int i=0 ; i<coefficient.length ; i++) {
            fValueBis[i] = coefficient[i];
            variableBis[i] = variables[i];
        }
        fValueBis[coefficient.length] += -1.0D;
        variableBis[variables.length] = exactVariable;

        addLinearConstraintEq(fValueBis, variableBis, 0);
    }

    /**
     * Add constraint equality Ax = b
     * This function is based on the equivalence -ax <= -b  <=>  ax >= b.
     * 
     * @param coefficients
     * @param variables
     * @param sumValue
     */
    public void addLinearConstraintEq( double[] coefficients , Variable[] variables , double sumValue ) {

        int indexLine = matrixA.getNumberofLines();

        // Design of the constraint line as usual. 
        ArrayList<ElementMatrix> line = new ArrayList<ElementMatrix>();
        for (int i=0 ; i<coefficients.length ; i++) {
            int colonne = variables[i].getIndex();
            line.add(new ElementMatrix(indexLine, colonne, coefficients[i]));
        }

        // New artificial variable with a high negative value.
//        Variable artificialVariable = newVariable("leq"+indexLine);
//        listArtificialVariables.add(artificialVariable);
//        int colonne = artificialVariable.getIndex();
//        ElementMatrix artificialElement = new ElementMatrix(indexLine, colonne, 1.0);
//        line.add(artificialElement);
//        listArtificialLocation.add(artificialElement);

        matrixA.addLine(line);
        vectorB.add(sumValue);
    }

    public void setMinimizeObjective() {
        objectif = Objective.MINIMIZE;
    }

    public void setMaximizeObjective() {
        objectif = Objective.MAXIMIZE;
    }
    /**
     * Add the cost objective function.
     * @param costs
     * @param variables
     */
    public void addLinearObjectif(double[] costs , Variable[] variables) {
        vectorC = new ArrayList<>();
        for(int i=0 ; i<costs.length ; i++) {
            vectorC.add(0.0);
        }
        
        for(int index=0 ; index<variables.length ; index++) {
            vectorC.set(variables[index].getIndex(),costs[index]);
        }
//        for (int index=0 ; index<listArtificialVariables.size() ; index++) {
//            Variable var = listArtificialVariables.get(index);
//            vectorC[ var.getIndex() ] = -ToleranceConstants.BIG_M;
//        }
    }

    /**
     * Launch the resolution by calling a Simplex.
     * @throws UnboundedLinearProblemException 
     */
    public void solve() throws UnboundedLinearProblemException {
        
        double[] c = new double[vectorC.size()];
        if (objectif==Objective.MINIMIZE) {
            for (int indexVariable=0 ; indexVariable<vectorC.size() ; indexVariable++) {
                c[indexVariable] = -vectorC.get(indexVariable);
            }
        } 
        else {
            for (int indexVariable=0 ; indexVariable<vectorC.size() ; indexVariable++) {
                c[indexVariable] = vectorC.get(indexVariable);
            }
        }
        
        double[] b = new double[matrixA.getNumberofLines()];
        for (int i=0 ; i<matrixA.getNumberofLines() ; i++) {
            b[i] = vectorB.get(i);
        }
        simplex = new SimplexSolverService(matrixA, b, c, listArtificialLocation);

        simplex.solve();
    }
    /**
     * Create and add a new variable in the model that could be use in the constraints.
     * 
     * @param nom
     * @return
     */
    public Variable newVariable(String nom) {
        Variable variable = new Variable();
        variable.setName(nom);
        variable.setIndex(listVariables.size());
        listVariables.add(variable);
        return variable;
    }

    public boolean checkOptimality() {
        SimplexSolverAnalyser analyser = new SimplexSolverAnalyser(simplex);
        
        /**/
        for (Variable variable : listVariables) {
            System.out.println("LinearProblem  v["+variable.getIndex()+"]="+this.getValue(variable));
        }
        /**/
        
        return analyser.checkSolution();
    }

    public double getValue(Variable variable) {
        return simplex.primalValues(variable.getIndex());
    }

    public double getObjectiveValue() {
        double objectiveValue = simplex.costFunctionvalue();
        if (objectif==Objective.MINIMIZE) {
            objectiveValue = -objectiveValue;
        }
        return objectiveValue;
    }

    public ArrayList<Variable> getListVariables() {
        return listVariables;
    }

    public ColumnSelectionStrategyEnum getColumnSelectionStrategy() {
        return columnSelectionStrategy;
    }

    public void setColumnSelectionStrategy(ColumnSelectionStrategyEnum columnSelectionStrategy) {
        this.columnSelectionStrategy = columnSelectionStrategy;
    }
}
