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
	
	public void returnToOld(Graph g, Path p)
	{
		for(Node n : p.getNodeSet()) // REMOVE ALL LABEL X
		{
			Node node = g.getNode(n.getId());
			node.addAttribute("ui.class", n.getAttribute("oldClass"));
		}
		for(Edge e : p.getEdgeSet()) // REMOVE ALL LABEL X
		{
			Edge edge = g.getEdge(e.getId());
			if(!e.getAttribute("oldClass").equals("diameter"))
			{
				edge.addAttribute("ui.class", e.getAttribute("ligne"));
			}
			else
			{
				edge.addAttribute("ui.class", e.getAttribute("oldClass"));
			}
		}
	}
	
	public void dPath(Graph g, Path p, String l)
	{
		if(l.equals("path"))
		{
			for(Node n : p.getNodeSet()) // REMOVE ALL LABEL X
			{
				if(n.hasAttribute("ui.class"))
				{
					Node node = g.getNode(n.getId());
					node.addAttribute("oldClass", n.getAttribute("ui.class"));
					node.addAttribute("ui.class", "path");
				}
			}
			for(Edge e : p.getEdgeSet()) // REMOVE ALL LABEL X
			{
				if(e.hasAttribute("ui.class"))
				{
					Edge edge = g.getEdge(e.getId());
					edge.addAttribute("oldClass", e.getAttribute("ligne"));
					edge.addAttribute("ui.class", "path");
				}
			}
		}
		else // Cas "normal" et "diameter"
		{
			for(Node n : p.getNodeSet()) 
			{
				Node node = g.getNode(n.getId());
				node.addAttribute("ui.class", l);
				node.addAttribute(l);
			}
			for(Edge e : p.getEdgeSet()) 
			{
				Edge edge = g.getEdge(e.getId());
				if(l.equals("n"))
				{
					edge.addAttribute("ui.class", e.getAttribute("ligne"));
				}
				else
				{
					edge.addAttribute("ui.class", l);
				}
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
    			else
    			{
    				n.addAttribute("ui.class", "n");
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
		if(selected)
		{
			dPath(g, diam, "diameter");
		}
		else
		{
			dPath(g, diam, "n");
		}
	} //OK
	
	

	
	public void showStationsName(boolean selected, Graph g) {
		if(selected)
		{
			for(Node n : g.getNodeSet())
			{
				if(n.hasAttribute("nom"))
				{
					n.addAttribute("ui.label", n.getAttribute("nom"));
				}
				else
				{
					n.addAttribute("ui.label", "null");
				}
			}
		}
		else
		{
			for(Node n : g.getNodeSet())
			{
				n.removeAttribute("ui.label");
			}
		}
	}//OK

	public void showPathStationsName(boolean selected, Graph g)
	{
		if(selected)
		{
			for(Node n : g.getNodeSet())
			{
				if(n.hasAttribute("diameter") ||  n.hasAttribute("path") || 
				n.getAttribute("ui.class").equals("diameter") || n.getAttribute("ui.class").equals("path"))
				{
					if(n.hasAttribute("nom")) { n.addAttribute("ui.label", n.getAttribute("nom")); }
					else { n.addAttribute("ui.label", "null"); }
				}
			}
		}
		else
		{
			for(Node n : g.getNodeSet())
			{
				n.removeAttribute("ui.label");
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
