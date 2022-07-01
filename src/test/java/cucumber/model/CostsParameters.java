package cucumber.model;

import java.util.Arrays;

public class CostsParameters {

    private double[] vectorC = null;

    public CostsParameters() {
    }

    public CostsParameters(double[] vectorC) {
        this.vectorC = vectorC;
    }

    public double[] getVectorC() {
        return vectorC;
    }

    public void setVectorC(double[] vectorC) {
        this.vectorC = vectorC;
    }

    @Override
    public String toString() {
        return "VectorCParameters [vectorC=" + Arrays.toString(vectorC) + "]";
    }
}
