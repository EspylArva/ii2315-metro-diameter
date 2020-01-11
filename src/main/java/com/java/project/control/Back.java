package com.java.project.control;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;

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
import com.java.project.view.NetworkViewer;

public class Back {
	
	private static final Gson g = new Gson();
	private static Logger logger = App.logger;
	public static final Object lock = new Object();

	
	private static java.nio.file.Path pathToJson;
	
    public static Graph computeWeightlessGraph()
    {
    	Graph graph = null;
    	// *** BUILDING MODEL *** //
    	if(!World.isBuilt())
    	{
	    	parseWorld(getPathToJson());
	    	World.setBuilt(true);
    	}
    	graph = buildSimpleWorld();
//      Graph graph = buildSimplePartialWorld("B", "A");
      
    	// *** GRAPHICAL CONFIGURATION *** //
		configureGraphUI(graph);
      
		// *** DISPLAY GRAPH *** //
		logger.info("Displaying graph");
		
		
//		addWeights(graph);
		

    	
    	
//    	org.graphstream.ui.view.Viewer viewer = graph.display();
    	return graph;
    }
    
//     public static void computeDiameter(Graph graph)
//    {
//    	logger.info("Début du calcul de diamètre...");
//    	Path path = findDiameter(graph);
//    	logger.info("Diamètre : " + path.getNodeCount());
//    	logger.info("Diamètre : " + path);
////    	displayPath(graph, path, "diameter");
//    }
    
    
    public static void addWeights(Graph graph)
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
            logger.trace(formula);
            
            
            e.addAttribute("distance",formula);
//            e.addAttribute("ui.label",formula);
    		
            
    	}
    }

    /*
     * Fonction qui retourne le Path du diamètre 
     * Input : Graph graph, le graphe où chercher
     * Output : Path diameter, le Path du diamètre
     */
    public static synchronized Path findDiameter(Graph graph, Thread thread, boolean show) {
    	
    	
    	// ArrayList qui va contenir tous les paths testés
    	ArrayList<Path> history = new ArrayList<Path>();
    	
    	// Boolean qui indique s'il on a déjà le chemin dans un essai précédent
    	boolean skip;
    	
    	// Path du diamètre
    	Path diameter = null;
    	
    	// Nombre d'itération d'A* pour information
    	int iteration = 0;
    	
    	// On créer et paramètre un A*
    	AStar astar = new AStar(graph);
    	astar.setCosts(new AStar.DefaultCosts("distance"));
    	
    	// Pour chaque noeud du graphe en départ
    	for(Node start : graph.getEachNode()) {
    		
    		// On l'associe a tous les autres en fin
    		for(Node end : graph.getEachNode()) {
    			skip = false;
    			
    			// Pour chaque path de l'historique
    			for(Path p : history) {
    				
    				// Si le départ et l'arrivée sont dans un des chemins alors inutile de faire un A*
        			if(p.contains(start) && p.contains(end)) {
        				skip = true;
        				break;
        			}
    			}
    			
    			// Si nous devont rechercher le path
    			if(!skip) {
    				
    				// On paramètre A* avec  notre départ et arrivée
					astar.compute(start.getId(), end.getId());
					Path path = astar.getShortestPath();
					String pathSize;
					if(path != null)
					{
						pathSize = String.valueOf(path.size());
					}
					else
					{
						pathSize = "+INF";
					}
					NetworkViewer.addLogConsoleLine(String.format("(Length: %s) Computing shortest path between %s and %s",
							pathSize,
							start.getAttribute("nom"),
							end.getAttribute("nom")));
					
					if(show)
					{
//						synchronized(lock)
//						{
//								thread = new Thread(new MonThread(graph,path));
//								thread.run();//*/
//						}
						
//						displayPath(graph, path, "path");
//						try {
//							Back.class.wait(10);
//						} catch (InterruptedException e) { e.printStackTrace(); }
//						resetColor(graph, path);

					}
					
					iteration += 1;

					// Si le path est non null
					if(path != null) {
	
						// Ajout a l'historique 
						history.add(path);
	
						// Si le path est plus long que le diamètre actuel alors le path devient le diamètre
						if(diameter == null || path.getNodeCount() > diameter.getNodeCount()) {
							diameter = path;
						}
				}
    		    	
    			}
    			
    		}
    		
    	}
    	logger.info("Nombre d'itération d'A* pour trouver le diamètre : " + iteration);
    	
    	for(Node n : diameter.getNodeSet())
    	{
    		graph.getNode(n.getId()).addAttribute("diameter", true);
    	}
    	
    	return diameter;
    	
    }
	
    public static Graph displayPath(Graph graph, Path path, String label)
    {
	    List<Node> vertices = path.getNodePath();
	    List<Edge> edges = path.getEdgePath();
	    
	    for(Node vertex : vertices)
	    {
	    	graph.getNode(vertex.getId()).addAttribute("ui.class", label);
	    	
	    }
	    for(Edge edge : edges)
	    {
	    	graph.getEdge(edge.getId()).addAttribute("ui.class", label);
	    }
	    return graph;
    }
    
    public static Graph resetColor(Graph graph, Path path)
    {
    	List<Node> vertices = path.getNodePath();
	    List<Edge> edges = path.getEdgePath();
	    
	    for(Node vertex : vertices)
	    {
	    	graph.getNode(vertex.getId()).removeAttribute("ui.class");
	    	
	    }
	    for(Edge edge : edges)
	    {
	    	String colorLabel = graph.getEdge(edge.getId()).getAttribute("ligne");
	    	graph.getEdge(edge.getId()).addAttribute("ui.class", colorLabel);
	    }
	    
	    return graph;
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
        graph = removeLoneNode(graph);
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

	public static void showStationsName(boolean selected, Graph g) {
		if(selected)
		{
			for(Node n : g.getNodeSet())
			{
				n.addAttribute("ui.class", "showName");
			}
		}
		else
		{
			for(Node n : g.getNodeSet())
			{
				n.changeAttribute("ui.class", "node");
			}
		}
	}
	public static void showPathStationsName(boolean selected, Graph g)
	{
		if(selected)
		{
			for(Node n : g.getNodeSet())
			{
				if(n.hasAttribute("diameter") || n.hasAttribute("path"))
				{					
					if(n.getAttribute("diameter").equals(true) || n.getAttribute("path").equals(true))
					{
						n.addAttribute("ui.class", "showName");
					}
				}
			}
		}
		else
		{
			for(Node n : g.getNodeSet())
			{
				n.changeAttribute("ui.class", "node");
			}
		}
	}
	
	public static void showDistances(boolean selected, Graph g) {
		if(selected)
		{
			for(Edge e : g.getEdgeSet())
			{
				if(e.hasAttribute("distance"))
				{
					e.addAttribute("ui.label", e.getAttribute("distance"));
				}
				else
				{
					e.addAttribute("ui.label", 1);
				}
			}
		}
		else
		{
			for(Edge e : g.getEdgeSet())
			{
				e.removeAttribute("ui.label");
			}
		}
	}
	
	public static void showDiameter(boolean show, Graph g, Path p)
	{
		if(show)
		{
			displayPath(g, p, "diameter");
		}
		else
		{
	    	g = resetColor(g, p);
		}
	}
	
}
