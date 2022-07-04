package analyser;

import model.ToleranceConstants;
import model.matrix.ElementMatrix;
import model.matrix.SparseMatrix;
import service.SimplexSolverService;

public class SimplexSolverAnalyser {
    
    SimplexSolverService simplex;
    
    public SimplexSolverAnalyser(SimplexSolverService simplexSolverService) {
        this.simplex = simplexSolverService;
    }
    /**
     * Check optimality conditions, ie:
     * (1) current solution of primal problem is feasible.
     * (2) current solution of dual problem is feasible.
     * (3) equality: cx = yb.
     */
    public boolean checkSolution() {
        return isPrimalFeasible() && isDualFeasible() && isOptimal();
    }
    /**
     * Check if the current solution of the primal problem is feasible. 
     */
    private boolean isPrimalFeasible() {
        
        SparseMatrix A = simplex.getMatrixA(); 
        double[] b = simplex.getVectorB();
        double[] x = simplex.primalValues();
        
        // check that x >= 0.
        for (int j = 0; j < x.length; j++) {
            if (x[j] < 0.0) {
                System.out.println("x[" + j + "] = " + x[j] + " is negative");
                return false;
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
            if (sum > b[i] + ToleranceConstants.EPSILON) {
                System.out.println("not primal feasible");
                System.out.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the current solution of the dual problem is feasible. 
     */
    private boolean isDualFeasible() {
        
        SparseMatrix A = simplex.getMatrixA(); 
        double[] c = simplex.getCosts();
        double[] dualValuesY = simplex.dualValues();

        // Check that y >= 0
        for (int i = 0; i < dualValuesY.length; i++) {
            if (dualValuesY[i] < 0.0) {
                System.out.println("y[" + i + "] = " + dualValuesY[i] + " is negative");
                return false;
            }
        }
        // Check that yA >= c
        for (int j = 0; j < c.length; j++) {
            double sum = 0.0;
            for (int i = 0; i < A.getNumberofLines() ; i++) {
                ElementMatrix element = A.getElementMatrix(i,j);
                if (element!=null) {
                    sum += element.getValue() * dualValuesY[i];
                }
            }
            sum -= simplex.getInitialObjectiveValue();
            //          for (int i = 0; i < M; i++) {
            //              sum += A[i][j] * y[i];
            //          }
            if (sum < c[j] - ToleranceConstants.EPSILON) {
                System.out.println("not dual feasible");
                System.out.println("c[" + j + "] = " + c[j] + ", sum = " + sum);
                return false;
            }
        }
        return true;
    }

    /**
     * Check that both primal and dual solutions are optimal (condition: cx = yb)
     */
    private boolean isOptimal() {

        double[] b = simplex.getVectorB();
        double[] c = simplex.getCosts();
        double[] primalX = simplex.primalValues();
        double[] dualY = simplex.dualValues();
        double value = simplex.costFunctionvalue();

        // cx calculation.
        double valueOfcx = 0.0;
        for (int j = 0; j < primalX.length; j++) {
            valueOfcx += c[j] * primalX[j];
        }
        // yb calculation.
        double valueOfyb = simplex.getInitialObjectiveValue();
        for (int i = 0; i < dualY.length; i++) {
            valueOfyb += dualY[i] * b[i];
        }
        
        // optimality test
        if (Math.abs(value - valueOfcx) > ToleranceConstants.EPSILON || Math.abs(value - valueOfyb) > ToleranceConstants.EPSILON) {
            System.out.println("value = " + value + ", cx = " + valueOfcx + ", yb = " + valueOfyb);
            return false;
        }
        return true;
    }

    // print tableaux
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
