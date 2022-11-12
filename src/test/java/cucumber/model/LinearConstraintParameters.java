package cucumber.model;

import model.constraint.LinearConstraintType;

public class LinearConstraintParameters {
    
    double[] coefficients;
    double value;
    LinearConstraintType constraintType;
    
    public LinearConstraintParameters() {
    }
    
    public LinearConstraintParameters(double[] coefficientIndexedByVariable, LinearConstraintType constraintType, double value) {
        super();
        this.coefficients = coefficientIndexedByVariable;
        this.value = value;
        this.constraintType = constraintType;
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(double[] coefficients) {
        this.coefficients = coefficients;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public LinearConstraintType getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(LinearConstraintType constraintType) {
        this.constraintType = constraintType;
    }
}
