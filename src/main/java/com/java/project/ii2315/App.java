package com.java.project.ii2315;


import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import com.java.project.view.MainMenu;

public class App 
{

	public static Logger logger = Logger.getLogger(App.class);
	
    public static void main( String[] args ) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
    {
   	
    	// *** DEV CONF *** //
    	// log4j
    	PropertyConfigurator.configure(Paths.get(".").toAbsolutePath() + "\\src\\main\\resources\\log4j.properties");
    	
    	// *** RUN *** //
    	// GUI: MainMenu
    	SwingUtilities.invokeLater(new Runnable()
		{	
			public void run() {
				try
				{
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    new MainMenu("Diameter Calculator - Network Settings");
                }
				catch (ClassNotFoundException | InstantiationException |
						IllegalAccessException | UnsupportedLookAndFeelException ex)
				{
					logger.fatal(ex);
                }
			}
		});
     }
}
