package it.elfgames.gameboy.TileEditor;
import it.elfgames.gameboy.TileEditor.IO.Exceptions.CantImportException;
import it.elfgames.gameboy.TileEditor.IO.Exceptions.CantSaveException;
import it.elfgames.gameboy.TileEditor.IO.Exceptions.WrongImageTypeException;
import it.elfgames.gameboy.TileEditor.IO.Exceptions.WrongProportionException;
import it.elfgames.gameboy.TileEditor.IO.Exceptions.WrongSizeException;
import it.elfgames.gameboy.TileEditor.IO.SaveLoadImport;
import it.elfgames.gameboy.TileEditor.history.HystoryElement;
import it.elfgames.gameboy.TileEditor.utils.Hfilter;
import it.elfgames.gameboy.TileEditor.utils.ImageFilter;
import it.elfgames.gameboy.TileEditor.utils.Utils;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.io.File;
import java.util.Vector;

public class TileMain {
	private static String filename = null;
	private static String dir = null;
	private static GBTileEditor f = null;
	private static ImageViewer imViewer = null;
	private static ImageMapper imMapper = null;
	private static int smode = 1;
	
	private static String lastPosition=null;
	private static Vector<HystoryElement> hystory;
	private static Vector<HystoryElement> hystoryRedo;
    public static void main(String[] args) {
	    	hystory = new Vector<HystoryElement>();
	    	hystoryRedo = new Vector<HystoryElement>();
	    	Utils.setupLanguages();
	    	try
	    	{
	    		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	    	}
	    	catch (Exception e) {}
	    	
	    SwingUtilities.invokeLater(new Runnable() {
	    		public void run() {
	            mainOnEventDispatchThread();
	    		}
	    	});
    }
    private static void mainOnEventDispatchThread() {
    		imViewer = new ImageViewer();
	    imMapper = new ImageMapper(hystory, hystoryRedo);
        imMapper.setPreferredSize(new Dimension(imMapper.getMode()[0],imMapper.getMode()[1]));
        f = new GBTileEditor(imViewer, imMapper);
    }
    
