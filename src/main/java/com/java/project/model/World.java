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

	/**
	 * Build correspondances in the graph accordingly to the model of the world.
	 * <p>
	 * @param graph Graph which needs to get the correspondances
	 * @author Tchong-Kite Huam
	 */
	public static void buildCorrespondances(Graph graph)
	{
		try
		{
			for(ArrayList<String> correspondance : World.getInstance().getCorresp())
			{
				for(int i=0; i<correspondance.size()-1 ; i++)
				{
					graph.addEdge(String.format("%s-%s", correspondance.get(i), correspondance.get(i+1)),
							correspondance.get(i), correspondance.get(i+1));
					graph.getEdge(String.format("%s-%s", correspondance.get(i), correspondance.get(i+1)))
						.addAttribute("ui.class", "Correspondance");
				}
			}
			App.logger.info("Successfully built all correspondances");
		}
		catch(Exception e) {App.logger.error(e);}
	}
	
	/**
	 * Build stations in the graph accordingly to the model of the world.
	 * <p>
	 * @param graph Graph which needs to get the stations
	 * @author Tchong-Kite Huam
	 */
	public static void buildStations(Graph graph)
	{
		try
        {
    		for(Station station : World.getInstance().getStations())
    		{
    			Node n = graph.addNode(station.getNum());
    			n.addAttribute("nom", station.getNom());
    			n.addAttribute("ui.label", station.getNom());
//    			n.addAttribute("ui.label", station.num);
    		}
    		App.logger.info("Successfully built all stations");
        }
        catch(Exception e) {App.logger.error(e);}
	}
	
	/**
	 * Build required lines in the graph accordingly to the model of the world.
	 * <p>
	 * @param graph Graph which needs to get the lines
	 * @param lignes Lines to add to the graph. If lignes is null, all the available lines will be added.
	 * @author Tchong-Kite Huam
	 */
	public static void buildLignes(Graph graph, ArrayList<String> lignes)
	{
		if(lignes == null)
		{
			App.logger.trace("Building all the lines");	
		}
		else
		{
			App.logger.trace(String.format("Only building set lines: %s", lignes));			
		}
		try
        {
        	for(Ligne line : World.getInstance().getLignes())
	        {
        		if(lignes == null || lignes.contains(line.getNum()))
        		{
        			App.logger.trace(String.format("Building %s %s, going to %s", line.getType(), line.getNum() , line.getName()));
		        	for(ArrayList<String> branch : line.getArrets())
		        	{
		        		App.logger.trace(String.format("Switching branch in %s %s", line.getType(), line.getNum()));
			        	for(int i=0 ; i<branch.size()-1 ; i++)
			        	{
			        		if(graph.getEdge(branch.get(i) + "-" + branch.get(i+1)) == null)
			        		{
			        			Edge e = graph.addEdge(branch.get(i) + "-" + branch.get(i+1),
			        				branch.get(i), branch.get(i+1));
			        			// Colorizing the branch
			        			e.addAttribute("ui.class", "C"+line.getNum());
			        			App.logger.trace(String.format("Successfully linked %s to %s", branch.get(i), branch.get(i+1)));
			        		}
			        	}
		        	}
        		}
        		else
        		{
        			App.logger.trace(String.format("Line %s %s is not to be built", line.getType(), line.getNum()));
        		}
	        }
        	App.logger.info("Successfully built all lines");
        }
		catch(Exception e) {App.logger.error(e);}	
	}
	
	
	
	
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
	
	public static World getInstance()
    {   return WORLD_INSTANCE;
    }

}
