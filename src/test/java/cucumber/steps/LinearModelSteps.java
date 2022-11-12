package cucumber.steps;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import controller.Solver;
import cucumber.model.CostsParameters;
import cucumber.model.LinearConstraintParameters;
import cucumber.model.SolutionParameters;
import exception.UnboundedLinearProblemException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.ColumnSelectionStrategyEnum;
import model.LinearModel;
import model.Objective;
import model.SimplexConstants;
import model.Variable;
import model.constraint.LinearConstraint;
import model.constraint.LinearConstraintType;
import service.solutionanalyser.SolutionState;
import service.solutionanalyser.SolutionStateEnum;

public class LinearModelSteps {
    
    int numberOfVariables;
    LinearModel model;
    Variable[] variables;
    private boolean isUnboundedmodel;
    private Solver solver;
    
    @Given("^The linear model have (-?\\d+) variables.$")
    public void definition(int numberOfVariables) throws Throwable {
        this.numberOfVariables = numberOfVariables;
        
        this.model = new LinearModel();
        variables = new Variable[this.numberOfVariables];
        for(int variableIndex=0 ; variableIndex<this.numberOfVariables ; variableIndex++) {
            variables[variableIndex] = this.model.newVariable("x_"+variableIndex);
        }
    }
    
    @Given("^The linear cost values are:$")
    public void given_the_cost_values(List<CostsParameters> vectorCParameters)
            throws Exception {
        double[] costs = vectorCParameters.get(0).getVectorC();
        assertEquals(numberOfVariables, costs.length);
        for (double cost : costs) {
            this.model.getListOfCosts().add(cost);
        }
    }
    
    @Given("^The linear constraints are:$")
    public void given_the_linear_constraints(List<LinearConstraintParameters> constraintCParameters)
            throws Exception {
        for (LinearConstraintParameters constraint : constraintCParameters) {
            double[] coefficients = constraint.getCoefficients();
            assertEquals(this.numberOfVariables, coefficients.length);
            LinearConstraintType constraintType = constraint.getConstraintType();
            double value = constraint.getValue();
            
            LinearConstraint linearConstraint = new LinearConstraint(coefficients, variables, constraintType, value);
            this.model.addConstraint(linearConstraint);
        }
    }

    @Given("^The objective strategy is maximizing cost function.$")
    public void given_the_objective_strategy_is_maximizing() {
        this.model.setObjectif(Objective.MAXIMIZE);
        this.solver = new Solver(this.model);
    }
    
    @Given("^The objective strategy is minimizing cost function.$")
    public void given_the_objective_strategy_is_minimizing() {
        this.model.setObjectif(Objective.MINIMIZE);
        this.solver = new Solver(this.model);
    }
    
    @Given("^The column selection is Bland strategy.$")
    public void given_the_bland_column_strategy() {
        this.solver.setColumnSelectionStrategy(ColumnSelectionStrategyEnum.BLAND);
    }
    
    @Given("^The column selection is Dantzig strategy.$")
    public void given_the_dantzig_column_strategy() {
        this.solver.setColumnSelectionStrategy(ColumnSelectionStrategyEnum.DANTZIG);
    }
    
    @When("^The linear problem is solved by Simplex solver.$")
    public void when_linear_problem_is_solved_by_simplex() {
        try {
            this.solver.execute();
        }
        catch (UnboundedLinearProblemException e) {
            this.isUnboundedmodel = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Then("^The linear problem is unbounded.$")
    public void linear_problem_is_unbounded() {
        assertEquals(true, this.isUnboundedmodel);
    }
    
    @Then("^The found solution is optimal.$")
    public void solution_is_optimal() {
        SolutionState status = this.solver.checkOptimality();
        assertEquals(SolutionStateEnum.IS_OPTIMAL, status.getState());
    }
    
    @Then("^The found solution is not optimal.$")
    public void solution_is_not_optimal() {
        SolutionState status = this.solver.checkOptimality();
        assertEquals(SolutionStateEnum.IS_NOT_OPTIMAL, status.getState());
    }
    
    @Then("^The linear solution is:$")
    public void expected_solution_is(List<SolutionParameters> solutionParameters) {

        double[] expectedVectorX = solutionParameters.get(0).getvectorX();
        for (int variableIndex=0 ; variableIndex<numberOfVariables ; variableIndex++) {
            double expectedValue = expectedVectorX[variableIndex];
            double actualValue = this.solver.getValue(this.variables[variableIndex]);
            assertEquals(expectedValue, actualValue, SimplexConstants.EPSILON);
        }
        
        double solutionCost = solutionParameters.get(0).getSolutionCost();
        double actualCost = this.solver.getObjectiveValue();
        assertEquals(solutionCost, actualCost, SimplexConstants.EPSILON);
    }
}