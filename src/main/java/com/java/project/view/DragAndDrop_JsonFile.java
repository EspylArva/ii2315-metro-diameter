package com.java.project.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.java.project.control.Back;
import com.java.project.ii2315.App;

public class DragAndDrop_JsonFile extends JPanel implements DropTargetListener
{
	
	private String filePath;
	private JLabel lbl_JsonPath;
	public enum DragState {
        Waiting,
        Accept,
        Reject
    }
    private DragState state = DragState.Waiting;
    private BufferedImage wait_json;
    private BufferedImage accept_json;
    
	public DragAndDrop_JsonFile()
	{
		DropTarget dt = new DropTarget(
                this,
                DnDConstants.ACTION_COPY_OR_MOVE,
                this,
                true);

        setBackground(Color.BLACK);
        try {
        	wait_json = ImageIO.read(getClass().getResource("/useJson.png"));
        	accept_json = ImageIO.read(getClass().getResource("/useJson_file.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        lbl_JsonPath = new JLabel();
        this.add(lbl_JsonPath);
        
	}

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(433,255);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage bg = null;
        switch (state) {
            case Waiting:
                bg = wait_json;
                break;
            case Accept:
                bg = accept_json;
                break;
            case Reject:
                bg = wait_json;
                break;
        }
        if (bg != null) {
            int x = (getWidth() - bg.getWidth()) / 2;
            int y = (getHeight() - bg.getHeight()) / 2;
            g.drawImage(bg, x, y, this);
        }
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        state = DragState.Reject;
        Transferable t = dtde.getTransferable();
        if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            try
            {
                Object td = t.getTransferData(DataFlavor.javaFileListFlavor);
                if (td instanceof List && ((List) td).size() ==1)
                {
                    state = DragState.Accept;
//                    Object value = td;
                    for (Object value : ((List) td))
                    {
                        if (value instanceof File)
                        {
                            File file = (File) value;
                            String name = file.getName().toLowerCase();
                            if (!name.endsWith(".json"))
                            {
                                state = DragState.Reject;
                                break;
                            }
                        }
                    }
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                ex.printStackTrace();
            }
        }
        if (state == DragState.Accept) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        } else {
            dtde.rejectDrag();
        }
        repaint();
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        if(Back.getPathToJson() == null)
        {
        	state = DragState.Waiting;
        }
        else
        {
        	state = DragState.Accept;
        }
        repaint();
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        state = DragState.Waiting;
        System.out.println("Dropped ");

        try {
        	Transferable tr = dtde.getTransferable();
        	DataFlavor[] flavors = tr.getTransferDataFlavors();
        	for (int i = 0; i < flavors.length; i++) {
        		System.out.println("Possible flavor: " + flavors[i].getMimeType());
        		if (flavors[i].isFlavorJavaFileListType()) {
        			dtde.acceptDrop(DnDConstants.ACTION_COPY);
        			System.out.println("Successful file list drop.");
        			
        			java.util.List list = (java.util.List) tr.getTransferData(flavors[i]);
        			for (int j = 0; j < list.size(); j++) {
        				setResource(Paths.get(list.get(j).toString()));
//        				lbl_JsonPath.setText(list.get(j).toString());
////        				System.out.println(list.get(j).toString());
//        				Back.setPathToJson(Paths.get(list.get(j).toString()));
//        				java.nio.file.Path p = Paths.get(new URI(list.get(j).toString()));
        			}
        			dtde.dropComplete(true);
//        			MainMenu.getInstance().setEnabledButtons(true);
        			return;
        		}
        	}
        	System.out.println("Drop failed: " + dtde);
        	dtde.rejectDrop();
        }
        catch (Exception e) {
        	e.printStackTrace();
        	dtde.rejectDrop();
        }
        
        
    }

	public void setResource(Path path) {
		lbl_JsonPath.setText(path.toString());
		Back.setPathToJson(path);
		Back.setEnabledButtons(true);
		state = DragState.Accept;
		repaint();
	}
}