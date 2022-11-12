package model;

public class LinearConstraint {
    
    double[] coefficients;
    Variable[] variables;
    ConstraintTypeEnum symbol;
    double value;
    
    public LinearConstraint( double[] coefficients, Variable[] variables, ConstraintTypeEnum symbol, double value) {
        this.coefficients = coefficients;
        this.variables = variables;
        this.symbol = symbol;
        this.value = value;
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(double[] coefficients) {
        this.coefficients = coefficients;
    }

    public Variable[] getVariables() {
        return variables;
    }

    public void setVariables(Variable[] variables) {
        this.variables = variables;
    }

    public ConstraintTypeEnum getSymbol() {
        return symbol;
    }

    public void setSymbol(ConstraintTypeEnum symbol) {
        this.symbol = symbol;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
