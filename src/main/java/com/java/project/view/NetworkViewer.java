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
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
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
		
		this.setSize(800,800);
		this.setLocationRelativeTo(null);
//		GridBagLayout layout = ;
		this.setLayout(new GridBagLayout());
		
		JLabel lbl_title = new JLabel(graph.getId());
		
		// GraphStream visualizer : view
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		DefaultView view = (DefaultView) viewer.addDefaultView(false);
		view.setPreferredSize(new Dimension(600, 600));
	    
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
    	
    	addElementsInGrid(lbl_title, view, logs, spinnerTime , compute,
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
    

}
