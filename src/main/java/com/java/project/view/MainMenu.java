package com.java.project.view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.graphstream.graph.Graph;

import com.java.project.control.Back;
import com.java.project.control.ViewControl;
import com.java.project.ii2315.App;

public class MainMenu extends JFrame implements ActionListener, ChangeListener
{
//	private JFrame frame;

	private static final long serialVersionUID = 1L;	
	
	private GridBagConstraints gbc = new GridBagConstraints();
	private static JButton btn_SimpleWorld;
	private static JButton btn_WeightedWorld;
	private JButton btn_useDefaultJson;
	private JButton btn_useFullJson;
	private JButton btn_useSimpleJson;
	private static JSpinner updown_frozenPicker;
	private DragAndDrop_JsonFile dragAndDrop_image;
	
	public MainMenu(String title)
	{
		this.setTitle(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel content = new JPanel(new GridBagLayout());
    	this.setSize(500,400);

    	this.setResizable(true);
    	this.setLocationRelativeTo(null);

    	SpinnerListModel frozen = new SpinnerListModel(new String[] {"Keep geographic exactitude","Allow floating representation"});
    	updown_frozenPicker = new JSpinner(frozen);
    	updown_frozenPicker.addChangeListener(this);
    	updown_frozenPicker.setValue("Allow floating representation");
    	
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
    	
    	updown_frozenPicker.setEnabled(false);
    	btn_SimpleWorld.setEnabled(false);
    	btn_WeightedWorld.setEnabled(false);
    	
    	dragAndDrop_image = new DragAndDrop_JsonFile(433,255);
    	

    	JPanel p = new JPanel(new GridBagLayout());
		p.setBorder(BorderFactory.createTitledBorder("Select .JSON file"));
		
		addComp(p, dragAndDrop_image, 0, 0, 3, 1, GridBagConstraints.BOTH, 1, 0.5);
		addComp(p, btn_useDefaultJson, 0, 1, 1, 1, GridBagConstraints.BOTH, 0.33, 0.5);
		addComp(p, btn_useFullJson, 1, 1, 1, 1, GridBagConstraints.BOTH, 0.33, 0.5);
		addComp(p, btn_useSimpleJson, 2, 1, 1, 1, GridBagConstraints.BOTH, 0.33, 0.5);
		
		
		JPanel p2 = new JPanel(new GridBagLayout());
		p2.setBorder(BorderFactory.createTitledBorder("Select world model"));
		
		addComp(p2, btn_SimpleWorld, 0, 0, 1, 1, GridBagConstraints.BOTH, 0.375, 1);
		addComp(p2, btn_WeightedWorld, 1, 0, 1, 1, GridBagConstraints.BOTH, 0.375, 1);
		addComp(p2, updown_frozenPicker, 2,0,1,1, GridBagConstraints.BOTH, 0.25,1);
		
		addComp(content, p, 0, 0, 1, 1, GridBagConstraints.BOTH, 1, 0.7);
		addComp(content, p2, 0, 1, 1, 1, GridBagConstraints.BOTH, 1, 0.3);
		
		this.setContentPane(content);
		this.pack();
    	this.setVisible(true);
    	
	}
	
	private void addComp(JPanel panel, JComponent comp, int x, int y, int gWidth , int gHeight, int fill, double weightx, double weighty) {
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = gWidth;
		gbc.gridheight = gHeight;
		gbc.fill = fill;
		gbc.weightx = weightx;
		gbc.weighty = weighty; 

		panel.add(comp, gbc);
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
	
	public void stateChanged(ChangeEvent e) {
		App.logger.debug("StateChanged");
        SpinnerModel dateModel = this.updown_frozenPicker.getModel();
        // REFRESH VALUES HERE
//        operationDuration = spinnerTime.getValue().toString();
        
        boolean freeze = updown_frozenPicker.getValue().toString().equals("Keep geographic exactitude") ? true : false;
        if(freeze)
    	{
        	System.out.println("FREEZE");
    	}
        else
        {
        	System.out.println("NOT FREEZE");
        }
        ViewControl.setFreeze(freeze);
    }
	
	
	public static JButton getBtn_SimpleWorld() {
		return btn_SimpleWorld;
	}


	public static JButton getBtn_WeightedWorld() {
		return btn_WeightedWorld;
	}

	public static JSpinner getUpDown_Freeze() {
		return updown_frozenPicker;
	}
	


}
