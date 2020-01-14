package com.java.project.control;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import com.java.project.ii2315.App;
import com.java.project.view.MainMenu;
import com.java.project.view.NetworkViewer;

public class ViewControl {
	
	private boolean freeze = false;
	private boolean showComputation = false;
	private long delay = 0;
	private boolean computationFinished = true;
	
	private NetworkViewer networkViewer;
	private ClusterAndDiameter thread;
	
	public ViewControl(NetworkViewer networkViewer) {
		this.networkViewer = networkViewer;
	}

	/**
     * Using better setups for the graphical display of the graph.
     * See <a href="http://graphstream-project.org/doc/FAQ/Attributes/Is-there-a-list-of-attributes-with-a-predefined-meaning-for-the-layout-algorithms/"> GraphStream documentation</a> for more informations.
     * <p>
     * @param graph Graph to work on
     * @author Tchong-Kite Huam
     */
	public void configureGraphUI(Graph graph) {
		// Setting up the .css file for GraphStream implementation
		// Necessary for dynamic coloring	
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph.addAttribute("ui.stylesheet", String.format("url('%s')", Paths.get(".").toAbsolutePath() + "\\src\\main\\resources\\graph-style.css"));
		
		graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
    	graph.addAttribute("layout.weight", 100);
    	
    	App.logger.info("Successfully upgraded graphical display of the map");
	}
	
	public void freezeWorld(Graph g)
	{
		if(freeze)
		{	
			for(Node n : g.getNodeSet())
			{
				n.addAttribute("layout.frozen");
			}
		}
		else
		{
			for(Node n : g.getNodeSet())
			{
				n.removeAttribute("layout.frozen");
			}
		}
	}
	
	
	public static void setEnabledButtons(boolean bool)
	{
		MainMenu.getBtn_SimpleWorld().setEnabled(bool);
		MainMenu.getBtn_WeightedWorld().setEnabled(bool);
		MainMenu.getUpDown_Freeze().setEnabled(bool);
	}
	
	
	public void displayPath(Graph graph, Path path, String label)
    {	
	    for(Node vertex : path.getNodePath())
	    {
	    	if(graph.getNode(vertex.getId()) != null)
	    	{
//	    		graph.getNode(vertex.getId()).addAttribute("old", graph.getNode(vertex.getId()).getAttribute("ui.class"));
	    		graph.getNode(vertex.getId()).addAttribute(label);
	    		graph.getNode(vertex.getId()).addAttribute("ui.class", label);
//	    		System.out.println("-" + graph.getNode(vertex.getId()).getAttribute("ui.class"));
//	    		addTag(graph.getNode(vertex.getId()),label);
	    	}
	    }
	    for(Edge edge : path.getEdgePath())
	    {
	    	if(graph.getEdge(edge.getId()) != null)
	    	{
	    		graph.getEdge(edge.getId()).addAttribute(label);
	    		graph.getEdge(edge.getId()).addAttribute("ui.class", label);
//	    		addTag(graph.getEdge(edge.getId()),label);
	    	}
	    }
    }
	
	public void resetColor(Graph graph) {
		Collection<Node> gVertices = graph.getNodeSet();
    	Collection<Edge> gEdges = graph.getEdgeSet();
    	for(Node n : gVertices)
    	{
    		if(n.hasAttribute("ui.class"))
    		{
    			if(n.hasAttribute("old"))
    			{
    				n.setAttribute("ui.class", n.getAttribute("old"));
    			}
    		}
    	}
    	for(Edge e : gEdges)
    	{
    		if(e.hasAttribute("ui.class"))
    		{
    			e.setAttribute("ui.class", e.getAttribute("ligne"));
    		}
    	}
	}
	
	public void showDistances(boolean selected, Graph g) {
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
	}//OK


	public void showDiameter(boolean selected, Graph g, Path diam) {
//		if(selected)
//		{
////			displayPath(g , diam , "diameter");
//			for(Node n : g.getNodeSet())
//			{
//				addTag(n,"diameter");
////				addTag(n,"normal");
//			}
//			for(Edge e : g.getEdgeSet())
//			{
//				addTag(e, "diameter");
//			}
//		}
//		else
//		{
//			resetColor(g);
//			for(Node n : g.getNodeSet())
//			{
//				removeTag(n,"diameter");
////				addTag(n,"normal");
//			}
//			for(Edge e : g.getEdgeSet())
//			{
//				removeTag(e, "diameter");
//			}
//		}
		displayPath(g,diam,"diameter");
	} 
	
