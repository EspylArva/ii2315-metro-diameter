package com.java.project.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.View;
//import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.DefaultMouseManager;

import com.java.project.control.Back;
import com.java.project.control.ClusterAndDiameter;
import com.java.project.control.ViewControl;
import com.java.project.ii2315.App;

public class NetworkViewer extends JFrame implements ChangeListener, ActionListener
{
	private GridBagConstraints gbc = new GridBagConstraints();
	
    private JSpinner spinnerTime;
    private JCheckBox chk_names;
    private JCheckBox chk_dp_names;
    private JCheckBox chk_distances;
    private JCheckBox chk_paths;
    private JCheckBox chk_diam;
    private static JButton compute;
    private static JButton cluster;
    private JLabel lbl_title;
    private static JTextArea logConsole ;
    public Viewer viewer;
    
    private static String logs = "";
    private static String additionalTitle;
    
    private Graph graph = null;
    private Path diam;
    
    
	public NetworkViewer(Graph graph)
	{
		
		this.setTitle("Diameter Calculator");
		this.graph = graph;
		
//		graph.display();
		
		this.setSize(800,800);
		this.setLocationRelativeTo(null);
		this.setLayout(new GridBagLayout());
		
		JPanel content = new JPanel(new GridBagLayout());
    	
		// Label
		lbl_title = new JLabel(String.format("%s - %s", graph.getId(), additionalTitle));
		
		// GraphStream visualizer : view
		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		final DefaultView vi = (DefaultView) viewer.addDefaultView(false);
		vi.setPreferredSize(new Dimension(600, 600));
		addZoomControl(vi);
		
		
		
		// CheckBox : 
		chk_names = new JCheckBox("Show names of stations");
		chk_names.addActionListener(this);
		chk_dp_names = new JCheckBox("Show names of paths");
		chk_dp_names.addActionListener(this);
		chk_distances = new JCheckBox("Show distances between stations");
		chk_distances.addActionListener(this);
		chk_paths = new JCheckBox("Show path computing");
		chk_paths.addActionListener(this);
		chk_diam = new JCheckBox("Show diameters");
		chk_diam.addActionListener(this);
		
		chk_diam.setSelected(true);
		chk_dp_names.setSelected(true);
		chk_paths.setSelected(false);
		
		// Button : 
		compute = new JButton("Compute diameter");
		compute.addActionListener(this);
		compute.setEnabled(ViewControl.isComputationFinished());
		cluster = new JButton("Compute clusters");
		cluster.addActionListener(this);
		cluster.setEnabled(ViewControl.isComputationFinished());
		
		// Spinner :
		SpinnerListModel durationModel = new SpinnerListModel(new String[] {"1 ms", "5 ms" ,"10 ms"});
    	spinnerTime = new JSpinner(durationModel);
    	spinnerTime.setValue("10 ms");
    	ViewControl.setDisplayDelay(10);
    	spinnerTime.addChangeListener(this);
    	
    	// Log console :
    	logConsole = new JTextArea(5, 20);
    	logConsole.setSize(new Dimension(200,600));
    	JScrollPane logs = new JScrollPane(logConsole);
    	logConsole.setEditable(false);
    	logConsole.setBorder(BorderFactory.createLineBorder(Color.black));
    	
//    	addElementsInGrid(lbl_title, vi, logs, spinnerTime , compute,chk_names, chk_dp_names, chk_distances, chk_paths, chk_diam);
    	
    	JPanel visu = new JPanel(new GridBagLayout());
		visu.setBorder(BorderFactory.createTitledBorder("Graph Visualizer"));	
		addComp(visu, lbl_title, 0, 0, 1, 1, GridBagConstraints.BOTH, 1, 0.01);
		addComp(visu, vi, 0, 1, 1, 1, GridBagConstraints.BOTH, 1, 0.99);
	
		JPanel options = new JPanel(new GridBagLayout());int nOptions = 8; 
		options.setBorder(BorderFactory.createTitledBorder("Visualization options"));		
		addComp(options, new JLabel("Duration of step: "), 0, 0, 1, 1, GridBagConstraints.BOTH, 0.5, (1/nOptions));
		addComp(options, spinnerTime, 1, 0, 1, 1, GridBagConstraints.BOTH, 0.5, (1/nOptions));
		addComp(options, chk_distances, 0, 1, 1, 1, GridBagConstraints.BOTH, 1, (1/nOptions));
		addComp(options, chk_names, 	0, 2, 1, 1, GridBagConstraints.BOTH, 1, (1/nOptions));
		addComp(options, chk_dp_names, 	0, 3, 1, 1, GridBagConstraints.BOTH, 1, (1/nOptions));
		addComp(options, chk_diam, 		0, 4, 1, 1, GridBagConstraints.BOTH, 1, (1/nOptions));
		addComp(options, chk_paths, 	0, 5, 1, 1, GridBagConstraints.BOTH, 1, (1/nOptions));
		addComp(options, compute, 		0, 6, 1, 1, GridBagConstraints.BOTH, 1, (1/nOptions));
		addComp(options, cluster, 		0, 7, 1, 1, GridBagConstraints.BOTH, 1, (1/nOptions));
		
		
		JPanel logger = new JPanel(new GridBagLayout());
		logger.setBorder(BorderFactory.createTitledBorder("Operation logs"));
		addComp(logger, logs, 0, 0, 1, 1, GridBagConstraints.BOTH, 1, 1);
		
		addComp(content, options, 1, 0, 1, 1, GridBagConstraints.BOTH, 0.02, 0.8);
		addComp(content, visu, 0, 0, 1, 1, GridBagConstraints.BOTH, 0.98, 0.8);
		addComp(content, logger, 0, 1, 3, 1, GridBagConstraints.BOTH, 1, 0.2);
    	
    	this.setContentPane(content);
    	this.setVisible(true);
    	Back.computeDiameter(graph);
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

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
 
		if(source == compute)
		{			
			if(Back.getDiameter() != null)
			{
				diam = Back.getDiameter();
			}
			
			ViewControl.setShowComputation(chk_paths.isSelected());	
	    	if(diam != null)
	    	{
	    		
				ViewControl.showDiameter(chk_diam.isSelected(), graph, diam);
	    		ViewControl.showPathStationsName(chk_diam.isSelected(), graph);
	//*/
	//			AStar a = new AStar(graph);
	//			a.compute("A_161468","B_1673");
	//			Path path = a.getShortestPath();
	//			ViewControl.displayPath(graph, path, "path");
				
		    	App.logger.info("Diamètre : " + diam.getNodeCount());
		    	App.logger.info("Diamètre : " + diam);
		    	addLogConsoleLine("Diameter length: " + diam.getNodeCount());
		    	String diamString = "[";
		    	for(Node n : diam.getNodePath())
		    	{
		    		diamString += n.getAttribute("nom") + ", ";
		    	}
		    	diamString = diamString.substring(0, diamString.length()-2) + "]";
				addLogConsoleLine("Diameter path: " + diamString);
	    	}
		}
		else if(source == cluster) {}
		else if(source == chk_names || source == chk_dp_names)
		{
			App.logger.trace("Checkbox: names " + chk_names.isSelected());
			ViewControl.showStationsName(chk_names.isSelected(), graph);
			ViewControl.showPathStationsName(chk_dp_names.isSelected(), graph);
		}
		else if(source == chk_dp_names)
		{
			App.logger.trace("Checkbox: path names " + chk_dp_names.isSelected());
			ViewControl.showPathStationsName(chk_dp_names.isSelected(), graph);
		}
		else if(source == chk_distances)
		{
			App.logger.trace("Checkbox: distances " + chk_distances.isSelected());
			ViewControl.showDistances(chk_distances.isSelected(), graph);
		}
		else if(source == chk_diam)
		{
			App.logger.trace("Checkbox: diameter " + chk_diam.isSelected());
			if(diam != null)
			{
				ViewControl.showDiameter(chk_diam.isSelected(), graph, diam);
			}	
		}
		else if(source == chk_paths)
		{
			App.logger.trace("Checkbox: paths " + chk_paths.isSelected());
			ViewControl.setShowComputation(chk_paths.isSelected());
		}
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
	
	public void stateChanged(ChangeEvent e) {
		App.logger.debug("StateChanged");
        SpinnerModel dateModel = this.spinnerTime.getModel();
        long delay;
        switch(dateModel.getValue().toString())
        {
        case "1 ms":
        	delay = 1;break;
        case "5 ms":
        	delay = 5;break;
        case "10 ms":
        	delay = 10;break;
    	default:
    		delay = 1;break;
        }
        ViewControl.setDisplayDelay(delay);        
    }
	
    
    public static void addLogConsoleLine(String s)
    {
    	if(!logs.equals("")) { logs += ""+'\n' + ""; }
    	if (logConsole.getLineCount() > 50)
    	{
    		ArrayList<String> sss = new ArrayList<String>(Arrays.asList(logs.split("\\r?\\n")));
    		sss.remove(0);
    		String tmpS = "";
    		for(String str: sss )
    		{
    			tmpS += str + '\n';
    		}
    		
    		logs = tmpS;
    	}
		logs += s;
		logConsole.setText(logs);
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
    
    public static JButton getComputeButton()
    {
    	return compute;
    }
    
    public static JButton getClusterButton()
    {
    	return cluster;
    }

	public static String getAdditionalTitle() {
		return additionalTitle;
	}

	public static void setAdditionalTitle(String additionalTitle) {
		NetworkViewer.additionalTitle = additionalTitle;
	}
    
    

}
