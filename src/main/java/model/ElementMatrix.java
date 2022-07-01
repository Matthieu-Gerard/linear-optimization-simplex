package model;

public class ElementMatrix implements Comparable<ElementMatrix> {
	
	private int iLine;
	private int iColumn;
	private double fValue;
	
	public ElementMatrix(int line, int column, double value) {
		iLine = line;
		iColumn = column;
		fValue = value;
	}
	
	public int getLine() {
		return iLine;
	}

	public void setLine(int line) {
		this.iLine = line;
	}

	public int getColumn() {
		return iColumn;
	}

	public void setColonne(int column) {
		this.iColumn = column;
	}

	public double getValue() {
		return fValue;
	}

	public void setValue(double fValue) {
		this.fValue = fValue;
	}
	
	public ElementMatrix clone() {
		return new ElementMatrix(iLine, iColumn, fValue);
	}

	public int compareTo(ElementMatrix elementMatrix) {
		if (elementMatrix.getColumn()<this.getColumn()) {
			return 1;
		}
		return 0;
	}
}
