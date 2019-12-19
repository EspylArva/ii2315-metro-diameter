package com.java.project.control;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.graphstream.algorithm.AStar;
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
import com.java.project.view.MainMenu;

public class Back {
	
	private static final Gson g = new Gson();
	private static Logger logger = App.logger;
	
	private static java.nio.file.Path pathToJson;
	
    public static Graph compute()
    {
    	parseWorld(getPathToJson());
		// *** BUILDING MODEL *** //
    	Graph graph = buildSimpleWorld();
//      Graph graph = buildSimplePartialWorld("B", "A");
      
    	// *** GRAPHICAL CONFIGURATION *** //
		configureGraphUI(graph);
      
		// *** DISPLAY GRAPH *** //
		logger.info("Displaying graph");
		
		
		addWeights(graph);
		
    	AStar astar = new AStar(graph);
    	astar.setCosts(new AStar.DefaultCosts("distance"));
    	astar.compute("1621", "B_1998");
    	Path path = astar.getShortestPath();
    	displayPath(graph, path);
	    

    	

    	
	    astar.compute("1889", "1839");

    	path = astar.getShortestPath();
    	for(Node n : path.getNodePath())
    	{
    		System.out.println("name " + n.getAttribute("nom").toString());
    	}
    	
    	displayPath(graph, path);
    	for (Node n:path.getNodePath()) {
    		System.out.println(n.getAttribute("nom"));
    	}
    	System.out.println("Diamètre : " + org.graphstream.algorithm.Toolkit.diameter(graph));
    	
    	
    	
//    	org.graphstream.ui.view.Viewer viewer = graph.display();
    	return graph;
    }
    
    
    private static void addWeights(Graph graph)
    {
    	Collection<Edge> edges = graph.getEdgeSet();
    	for(Edge e : edges)
    	{
    		Node s1 = e.getSourceNode();
    		Node s2 = e.getTargetNode();
    		
    	
    			        		
            double Xs1 = s1.getAttribute("latitude");
            double Ys1 =s1.getAttribute("longitude");
            
            double Xs2 = s2.getAttribute("latitude");
            double Ys2 =s2.getAttribute("longitude");
            
            
            
            double formula = Math.sqrt(Math.pow(Xs1-Xs2, 2)+Math.pow(Ys1-Ys2, 2));
            System.out.println(formula);
            
            
            e.addAttribute("distance",formula);
//            e.addAttribute("ui.label",formula);
    		
            
    	}
    }

    /**
    * Récupère le plus long des plus court chemin
    * Todo faire un check de la distance pour raccourcir l'execution
    * gérer le java.lang.OutOfMemoryError: Java heap space ^^
    * @param graph
    * @param start
    * @param end
    * @return
    */
    public static Path longShortestPath(Graph graph,String start, String end) {
    	System.out.println("-----TEST-----");
    	Node root = graph.getNode(start);
    	Node goal = graph.getNode(end);
    	ArrayList<ArrayList<Node>> completePath = new ArrayList<ArrayList<Node>>();
    	Stack<ArrayList<Node>> todo = new Stack<ArrayList<Node>>();
    	
    	
    	// initialisation ddu chemin de départ -- a supprimer
    	ArrayList<Node> tmp = null;
		for(Edge e : root.getEachLeavingEdge()) {
			Node neighbor = e.getTargetNode();
			tmp = new ArrayList<Node>();
			tmp.add(root);
			tmp.add(neighbor);
			todo.push(tmp);
			System.out.println(neighbor.getAttribute("nom"));
		}
		System.out.println(todo);
		
		// on parcours chaque node pour trouver un chemin possible
		ArrayList<Node> newPath = null;
		while(!todo.empty()) {
			tmp = todo.peek();
			Node last = tmp.get(tmp.size()-1);
			for(Edge e : last.getEachLeavingEdge()) {
				Node neighbor = e.getTargetNode();
				newPath = (ArrayList<Node>) tmp.clone();
				newPath.add(neighbor);
				if(neighbor == goal) {
					completePath.add(newPath);
				}
				else {
					todo.push(newPath);
				}
			}
		}
	    System.out.println("-----TEST-----");
	    return null;
    }

    public static void displayPath(Graph graph, Path path)
    {
    	Collection<Node> gVertices = graph.getNodeSet();
    	Collection<Edge> gEdges = graph.getEdgeSet();
    	for(Node n : gVertices)
    	{
    		if("path".equals(n.getLabel("ui.class")))
    		{
    			System.out.println("Path detected (n)");
    			n.removeAttribute("ui.class");
    		}
    	}
    	for(Edge e : gEdges)
    	{
    		if("path".equals(e.getLabel("ui.class")))
    		{
    			System.out.println("Path detected (e)");
    			e.removeAttribute("ui.class");
    		}
    	}
    	
    	
	    List<Node> vertices = path.getNodePath();
	    List<Edge> edges = path.getEdgePath();
	    App.logger.debug(String.format("Shortest path from %s to %s: %s",path.getNodePath().get(0),path.getNodePath().get(path.getNodePath().size()-1) , path));
	    for(Node vertex : vertices)
	    {
	    	graph.getNode(vertex.getId()).addAttribute("ui.class", "path");
	    	
	    }
	    for(Edge edge : edges)
	    {
	    	graph.getEdge(edge.getId()).addAttribute("ui.class", "path");
	    }
    }


	/**
     * Using better setups for the graphical display of the graph.
     * See <a href="http://graphstream-project.org/doc/FAQ/Attributes/Is-there-a-list-of-attributes-with-a-predefined-meaning-for-the-layout-algorithms/"> GraphStream documentation</a> for more informations.
     * <p>
     * @param graph Graph to work on
     * @author Tchong-Kite Huam
     */
	private static void configureGraphUI(Graph graph) {
		// Setting up the .css file for GraphStream implementation
		// Necessary for dynamic coloring	
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph.addAttribute("ui.stylesheet", String.format("url('%s')", Paths.get(".").toAbsolutePath() + "\\src\\main\\resources\\graph-style.css"));
		
		graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
    	graph.addAttribute("layout.weight", 100);
    	
    	logger.info("Successfully upgraded graphical display of the map");
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
    private static Graph buildSimplePartialWorld(String... args )
    {
    	// Liste des lignes à mettre
    	
    	Graph graph = new MultiGraph("Plan du Métro");
    	
    	WorldControl.buildStations(graph);
		WorldControl.buildCorrespondances(graph);
		
		ArrayList<String> partialWorldSetup = new ArrayList<String>(); for(String s : args)partialWorldSetup.add(s);
        
		WorldControl.buildLignes(graph, partialWorldSetup);
		
        // Clean the graph of useless stations
        return removeLoneNode(graph);
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
        Graph graph = new MultiGraph("Plan du Métro");
        WorldControl.buildStations(graph);
        WorldControl.buildCorrespondances(graph);
        WorldControl.buildLignes(graph, null);   
        return graph;
		
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
	
	// *** GUI *** //
	public static void setEnabledButtons(boolean bool)
	{
		MainMenu.getBtn_SimpleWorld().setEnabled(bool);
		MainMenu.getBtn_WeightedWorld().setEnabled(bool);
	}
	
	// *** GETTERS & SETTERS *** //
	
	public static java.nio.file.Path getPathToJson() {
		return pathToJson;
	}

	public static void setPathToJson(java.nio.file.Path pathToJson) {
		Back.pathToJson = pathToJson;
	}
	
}
