package service.solutionanalyser;

import model.SimplexConstants;
import model.matrix.ElementMatrix;
import model.matrix.SparseMatrix;
import service.simplex.SimplexSolverService;

public class SolutionAnalyserService {
    
    SimplexSolverService simplex;
    
    public SolutionAnalyserService(SimplexSolverService simplexSolverService) {
        this.simplex = simplexSolverService;
    }
    /**
     * Check optimality conditions, ie:
     * (1) current solution of primal problem is feasible.
     * (2) current solution of dual problem is feasible.
     * (3) equality: cx = yb.
     */
    public SolutionState checkSolution() {
        
        SolutionState solutionState = isPrimalFeasible();
        if (solutionState.getState()!=SolutionStateEnum.IS_PRIMAL_FEASIBLE) {
            return solutionState;
        }
        
        solutionState = isDualFeasible();
        if (solutionState.getState()!=SolutionStateEnum.IS_DUAL_FEASIBLE) {
            return solutionState;
        }
        
        solutionState = isOptimal();
        if (solutionState.getState()!=SolutionStateEnum.IS_OPTIMAL) {
            return solutionState;
        }
        
        return solutionState;
    }
    /**
     * Check if the current solution of the primal problem is feasible. 
     */
    private SolutionState isPrimalFeasible() {
        
        SparseMatrix A = simplex.getMatrixA(); 
        double[] b = simplex.getVectorB();
        double[] x = simplex.primalValues();
        SolutionState solutionState = new SolutionState();
        solutionState.setState(SolutionStateEnum.IS_PRIMAL_FEASIBLE);
        
        // check that x >= 0.
        for (int j = 0; j < x.length; j++) {
            if (x[j] < 0.0) {
                solutionState.setState(SolutionStateEnum.IS_NOT_PRIMAL_FEASIBLE);
                solutionState.setMessage("not primal feasible --> x[" + j + "] = " + x[j] + " is negative");
                return solutionState;
            }
        }

        // check that Ax <= b
        for (int i = 0; i < A.getNumberofLines(); i++) {
            double sum = 0.0;
            for (ElementMatrix element : A.getLine(i)) {
                sum += element.getValue() * x[element.getColumnIndex()];
            }
            //          for (int j = 0; j < N; j++) {
            //              sum += A[i][j] * x[j];
            //          }
            if (sum > b[i] + SimplexConstants.EPSILON) {
                solutionState.setState(SolutionStateEnum.IS_NOT_PRIMAL_FEASIBLE);
                solutionState.setMessage("not primal feasible --> b[" + i + "] = " + b[i] + ", sum = " + sum);
                return solutionState;
            }
        }
        
        return solutionState;
    }

    /**
     * Check if the current solution of the dual problem is feasible. 
     */
    private SolutionState isDualFeasible() {
        
        SparseMatrix A = simplex.getMatrixA(); 
        double[] c = simplex.getCosts();
        double[] dualValuesY = simplex.dualValues();
        
        SolutionState solutionState = new SolutionState();
        solutionState.setState(SolutionStateEnum.IS_DUAL_FEASIBLE);

        // Check that 0 <= y
        for (int i = 0; i < dualValuesY.length; i++) {
            if (dualValuesY[i] < 0.0) {
                solutionState.setState(SolutionStateEnum.IS_NOT_DUAL_FEASIBLE);
                solutionState.setMessage("not dual feasible --> y[" + i + "] = " + dualValuesY[i] + " is negative");
                return solutionState;
            }
        }
        // Check that c <= yA
        for (int j = 0; j < c.length; j++) {
            double sum = 0;
            for (int i = 0; i < A.getNumberofLines() ; i++) {
                ElementMatrix element = A.getElementMatrix(i,j);
                if (element!=null) {
                    sum += element.getValue() * dualValuesY[i];
                }
            }
            //          for (int i = 0; i < M; i++) {
            //              sum += A[i][j] * y[i];
            //          }
            if (sum < c[j] - SimplexConstants.EPSILON) {
                solutionState.setState(SolutionStateEnum.IS_NOT_DUAL_FEASIBLE);
                solutionState.setMessage("c[" + j + "] = " + c[j] + " is greater than yA = " + sum);
                return solutionState;
            }
        }
        return solutionState;
    }

    /**
     * Check that both primal and dual solutions are optimal (condition: cx = yb)
     */
    private SolutionState isOptimal() {

        double[] b = simplex.getVectorB();
        double[] c = simplex.getCosts();
        double[] primalX = simplex.primalValues();
        double[] dualY = simplex.dualValues();
        double value = simplex.costFunctionvalue();
        
        SolutionState solutionState = new SolutionState();
        solutionState.setState(SolutionStateEnum.IS_OPTIMAL);

        // cx calculation.
        double valueOfcx = 0.0;
        for (int j = 0; j < primalX.length; j++) {
            valueOfcx += c[j] * primalX[j];
        }
        // yb calculation.
        double valueOfyb = 0;//-simplex.getInitialObjectiveValue();
        for (int i = 0; i < dualY.length; i++) {
            double dualValue = dualY[i];
            if (simplex.getConstraintHasAnArtificialVariable().get(i)) {
                dualValue -= SimplexConstants.BIG_M;
            }
            valueOfyb += dualValue * b[i];
        }
        
        // optimality test
        if (Math.abs(value - valueOfcx) > SimplexConstants.EPSILON || Math.abs(value - valueOfyb) > SimplexConstants.EPSILON) {
            solutionState.setState(SolutionStateEnum.IS_NOT_OPTIMAL);
            solutionState.setMessage("is not optimal --> value = " + value + ", cx = " + valueOfcx + ", yb = " + valueOfyb);
            return solutionState;
        }
        return solutionState;
    }

    // print tableau
    public void printTableau() {
        System.out.println("M = " + simplex.getNumberOfConstraints());
        System.out.println("N = " + simplex.getNumberOfVariables());
        for (int i = 0; i <= simplex.getNumberOfConstraints(); i++) {
            for (int j = 0; j <= simplex.getNumberOfConstraints() + simplex.getNumberOfVariables(); j++) {
                ElementMatrix A_i_j = simplex.getTableau().getElementMatrix(i, j);
                if (A_i_j==null) {
                    System.out.printf("%7.1f- ", 0.0);
                } else {
                    System.out.printf("%7.2f ", A_i_j.getValue());
                }
                //              System.out.printf("%7.2f ", a[i][j]);
            }
            System.out.println();
        }
        System.out.println("value = " + simplex.costFunctionvalue());
//      for (int i = 0; i < M; i++)
//          if (variablesEnBase[i] < N) {
//              System.out.println("x_" + variablesEnBase[i] + " = " + a.getLastElementMatrixOfLine(i).getValue());
//              //              System.out.println("x_" + variablesEnBase[i] + " = " + a[i][M+N]);
//          }
//      System.out.println();
    }
}
