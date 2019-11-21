package com.java.project.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Route {

	// *** ATTRIBUTES *** //
	private String ligne;
	private String type;
	private String direction;
	private ArrayList<String> arrets = new ArrayList<String>();
	private HashMap<String, ArrayList<String>>  intersections = new HashMap<String, ArrayList<String>>();
	
	// *** METHODS *** //
	public String toString()
	{
		return type + ligne + " going to " + direction + " has " + arrets.size() + " stops";
	}

	// *** GETTERS & SETTERS *** //
	public String getLigne() {
		return ligne;
	}
	public void setLigne(String ligne) {
		this.ligne = ligne;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public ArrayList<String> getArrets() {
		return arrets;
	}
	public void setArrets(ArrayList<String> arrets) {
		this.arrets = arrets;
	}
	public HashMap<String, ArrayList<String>> getIntersections() {
		return intersections;
	}
	public void setIntersections(HashMap<String, ArrayList<String>> intersections) {
		this.intersections = intersections;
	}
}
