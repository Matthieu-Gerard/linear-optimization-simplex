package cucumber.model;

import java.util.Arrays;

public class SolutionParameters {

    private double[] vectorX = null;
    private double solutionCost;

    public SolutionParameters() {
    }

    public SolutionParameters(double[] vectorX, double solutionCost) {
        this.vectorX = vectorX;
        this.solutionCost = solutionCost;
    }

    public double[] getvectorX() {
        return vectorX;
    }

    public void setvectorX(double[] vectorX) {
        this.vectorX = vectorX;
    }

    @Override
    public String toString() {
        return "VectorXParameters [vectorX=" + Arrays.toString(vectorX) + "]";
    }

    public double getSolutionCost() {
        return solutionCost;
    }

    public void setSolutionCost(double solutionCost) {
        this.solutionCost = solutionCost;
    }
}
