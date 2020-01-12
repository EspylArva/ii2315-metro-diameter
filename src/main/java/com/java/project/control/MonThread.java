package com.java.project.control;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Path;

public class MonThread implements Runnable{
	private Graph graph;
	private Path path;

	
	public MonThread(Graph graph, Path path) {
	      this.graph = graph; this.path = path;
	    }
	
	@Override
	public void run() {
//		Back.displayPath(graph, path, "path");
//		try {
//			Back.lock.wait(100);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.out.println(path.getEdgeSet());
//		Back.resetColor(graph, path);
//		Back.lock.notifyAll();
	}

}
