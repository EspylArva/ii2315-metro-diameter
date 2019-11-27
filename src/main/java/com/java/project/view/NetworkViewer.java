package com.java.project.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

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
import org.graphstream.ui.swingViewer.Viewer;

import com.java.project.ii2315.App;

public class NetworkViewer extends JFrame implements ChangeListener 
{
	
	
	private String operationDuration;
    private JSpinner spinner;
    
	public NetworkViewer(Graph graph)
	{
//		Graph graph = Back.compute();
		
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		DefaultView view = (DefaultView) viewer.addDefaultView(false);
		view.setPreferredSize(new Dimension(400, 400));
	    
		JCheckBox exchangingCard1 = new JCheckBox("A");
        this.add(exchangingCard1);
		
		this.add(view);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());
		this.setSize(800,800);
		this.setLocationRelativeTo(null);
		
		SpinnerListModel durationModel = new SpinnerListModel(new String[] {"100 ms","500 ms", "1 s"});
    	spinner = new JSpinner(durationModel);
    	
    	
    	spinner.addChangeListener(this);
    	this.add(spinner);
    	
    	
    	this.setVisible(true);
	}
	
	public void stateChanged(ChangeEvent e) {
		System.out.println("StateChanged");
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

//	private String getOperationDuration() {
//		return operationDuration;
//	}
//
//	private void setOperationDuration(String operationDuration) {
//		this.operationDuration = operationDuration;
//	}

//	public JSpinner getSpinner() {
//		return spinner;
//	}
//
//	public void setSpinner(JSpinner spinner) {
//		this.spinner = spinner;
//	}

}
