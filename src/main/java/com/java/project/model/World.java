package com.java.project.model;

import java.util.ArrayList;
import java.util.HashSet;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import com.java.project.ii2315.App;


public class World //Info: Singleton
{
	// *** CONSTRUCTORS *** //
	private World()
	{
		this.corresp = new ArrayList<ArrayList<String>>();
		this.stations = new HashSet<Station>();
		this.routes = new ArrayList<Route>();
		this.lignes = new ArrayList<Ligne>();
	}
	
	// *** METHODS *** //

	
	
	
	// *** ATTRIBUTES *** //
	private static World WORLD_INSTANCE = new World();
	
	// Ensemble des correspondances entre tramway et métro/rer
	private ArrayList<ArrayList<String>> corresp;
	// Ensemble des stations, sans les liens entre. Liste de Vertex
	private HashSet<Station> stations;
	// Ensemble des itinéraires, déterminé par un chemin (Array de stations), une direction, etc.
	// Entraîne potentiellement des doublons, car une ligne a deux ou plus itinéraires (1 par terminus)
	private ArrayList<Route> routes;
	// Ensemble des lignes de métro, tramway et rer
	private ArrayList<Ligne> lignes;
	
	// *** GETTERS & SETTERS *** //
		
	public ArrayList<ArrayList<String>> getCorresp() {
		return corresp;
	}
	public void setCorresp(ArrayList<ArrayList<String>> corresp) {
		this.corresp = corresp;
	}
	public HashSet<Station> getStations() {
		return stations;
	}
	public void setStations(HashSet<Station> stations) {
		this.stations = stations;
	}
	public ArrayList<Route> getRoutes() {
		return routes;
	}
	public void setRoutes(ArrayList<Route> routes) {
		this.routes = routes;
	}
	public ArrayList<Ligne> getLignes() {
		return lignes;
	}
	public void setLignes(ArrayList<Ligne> lignes) {
		this.lignes = lignes;
	}
	
	public static World getInstance(){ return WORLD_INSTANCE; }

}
