package com.java.project.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
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
	private JFrame frame;

	private static final long serialVersionUID = 1L;	
	
	private static JButton btn_SimpleWorld;


	private static JButton btn_WeightedWorld;
	private JButton btn_useDefaultJson;
	private DragAndDrop_JsonFile dragAndDrop_image;
	
	public MainMenu()
	{
		frame = new JFrame("Diameter Calculator");
		frame.setLayout(new FlowLayout());
    	frame.setSize(1200,800);

    	frame.setResizable(true);
    	frame.setLocationRelativeTo(null);

    	btn_SimpleWorld = new JButton("Simple world");
    	btn_WeightedWorld = new JButton("Complex world");
    	btn_useDefaultJson = new JButton("Use default .JSON resource");
    	btn_SimpleWorld.addActionListener(this);
    	btn_WeightedWorld.addActionListener(this);
    	btn_useDefaultJson.addActionListener(this);
    	
    	btn_SimpleWorld.setEnabled(false);
    	btn_WeightedWorld.setEnabled(false);
    	
    	dragAndDrop_image = new DragAndDrop_JsonFile();
    	
    	
    	
    	frame.add(dragAndDrop_image);
    	frame.add(btn_useDefaultJson);
    	frame.add(btn_SimpleWorld);
    	frame.add(btn_WeightedWorld);
    	
    	
    	frame.setVisible(true);
    	
    	
	}
	
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
 
		if(source == btn_SimpleWorld)
		{			
			NetworkViewer newWindow = new NetworkViewer(Back.compute());
			newWindow.setVisible(true);
			this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
		else if(source == btn_WeightedWorld)
		{
			// load complex world
			App.logger.error("Not yet implemented!");
		}
		else if(source == btn_useDefaultJson)
		{
			Back.setPathToJson(Paths.get("src","main","resources","reseau.json").toAbsolutePath()); 
			dragAndDrop_image.setResource(Paths.get("src","main","resources","reseau.json").toAbsolutePath());
			System.out.println(Paths.get("src","main","resources","reseau.json").toAbsolutePath());
		}
	}
	
	public static JButton getBtn_SimpleWorld() {
		return btn_SimpleWorld;
	}


	public static JButton getBtn_WeightedWorld() {
		return btn_WeightedWorld;
	}
	


}
