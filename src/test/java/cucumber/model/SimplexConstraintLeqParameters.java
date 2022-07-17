package cucumber.model;

public class SimplexConstraintLeqParameters {
    
    double[] coefficients;
    double maximumValue;
    
    public SimplexConstraintLeqParameters() {
    }
    
    public SimplexConstraintLeqParameters(double[] coefficientIndexedByVariable, double secondMemberB) {
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
