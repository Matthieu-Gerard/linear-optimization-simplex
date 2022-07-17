package cucumber.model;

import model.ConstraintTypeEnum;

public class LinearConstraintParameters {
    
    double[] coefficients;
    double value;
    ConstraintTypeEnum constraintType;
    
    public LinearConstraintParameters() {
    }
    
    public LinearConstraintParameters(double[] coefficientIndexedByVariable, ConstraintTypeEnum constraintType, double value) {
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

    public ConstraintTypeEnum getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(ConstraintTypeEnum constraintType) {
        this.constraintType = constraintType;
    }
}
