package model;

import java.util.ArrayList;

public class SparseMatrix {
	
	private List2D<ElementMatrix> list2DElementMatrix;
	
	public SparseMatrix () {
		list2DElementMatrix = new List2D<ElementMatrix>(0);
	}
	
	public ArrayList<ElementMatrix> getLine(int line) {
		return list2DElementMatrix.getLine(line);
	}
	
	public ElementMatrix getElementMatrix(int line, int column) {
		for (ElementMatrix element : list2DElementMatrix.getLine(line)) {
			if (element.getColumn()==column) {
				return element;
			}
		}
		return null;
	}
	
	public void addLine(ArrayList<ElementMatrix> line) {
		list2DElementMatrix.addLine(line);
	}
	/**
	 * on remplace completement une ligne deja existante dans la matrice 
	 */
	public void setLine(int indexLine, ArrayList<ElementMatrix> line) {
		list2DElementMatrix.setLine(indexLine, line);
	}
	/**
	 * retourne le clone de la ligne indexLine
	 */
	public ArrayList<ElementMatrix> cloneLine(int indexLine) {
		
		ArrayList<ElementMatrix> listeClone = new ArrayList<ElementMatrix>();
		for (ElementMatrix element : list2DElementMatrix.getLine(indexLine)) {
			listeClone.add(element.clone());
		}
		return listeClone;
	}
	/**
	 * ajoute un element sans securite (TODO)
	 */
	public void add(ElementMatrix element) {
		list2DElementMatrix.add(element.getLine(), element);
	}
	/**
	 * retourne l'element en dernier colonne de la ligne (indexLine)
	 * @param indexLine
	 * @return
	 */
	public ElementMatrix getLastElementMatrixOfLine(int indexLine) {
		return list2DElementMatrix.getElement(indexLine, list2DElementMatrix.getSize2(indexLine)-1 );
	}
	/**
	 * permet d'effacer une case dans la SparseMatrix.s
	 * @param line
	 * @param column
	 */
	public void deleteElementMatrix(int line, int column) {
		ElementMatrix elementToDelete = null;
		
		// on recherche si l'element existe ou non dans la liste
		for (ElementMatrix element : list2DElementMatrix.getLine(line)) {
			if (element.getColumn()==column) {
				elementToDelete = element;
				break;
			}
		}
		
		// si l'element existe, on le supprime
		if (elementToDelete!=null) {
			list2DElementMatrix.getLine(line).remove(elementToDelete);
		}
	}
	
	public int getNbLines() {
		return list2DElementMatrix.getSize1();
	}
}
