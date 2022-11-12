package controller;

import java.util.ArrayList;
import java.util.List;

import exception.UnboundedLinearProblemException;
import model.ColumnSelectionStrategyEnum;
import model.LinearModel;
import model.Objective;
import model.Variable;
import model.constraint.LinearConstraintType;
import model.constraint.LinearConstraint;
import model.matrix.ElementMatrix;
import model.matrix.SparseMatrix;
import service.simplex.SimplexSolverService;
import service.solutionanalyser.SolutionAnalyserService;
import service.solutionanalyser.SolutionState;

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
            
            LinearConstraintType symbol = initialConstraint.getSymbol();
            List<Double> modifiedCoefficients = new ArrayList<>();
            List<Variable> modifiedVariables = new ArrayList<>();
            double modifiedValue = initialConstraint.getValue();
            
            // if negative value (b<0), we return the b to get a positive b value.
            System.out.println("SOLVER  if negative value (b<0), we return the b to get a positive b value.");
            if (modifiedValue<0.0D) {
                symbol = LinearConstraintType.getOppositeConstraintType(symbol);
                for (int index=0 ; index<modifiedCoefficients.size() ; index++) {
                    int variableIndex = initialConstraint.getVariables().get(index).getIndex();
                    double coefficient = initialConstraint.getCoefficients().get(index);
                    Variable variable = modifiedModel.getListOfVariables().get(variableIndex);
                    
                    modifiedCoefficients.add(-coefficient);
                    modifiedVariables.add(variable);
                }
                modifiedValue = -modifiedValue;
            }
            else {
                for (int index=0 ; index<initialConstraint.getCoefficients().size() ; index++) {
                    double coefficient = initialConstraint.getCoefficients().get(index);
                    
                    int variableIndex = initialConstraint.getVariables().get(index).getIndex();
                    Variable variable = modifiedModel.getListOfVariables().get(variableIndex);
                    
                    modifiedCoefficients.add(coefficient);
                    modifiedVariables.add(variable);
                }
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
            for (int indexCoefficient=0 ; indexCoefficient<constraint.getCoefficients().size() ; indexCoefficient++) {
                int indexVariable = constraint.getVariables().get(indexCoefficient).getIndex();
                double value = constraint.getCoefficients().get(indexCoefficient);
                ElementMatrix elementMatrix = new ElementMatrix(indexLine, indexVariable, value);
                line.add(elementMatrix);
            }
            indexLine ++;
            A.addLine(line);
        }
        
        simplexSolver = new SimplexSolverService(modifiedModel);
        simplexSolver.solve();
    }
    
    public double[] getPrimalSolution() {
        return simplexSolver.primalValues();
    }
    
    public double[] getDualSolution() {
        return simplexSolver.dualValues();
    }
    
    public SolutionState checkOptimality() {
        SolutionAnalyserService analyser = new SolutionAnalyserService(simplexSolver);
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