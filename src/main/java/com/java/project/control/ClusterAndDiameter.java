package com.java.project.control;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.Graphs;

import com.java.project.ii2315.App;
import com.java.project.view.NetworkViewer;

public class ClusterAndDiameter extends Thread {

	private static Logger logger = App.logger;
	NetworkViewer nv;
	private Graph cluster;
	private Graph graph;
	private int minimumCluster;
	
	private Path diameter;
	
	public Path getDiameter() {
		return diameter;
	}


	public void setDiameter(Path diameter) {
		this.diameter = diameter;
	}


	public ClusterAndDiameter(String name,Graph graph, int minimumCluster, NetworkViewer networkViewer) {
		super(name);
//		this.graph = Graphs.clone(graph);
		this.graph = graph;
		this.minimumCluster = minimumCluster;
		this.nv = networkViewer;
	}
	
	
	public void run() {
		
		this.nv.getVc().deleteLogs();
		nv.getVc().computing(true);
		this.cluster = Graphs.clone(this.graph);
		
		HashMap<Edge,Integer> edgeList = new HashMap<Edge,Integer>();    	    	
    	diameter = null;
    	int iteration = 0;
    	
    	AStar astar = new AStar(this.graph);
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
//							nv.getVc().displayPath(graph, path, "path");
//							System.out.println("SHOULD SHOW COMP");
							nv.getVc().dPath(graph, path, "path");
//							nv.getVc().dPath(graph, path, "diameter");
							Thread.sleep(nv.getVc().getDisplayDelay());
//							nv.getVc().returnToOld(graph, path);
							nv.getVc().resetColor(graph);
						}
						catch (Exception e) {
							App.logger.error(e);
//							nv.getVc().resetColor(graph);
						}
					}

					if(diameter == null || path.getNodeCount() > diameter.getNodeCount()) {
						diameter = path;
						
					}
				}
    			
    		}
    		
    	}
    	
    	for(Node n : diameter.getNodePath())
    	{
    		n.addAttribute("diameter");
    		n.addAttribute("ui.class", "diameter");
    	}
    	for(Edge e : diameter.getEdgePath())
    	{
    		e.addAttribute("diameter");
    		e.addAttribute("ui.class", "diameter");
    	}
    	
//    	Back.setDiameter(diameter);
    	logger.info("Diamètre : " + diameter.getNodeCount());
    	logger.info("Diamètre : " + diameter);
    	
    	
    	
    	// -------------- Utility cluster
    	logger.info("Create utility cluster : ");
    	logger.info("| - Cluster minimum Edge value : " + this.minimumCluster );
    	this.cluster = Graphs.clone(this.graph);
    	for(Edge e : edgeList.keySet()) {
    		if(edgeList.get(e) < this.minimumCluster && e.getNode0().getDegree() > 1 && e.getNode1().getDegree() > 1 ) {
    			this.cluster.removeEdge(e.getId());
    			//this.cluster.getEdge(e.getId()).addAttribute("ui.class", "ClusterCut");
    		}
    	}
    	logger.info("| - Cluster Nodes: " + this.cluster.getNodeCount() );
    	logger.info("| - Cluster Edges: " + this.cluster.getEdgeCount() + "/" + this.graph.getEdgeCount());
    	nv.setUtilityCluster(this.cluster);
    	
    	
    	// -------------- Distance cluster with Corentin Celton
    	logger.info("Create distance cluster to Corentin Celton:");
    	this.cluster = Graphs.clone(graph);
    	Node cible = this.graph.getNode("1889");
    	HashMap<Integer,ArrayList<Node>> distances = new HashMap<Integer,ArrayList<Node>>();
    	ArrayList<Node> list;
    	for(Node n : this.cluster.getNodeSet() ) {
    		astar.compute(n.getId(), cible.getId());
    		Path p = astar.getShortestPath();
    		if(p != null) {
    			if(distances.containsKey(p.getNodeCount())) {
        			list = distances.get(p.getNodeCount());
        		}
        		else {
        			list = new ArrayList<Node>();
        		}
        		list.add(n);
    			distances.put(p.getNodeCount(),list);
    		}
    	}
    	for(Edge e : this.cluster.getEachEdge()){
    		this.cluster.removeEdge(e);
    	}
    	for(Integer key : distances.keySet()) {
    		for(Node n : distances.get(key)) {
    			for(Node n2 : distances.get(key)) {
    				if(this.cluster.getEdge("distance_" + key + "_" + n2.getId() +"_" + n.getId()) == null) {
        				this.cluster.addEdge("distance_" + key + "_" + n.getId() +"_" + n2.getId(), n, n2);
    				}
    			}
    		}
    		logger.info("| - Create cluster for distance : " + key + " with " + distances.get(key).size() + " node(s)");
    		
    	}
    	nv.setDistanceCluster(this.cluster);
    	
    	// -------------- Degree cluster
    	logger.info("Create degree cluster :");
    	this.cluster = Graphs.clone(graph);
    	HashMap<Integer,ArrayList<Node>> degrees = new HashMap<Integer,ArrayList<Node>>();
    	for(Node n : this.cluster.getNodeSet() ) {
    		int degree = n.getOutDegree();
			if(degrees.containsKey(degree)) {
    			list = degrees.get(degree);
    		}
    		else {
    			list = new ArrayList<Node>();
    		}
    		list.add(n);
    		degrees.put(degree,list);
		}
    	for(Edge e : this.cluster.getEachEdge()){
    		this.cluster.removeEdge(e);
    	}
    	for(Integer key : degrees.keySet()) {
    		for(Node n : degrees.get(key)) {
    			for(Node n2 : degrees.get(key)) {
    				if(this.cluster.getEdge("degree_" + key + "_" + n2.getId() +"_" + n.getId()) == null) {
    					this.cluster.addEdge("degree_" + key + "_" + n.getId() +"_" + n2.getId(), n, n2);
    				}
        				
    			}
    		}
    		logger.info("| - Create cluster for degree : " + key + " with " + degrees.get(key).size() + " node(s)");
    	}
    	nv.setDegreeCluster(this.cluster);
    		
//    	nv.setGraph(graph);
		nv.getVc().computing(false);
		
	}
	
}
