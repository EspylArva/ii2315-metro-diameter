package com.java.project.control;

import java.util.Collection;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import com.java.project.view.MainMenu;
import com.java.project.view.NetworkViewer;

public class ViewControl {
	
	public static void setEnabledButtons(boolean bool)
	{
		MainMenu.getBtn_SimpleWorld().setEnabled(bool);
		MainMenu.getBtn_WeightedWorld().setEnabled(bool);
	}
	
	
	public static void displayPath(Graph graph, Path path, String label)
    {
	    for(Node vertex : path.getNodePath())
	    {
	    	graph.getNode(vertex.getId()).addAttribute("ui.class", label);
	    }
	    for(Edge edge : path.getEdgePath())
	    {
	    	if(graph.getEdge(edge.getId()) != null)
	    	{
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
    			n.setAttribute("ui.class", "node");
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

	

	public static void showPathStationsName(boolean selected, Graph g) {
		if(selected)
		{
			for(Node n : g.getNodeSet())
			{
				if( n.hasAttribute("diameter") )
				{
					n.addAttribute("ui.class", "showName");
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
	
	public static void addLog(String s)
	{
		NetworkViewer.addLogConsoleLine(s);
	}
	
	
	

}
