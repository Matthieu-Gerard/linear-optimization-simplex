package cucumber.model;

public class ConstraintLeqParameters {
    
    double[] coefficients;
    double maximumValue;
    
    public ConstraintLeqParameters() {
    }
    
    public ConstraintLeqParameters(double[] coefficientIndexedByVariable, double secondMemberB) {
        super();
        this.coefficients = coefficientIndexedByVariable;
        this.maximumValue = secondMemberB;
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(double[] coefficients) {
        this.coefficients = coefficients;
    }

    public double getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(double maximumValue) {
        this.maximumValue = maximumValue;
    }
}
