package com.java.project.model;

import java.util.ArrayList;

public class Ligne {
	
	// ** ATTRIBUTES ** // 
	protected String type;
	protected String name;
	protected String num;
	protected String color;
	protected int[][] labels;
	
	ArrayList<ArrayList<String>> arrets = new ArrayList<ArrayList<String>>();
	
	// *** METHODS *** //
	
	public String toString()
	{
		int fullSize = 0;
		for(ArrayList<String> br : arrets)
		{
			fullSize += br.size();
		}
		return (this.type + this.num + ": " + this.name + " has " + arrets.size() + " branche(s), for a total of " + fullSize + " stop(s)");
	}
	
	
	// ** GETTERS & SETTERS ** //

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int[][] getLabels() {
		return labels;
	}

	public void setLabels(int[][] labels) {
		this.labels = labels;
	}

	public ArrayList<ArrayList<String>> getArrets() {
		return arrets;
	}

	public void setArrets(ArrayList<ArrayList<String>> arrets) {
		this.arrets = arrets;
	}

}
