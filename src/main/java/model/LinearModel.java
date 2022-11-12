package model;

import java.util.ArrayList;
import java.util.List;

import model.constraint.LinearConstraint;
import model.constraint.LinearConstraintType;
import model.matrix.ElementMatrix;
import model.matrix.SparseMatrix;

public class LinearModel {
    
    private Objective objectif = Objective.MAXIMIZE;
    private List<Double> listOfCosts = null;
    private List<Variable> listOfVariables = null;
    private List<LinearConstraint> listOfConstraints = null;
    
    public LinearModel() {
        setListOfCosts(new ArrayList<>());
        listOfVariables = new ArrayList<>();
        listOfConstraints = new ArrayList<>();
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
        variable.setIndex(listOfVariables.size());
        listOfVariables.add(variable);
        return variable;
    }
    
    public double[] generateVectorC() {
        double[] c = new double[this.getListOfVariables().size()];
        for (int indexVariable=0 ; indexVariable<this.getListOfVariables().size() ; indexVariable++) {
            c[indexVariable] = this.getListOfCosts().get(indexVariable);
        }
        return c;
    }
    
    public double[] generateVectorB() {
        double[] b = new double[this.getListOfConstraints().size()];
        for (int indexConstraint=0 ; indexConstraint<this.getListOfConstraints().size() ; indexConstraint++) {
            b[indexConstraint] = this.getListOfConstraints().get(indexConstraint).getValue();
        }
        return b;
    }
    
    public SparseMatrix generateMatrixA() {
        SparseMatrix matrixA = new SparseMatrix();
        int lineIndex = 0;
        for (LinearConstraint constraint : this.getListOfConstraints()) {
            
            ArrayList<ElementMatrix> line = new ArrayList<>();
            for (int index=0 ; index<constraint.getCoefficients().size() ; index++) {
                double coefficient = constraint.getCoefficients().get(index);
                int variableIndex = constraint.getVariables().get(index).getIndex();
                
                ElementMatrix element = new ElementMatrix(lineIndex, variableIndex, coefficient);
                line.add(element);
            }
            matrixA.addLine(line);
            lineIndex++;
        }
        return matrixA;
    }
    
    public void addConstraint(LinearConstraint constraint) {
        
        // Ax = b <=> [ Ax <= b AND b <= Ax ]
        if (constraint.getSymbol()==LinearConstraintType.EQ) { 
            System.out.println("LINEAR_MODEL.addConstraint(LinearConstraintType.EQ)");
            LinearConstraint constraintLEQ = constraint.clone();
            LinearConstraint constraintGEQ = constraint.clone();
            
            constraintLEQ.setSymbol(LinearConstraintType.LEQ);
            constraintGEQ.setSymbol(LinearConstraintType.GEQ);
            
            listOfConstraints.add(constraintLEQ);
            listOfConstraints.add(constraintGEQ);
            
        }
        else {
            listOfConstraints.add(constraint);
        }
    }

    public Objective getObjectif() {
        return objectif;
    }

    public void setObjectif(Objective objectif) {
        this.objectif = objectif;
    }

    public List<Variable> getListOfVariables() {
        return listOfVariables;
    }

    public void setListOfVariables(List<Variable> listOfVariables) {
        this.listOfVariables = listOfVariables;
    }

    public List<LinearConstraint> getListOfConstraints() {
        return listOfConstraints;
    }

    public void setListOfConstraints(List<LinearConstraint> listOfConstraints) {
        this.listOfConstraints = listOfConstraints;
    }

    public List<Double> getListOfCosts() {
        return listOfCosts;
    }
    
    public void setListOfCosts(List<Double> listOfCosts) {
        this.listOfCosts = listOfCosts;
    }
    
    public void setListOfCosts(double[] costs) {
        for (double cost : costs) {
            this.listOfCosts.add(cost);
        }
    }
}
