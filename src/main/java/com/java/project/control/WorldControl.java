package com.java.project.control;

import java.util.ArrayList;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import com.java.project.ii2315.App;
import com.java.project.model.Ligne;
import com.java.project.model.Station;
import com.java.project.model.World;

public class WorldControl {
	

	/**
	 * Build correspondances in the graph accordingly to the model of the world.
	 * <p>
	 * @param graph Graph which needs to get the correspondances
	 * @author Tchong-Kite Huam
	 */
	public static void buildCorrespondances(Graph graph)
	{
		try
		{
			for(ArrayList<String> correspondance : World.getInstance().getCorresp())
			{
				for(int i=0; i<correspondance.size()-1 ; i++)
				{
					Edge e = graph.addEdge(String.format("%s-%s", correspondance.get(i), correspondance.get(i+1)),
							correspondance.get(i), correspondance.get(i+1));
					e.addAttribute("ui.class", "Correspondance");
				}
			}
			App.logger.info("Successfully built all correspondances");
		}
		catch(Exception e) {App.logger.error(e);}
	}
	
	/**
	 * Build stations in the graph accordingly to the model of the world.
	 * <p>
	 * @param graph Graph which needs to get the stations
	 * @author Tchong-Kite Huam
	 */
	public static void buildStations(Graph graph)
	{
		try
        {
    		for(Station station : World.getInstance().getStations())
    		{
    			Node n = graph.addNode(station.getNum());
    			n.addAttribute("nom", station.getNom());
    			n.addAttribute("ui.label", station.getNom());
//    			n.addAttribute("ui.label", station.num);
    			n.addAttribute("latitude", station.getLat());
    			n.addAttribute("longitude", station.getLng());
    			
//    			n.addAttribute("layout.frozen");
    			n.addAttribute("xy", station.getLng(),station.getLat());
    			
    			}
    		App.logger.info("Successfully built all stations");
        }
        catch(Exception e) {App.logger.error(e);}
	}
	
	/**
	 * Build required lines in the graph accordingly to the model of the world.
	 * <p>
	 * @param graph Graph which needs to get the lines
	 * @param lignes Lines to add to the graph. If lignes is null, all the available lines will be added.
	 * @author Tchong-Kite Huam
	 */
	public static void buildLignes(Graph graph, ArrayList<String> lignes)
	{
		if(lignes == null)
		{
			App.logger.trace("Building all the lines");	
		}
		else
		{
			App.logger.trace(String.format("Only building set lines: %s", lignes));			
		}
		try
        {
        	for(Ligne line : World.getInstance().getLignes())
	        {
        		if(lignes == null || lignes.contains(line.getNum()))
        		{
        			App.logger.trace(String.format("Building %s %s, going to %s", line.getType(), line.getNum() , line.getName()));
		        	for(ArrayList<String> branch : line.getArrets())
		        	{
		        		App.logger.trace(String.format("Switching branch in %s %s", line.getType(), line.getNum()));
			        	for(int i=0 ; i<branch.size()-1 ; i++)
			        	{
			        		if(graph.getEdge(branch.get(i) + "-" + branch.get(i+1)) == null)
			        		{
			        			Edge e = graph.addEdge(branch.get(i) + "-" + branch.get(i+1),
			        				branch.get(i), branch.get(i+1));
			        			// Colorizing the branch
			        			e.addAttribute("ui.class", "C"+line.getNum());
			        			e.addAttribute("ligne", "C"+line.getNum());
			        			App.logger.trace(String.format("Successfully linked %s to %s", branch.get(i), branch.get(i+1)));
			        		}
			        	}
		        	}
        		}
        		else
        		{
        			App.logger.trace(String.format("Line %s %s is not to be built", line.getType(), line.getNum()));
        		}
	        }
        	App.logger.info("Successfully built all lines");
        }
		catch(Exception e) {App.logger.error(e);}	
	}
	

}
