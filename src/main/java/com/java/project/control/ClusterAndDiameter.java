package com.java.project.control;


import java.util.HashMap;

import org.apache.log4j.Logger;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import com.java.project.ii2315.App;
import com.java.project.view.DragAndDrop_JsonFile;
import com.java.project.view.NetworkViewer;

public class ClusterAndDiameter extends Thread {

	private static Logger logger = App.logger;
	NetworkViewer nv;
	private Graph cluster;
	private Graph graph;
	private int minimumCluster;
	
	public ClusterAndDiameter(String name,Graph graph, int minimumCluster, NetworkViewer networkViewer) {
		super(name);
		this.cluster = graph; // should get a clone
		this.graph = graph;
		this.minimumCluster = minimumCluster;
		this.nv = networkViewer;
	}
	
	
	public void run() {
		
		this.nv.getVc().deleteLogs();
		nv.getVc().computing(true);
		
		HashMap<Edge,Integer> edgeList = new HashMap<Edge,Integer>();    	    	
    	Path diameter = null;
    	int iteration = 0;
    	
    	AStar astar = new AStar(this.cluster);
    	astar.setCosts(new AStar.DefaultCosts("distance"));
    	
    	for(Node start : this.cluster.getEachNode()) {
    		
    		for(Node end : this.cluster.getEachNode()) {
    		
				astar.compute(start.getId(), end.getId());
				Path path = astar.getShortestPath();
				
				iteration += 1;
				if(iteration % 50000 == 0) {
					logger.info("Cluster et diamètre thread itérations : " + iteration);
				}
				
				if(path != null ) {

					for(Edge e : path.getEdgeSet()) {
						if(edgeList.containsKey(e)) {
							edgeList.put(e, edgeList.get(e) + 1);
						}
						else {
							edgeList.put(e, 1);
						}
					}
	
//					NetworkViewer.addLogConsoleLine("a");
					nv.getVc().addLog(String.format("(%s) Computing path between %s and %s",
//							String.valueOf(0),"a","b"							
							path.size(),start.getAttribute("nom"),end.getAttribute("nom")
							));
					if(nv.getVc().isShowComputation())
					{						
						try
						{
							ViewControl.displayPath(graph, path, "path");
							Thread.sleep(nv.getVc().getDisplayDelay());
							ViewControl.resetColor(graph);
						}
						catch (Exception e) {
							App.logger.error(e);
							ViewControl.resetColor(graph);
						}
					}
					
					
					if(diameter == null || path.getNodeCount() > diameter.getNodeCount()) {
						diameter = path;
						
					}
				}
    			
    		}
    		
    	}
    	for(Node n : diameter.getNodeSet())
    	{
    		n.addAttribute("diameter", true);
    	}
    	Back.setDiameter(diameter);
    	logger.info("Diamètre : " + Back.getDiameter().getNodeCount());
    	logger.info("Diamètre : " + Back.getDiameter());
    	
    	logger.info("Graph Nodes: " + this.cluster.getNodeCount() );
    	logger.info("Graph Edges: " + this.cluster.getEdgeCount() );
    	logger.info("Nombre d'AStar : " + iteration);
    	
    	for(Edge e : edgeList.keySet()) {
    		if(edgeList.get(e) < this.minimumCluster) {
//    			this.cluster.removeEdge(e);
//    			this.cluster.getEdge(e.getId()).addAttribute("ui.class", "ClusterCut");
    		}
    	}
    	
    	
    	Back.setCluster(this.cluster);
    	logger.info("Cluster minimum Edge value : " + this.minimumCluster );
    	logger.info("Cluster Nodes: " + Back.getCluster().getNodeCount() );
    	logger.info("Cluster Edges: " + Back.getCluster().getEdgeCount() );
  
		nv.getVc().computing(false);
	}
	
}
