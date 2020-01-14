package com.java.project.control;

import java.nio.file.Paths;
import java.util.Collection;

import org.graphstream.graph.Edge;
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
	
	
	public static void displayPath(Graph graph, Path path, String label)
    {	
	    for(Node vertex : path.getNodePath())
	    {
	    	if(graph.getNode(vertex.getId()) != null)
	    	{
	    		graph.getNode(vertex.getId()).addAttribute(label);
	    		graph.getNode(vertex.getId()).addAttribute("ui.class", label);
	    	}
	    }
	    for(Edge edge : path.getEdgePath())
	    {
	    	if(graph.getEdge(edge.getId()) != null)
	    	{
	    		graph.getEdge(edge.getId()).addAttribute(label);
	    		graph.getEdge(edge.getId()).addAttribute("ui.class", label);
	    	}
	    }
    }
	
	public static void resetColor(Graph graph) {
		Collection<Node> gVertices = graph.getNodeSet();
    	Collection<Edge> gEdges = graph.getEdgeSet();
    	for(Node n : gVertices)
    	{
    		if(n.hasAttribute("ui.class"))
    		{
    			n.setAttribute("ui.class");
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


	public static void showDiameter(boolean selected, Graph g, Path diam) {
		if(selected)
		{
			ViewControl.displayPath(g,diam,"diameter");
		}
		else
		{
			ViewControl.resetColor(g);
		}
	}

	

	public void showPathStationsName(boolean selected, Graph g) {
		if(selected)
		{
			for(Node n : g.getNodeSet())
			{
				if( n.hasAttribute("diameter") || (n.getAttribute("ui.class") != null  && n.getAttribute("ui.class").equals("path")) )
				{
					n.addAttribute("ui.class", "showName");
//					n.addAttribute("ui.label");
//					n.addAttribute("ui.style", "text-mode:normal;");
				}
			}
		}
		else
		{
			for(Node n : g.getNodeSet())
			{
				if( n.hasAttribute("diameter") || (n.getAttribute("ui.class") != null  && n.getAttribute("ui.class").equals("path")) )
				{
					n.addAttribute("ui.class", "showName");
//					n.removeAttribute("ui.style");
				}
			}
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

	public void addTitleToWindow(String string) {
		this.networkViewer.setAdditionalTitle(string);
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

}