	public void showStationsName(boolean selected, Graph g) {
		if(selected)
		{
			for(Node n : g.getNodeSet())
			{
//				n.addAttribute("ui.class", "showName");
				addTag(n, "showName");
			}
		}
		else
		{
			for(Node n : g.getNodeSet())
			{
//				n.addAttribute("ui.class", "nshowName");
//				removeTag(n,"showName");
				addTag(n, "nshowName");
			}
		}
	}

	public void showPathStationsName(boolean selected, Graph g) {
//		this.resetColor(g);
		if(selected)
		{
			for(Node n : g.getNodeSet())
			{
				if(n.hasAttribute("diameter") || n.hasAttribute("path"))
				{
					addTag(n, "showName");
				}
			}
		}
		else
		{
			for(Node n : g.getNodeSet())
			{
				if(n.hasAttribute("diameter") || n.hasAttribute("path"))
				{
					addTag(n, "nshowName");
				}
			}
		}
		
	}
	
	public static void removeTag(Element e, String tag)
	{
		if(e.hasAttribute("ui.class"))
		{
			ArrayList<String> classes = new ArrayList<String>(Arrays.asList(e.getAttribute("ui.class").toString().split(",")));
			if(classes.size() > 0)
			{
				switch(tag)
				{
					case "showName":
					case "nshowName" :
						classes.remove("nshowName");
						classes.remove("showName");
						break;
					case "path":
					case "diameter":
						classes.remove("diameter");
						classes.remove("path");
						break;
					default:
						break;
				}
			}
			if(classes.size() == 1)
			{
				e.setAttribute("ui.class", classes.get(0));
			}
		}
		else
		{
			return;
		}
	}
	
	public static void addTag(Element n, String tag)
	{
		/**
		 * Tags possibles :
		 * showName
		 * nshowName
		 * path
		 * diameter
		 */
		if(n.hasAttribute("ui.class"))
		{
			String[] ar = n.getAttribute("ui.class").toString().split(",");
			ArrayList<String> classes;
			if(ar.length > 1)
			{				
				classes = new ArrayList<String>(Arrays.asList(ar));
			}
			else
			{
				classes = new ArrayList<String>(); classes.add(ar[0]);
			}
			if(classes.size() > 0)
			{
				switch(tag)
				{
					case "normal":
						if(classes.contains("diameter")){ classes.remove("diameter"); }	
						if(classes.contains("path")){ classes.remove("path"); }
						classes.add("normal"); break;
					case "showName":
						if(classes.contains("nshowName")){ classes.remove("nshowName"); }
						classes.add("showName"); break;
					case "nshowName" :
						if(classes.contains("showName")){ classes.remove("showName"); }	
						classes.add("nshowName"); break;
					case "path":
						if(classes.contains("diameter")){ classes.remove("diameter"); }	
						classes.add("path"); break;
					case "diameter":
						if(classes.contains("path")){ classes.remove("path"); }
						classes.add("diameter"); break;
					default:
						break;
				}
			}
			n.addAttribute("ui.class", String.join(",", classes));
		}
		else
		{
			n.addAttribute("ui.class", tag);
		}
	}
	
	public void addLog(String s)
	{
		this.networkViewer.addLogConsoleLine(s);
	}


	// *** GETTERS & SETTERS *** //
	
	public  boolean isFreeze() {
		return freeze;
	}
	public  void setFreeze(boolean freeze) {
		this.freeze = freeze;
	}

	public  boolean isShowComputation() {
		return showComputation;
	}

	public  void setShowComputation(boolean showComputation) {
		this.showComputation = showComputation;
	}

	public long getDisplayDelay() {
		return delay;
	}	
	public void setDisplayDelay(long d)
	{
		delay = d;
	}
	
	
	public void computing(boolean computing)
	{
		computationFinished = !computing;
		networkViewer.getComputeButton().setEnabled(!computing);
		networkViewer.getClusterButton().setEnabled(!computing);
	}
	
	public boolean isComputationFinished()
	{
		return computationFinished;
	}
	
	public void setComputationFinished(boolean b)
	{
		computationFinished = b;
	}

	public void deleteLogs() {
		this.networkViewer.deleteLogs();	
	}

	public ClusterAndDiameter getThread() {
		return thread;
	}

	public void setThread(ClusterAndDiameter thread) {
		this.thread = thread;
	}

	public void resetTag(String string, Graph g)
	{
		for(Node n : g.getNodeSet())
		{
			if(n.hasAttribute(string))
			{
				n.removeAttribute(string);
			}
		}
	}

}
