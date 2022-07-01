package cucumber.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import cucumber.model.ConstraintLeqParameters;
import cucumber.model.CostsParameters;
import cucumber.model.SolutionParameters;
import exception.UnboundedLinearProblemException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.ElementMatrix;
import model.SparseMatrix;
import service.SimplexService;

public class SimplexTestSteps {
    
    int numberOfVariables;
    int numberOfConstraints;
    
    SparseMatrix matrixA;
    double[] vectorB;
    double[] vectorC;
    
    SimplexService simplexService;
    boolean isUnboundedLinearProblem = false;
    
    @Given("^I have (-?\\d+) variables and (-?\\d+) constraints.$")
    public void definition(int numberOfVariables, int numberOfConstraints) throws Throwable {
        this.numberOfVariables = numberOfVariables;
        this.numberOfConstraints = numberOfConstraints;
        this.vectorB = new double[this.numberOfConstraints];
    }
    
    @Given("^The cost values :$")
    public void given_the_cost_values(List<CostsParameters> vectorCParameters)
            throws Exception {
        vectorC = vectorCParameters.get(0).getVectorC();
        assertEquals(numberOfVariables, vectorC.length);
    }
    
    @Given("^The constraints :$")
    public void given_the_constraints(List<ConstraintLeqParameters> constraintCParameters)
            throws Exception {
        
        this.matrixA = new SparseMatrix();
        
        int constraintIndex = 0;
        for (ConstraintLeqParameters constraintLeq : constraintCParameters) {
            
            double[] coefficients = constraintLeq.getCoefficients();
            assertEquals(this.numberOfVariables, coefficients.length);
            
            this.vectorB[constraintIndex] = constraintLeq.getMaximumValue();
            
            int variableIndex = 0;
            ArrayList<ElementMatrix> lineOfMatrixA = new ArrayList<ElementMatrix>();
            for (double coefficient : coefficients) {
                ElementMatrix coefficientAij = new ElementMatrix(constraintIndex, variableIndex, coefficient);
                lineOfMatrixA.add(coefficientAij);
                variableIndex++;
            }
            assertEquals(this.numberOfVariables, variableIndex);
            this.matrixA.addLine(lineOfMatrixA);
            
            constraintIndex++;
        }
        assertEquals(this.numberOfConstraints, constraintIndex);
    }

    @When("^I execute Simplex method.$")
    public void when_Simplex_algorithm_is_executed() {
        this.simplexService = new SimplexService(matrixA, vectorB, vectorC, null);
        
        try {
            simplexService.solve();
        }
        catch (UnboundedLinearProblemException e) {
            this.isUnboundedLinearProblem = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Then("^Linear problem is unbounded.$")
    public void linear_problem_is_unbounded() {
        assertEquals(true, this.isUnboundedLinearProblem);
    }
    
    @Then("^Optimal solution is found.$")
    public void solution_is_optimal() {
        boolean status = this.simplexService.checkSolution(matrixA, vectorB, vectorC);
        assertEquals(true, status);
    }
    
    @Then("^Solution is not optimal.$")
    public void solution_is_not_optimal() {
        boolean status = this.simplexService.checkSolution(matrixA, vectorB, vectorC);
        assertEquals(false, status);
    }
    
    @Then("^Expected solution is :$")
    public void solution_values() {
//        boolean status = this.linearProblem.checkOptimality();
    }
    
    @Then("^Found solution is :$")
    public void found_solution_is(List<SolutionParameters> solutionParameters) {
        
        double solutionCost = solutionParameters.get(0).getSolutionCost();
        assertEquals(solutionCost, this.simplexService.value(), SimplexService.EPSILON);
        
        double[] expectedVectorX = solutionParameters.get(0).getvectorX();
        assertEquals(this.numberOfVariables, expectedVectorX.length);
        
        double[] vectorX = this.simplexService.primal();
        
        for (int indexOfVariable=0 ; indexOfVariable<this.numberOfVariables ; indexOfVariable++) {
            assertEquals(expectedVectorX[indexOfVariable], vectorX[indexOfVariable], SimplexService.EPSILON);
            indexOfVariable++;
        }
    }
}
