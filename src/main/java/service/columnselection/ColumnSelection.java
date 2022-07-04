package service.columnselection;

import model.matrix.SparseMatrix;

public interface ColumnSelection {
    
    public int getColumnIndex(SparseMatrix matrixA, int numberOfVariables, int numberOfConstraints);
}
