package model;

import java.util.ArrayList;
import java.util.List;

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
    
    public void addConstraint(LinearConstraint constraint) {
        listOfConstraints.add(constraint);
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
}
