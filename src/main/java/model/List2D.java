package model;

import java.util.ArrayList;

public class List2D <T> {

	ArrayList<ArrayList<T>> list;
	
	/**
	 * constructeur de la liste à deux dimensions.
	 * @param nbLines
	 */
	public List2D(int nbLines) {
		list = new ArrayList<ArrayList<T>>();
		for (int iEmployee=0 ; iEmployee<nbLines ; iEmployee++) {
			list.add(new ArrayList<T>());
		}
	}
	/**
	 * ajout d'une ligne à deux dimensions.
	 * @param indexLine
	 * @param element
	 */
	public void add(int indexLine, T element ) {
		list.get(indexLine).add(element);
	}
	/**
	 * recuperation d'un element localise dans la matrice
	 * @param indexLine
	 * @param indexElement
	 * @return
	 */
	public T getElement(int indexLine, int indexElement) { return list.get(indexLine).get(indexElement); }
	/**
	 * retourne le nombre de lignes composant la matrice
	 * @return
	 */
	int getSize1() { return list.size(); }
	/**
	 * retourne le nombre de colonnes composant la ligne indexLine
	 * @param indexLine
	 * @return
	 */
	int getSize2(int indexLine) { return list.get(indexLine).size(); }
	/**
	 * on ajoute une nouvelle ligne a la liste existante
	 * @param line
	 */
	public void addLine(ArrayList<T> line)  {
		list.add(line);
	}
	/**
	 * on remplace completement une ancienne ligne par une nouvelle
	 * @param line
	 */
	public void setLine(int indexLine, ArrayList<T> line)  {
		list.set(indexLine, line);
	}
	/**
	 * retourne une ligne complete
	 * @param indexLine
	 * @return
	 */
	public ArrayList<T> getLine(int indexLine)  {
		return list.get(indexLine);
	}
}