    static class OpenL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
          JFileChooser c = new JFileChooser();
          if (lastPosition == null)
        	  	c.setCurrentDirectory(new File("."));
          else
        	  	c.setCurrentDirectory(new File(lastPosition));
          c.setAcceptAllFileFilterUsed(false);
          c.addChoosableFileFilter(new ImageFilter());
          int rVal = c.showOpenDialog(TileMain.f);
          if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = c.getSelectedFile().getName();
            dir = c.getCurrentDirectory().toString();
            BufferedImage temp = null;
			try {
				temp = SaveLoadImport.loadImage(dir+File.separatorChar+filename, imViewer, imMapper);
			} catch (WrongSizeException err) {
				JOptionPane.showMessageDialog(f, Utils.lang("errBigImage"));
				temp = null;
			} catch (WrongProportionException err) {
				JOptionPane.showMessageDialog(f, Utils.lang("errPropImage"));
				temp = null;
			} catch (WrongImageTypeException err) {
				JOptionPane.showMessageDialog(f, Utils.lang("errCantOpen"));
				temp = null;
			}
            lastPosition = dir;
            if (temp!=null) {
            		imViewer.setImage(temp);
            		imMapper.repaint();
            		f.setZoomEnabled(true);
            } else {
	            	filename=null;
	            	dir=null;
            }
          }
          if (rVal == JFileChooser.CANCEL_OPTION) {
        	  	
          }
        }
      }
    static class ImportL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
          JFileChooser c = new JFileChooser();
          // Demonstrate "Open" dialog:
          if (lastPosition == null)
        	  	c.setCurrentDirectory(new File("."));
          else
        	  	c.setCurrentDirectory(new File(lastPosition));
          c.setAcceptAllFileFilterUsed(false);
          c.addChoosableFileFilter(new Hfilter());
          int rVal = c.showOpenDialog(TileMain.f);
          if (rVal == JFileChooser.APPROVE_OPTION) {
            filename = c.getSelectedFile().getName();
            dir = c.getCurrentDirectory().toString();
            try {
				SaveLoadImport.loadMap(dir+File.separatorChar+filename, imViewer, imMapper);
			} catch (CantImportException err) {
				JOptionPane.showMessageDialog(f, Utils.lang("errCantOpen"));
			}
            f.setMapWidth(imMapper.getTileWidth());
            f.setMapHeight(imMapper.getTileHeight());
            lastPosition = dir;
          }
          if (rVal == JFileChooser.CANCEL_OPTION) {}
        }
      }
      static class SaveL implements ActionListener {
    	  	private boolean rle;
    	  	public SaveL(boolean rle) {
    	  		super();
    	  		this.rle = rle;
    	  	}
        public void actionPerformed(ActionEvent e) {
          if (filename == null) return;
	          JFileChooser c = new JFileChooser();
	          if (lastPosition == null)
	        	  	c.setCurrentDirectory(new File("."));
	          else
	        	  	c.setCurrentDirectory(new File(lastPosition));
	          c.setAcceptAllFileFilterUsed(false);
	          c.addChoosableFileFilter(new Hfilter());
	          int rVal = c.showSaveDialog(TileMain.f);
	          if (rVal == JFileChooser.APPROVE_OPTION) {
	        	  String dir2 = c.getCurrentDirectory().toString();
	        	  String fn = c.getSelectedFile().getName();
	        	  try {
	        		  SaveLoadImport.saveImage(imViewer.getCachedImage(), imViewer, imMapper, dir2+File.separatorChar+fn, smode, rle);
				} catch (CantSaveException err) {
					JOptionPane.showMessageDialog(f, Utils.lang("errCantSave"));
				}
	        	  lastPosition = dir2;
          }
          if (rVal == JFileChooser.CANCEL_OPTION) {
          }
        }
      }
      static class CloseL implements ActionListener {
          public void actionPerformed(ActionEvent e) {
            if (filename == null) return;
            filename = null;
            dir = null;
            imViewer.setImage(null);
            f.setZoomEnabled(false);
          }
        }
      static class SpriteBackgroundMode implements ActionListener {
          public void actionPerformed(ActionEvent e) {
        	  if (e.getActionCommand() == "spr") {
        		  imViewer.setTileset(false);
        		  f.setSpriteModeSelectorEnabled(true);
        		  f.getGBMenuBar().getImportMap().setEnabled(false);
        		  f.setMapSizeEnabled(false);
        		  imMapper.disable();
        	  } else {
        		  imViewer.setTileset(true);
        		  f.setSpriteModeSelectorEnabled(false);
        		  smode = 1;
        		  f.getGBMenuBar().getImportMap().setEnabled(true);
        		  f.setMapSizeEnabled(true);
        		  imMapper.setup(imViewer);
        	  }
        	  
          }
        }
      static class SModeSaving implements ActionListener {
          public void actionPerformed(ActionEvent e) {
        	  if (e.getActionCommand() == "16") {
        		  if (imViewer.getImage()!=null && imViewer.getImage().getHeight()%16 != 0) {
        			  JOptionPane.showMessageDialog(f,Utils.lang("errSpriteHeight"));
        			  smode=1;
        		  } else smode=2;
        	  } else {
        		  smode=1;
        	  }
          }
        }
      static class MapMode implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			int w = f.getMapWidth();
			int h = f.getMapHeight();
			if (w == hystory.get(hystory.size()-1).getWidth() && h == hystory.get(hystory.size()-1).getHeight()) return;
      		imMapper.setMode(w*8, h*8);
      		Vector<Integer> clone = new Vector<Integer>();
      		for (int a : imMapper.getMap()) clone.add(a);
     		hystory.add(new HystoryElement(clone, w, h));
       		hystoryRedo.clear();
		}
        }
      static class ZoomSaving implements ActionListener {
          public void actionPerformed(ActionEvent e) {
        	  if (e.getActionCommand() == "2") {
        		  imViewer.setZoom(2);
        		  imMapper.setZoom(2);
        	  } else {
        		  imViewer.setZoom(1);
        		  imMapper.setZoom(1);
        	  }
          }
        }
      static class About implements ActionListener {
          public void actionPerformed(ActionEvent e) {
			  JOptionPane.showMessageDialog(f,Utils.lang("aboutText"));
          }
        }
      static class Undo implements ActionListener {
          public void actionPerformed(ActionEvent e) {
        	  if (imMapper.undo()) {
        		  f.setMapWidth(hystory.lastElement().getWidth());
        		  f.setMapHeight(hystory.lastElement().getHeight());
        	  }
          }
        }
      static class Redo implements ActionListener {
          public void actionPerformed(ActionEvent e) {
        	  if (imMapper.redo()) {
        		  f.setMapWidth(hystory.lastElement().getWidth());
        		  f.setMapHeight(hystory.lastElement().getHeight());
        	  }
          }
        }
      static class GridV implements ActionListener {
          public void actionPerformed(ActionEvent e) {
        	  	imMapper.gridVisible();
          }
        }
      static class RefreshMenu implements FocusListener {
			public void focusGained(FocusEvent e) {
			    SwingUtilities.updateComponentTreeUI(f.getGBMenuBar());
			}
			public void focusLost(FocusEvent e) {}
        }
      static class ChangeLanguage implements ActionListener {
  	  	private String name;
  	  	public ChangeLanguage(String name) {
  	  		super();
  	  		this.name = name;
  	  	}
  	  	public void actionPerformed(ActionEvent e) {
  	  		Utils.setActiveLang(name);
  	  	}
    }
}
