package com.java.project.view;

import javax.swing.JFrame;

public class Frame extends JFrame{
	
	public Frame(String title)
	{
		JFrame frame = new JFrame(title);
    	frame.setSize(1200,800);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setResizable(true);
    	frame.setLocationRelativeTo(null);
    	frame.add(new TestPanel());
    	frame.pack();
    	frame.setVisible(true);
	}

}
