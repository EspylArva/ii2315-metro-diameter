package com.java.project.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.graphstream.graph.Graph;

import com.java.project.view.NetworkViewer;


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
	
	private static boolean built = false;
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
	private Graph graph;
	private ArrayList<Graph> clusters = new ArrayList<Graph>();
	
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
	public Graph getGraph() {
		return graph;
	}
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	public ArrayList<Graph> getClusters() {
		return clusters;
	}
	public void setClusters(ArrayList<Graph> clusters) {
		this.clusters = clusters;
	}
	
	public static World getInstance(){ return WORLD_INSTANCE; }
	public static boolean isBuilt() {
		return built;
	}
	public static void setBuilt(boolean built) {
		World.built = built;
	}
	public static void destroy() {
		WORLD_INSTANCE = new World();
	}
	public static void linkGraphWindow(Graph g, NetworkViewer newWindow) {
		map.put(g,newWindow);
		newWindow.setGraph(g);
	}
	
	public static Map<Graph, NetworkViewer> getMap() {
		return map;
	}
	public static void setMap(Map<Graph, NetworkViewer> map) {
		World.map = map;
	}

	private static Map<Graph, NetworkViewer> map = new HashMap<Graph,NetworkViewer>();
	
	
	

}
