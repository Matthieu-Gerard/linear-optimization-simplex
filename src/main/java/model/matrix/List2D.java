package model.matrix;

import java.util.ArrayList;

public class List2D <T> {

	ArrayList<ArrayList<T>> list;
	
	public List2D(int numberOfLines) {
		list = new ArrayList<ArrayList<T>>();
		for (int iEmployee=0 ; iEmployee<numberOfLines ; iEmployee++) {
			list.add(new ArrayList<T>());
		}
	}

	public void addLine(ArrayList<T> line)  {
        list.add(line);
    }

	/**
     * Replace an existing line by a new one.
     * 
     * @param lineIndex
     * @param line
     */
    public void setLine(int lineIndex, ArrayList<T> line)  {
        list.set(lineIndex, line);
    }
	
	public void addElementInLine(int indexLine, T element ) {
		list.get(indexLine).add(element);
	}

	public T getElement(int lineIndex, int elementIndex) { 
	    return list.get(lineIndex).get(elementIndex); 
	}
	
	int getNumberOfLines() { 
	    return list.size(); 
	}
	
	int getNumberOfElementsInLine(int lineIndex) { 
	    return list.get(lineIndex).size(); 
	}
	
	public ArrayList<T> getLine(int indexLine)  {
		return list.get(indexLine);
	}
}
