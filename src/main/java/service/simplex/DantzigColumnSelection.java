package service.simplex;

import model.matrix.ElementMatrix;
import model.matrix.SparseMatrix;

public class DantzigColumnSelection implements ColumnSelection {

    /**
     * This function return the column / variable (out of base) with the highest positive current cost. 
     * This selection strategy was developed by Dantzig.
     *
     * Equivalent to :
     *      q = 0;
     *      for (int j = 1; j < M + N; j++) {
     *          if (a[M][j] > a[M][q]) q = j;
     *      }
     *      if (a[M][q] <= 0){
     *          return -1;
     *      }
     * 
     * If no column with positive cost is detected, it means the optimal solution is found.  
     *
     * @return index of column.
     */
    public int getColumnIndex(SparseMatrix matrixA, int numberOfVariables, int numberOfConstraints) {

        ElementMatrix q = matrixA.getFirstElementMatrixOfLine(numberOfConstraints);
        for (ElementMatrix element : matrixA.getLine(numberOfConstraints)) {
            if (element.getColumnIndex()<numberOfConstraints+numberOfVariables && element.getValue()>q.getValue()) {
                q = element;
            }
        }
        //        
        if (q.getValue() <= 0){
            return -1;
        }
        else return q.getColumnIndex();
    }
}
