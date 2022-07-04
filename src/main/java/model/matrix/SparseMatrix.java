package model.matrix;

import java.util.ArrayList;

public class SparseMatrix {
	
	private List2D<ElementMatrix> list2DElementMatrix;
	
	public SparseMatrix () {
		list2DElementMatrix = new List2D<ElementMatrix>(0);
	}
	
	public ArrayList<ElementMatrix> getLine(int lineIndex) {
		return list2DElementMatrix.getLine(lineIndex);
	}
	
	public ElementMatrix getElementMatrix(int lineIndex, int columnIndex) {
		for (ElementMatrix element : list2DElementMatrix.getLine(lineIndex)) {
			if (element.getColumnIndex()==columnIndex) {
				return element;
			}
		}
		return null;
	}
	
	public void addLine(ArrayList<ElementMatrix> line) {
		list2DElementMatrix.addLine(line);
	}
	/**
	 * Note that we completely replace the existing line into the matrix. 
	 */
	public void setLine(int indexLine, ArrayList<ElementMatrix> line) {
		list2DElementMatrix.setLine(indexLine, line);
	}
	/**
	 * Return a clone of the selected line.
	 */
	public ArrayList<ElementMatrix> cloneLine(int indexLine) {
		
		ArrayList<ElementMatrix> listeClone = new ArrayList<ElementMatrix>();
		for (ElementMatrix element : list2DElementMatrix.getLine(indexLine)) {
			listeClone.add(element.clone());
		}
		return listeClone;
	}
	/**
	 * Add an element into the matrix's line.
     * FIXME: check that there is no two element with the same index.
	 * 
	 * @param element
	 */
	public void add(ElementMatrix element) {
		list2DElementMatrix.addElementInLine(element.getLineIndex(), element);
	}
	/**
	 * Return the last element stored in the line.
	 * Attention : it returns the last stored element, not the element with the highest index in the line.
	 * 
	 * @param indexLine
	 * @return
	 */
	public ElementMatrix getLastElementMatrixOfLine(int indexLine) {
		return list2DElementMatrix.getElement(indexLine, list2DElementMatrix.getNumberOfElementsInLine(indexLine)-1 );
	}
	
	/**
     * Return the first element stored in the line.
     * Attention : it returns the first stored element, not the element with the lowest index in the line.
     * 
     * @param indexLine
     * @return     
     */
    public ElementMatrix getFirstElementMatrixOfLine(int indexLine) {
        return list2DElementMatrix.getElement(indexLine, 0 );
    }
	/**
	 * Delete a given element in the matrix.
	 * 
	 * @param lineIndex
	 * @param columnIndex
	 */
	public void deleteElementMatrix(int lineIndex, int columnIndex) {
		ElementMatrix elementToDelete = null;
		
		// we search if the element's indexes exists in the sparse matrix.
		for (ElementMatrix element : list2DElementMatrix.getLine(lineIndex)) {
			if (element.getColumnIndex()==columnIndex) {
				elementToDelete = element;
				break;
			}
		}
		
		// if element exists we delete it.
		if (elementToDelete!=null) {
			list2DElementMatrix.getLine(lineIndex).remove(elementToDelete);
		}
	}
	
	public int getNumberofLines() {
		return list2DElementMatrix.getNumberOfLines();
	}
}
