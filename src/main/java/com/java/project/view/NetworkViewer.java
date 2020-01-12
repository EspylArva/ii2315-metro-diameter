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
    private JButton compute;
    private Graph graph = null;
    private static JTextArea logConsole ;
    public Viewer viewer;
    
    private Path diam;
    
    
	public NetworkViewer(Graph graph)
	{
		
		this.setTitle("Diameter Calculator");
		this.graph = graph;
		
//		graph.display();
		
		this.setSize(800,800);
		this.setLocationRelativeTo(null);
//		GridBagLayout layout = ;
		this.setLayout(new GridBagLayout());
		
		JPanel content = new JPanel(new GridBagLayout());
    	
		
		
		JLabel lbl_title = new JLabel(graph.getId());
		
		// GraphStream visualizer : view
		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		final DefaultView vi = (DefaultView) viewer.addDefaultView(false);
		vi.setPreferredSize(new Dimension(600, 600));
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
		chk_paths.setSelected(true);
		
		// Button : 
		compute = new JButton("Compute diameter");
		compute.addActionListener(this);
		
		// Spinner :
		SpinnerListModel durationModel = new SpinnerListModel(new String[] {"100 ms","500 ms", "1 s"});
    	spinnerTime = new JSpinner(durationModel);
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
	
		JPanel options = new JPanel(new GridBagLayout());
		options.setBorder(BorderFactory.createTitledBorder("Visualization options"));		
		addComp(options, chk_distances, 0, 0, 1, 1, GridBagConstraints.BOTH, 1, (1/6));
		addComp(options, chk_names, 0, 1, 1, 1, GridBagConstraints.BOTH, 1, (1/6));
		addComp(options, chk_dp_names, 0, 2, 1, 1, GridBagConstraints.BOTH, 1, (1/6));
		addComp(options, chk_diam, 0, 3, 1, 1, GridBagConstraints.BOTH, 1, (1/6));
		addComp(options, chk_paths, 0, 4, 1, 1, GridBagConstraints.BOTH, 1, (1/6));
		addComp(options, compute, 0, 5, 1, 1, GridBagConstraints.BOTH, 1, (1/6));
		
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
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
 
		if(source == compute)
		{			
//			compute.setEnabled(false);
//			if(diam == null)
//			{
//				Back.computeDiameter(graph);//, thread, chk_paths.isSelected());
//			}
			while(diam == null)
			{
				diam = Back.getDiameter();
			}
			
	    	if(diam != null &&  chk_diam.isSelected())
			{
				ViewControl.showDiameter(chk_diam.isSelected(), graph, diam);
			}	
	    	
	    	if(chk_dp_names.isSelected())
			{
	    		ViewControl.showPathStationsName(chk_diam.isSelected(), graph);
			}	
	    	
	    	App.logger.info("Diamètre : " + diam.getNodeCount());
	    	App.logger.info("Diamètre : " + diam);
	    	addLogConsoleLine("Diameter length: " + diam.getNodeCount());
			addLogConsoleLine("Diameter path: " + diam);
//			ArrayList<String> diamPath = new ArrayList<String>();
//			for(Node n : diam.getNodeSet())
//			{
//				diamPath.add(n.getAttribute("nom").toString());
//			}
//			addLogConsoleLine(diamPath.toString());
		}
		if(source == chk_names || source == chk_dp_names)
		{
			App.logger.info("Checkbox: names " + chk_names.isSelected());
			ViewControl.showStationsName(chk_names.isSelected(), graph);
			ViewControl.showPathStationsName(chk_dp_names.isSelected(), graph);
		}
//		if(source == chk_dp_names)
//		{
//			App.logger.info("Checkbox: path names " + chk_dp_names.isSelected());
//			Back.showPathStationsName(chk_dp_names.isSelected(), graph);
//		}
		if(source == chk_distances)
		{
			App.logger.info("Checkbox: distances " + chk_distances.isSelected());
			ViewControl.showDistances(chk_distances.isSelected(), graph);
		}
		if(source == chk_diam)
		{
			App.logger.info("Checkbox: diameter " + chk_diam.isSelected());
			if(diam != null)
			{
				ViewControl.showDiameter(chk_diam.isSelected(), graph, diam);
			}	
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
	
	private void addElementsInGrid(Component lbl_title ,Component graph, Component logConsole,Component spinner, Component button,
			Component chk_names, Component chk_dp_names, Component chk_distances, Component chk_paths, Component chk_diam)
	{
		
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
//
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 5;
		this.add(graph, gbc);
//
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
		gbc.gridheight = 1;
        this.add(lbl_title, gbc);
//
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
		gbc.gridheight = 1;
        this.add(spinner, gbc);
//      
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
		gbc.gridheight = 1;
        this.add(new JLabel("Duration of step: "), gbc);
//      
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
		gbc.gridheight = 1;
        this.add(chk_names, gbc);
//      
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
		gbc.gridheight = 1;
        this.add(chk_dp_names, gbc);
//      
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
		gbc.gridheight = 1;
        this.add(chk_distances, gbc);
//
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
		gbc.gridheight = 1;
        this.add(chk_paths, gbc);
//
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
		gbc.gridheight = 1;
        this.add(chk_diam, gbc);
//
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
		gbc.gridheight = 1;
        this.add(button, gbc);
//
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
		gbc.gridheight = 1;
        this.add(logConsole, gbc);
		
	}
	
	public void stateChanged(ChangeEvent e) {
		App.logger.debug("StateChanged");
        SpinnerModel dateModel = this.spinnerTime.getModel();
        setSeasonalColor(dateModel.getValue().toString());
        // REFRESH VALUES HERE
//        operationDuration = spinnerTime.getValue().toString();
    }
	
	public JFormattedTextField getTextField(JSpinner spinner) {
	    JComponent editor = spinner.getEditor();
	    if (editor instanceof JSpinner.DefaultEditor)
	    {
	        return ((JSpinner.DefaultEditor)editor).getTextField();
	    }
	    else
	    {
	        App.logger.error(String.format("Unexpected editor type: %s isn't a descendant of DefaultEditor",
	        		spinner.getEditor().getClass()));
	        return null;
	    }
	}
	

    protected void setSeasonalColor(String date) {
        JFormattedTextField ftf = getTextField(this.spinnerTime);
        if (ftf == null) return;
        
        ftf.setForeground(Color.BLUE);
        ftf.setBackground(Color.RED);
        
    }
    
    private static String logs = "";
    
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
    
    

}
