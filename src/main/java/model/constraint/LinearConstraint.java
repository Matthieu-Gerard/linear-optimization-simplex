package model.constraint;

import java.util.ArrayList;
import java.util.List;

import model.Variable;

public class LinearConstraint {
    
    List<Double> coefficients;
    List<Variable> variables;
    LinearConstraintType symbol;
    double value;
    
    // TODO return une erreur si variable et coefficient n'ont pas le même size.
    public LinearConstraint(double[] coefficients, Variable[] variables, LinearConstraintType symbol, double value) {
        
        this.symbol = symbol;
        this.value = value;
        
        this.coefficients = new ArrayList<>();
        for (double coefficient : coefficients) {
            this.coefficients.add(coefficient);
        }
        
        this.variables = new ArrayList<>();
        for (Variable variable : variables) {
            this.variables.add(variable);
        }
    }
    
    public LinearConstraint(List<Double> coefficients, List<Variable> variables, LinearConstraintType symbol, double value) {
        
        this.symbol = symbol;
        this.value = value;
        
        this.coefficients = new ArrayList<>();
        for (double coefficient : coefficients) {
            this.coefficients.add(coefficient);
        }
        
        this.variables = new ArrayList<>();
        for (Variable variable : variables) {
            this.variables.add(variable);
        }
    }
    
    public void addVariable(double coefficient, Variable variable) {
        this.coefficients.add(coefficient);
        this.variables.add(variable);
    }
    
    public LinearConstraint clone() {
        double[] cloneCoefficients = new double[this.coefficients.size()];
        for (int index = 0 ; index<this.coefficients.size() ; index++) {
            cloneCoefficients[index] = this.coefficients.get(index);
        }
        
        Variable[] cloneVariables = new Variable[this.variables.size()];
        for (int index = 0 ; index<this.variables.size() ; index++) {
            cloneVariables[index] = this.variables.get(index);
        }
        
        return new LinearConstraint(cloneCoefficients, cloneVariables, this.symbol, this.value);
    }

    public LinearConstraintType getSymbol() {
        return symbol;
    }

    public void setSymbol(LinearConstraintType symbol) {
        this.symbol = symbol;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public List<Double> getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(List<Double> coefficients) {
        this.coefficients = coefficients;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }
}
