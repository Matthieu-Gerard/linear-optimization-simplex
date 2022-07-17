package controller;

import java.util.ArrayList;

import analyser.SimplexSolverAnalyser;
import exception.UnboundedLinearProblemException;
import model.ColumnSelectionStrategyEnum;
import model.ConstraintTypeEnum;
import model.LinearConstraint;
import model.LinearModel;
import model.Objective;
import model.Variable;
import model.matrix.ElementMatrix;
import model.matrix.SparseMatrix;
import service.SimplexSolverService;

public class Solver {

    LinearModel initialModel;
    LinearModel modifiedModel;

    SimplexSolverService simplexSolver = null;
    private ColumnSelectionStrategyEnum columnSelectionStrategy;

    public Solver(LinearModel model) {

        this.initialModel = model;
    }

    private void createModifiedModel() {
        this.modifiedModel = new LinearModel();

        // variables
        this.modifiedModel.setObjectif(Objective.MAXIMIZE);
        int indexVariable = 0;
        for (Variable variable : initialModel.getListOfVariables()) {
            modifiedModel.newVariable(variable.getName());

            double cost = initialModel.getListOfCosts().get(indexVariable);
            if (this.initialModel.getObjectif()==Objective.MINIMIZE) {
                cost = -cost;
            }            

            modifiedModel.getListOfCosts().add(cost);
            indexVariable++;
        }

        // linear constraints
        for (LinearConstraint initialConstraint : initialModel.getListOfConstraints()) {

            ConstraintTypeEnum symbol = initialConstraint.getSymbol();

            double[] modifiedCoefficients = new double[initialConstraint.getCoefficients().length];
            Variable[] modifiedVariables = new Variable[initialConstraint.getVariables().length];
            double modifiedValue = initialConstraint.getValue();
            for (int index=0 ; index<modifiedCoefficients.length ; index++) {
                int variableIndex = initialConstraint.getVariables()[index].getIndex();
                modifiedVariables[index] = modifiedModel.getListOfVariables().get(variableIndex);
                modifiedCoefficients[index] = initialConstraint.getCoefficients()[variableIndex];
            }

            LinearConstraint modifiedConstraint = new LinearConstraint(modifiedCoefficients, modifiedVariables, symbol, modifiedValue);
            modifiedModel.addConstraint(modifiedConstraint);
        }        
    }

    public void execute() throws UnboundedLinearProblemException {

        createModifiedModel();

        double[] c = new double[this.modifiedModel.getListOfCosts().size()];
        for (int index = 0 ; index<c.length ; index++) {
            c[index] = this.modifiedModel.getListOfCosts().get(index);
        }

        double[] b = new double[this.modifiedModel.getListOfConstraints().size()];
        for (int index = 0 ; index<b.length ; index++) {
            b[index] = this.modifiedModel.getListOfConstraints().get(index).getValue();
        }

        SparseMatrix A = new SparseMatrix();
        int indexLine = 0;
        for (LinearConstraint constraint : modifiedModel.getListOfConstraints()) {
            ArrayList<ElementMatrix> line = new ArrayList<ElementMatrix>();
            for (int indexCoefficient=0 ; indexCoefficient<constraint.getCoefficients().length ; indexCoefficient++) {
                int indexVariable = constraint.getVariables()[indexCoefficient].getIndex();
                double value = constraint.getCoefficients()[indexCoefficient];
                ElementMatrix elementMatrix = new ElementMatrix(indexLine, indexVariable, value);
                line.add(elementMatrix);
            }
            indexLine ++;
            A.addLine(line);
        }

        simplexSolver = new SimplexSolverService(A, b, c, null);
        simplexSolver.solve();
    }

    public double[] getPrimalSolution() {
        return simplexSolver.primalValues();
    }

    public double[] getDualSolution() {
        return simplexSolver.dualValues();
    }

    public boolean checkOptimality() {
        SimplexSolverAnalyser analyser = new SimplexSolverAnalyser(simplexSolver);
        return analyser.checkSolution();
    }

    public void setColumnSelectionStrategy(ColumnSelectionStrategyEnum columnSelectionStrategy) {
        this.columnSelectionStrategy = columnSelectionStrategy;
    }

    public double getObjectiveValue() {
        double objectiveValue = simplexSolver.costFunctionvalue();
        if (this.initialModel.getObjectif()==Objective.MINIMIZE) {
            objectiveValue = -objectiveValue;
        }
        return objectiveValue;
    }

    public double getValue(Variable variable) {
        return simplexSolver.primalValues(variable.getIndex());
    }

    public ColumnSelectionStrategyEnum getColumnSelectionStrategy() {
        return columnSelectionStrategy;
    }
}