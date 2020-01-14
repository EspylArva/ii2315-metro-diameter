package com.java.project.control;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.MultiGraph;
//import org.graphstream.ui.swingViewer.Viewer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.java.project.ii2315.App;
import com.java.project.model.Ligne;
import com.java.project.model.Route;
import com.java.project.model.Station;
import com.java.project.model.World;
import com.java.project.view.NetworkViewer;

public class Back {
	
	private static final Gson g = new Gson();
	private static Logger logger = App.logger;
	private static boolean run = false; 
	private static Path diameter;
	
	
	private static java.nio.file.Path pathToJson;
	
    public static Graph computeWeightlessGraph()
    {
//    	Graph graph = null;
    	// *** BUILDING MODEL *** //
    	if(!World.isBuilt())
    	{
	    	parseWorld(getPathToJson());
	    	World.setBuilt(true);
    	}
    	else
    	{
    		World.destroy();
    		parseWorld(getPathToJson());
	    	World.setBuilt(true);
    	}
    	Graph g = buildSimpleWorld();
//    	World.getInstance().addGraph(g);
//    	World.getInstance().setGraph(buildSimpleWorld());
//      Graph graph = buildSimplePartialWorld("B", "A");
      
    	// *** GRAPHICAL CONFIGURATION *** //
//		ViewControl.configureGraphUI(World.getInstance().getGraph());
//		ViewControl.freezeWorld(World.getInstance().getGraph());
      
		// *** DISPLAY GRAPH *** //
		logger.info("Displaying graph");
//		addWeights(graph);
//    	org.graphstream.ui.view.Viewer viewer = graph.display();
    	return g;
    }
    
	public static ClusterAndDiameter computeDiameter(Graph graph, NetworkViewer networkViewer)
    {
  
//    	if (Back.diameter == null) {
//    		if(Back.run == false){
    			logger.info("Lancement du thread de calcul");
    			ClusterAndDiameter t = new ClusterAndDiameter("Cluster",graph,graph.getNodeCount() * 4, networkViewer);
    			t.start();
        		
        		Back.run = true;
        		
//    		}
//    		else {
    			logger.info("En attente du thread qui le calcul");
//    		}
//    		
//    	}
//    	else {
//    		logger.info("Diamètre : " + Back.diameter.getNodeCount());
//        	logger.info("Diamètre : " + Back.diameter);
//    	}
    			return t;
    }
    
    
    public static void addWeights(Graph graph)
    {
    	Collection<Edge> edges = graph.getEdgeSet();
    	for(Edge e : edges)
    	{
    		Node s1 = e.getSourceNode();Node s2 = e.getTargetNode();
    			        		
            double Xs1 = s1.getAttribute("latitude");double Ys1 =s1.getAttribute("longitude");
            
            double Xs2 = s2.getAttribute("latitude");double Ys2 =s2.getAttribute("longitude");
            
            double formula = Math.sqrt(Math.pow(Xs1-Xs2, 2)+Math.pow(Ys1-Ys2, 2)); // Euclidian distance
            logger.trace(String.format("Distance between %s et %s: %s", s1.getAttribute("nom"), s2.getAttribute("nom"), formula));
            
            e.addAttribute("distance",formula);
    	}
    }
	
	/**
	 * Building a simple transportation map, limited to parameters.
	 * <p>
	 * Vertices are stations. Edges represent a connection between two stations, using any mean of
	 * transportation. Several connections between two vertices will not show; as this is a simple representation, only one connection is needed, thus represented.
	 * <p>
	 * The representation is said "simple" because all edges are considered the same weight-wise. This means that two connected stations are always considered to be at the same distance -- regardless of the real distance between them.
	 * <p>
	 * @param args  Lines that should be built
	 * @return graph Graph built according to the model and limited to the parameters
	 * @author Tchong-Kite Huam
	 */
    private static Graph buildSimpleWorld(String... args )
    {
    	// Liste des lignes à mettre
    	
//    	World.getInstance().setGraph(new MultiGraph("Plan du Métro"));
    	Graph g = new MultiGraph("Paris' transportation map");
    	
    	WorldControl.buildStations(g);
		WorldControl.buildCorrespondances(g);
		
		ArrayList<String> partialWorldSetup = new ArrayList<String>(); for(String s : args)partialWorldSetup.add(s);
        
		WorldControl.buildLignes(g, partialWorldSetup);
		
        // Clean the graph of useless stations
//        return removeLoneNode(World.getInstance().getGraph());
        return removeLoneNode(g);
	}


	/**
	 * Building a simple transportation map.
	 * <p>
	 * Vertices are stations. Edges represent a connection between two stations, using any mean of
	 * transportation. Several connections between two vertices will not show; as this is a simple representation, only one connection is needed, thus represented.
	 * <p>
	 * The representation is said "simple" because all edges are considered the same weight-wise. This means that two connected stations are always considered to be at the same distance -- regardless of the real distance between them.
	 * <p>
	 * @return graph Graph built according to the model
	 */
	private static Graph buildSimpleWorld()
	{		
		Graph g = new MultiGraph("Paris' transportation map");
//        World.getInstance().setGraph(new MultiGraph("Paris' transportation map"));
        WorldControl.buildStations(g);
        WorldControl.buildCorrespondances(g);
        WorldControl.buildLignes(g, null);   
//        return removeLoneNode(World.getInstance().getGraph());
        return removeLoneNode(g);
		
	}
	
	/**
	 * Build a world using the JSON file.
	 * <p>
	 * @param jsonFile Absolute path of the JSON file
	 * @return world Instance of World built from the JSON file
	 * @author Tchong-Kite Huam
	 */
	private static World parseWorld(java.nio.file.Path jsonFile)
	{
		World world = World.getInstance();
		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader(jsonFile.toString()));
			
