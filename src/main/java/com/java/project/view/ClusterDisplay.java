package com.java.project.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.graphstream.graph.Graph;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.DefaultMouseManager;

import com.java.project.ii2315.App;

public class ClusterDisplay extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagConstraints gbc = new GridBagConstraints();
	private JLabel lbl_utility;
	private JLabel lbl_degree;
	private JLabel lbl_distance;
	private Graph utility;
	private Graph degree;
	private Graph distance;
	private Viewer v_utility;
	private Viewer v_degree;
	private Viewer v_distance;
	
	public ClusterDisplay(Graph utility,
			Graph degree,
			Graph distance)
	{
//		vc = new ViewControl(this);
		
		this.utility = utility;
		this.degree = degree;
		this.distance = distance;
		
		this.setTitle("Diameter Calculator - Clusters");
		
		this.initialize();
		
		this.setSize(1800,600);
		this.setLocationRelativeTo(null);
		this.setLayout(new GridBagLayout());
		
		lbl_utility = new JLabel("Utility-based clustering");
		lbl_degree = new JLabel("Degree-based clustering");
		lbl_distance = new JLabel("Distance-based clustering");
		
    	this.setVisible(true);
    	
    	
    	
	}
	
	private void initialize() {
		
		JPanel content = new JPanel(new GridBagLayout());
		
		v_utility = new Viewer(this.utility, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
    	v_utility.enableAutoLayout();
		final DefaultView vi_utility = (DefaultView) v_utility.addDefaultView(false);
		vi_utility.setPreferredSize(new Dimension(500, 500));
		addZoomControl(vi_utility);
		
		v_degree = new Viewer(this.degree, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		v_degree.enableAutoLayout();
		final DefaultView vi_degree = (DefaultView) v_degree.addDefaultView(false);
		vi_degree.setPreferredSize(new Dimension(500, 500));
		addZoomControl(vi_degree);
		
		v_distance = new Viewer(this.distance, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		v_distance.enableAutoLayout();
		final DefaultView vi_distance = (DefaultView) v_distance.addDefaultView(false);
		vi_distance.setPreferredSize(new Dimension(500, 500));
		addZoomControl(vi_distance);
		
		JPanel visu = new JPanel(new GridBagLayout());
		visu.setBorder(BorderFactory.createTitledBorder("Graph Visualizer"));	
		
		vi_utility.setBorder(BorderFactory.createTitledBorder("Utility-based clustering"));
		vi_degree.setBorder(BorderFactory.createTitledBorder("Degree-based clustering"));
		vi_distance.setBorder(BorderFactory.createTitledBorder("Distance-based clustering"));
		addComp(visu, vi_utility, 0, 0, 1, 1, GridBagConstraints.BOTH, 0.33,1);
		addComp(visu, vi_degree, 1, 0, 1, 1, GridBagConstraints.BOTH, 0.33,1);
		addComp(visu, vi_distance, 2, 0, 1, 1, GridBagConstraints.BOTH, 0.33,1);
		
		
		addComp(content, visu, 0, 1, 1, 1, GridBagConstraints.BOTH, 1, 0.99);
		
		this.setContentPane(content);
		
	}

	private void addZoomControl(final DefaultView vi) {
		((Component) vi).addMouseWheelListener(new MouseWheelListener() {
		    @Override
		    public void mouseWheelMoved(MouseWheelEvent e) {
		    		
		        e.consume();
		        int i = e.getWheelRotation();
		        double factor = Math.pow(1.25, i);
		        Camera cam = vi.getCamera();
		        double zoom = cam.getViewPercent() * factor;
		        Point2 pxCenter  = cam.transformGuToPx(cam.getViewCenter().x, cam.getViewCenter().y, 0);
		        Point3 guClicked = cam.transformPxToGu(e.getX(), e.getY());
		       
		        
		        double newRatioPx2Gu = cam.getMetrics().ratioPx2Gu/factor;
		        double x = guClicked.x + (pxCenter.x - e.getX())/newRatioPx2Gu;
		        double y = guClicked.y - (pxCenter.y - e.getY())/newRatioPx2Gu;
		        cam.setViewCenter(x, y, 0);
		        cam.setViewPercent(zoom);

		    }
		});

		((Component) vi).addMouseListener(getGraphMouseManager(vi));
	}
	
	private void addComp(JPanel panel, Component graph2, int x, int y, int gWidth , int gHeight, int fill, double weightx, double weighty) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = gWidth;
		gbc.gridheight = gHeight;
		gbc.fill = fill;
		gbc.weightx = weightx;
		gbc.weighty = weighty; 

		panel.add(graph2, gbc);
	}
	
	private DefaultMouseManager getGraphMouseManager(final View vi)
    {
    	DefaultMouseManager m = new DefaultMouseManager(){

			MouseEvent last;
			public void zoomToSelection(MouseEvent last, MouseEvent now)
			{
				last.consume();
				now.consume();
				
				Camera cam = vi.getCamera();
//				App.logger.debug(vi.getCamera().getMetrics());
				double ratio = vi.getCamera().getMetrics().ratioPx2Gu;
				
				
//				App.logger.debug(String.format("Current center: %sx%s", vi.getCamera().getViewCenter().x, vi.getCamera().getViewCenter().y));				
//	
//				App.logger.debug("PRESSED ON " + cam.transformPxToGu(last.getX(),last.getY()));
//				App.logger.debug("RELEASED ON " + cam.transformPxToGu(now.getX(),now.getY()));
	
				double x = (last.getX() + now.getX())/2;
				double y = (last.getY() + now.getY())/2;
				
				Point3 newCenter = cam.transformPxToGu(x, y);
				
				App.logger.debug(String.format("New center: %sx%s. Zooming to %s",
						newCenter.x,newCenter.y , cam.getViewPercent()-0.1));
				cam.setViewPercent(cam.getViewPercent()*0.9);
		        cam.setViewCenter(newCenter.x, newCenter.y, newCenter.z);
	
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				vi.getCamera().resetView();	
				App.logger.debug("CLICKED ON " + vi.getCamera().transformPxToGu(e.getX(),e.getY()));
			}
			@Override
			public void mousePressed(MouseEvent e) {
			    last=e;				
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(!e.equals(last))
				{
					zoomToSelection(last, e);
				}
			}

		};
		return m;
    }

}
