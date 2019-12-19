package com.java.project.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.DefaultView;
//import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.view.Viewer;

import com.java.project.control.Back;
import com.java.project.ii2315.App;

public class NetworkViewer extends JFrame implements ChangeListener, ActionListener
{
    private JSpinner spinnerTime;
    private JCheckBox chk_names;
    private JCheckBox chk_paths;
    private JCheckBox chk_diam;
    private JButton compute;
    private Graph graph = null;
    
	public NetworkViewer(Graph graph)
	{
		this.graph = graph;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800,800);
		this.setLocationRelativeTo(null);
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
//		this.setLayout(new FlowLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		//		Graph graph = Back.compute();
		
		// GraphStream visualizer : view
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		DefaultView view = (DefaultView) viewer.addDefaultView(false);
		view.setPreferredSize(new Dimension(600, 600));
	    
		// CheckBox : 
		chk_names = new JCheckBox("Show names of stations");
		chk_paths = new JCheckBox("Show path computing");
		chk_diam = new JCheckBox("Show diameters");
		
		// Button : 
		compute = new JButton("Compute diameter");
		compute.addActionListener(this);
		
		
		// Spinner :
		SpinnerListModel durationModel = new SpinnerListModel(new String[] {"100 ms","500 ms", "1 s"});
    	spinnerTime = new JSpinner(durationModel);
    	spinnerTime.addChangeListener(this);
    	
    	// Log console :
    	JLabel logConsole = new JLabel();
    	logConsole.setSize(new Dimension(200,600));
    	logConsole.setText("TESTETSTETESTE");
    	logConsole.setBorder(BorderFactory.createLineBorder(Color.black));
    	
//    	this.add(exchangingCard1);
//    	this.add(view);
//    	this.add(spinner);
//    	
    	addElementsInGrid(view, logConsole, spinnerTime );
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
		}
	}
	
	
	private void addElementsInGrid(Component graph, Component logConsole,Component spinner)
	{
//		ArrayList<Component> e = new ArrayList<Component>();
//		for(Component o : elements)
//		{
//			e.add(o);
//		}
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
//		gbc.gridwidth = 2;
		gbc.gridheight = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(graph, gbc);
		
		

        gbc.gridx = 0;
        gbc.gridy = 4;
        this.add(logConsole, gbc);
//
        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(spinner, gbc);
 
		
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
    	if(!logs.equals("")) { logs += '\n'; }
		logs += '\n' + s;
    }
    

}
