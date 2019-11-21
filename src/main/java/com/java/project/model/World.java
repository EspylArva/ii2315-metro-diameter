package com.java.project.model;

import java.util.ArrayList;


public class World //Info: Singleton
{
	// *** CONSTRUCTORS *** //
	private World()
	{
		this.corresp = new ArrayList<Correspondance>();
		this.stations = new ArrayList<Station>();
		this.routes = new ArrayList<Route>();
		this.lignes = new ArrayList<Ligne>();
	}
	
	// *** ATTRIBUTES *** //
	private static World WORLD_INSTANCE = new World();
	
	
	// Ensemble des correspondances entre tramway et métro/rer
	private ArrayList<Correspondance> corresp;
	// Ensemble des stations, sans les liens entre. Liste de Vertex
	// TODO passer en hashset
	private ArrayList<Station> stations;
	// Ensemble des itinéraires, déterminé par un chemin (Array de stations), une direction, etc.
	// Entraîne potentiellement des doublons, car une ligne a deux ou plus itinéraires (1 par terminus)
	private ArrayList<Route> routes;
	// Ensemble des lignes de métro, tramway et rer
	private ArrayList<Ligne> lignes;
	
	// *** GETTERS & SETTERS *** //
	
	
	public ArrayList<Correspondance> getCorresp() {
		return corresp;
	}
	public void setCorresp(ArrayList<Correspondance> corresp) {
		this.corresp = corresp;
	}
	public ArrayList<Station> getStations() {
		return stations;
	}
	public void setStations(ArrayList<Station> stations) {
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
	
	public static World getInstance()
    {   return WORLD_INSTANCE;
    }

}
