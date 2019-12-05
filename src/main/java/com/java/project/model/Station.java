package com.java.project.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;


public class Station 
{	
	// *** ATTRIBUTES *** //
	
	private String nom;
	private String num;
	private String commune;
	private double lat;
	private double lng;
	private String type;
	private boolean isHub;
	private ArrayList<String[]> routes = new ArrayList<String[]>();
	private Map<String, ArrayList<String>> lignes = new HashMap<String, ArrayList<String>>();
	
	// *** METHODS *** //
	
	public String toString()
	{
		StringBuilder str = new StringBuilder(); 
		str.append("ID" + '\t'+ '\t' + num + '\n');
		str.append("Nom" + '\t' + '\t'+ nom + '\n');
		str.append("Commune" + '\t'+ '\t' + commune + '\n');
		str.append("Position" + '\t' + lat + " | " + lng + '\n');
		str.append("Type" + '\t'+ '\t' + type + '\n');
		str.append("Is Hub" + '\t'+ '\t' + isHub + '\n');
		return str.toString();
	}
	
	// *** GETTERS & SETTERS *** //
	

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getCommune() {
		return commune;
	}

	public void setCommune(String commune) {
		this.commune = commune;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isHub() {
		return isHub;
	}

	public void setHub(boolean isHub) {
		this.isHub = isHub;
	}

	public ArrayList<String[]> getRoutes() {
		return routes;
	}

	public void setRoutes(ArrayList<String[]> routes) {
		this.routes = routes;
	}

	public Map<String, ArrayList<String>> getLignes() {
		return lignes;
	}

	public void setLignes(Map<String, ArrayList<String>> lignes) {
		this.lignes = lignes;
	}
}