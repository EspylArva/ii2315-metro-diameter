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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

import com.java.project.control.Back;
import com.java.project.control.ViewControl;
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
		super.setPreferredSize(new Dimension(433,255));
		DropTarget dt = new DropTarget(
                this,
                DnDConstants.ACTION_COPY_OR_MOVE,
                this,
                true);
        try
        {
        	wait_json = ImageIO.read(Paths.get("src","main","resources","useJson.png").toAbsolutePath().toUri().toURL());
        	accept_json = ImageIO.read(Paths.get("src","main","resources","useJson_file.png").toAbsolutePath().toUri().toURL());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        lbl_JsonPath = new JLabel();
        this.add(lbl_JsonPath);
        addMouseListener(getJsonPicker());
        
	}
    
    public DragAndDrop_JsonFile(int w, int h)
	{		
		super.setPreferredSize(new Dimension(w,h));
		DropTarget dt = new DropTarget(
                this,
                DnDConstants.ACTION_COPY_OR_MOVE,
                this,
                true);
        try
        {
        	wait_json = ImageIO.read(Paths.get("src","main","resources","useJson.png").toAbsolutePath().toUri().toURL());
        	accept_json = ImageIO.read(Paths.get("src","main","resources","useJson_file.png").toAbsolutePath().toUri().toURL());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        lbl_JsonPath = new JLabel();
        this.add(lbl_JsonPath);
        addMouseListener(getJsonPicker());
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
                            if (!isJson(name))
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

        try {
        	Transferable tr = dtde.getTransferable();
        	DataFlavor[] flavors = tr.getTransferDataFlavors();
        	for (int i = 0; i < flavors.length; i++) {
        		if (flavors[i].isFlavorJavaFileListType()) {
        			dtde.acceptDrop(DnDConstants.ACTION_COPY);
        			java.util.List list = (java.util.List) tr.getTransferData(flavors[i]);
        			for (int j = 0; j < list.size(); j++) {
        				App.logger.debug(Paths.get(list.get(j).toString()));
        				setResource(Paths.get(list.get(j).toString()), "Custom network");
        			}
        			dtde.dropComplete(true);
        			return;
        		}
        	}
        	App.logger.error("Drop failed: " + dtde);
        	dtde.rejectDrop();
        }
        catch (Exception e) {
        	e.printStackTrace();
        	dtde.rejectDrop();
        }
        
        
    }

	public void setResource(Path path, String s) {
//		if(ViewControl.isComputationFinished())
//		{
			App.logger.info(String.format("Selected a .JSON file at %s. Setting this file as configuration file.", path.toString()));
			lbl_JsonPath.setText(path.toString());
			Back.setPathToJson(path);
			ViewControl.setEnabledButtons(true);
			state = DragState.Accept;
			repaint();
//		}
	}
	
	private boolean isJson(String file)
	{
		if(file == null) { return false; }
		else { return file.toLowerCase().endsWith(".json"); }
	}
	
	private MouseAdapter getJsonPicker()
	{
		return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        		int returnValue = jfc.showOpenDialog(null);
        		File selectedFile = jfc.getSelectedFile();
        		if (returnValue == JFileChooser.APPROVE_OPTION && isJson(selectedFile.getAbsolutePath()) ) {
        			setResource(Paths.get(selectedFile.getAbsolutePath().toString()), "Custom network");
        		}
            }
        };
		
	}
	
	
}