			/** Objet global, qui comprend les éléments suivants :
			 * corresp -- JsonArray
			 * stations -- JsonObject
			 * routes -- JsonArray
			 * lignes -- JsonObject
			 */
			JsonObject jsonWorld = ((JsonElement) g.fromJson(reader, JsonElement.class)).getAsJsonObject();
	        
			logger.info("Starting the parsing of file " + jsonFile);
	        for(Entry<String, JsonElement> o : jsonWorld.entrySet())
	        {
	        	logger.trace(String.format(
	        			"Parsing following element: %s. It is of class %s",
	        			o.getKey(), o.getValue().getClass() ));
	        	
	        	switch(o.getKey())
	        	{
	        	case "stations":
	        		logger.info('\t'+"Computing object \"stations\"...");
	        		JsonObject allStations = (JsonObject)o.getValue();
	        		
	        		for(Entry<String, JsonElement> station : allStations.entrySet())
	        		{
	        			logger.trace(String.format("Building station %s", station.getKey()));
	        			Station s = g.fromJson(station.getValue(), Station.class);
	        			logger.trace(s.toString());
	        			
	        			// Add to model
	        			world.getStations().add(s);
	        			
	        		}
	        		logger.info('\t'+"Object \"stations\" computed!");
	        		break;
	        	case "lignes":
	        		logger.info('\t'+"Computing object \"lignes\"...");
	    			JsonObject allLignes = (JsonObject)o.getValue();
	        		
	        		for(Entry<String, JsonElement> ligne : allLignes.entrySet())
	        		{
	        			if(((JsonObject)ligne.getValue()).has("arrets")) // arrêts != null : vraie ligne
	        			{        				
		        			Ligne l = g.fromJson(ligne.getValue(), Ligne.class);
		        			logger.trace(l.toString());
		        			// Add to model
		        			world.getLignes().add(l);
	        			}
	        			else         				// pas d'arrêt : ligne de test
	        			{
	        				// nothing happens
	        			}
	        		}
	        		logger.info('\t'+"Object \"lignes\" computed!");
	        		break;
	        	case "corresp":
	        		logger.info('\t'+"Computing object \"corresp\"...");
	        		JsonArray allCorresp = (JsonArray)o.getValue();
	        		ArrayList<ArrayList<String>> correspondances = g.fromJson(allCorresp, new TypeToken<ArrayList<ArrayList<String>>>(){}.getType());
	        		world.setCorresp(correspondances);
	        		logger.info('\t'+"Object \"corresp\" computed!");
	        		break;
	        	case "routes":
	        		logger.info('\t'+"Computing for object \"routes\"...");
	        		JsonArray allRoutes = (JsonArray)o.getValue();
	        		for(JsonElement r : allRoutes)
	        		{
	        			Route route = g.fromJson(r.getAsJsonObject(), Route.class);
	        			logger.trace(route.toString());
	        			// Add to model
	        			world.getRoutes().add(route);
	        		}
	        		logger.info('\t'+"Object \"routes\" computed!");
	        		break;
	    		default:
	    			logger.error('\t'+"Could not compute; unknown object.");
	    			break;
	        	}
	        }
	        logger.info(String.format("Parsing of file %s finished.'\n'Model has been built accordingly.", jsonFile));
		}
		catch (FileNotFoundException e)
		{
			logger.fatal(e);
		}
		return world;
	}

	/**
	 * Cleans a graph of any vertex that is not linked to another vertex.
	 * <p>
	 * @param graph Graph to clean
	 * @return cleaned graph (graph without "lonely" vertices)
	 * @author Tchong-Kite Huam
	 */
	private static Graph removeLoneNode(Graph graph)
	{
		ArrayList<String> toRemove = new ArrayList<String>();
		for(Node n : graph.getNodeSet())
		{
			List<Edge> trueEdge = new ArrayList<Edge>();
			for(Edge e : n.getEdgeSet())
			{
				if(!e.getAttribute("ui.class").equals("Correspondance"))
				{
					trueEdge.add(e);
				}
			}
			if(trueEdge.size() == 0)
			{
				toRemove.add(n.getId());
//				graph.removeNode(n.getId());
			}
		}
		for(String s : toRemove)
		{
			logger.trace(String.format("Removing station: ID=%s", s));
			graph.removeNode(s);
		}
		logger.info("Successfully trimmed the graph by deleting lonely vertices");
		return graph;
	}
	
	
	// *** GETTERS & SETTERS *** //
	
	public static java.nio.file.Path getPathToJson() {
		return pathToJson;
	}

	public static void setPathToJson(java.nio.file.Path pathToJson) {
		Back.pathToJson = pathToJson;
	}
	public static Path getDiameter() {
		return diameter;
	}
	public static void setDiameter(Path d) {
		if(d != null)
		{
			diameter = d;
		}
	}

//	public static Graph getUtilityCluster() {
//		return utilityCluster;
//	}
//
//	public static void setUtilityCluster(Graph utilityCluster) {
//		Back.utilityCluster = utilityCluster;
//	}
//
//	public static Graph getDegreeCluster() {
//		return DegreeCluster;
//	}
//
//	public static void setDegreeCluster(Graph degreeCluster) {
//		DegreeCluster = degreeCluster;
//	}
//
//	public static Graph getDistanceCluster() {
//		return DistanceCluster;
//	}
//
//	public static void setDistanceCluster(Graph distanceCluster) {
//		DistanceCluster = distanceCluster;
//	}
//	
}
