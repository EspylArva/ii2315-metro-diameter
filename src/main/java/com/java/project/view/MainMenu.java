package com.java.project.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.DefaultView;
//import org.graphstream.ui.swingViewer.Viewer;

import com.java.project.control.Back;
import com.java.project.ii2315.App;

public class MainMenu extends JFrame implements ActionListener
{
//	private JFrame frame;

	private static final long serialVersionUID = 1L;	
	
	private static JButton btn_SimpleWorld;
	private static JButton btn_WeightedWorld;
	private JButton btn_useDefaultJson;
	private JButton btn_useFullJson;
	private JButton btn_useSimpleJson;
	private DragAndDrop_JsonFile dragAndDrop_image;
	
	public MainMenu(String title)
	{
		this.setTitle(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
    	this.setSize(1200,800);

    	this.setResizable(true);
    	this.setLocationRelativeTo(null);

    	btn_SimpleWorld = new JButton("Simple world");
    	btn_WeightedWorld = new JButton("Complex world");
    	btn_useDefaultJson = new JButton("Use default .JSON resource");
    	btn_useFullJson = new JButton("Use full .JSON resource");
    	btn_useSimpleJson = new JButton("Use reduced .JSON resource");
    	
    	btn_SimpleWorld.addActionListener(this);
    	btn_WeightedWorld.addActionListener(this);
    	btn_useDefaultJson.addActionListener(this);
    	btn_useFullJson.addActionListener(this);
    	btn_useSimpleJson.addActionListener(this);
    	
    	btn_SimpleWorld.setEnabled(false);
    	btn_WeightedWorld.setEnabled(false);
    	
    	dragAndDrop_image = new DragAndDrop_JsonFile();
    	
    	
    	GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
//
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 6;
		gbc.gridheight = 1;
		this.add(dragAndDrop_image, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
    	this.add(btn_useDefaultJson, gbc);
    	gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
    	this.add(btn_useFullJson, gbc);
    	gbc.gridx = 4;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
    	this.add(btn_useSimpleJson, gbc);
    	
    	gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
    	this.add(btn_SimpleWorld, gbc);
    	gbc.gridx = 3;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
    	this.add(btn_WeightedWorld, gbc);

    	this.setVisible(true);
    	
    	
	}
	
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
 
		if(source == btn_SimpleWorld)
		{			
			NetworkViewer newWindow = new NetworkViewer(Back.computeWeightlessGraph());
			newWindow.setVisible(true);
		}
		else if(source == btn_WeightedWorld)
		{
			// load complex world
			Graph g = Back.computeWeightlessGraph();
			Back.addWeights(g);
			NetworkViewer newWindow = new NetworkViewer(g);
			newWindow.setVisible(true);
//			App.logger.error("Not yet implemented!");
		}
		else if(source == btn_useDefaultJson)
		{
			dragAndDrop_image.setResource(Paths.get("src","main","resources","reseau.json").toAbsolutePath());
		}
		else if(source == btn_useFullJson)
		{
			dragAndDrop_image.setResource(Paths.get("src","main","resources","reseau_RER.json").toAbsolutePath());
		}
		else if(source == btn_useSimpleJson)
		{
			dragAndDrop_image.setResource(Paths.get("src","main","resources","reducedNetwork.json").toAbsolutePath());
		}
	}
	
	public static JButton getBtn_SimpleWorld() {
		return btn_SimpleWorld;
	}


	public static JButton getBtn_WeightedWorld() {
		return btn_WeightedWorld;
	}
	


}
