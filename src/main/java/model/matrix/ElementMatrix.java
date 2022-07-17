package model.matrix;

public class ElementMatrix implements Comparable<ElementMatrix> {
	
	private int lineIndex;
	private int columnIndex;
	private double value;
	
	public ElementMatrix(int lineIndex, int columnIndex, double value) {
		this.lineIndex = lineIndex;
		this.columnIndex = columnIndex;
		this.value = value;
	}
	
	public int getLineIndex() {
		return lineIndex;
	}
	
	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}
	
	public int getColumnIndex() {
		return columnIndex;
	}
	
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public ElementMatrix clone() {
		return new ElementMatrix(lineIndex, columnIndex, value);
	}
	
	public int compareTo(ElementMatrix elementMatrix) {
		if (elementMatrix.getColumnIndex()<this.getColumnIndex()) {
			return 1;
		}
		return 0;
	}
}
