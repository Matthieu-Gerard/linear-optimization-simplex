package service.columnselection;

import model.matrix.ElementMatrix;
import model.matrix.SparseMatrix;

public class BlandColumnSelection implements ColumnSelection {

    /**
     * This function return the first column / variable (out of base) with positive current cost. 
     * This selection strategy was developed by R. G. Bland to avoid cycle during Simplex resolution.
     *
     * Equivalent to :
     * for (int j = 0; j < M + N; j++) {
     *      if (a[M][j] > 0) {
     *          return j;
     *      }
     * }
     * 
     * If no column with positive cost is detected, it means the optimal solution is found.  
     *
     * @return index of column.
     */
    @Override
    public int getColumnIndex(SparseMatrix matrixA, int numberOfVariables, int numberOfConstraints) {
        for (ElementMatrix element : matrixA.getLine(numberOfConstraints)) {
            if (element.getColumnIndex()<numberOfConstraints+numberOfVariables && 0<element.getValue()) {
                return element.getColumnIndex();
            }
        }
        return -1;  // optimal
    }
}
