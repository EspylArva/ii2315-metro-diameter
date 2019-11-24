package com.java.project.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;

import com.java.project.control.Back;
import com.java.project.ii2315.App;

public class MainMenu extends JFrame implements ActionListener
{
	private JFrame frame;

	private static final long serialVersionUID = 1L;
	
	
	private JButton btn_SimpleWorld;
	private JButton btn_WeightedWorld;
	private JButton btn_useDefaultJson;
	private DragAndDrop_JsonFile dragAndDrop_image;
	
	private static MainMenu MAINMENU_INSTANCE;
	
	public static MainMenu getInstance()
	{
		if(MAINMENU_INSTANCE == null)
		{
			MAINMENU_INSTANCE = new MainMenu();
		}
		return MAINMENU_INSTANCE;
	}
	private MainMenu()
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
    	
//    	image = new JLabel(new ImageIcon(getClass().getResource("/useJson.png")));
    	dragAndDrop_image = new DragAndDrop_JsonFile();
    	
//    	frame.add(image);
    	frame.add(dragAndDrop_image);
    	frame.add(btn_useDefaultJson);
    	frame.add(btn_SimpleWorld);
    	frame.add(btn_WeightedWorld);
    	
		
//		Paths.get(".").toAbsolutePath() + "\\src\\main\\resources\\useJson.png")
    	frame.setVisible(true);
    	
    	
	}
	
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
 
		if(source == btn_SimpleWorld)
		{
			// Back.simpleWorld();
			
			JFrame newWindow = new JFrame("Diameter Calculator");
			newWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
			newWindow.setLayout(new FlowLayout());
			newWindow.setSize(800,800);
			newWindow.setLocationRelativeTo(null);

			Graph graph = Back.compute();
			graph.setStrict(false);
		
			Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
			viewer.enableAutoLayout();
			DefaultView view = (DefaultView) viewer.addDefaultView(false);
			view.setPreferredSize(new Dimension(400, 400));
		    

			newWindow.add(view);
		    newWindow.setVisible(true);
			
			
//			this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
		else if(source == btn_WeightedWorld)
		{
			// load complex world

		}
		else if(source == btn_useDefaultJson)
		{
			App.pathToJson = Paths.get("src","main","resources","reseau.json").toAbsolutePath();
			dragAndDrop_image.setResource(Paths.get("src","main","resources","reseau.json").toAbsolutePath());
			System.out.println(Paths.get("src","main","resources","reseau.json").toAbsolutePath());
		}
	}
	
	public void setEnabledButtons(boolean bool)
	{
		btn_SimpleWorld.setEnabled(bool);
		btn_WeightedWorld.setEnabled(bool);
	}

}
