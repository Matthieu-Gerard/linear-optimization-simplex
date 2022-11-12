package cucumber.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import cucumber.model.SimplexConstraintLeqParameters;
import cucumber.model.CostsParameters;
import cucumber.model.SolutionParameters;
import exception.UnboundedLinearProblemException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.SimplexConstants;
import model.matrix.ElementMatrix;
import model.matrix.SparseMatrix;
import service.simplex.BlandColumnSelection;
import service.simplex.DantzigColumnSelection;
import service.simplex.SimplexSolverService;
import service.solutionanalyser.SolutionAnalyserService;
import service.solutionanalyser.SolutionState;
import service.solutionanalyser.SolutionStateEnum;

public class SimplexTestSteps {
    
    int numberOfVariables;
    int numberOfConstraints;
    
    SparseMatrix matrixA;
    double[] vectorB;
    double[] vectorC;
    
    SimplexSolverService simplexSolver;
    boolean isUnboundedLinearProblem = false;
    
    @Given("^I have (-?\\d+) variables and (-?\\d+) constraints.$")
    public void definition(int numberOfVariables, int numberOfConstraints) throws Throwable {
        this.numberOfVariables = numberOfVariables;
        this.numberOfConstraints = numberOfConstraints;
        this.vectorB = new double[this.numberOfConstraints];
    }
    
    @Given("^The cost values are:$")
    public void given_the_cost_values(List<CostsParameters> vectorCParameters)
            throws Exception {
        vectorC = vectorCParameters.get(0).getVectorC();
        assertEquals(numberOfVariables, vectorC.length);
    }
    
    @Given("^The constraints are:$")
    public void given_the_constraints(List<SimplexConstraintLeqParameters> constraintCParameters)
            throws Exception {
        
        this.matrixA = new SparseMatrix();
        
        int constraintIndex = 0;
        for (SimplexConstraintLeqParameters constraintLeq : constraintCParameters) {
            
            double[] coefficients = constraintLeq.getCoefficients();
            assertEquals(this.numberOfVariables, coefficients.length);
            
            this.vectorB[constraintIndex] = constraintLeq.getMaximumValue();
            
            int variableIndex = 0;
            ArrayList<ElementMatrix> lineOfMatrixA = new ArrayList<ElementMatrix>();
            for (double coefficient : coefficients) {
                if (coefficient!=0.0D) {
                    ElementMatrix coefficientAij = new ElementMatrix(constraintIndex, variableIndex, coefficient);
                    lineOfMatrixA.add(coefficientAij);
                }
                variableIndex++;
            }
            assertEquals(this.numberOfVariables, variableIndex);
            this.matrixA.addLine(lineOfMatrixA);
            
            constraintIndex++;
        }
        assertEquals(this.numberOfConstraints, constraintIndex);
    }

    @Given("^The Bland column selection strategy.$")
    public void given_the_bland_column_strategy() {
        this.simplexSolver = new SimplexSolverService(matrixA, vectorB, vectorC, null);
        this.simplexSolver.setColumnSelection(new BlandColumnSelection());
    }
    
    @Given("^The Dantzig column selection strategy.$")
    public void given_the_dantzig_column_strategy() {
        this.simplexSolver = new SimplexSolverService(matrixA, vectorB, vectorC, null);
        this.simplexSolver.setColumnSelection(new DantzigColumnSelection());
    }
    
    @When("^I execute Simplex method.$")
    public void when_Simplex_algorithm_is_executed() {
        try {
            simplexSolver.solve();
        }
        catch (UnboundedLinearProblemException e) {
            this.isUnboundedLinearProblem = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Then("^Problem is unbounded.$")
    public void linear_problem_is_unbounded() {
        assertEquals(true, this.isUnboundedLinearProblem);
    }
    
    @Then("^Optimal solution is found.$")
    public void solution_is_optimal() {
        SolutionAnalyserService simplexSolverAnalyser = new SolutionAnalyserService(this.simplexSolver);
        SolutionState status = simplexSolverAnalyser.checkSolution();
        assertEquals(SolutionStateEnum.IS_OPTIMAL, status.getState());
    }
    
    @Then("^Solution is not optimal.$")
    public void solution_is_not_optimal() {
        SolutionAnalyserService simplexSolverAnalyser = new SolutionAnalyserService(this.simplexSolver);
        SolutionState status = simplexSolverAnalyser.checkSolution();
        assertEquals(SolutionStateEnum.IS_NOT_OPTIMAL, status.getState());
    }
    
    @Then("^Found solution is:$")
    public void found_solution_is(List<SolutionParameters> solutionParameters) {
        
        double[] expectedVectorX = solutionParameters.get(0).getvectorX();
        assertEquals(this.numberOfVariables, expectedVectorX.length);
        
        double[] vectorX = this.simplexSolver.primalValues();
        
        for (int indexOfVariable=0 ; indexOfVariable<this.numberOfVariables ; indexOfVariable++) {
            assertEquals(expectedVectorX[indexOfVariable], vectorX[indexOfVariable], SimplexConstants.EPSILON);
        }
        
        double solutionCost = solutionParameters.get(0).getSolutionCost();
        assertEquals(solutionCost, this.simplexSolver.costFunctionvalue(), SimplexConstants.EPSILON);
    }
}
