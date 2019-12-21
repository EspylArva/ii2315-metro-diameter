package com.java.project.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.graphstream.graph.Graph;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.View;
//import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.DefaultMouseManager;

import com.java.project.control.Back;
import com.java.project.ii2315.App;

public class NetworkViewer extends JFrame implements ChangeListener, ActionListener
{
    private JSpinner spinnerTime;
    private JCheckBox chk_names;
    private JCheckBox chk_distances;
    private JCheckBox chk_paths;
    private JCheckBox chk_diam;
    private JButton compute;
    private Graph graph = null;
    private JTextArea logConsole ;
    
    
    
    
	public NetworkViewer(Graph graph)
	{
		this.setTitle("Diameter Calculator");
		this.graph = graph;
		
//		graph.display();
		
		this.setSize(800,800);
		this.setLocationRelativeTo(null);
//		GridBagLayout layout = ;
		this.setLayout(new GridBagLayout());
		
		JLabel lbl_title = new JLabel(graph.getId());
		
		// GraphStream visualizer : view
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
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
		chk_distances = new JCheckBox("Show distances between stations");
		chk_distances.addActionListener(this);
		chk_paths = new JCheckBox("Show path computing");
		chk_paths.addActionListener(this);
		chk_diam = new JCheckBox("Show diameters");
		chk_diam.addActionListener(this);
		
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
    	
    	addElementsInGrid(lbl_title, vi, logs, spinnerTime , compute,
    			chk_names, chk_distances, chk_paths, chk_diam);
    	//, spinner, chk
    	this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
 
		if(source == compute)
		{			
			compute.setEnabled(false);
			Back.computeDiameter(this.graph);
			compute.setEnabled(true);
			
			addLogConsoleLine("New Line: rnd " + Math.random());
		}
		if(source == chk_names)
		{
			App.logger.info("Checkbox: names " + chk_names.isSelected());
			Back.showStationsName(chk_names.isSelected(), graph);
		}
		
		if(source == chk_distances)
		{
			App.logger.info("Checkbox: distances " + chk_distances.isSelected());
			Back.showDistances(chk_distances.isSelected(), graph);
		}
	}
	
	
	private void addElementsInGrid(Component lbl_title ,Component graph, Component logConsole,Component spinner, Component button,
			Component chk_names, Component chk_distances, Component chk_paths, Component chk_diam)
	{
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
//
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 4;
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
        this.add(chk_distances, gbc);
//
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
		gbc.gridheight = 1;
        this.add(chk_paths, gbc);
//
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
		gbc.gridheight = 1;
        this.add(chk_diam, gbc);
//
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
		gbc.gridheight = 1;
        this.add(button, gbc);
//
        gbc.gridx = 0;
        gbc.gridy = 6;
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
    
    private String logs = "";
    
    public void addLogConsoleLine(String s)
    {
    	if(!logs.equals("")) { logs += ""+'\n' + ""; }
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
				
				App.logger.debug(String.format("Current center: %sx%s", vi.getCamera().getViewCenter().x, vi.getCamera().getViewCenter().y));
				
				App.logger.debug(vi.getCamera().getMetrics().ratioPx2Gu);
				
				double ratio = vi.getCamera().getMetrics().ratioPx2Gu;
				
				Point2 pLast = new Point2(last.getX(), last.getY());
				Point2 pNow = new Point2(now.getX(), now.getY());
	
				App.logger.debug("PRESSED ON " + last.getX() + "x" + last.getY());
				App.logger.debug("RELEASED ON " + now.getX() + "x" + now.getY());
	
				double x = Math.abs(pLast.x + pNow.x)/2;
				double y = Math.abs(pLast.y + pNow.y)/2;
				
		        cam.setViewCenter(x/ratio, y/ratio, 0);
		        App.logger.debug(String.format("New center: %sx%s", x,y));
		        App.logger.debug(String.format("New center with gu ratio: %sx%s", x/ratio,y/ratio));
		        
		        cam.setViewPercent(0.99);
	//	        vi.getCamera().resetView();
	
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				App.logger.debug(String.format("Current center: %sx%s", vi.getCamera().getViewCenter().x, vi.getCamera().getViewCenter().y));
				vi.getCamera().resetView();
				
			}
			@Override
			public void mousePressed(MouseEvent e) {
//				resetDrag();
			    last=e;				
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(!e.equals(last))
				{
					zoomToSelection(last, e);
				}
			}
//		    public void processDrag(MouseEvent event)
//		    {
//			    if(last!=null) {
//				    //see DefaultShortcutManager
//				    Camera camera = view.getCamera();
//				    Point3 p1 = camera.getViewCenter();
//				    Point3 p2=camera.transformGuToPx(p1.x,p1.y,0);
//				    int xdelta=event.getX()-last.getX();//determine direction
//				    int ydelta=event.getY()-last.getY();//determine direction
//				    //sysout("xdelta "+xdelta+" ydelta "+ydelta);
//				    p2.x-=xdelta;
//				    p2.y-=ydelta;
//				    Point3 p3=camera.transformPxToGu(p2.x,p2.y);
//				    camera.setViewCenter(p3.x,p3.y, 0);
//			    }
//			    last=event;
//		    }
			public void resetDrag() {
				this.last=null;
			}
		};
		return m;
    }
    
    

}
