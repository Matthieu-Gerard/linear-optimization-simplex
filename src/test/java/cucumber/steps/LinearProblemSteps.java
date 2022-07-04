package cucumber.steps;

/*
// FIXME : do not add element with value 0.
public class LinearProblemSteps {
    
    int numberOfVariables;
    int numberOfConstraints;
    
    LinearProblem linearProblem;
    Variable[] variables;
    
    
    SparseMatrix matrixA;
    double[] vectorB;
    
    boolean isUnboundedLinearProblem = false;
    
    @Given("^The linear problem have (-?\\d+) variables and (-?\\d+) constraints.$")
    public void definition(int numberOfVariables, int numberOfConstraints) throws Throwable {
        this.numberOfVariables = numberOfVariables;
        this.numberOfConstraints = numberOfConstraints;
        
        this.linearProblem = new LinearProblem();
        this.variables = new Variable[this.numberOfVariables];
        for(int variableIndex=0 ; variableIndex<this.numberOfVariables ; variableIndex++) {
            this.variables[variableIndex] = this.linearProblem.newVariable("x_"+variableIndex);
        }
        
        this.vectorB = new double[this.numberOfConstraints];
    }
    
    @Given("^The linear cost values are:$")
    public void given_the_cost_values(List<CostsParameters> vectorCParameters)
            throws Exception {
        double[] costs = vectorCParameters.get(0).getVectorC();
        assertEquals(numberOfVariables, costs.length);
        this.linearProblem.addLinearObjectif(costs, variables);
    }
    
    @Given("^The linear constraints are:$")
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
        this.simplexService = new SimplexSolverService(matrixA, vectorB, vectorC, null);
        this.simplexService.setColumnSelection(new BlandColumnSelection());
    }
    
    @Given("^The Dantzig column selection strategy.$")
    public void given_the_dantzig_column_strategy() {
        this.simplexService = new SimplexSolverService(matrixA, vectorB, vectorC, null);
        this.simplexService.setColumnSelection(new DantzigColumnSelection());
    }
    
    @When("^I execute Simplex method.$")
    public void when_Simplex_algorithm_is_executed() {
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
    
    @Then("^Found solution is :$")
    public void found_solution_is(List<SolutionParameters> solutionParameters) {
        
        double solutionCost = solutionParameters.get(0).getSolutionCost();
        assertEquals(solutionCost, this.simplexService.value(), ToleranceConstants.EPSILON);
        
        double[] expectedVectorX = solutionParameters.get(0).getvectorX();
        assertEquals(this.numberOfVariables, expectedVectorX.length);
        
        double[] vectorX = this.simplexService.primal();
        
        for (int indexOfVariable=0 ; indexOfVariable<this.numberOfVariables ; indexOfVariable++) {
            assertEquals(expectedVectorX[indexOfVariable], vectorX[indexOfVariable], ToleranceConstants.EPSILON);
            indexOfVariable++;
        }
    }
}
*/