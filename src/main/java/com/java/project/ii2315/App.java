package com.java.project.ii2315;


import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.Viewer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.java.project.model.Ligne;
import com.java.project.model.Route;
import com.java.project.model.Station;
import com.java.project.model.World;
import com.java.project.view.Frame;
import com.java.project.view.MainMenu;

public class App 
{

	public static Logger logger = Logger.getLogger(App.class);
	public static java.nio.file.Path pathToJson;
	
    public static void main( String[] args ) throws IOException,
    ClassNotFoundException , InstantiationException ,
	IllegalAccessException , UnsupportedLookAndFeelException
    {
    	// *** DEV CONF *** //
    	// log4j
    	PropertyConfigurator.configure(Paths.get(".").toAbsolutePath() + "\\src\\main\\resources\\log4j.properties");
    	
    	// *** RUN *** //
    	// GUI: MainMenu
    	SwingUtilities.invokeLater(new Runnable()
		{	
			@Override
			public void run() {
				try
				{
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    new MainMenu();
                }
				catch (ClassNotFoundException | InstantiationException |
						IllegalAccessException | UnsupportedLookAndFeelException ex)
				{
					logger.fatal(ex);
                }
			}
		});
    	
    	
        // *** PARSAGE *** //
//    	
////        pathToJson = (Paths.get(".").toAbsolutePath() + "\\src\\main\\resources\\reseau.json");
//    	App.pathToJson = Paths.get("src","main","resources","reseau.json").toAbsolutePath();
////    	pathToJson = null;
//        parseWorld(pathToJson);
////        System.out.println(App.class.getResource("/reseau.json").toString());
//		// *** BUILDING MODEL *** //
//        Graph graph = buildSimpleWorld();
////        Graph graph = buildSimplePartialWorld("B", "A");
//        
//        // *** GRAPHICAL CONFIGURATION *** //
//		configureGraphUI(graph);
//        
//        // *** DISPLAY GRAPH *** //
//		logger.info("Displaying graph");
//        Viewer viewer = graph.display();
//        
//        
//        AStar astar = new AStar(graph);
//        astar.compute("1621", "B_1998");
//        Path path = astar.getShortestPath();
//        List<Node> stations = astar.getShortestPath().getNodePath();
//        System.out.println(path);
//        for(Node station : stations)
//        {
//        	System.out.println(station.getId() + ": " + station.getAttribute("nom"));
//        }
        
     }
}
