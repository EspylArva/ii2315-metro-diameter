package com.java.project.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.DefaultView;
//import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.view.Viewer;

import com.java.project.ii2315.App;

public class NetworkViewer extends JFrame implements ChangeListener 
{
	private String operationDuration;
    private JSpinner spinner;
    
	public NetworkViewer(Graph graph)
	{
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
		JCheckBox chk = new JCheckBox("A");
		
		
		
		
		// Spinner :
		SpinnerListModel durationModel = new SpinnerListModel(new String[] {"100 ms","500 ms", "1 s"});
    	spinner = new JSpinner(durationModel);
    	spinner.addChangeListener(this);
    	
    	
    	
//    	this.add(exchangingCard1);
//    	this.add(view);
//    	this.add(spinner);
//    	
    	addElementsInGrid(view);
    	//, spinner, chk
    	this.setVisible(true);
	}
	
	
	private void addElementsInGrid(Component graph)
	{
//		ArrayList<Component> e = new ArrayList<Component>();
//		for(Component o : elements)
//		{
//			e.add(o);
//		}
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		this.add(graph, gbc);

//		gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        this.add(e.get(0), gbc);
// 
//        gbc.gridx = 1;
//        gbc.gridy = 0;
//        this.add(e.get(1), gbc);
 
		
	}
	
	public void stateChanged(ChangeEvent e) {
		App.logger.debug("StateChanged");
        SpinnerModel dateModel = spinner.getModel();
        setSeasonalColor(dateModel.getValue().toString());
        // REFRESH VALUES HERE
        operationDuration = spinner.getValue().toString();
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
        JFormattedTextField ftf = getTextField(spinner);
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
