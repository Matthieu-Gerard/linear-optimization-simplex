package model;

public class Variable {
	
	private String name = null;
	private int index;
	
	public Variable() {
		name = null;
		index = -1;
	}
	
	public void setName(String nom) {
		name = nom;
	}
	
	public String getName() {
		return name;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int i) {
		this.index = i;
	}
}
