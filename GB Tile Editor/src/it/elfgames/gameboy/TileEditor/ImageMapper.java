package it.elfgames.gameboy.TileEditor;
import it.elfgames.gameboy.TileEditor.history.HystoryElement;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
public class ImageMapper extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private ImageViewer image;
    private Vector<Integer> map;
    private JScrollPane spanel;
    private BufferedImage grid;
    private int width;
    private int height;
    private boolean toRefresh, refreshSelection, active, gridV, dragging;
    private Image offsetPaint, selection;
    private int zoom;
    private Vector<HystoryElement> hystory;
    private Vector<HystoryElement> hystoryRedo;
    public ImageMapper(Vector<HystoryElement> hystory, Vector<HystoryElement> hystoryRedo) {
	    	this.hystory = hystory;
	    this.hystoryRedo = hystoryRedo;
	    toRefresh = refreshSelection = active = dragging = false;
	    gridV = true;
	    	width = height = 256;
		map = null;
		image = null;
		spanel = null;
		offsetPaint = null;
		zoom = 1;
	    	grid = new BufferedImage(257, 257, BufferedImage.TYPE_INT_ARGB);
	    	selection = new BufferedImage(256*zoom + 1, 256*zoom + 1, BufferedImage.TYPE_INT_ARGB);
	    	Graphics g = grid.getGraphics();
		g.setColor(new Color(0, 0, 0, 50));
		for (int x = 0; x <256; x+=8) {
		    	for (int y = 0;y < 256; y+=8) {
		    		g.drawRect(x, y, 8, 8);
		    	}
		}
	    	addMouseListener(this);
	    	addMouseMotionListener(this);
    }
  public void setSpanel(JScrollPane sp) {
	  spanel = sp;
  }
  public boolean undo() {
	  if (hystory.size()<=1) return false;
	  int index = hystory.size() - 1;
	  hystoryRedo.add(hystory.remove(index));
	  hystory.trimToSize();
	  setMode(hystory.lastElement().getWidth()*8, hystory.lastElement().getHeight()*8);
	  map = new Vector<Integer>();
	  for (int a : hystory.lastElement().getMap()) map.add(a);
	  toRefresh=true;
	  repaint();
	  return true;
  }
  public boolean redo() {
	  if (hystoryRedo.size()==0) return false;
	  int index = hystoryRedo.size() - 1;
	  hystory.add(hystoryRedo.remove(index));
	  hystoryRedo.trimToSize();
	  setMode(hystory.lastElement().getWidth()*8, hystory.lastElement().getHeight()*8);
	  map = new Vector<Integer>();
	  for (int a : hystory.lastElement().getMap()) map.add(a);
	  toRefresh=true;
	  repaint();
	  return true;
  }
  public void setup(ImageViewer im) {
	  image = im;
	  active = true;
	  int d = (width/8)*(height/8);
	  map = new Vector<Integer>();
	  for (int i = 0;i < d; i++) {
		  map.add(0);
	  }
	  toRefresh=true;
	  Vector<Integer> clone = new Vector<Integer>();
	  for (int a : map)
		  clone.add(a);
	  hystory.add(new HystoryElement(clone, width/8, height/8));
	  hystoryRedo.clear();
	  repaint();
  }
  public void gridVisible() {
	  if (gridV)
		  gridV=false;
	  else
		  gridV=true;
	  toRefresh=true;
	  repaint();
  }
  public int [] getMode() {
	  int size[] = new int[2];
	  size[0] = width;
	  size[1] = height;
	  return size;
  }
  public boolean isActive() {
	  return active;
  }
  public int getTileWidth() {
	  return width / 8;
  }
  public int getTileHeight() {
	  return height / 8;
  }
  public Vector<Integer> getMap() {
	  return map;
  }
  public void setMap(Vector<Integer> newMap, int width, int height) {
	  map = newMap;
	  this.width = width*8;
	  this.height = height*8;
	  toRefresh = true;
	  repaint();
  }
  public void setZoom(int _zoom) {
	  zoom = _zoom;
	  Image offpt=createImage(width*zoom +1, height*zoom +1);
	  offpt.getGraphics().drawImage(offsetPaint, 0, 0, width*zoom +1, height*zoom +1, null);
	  offsetPaint.flush();
	  offsetPaint=offpt;
	  selection = new BufferedImage(width*zoom + 1, height*zoom + 1, BufferedImage.TYPE_INT_ARGB);
	  grid = new BufferedImage(width*zoom + 1, height*zoom + 1, BufferedImage.TYPE_INT_ARGB);
	  Graphics g = grid.getGraphics();
	  g.setColor(new Color(0, 0, 0, 50));
	  for (int x = 0; x <width*zoom; x=x+(8*zoom)) {
		  for (int y=0;y<height*zoom;y=y+(8*zoom)) {
			  g.drawRect(x, y, 8*zoom, 8*zoom);
		  }
	  }
	  toRefresh=true;
	  refreshSelection=true;
	  repaint();
  }
  public void disable() {
	  image = null;
	  active = false;
	  toRefresh=true;
	  hystory.clear();
	  hystoryRedo.clear();
	  repaint();
  }
  public void paint(Graphics g) {
	  	if (offsetPaint==null)  offsetPaint=createImage(257, 257);
	  	if (toRefresh) {
		    setPreferredSize(new Dimension(getMode()[0]*zoom, getMode()[1]*zoom));
	  		g.clearRect(0, 0, 100*8*zoom, 100*8*zoom);
	  		toRefresh=false;
	  		if (spanel!=null) {
	  			spanel.getGraphics().clearRect(0, 0, spanel.getWidth(), spanel.getHeight());
	  			spanel.getVerticalScrollBar().revalidate();
	  			spanel.getVerticalScrollBar().repaint();
	  			spanel.getHorizontalScrollBar().revalidate();
	  			spanel.getHorizontalScrollBar().repaint();
	  			spanel.revalidate();
	  			spanel.repaint();
	  		}
	  	}
		  if (image!=null && image.getImage()!=null && map != null && !refreshSelection) {
			  Graphics ofg = offsetPaint.getGraphics();
			  for (int i=0;i<map.size();i++) {
				  if (map.get(i) == -1) continue;
				  ofg.drawImage( image.getImage().getSubimage((map.get(i) % (image.getImage().getWidth()/(8*zoom)))*(8*zoom), (map.get(i) / (image.getImage().getWidth()/(8*zoom))) * (8*zoom), (8*zoom), (8*zoom)), (i%(width/8))*(8*zoom), (i/(width/8))*(8*zoom), null);
			  }
		}
		if (active) {
		g.drawImage(offsetPaint, 0, 0, null);
		if (gridV) g.drawImage(grid.getSubimage(0, 0, width*zoom+1, height*zoom+1), 0, 0, null);
		g.drawImage(selection, 0, 0, null);
		refreshSelection = false;
		}
  }
  public void setMode(int w, int h) {
	  if (w>width) {
		  int j=0;
		  Vector<Integer> _map = new Vector<Integer>();
		  for (int y=0;y<(height/8);y++) {
			  for (int x=0;x<(width/8);x++) {
				  _map.add(map.get(j));
				  j++;
		       }
			  for (int k=0;k<(w/8)-(width/8);k++)
				  _map.add(0);
	      }
		  map = _map;
	  } else if (w<width) {
		  Vector<Integer> _map = new Vector<Integer>();
		  int j=0;
		  for (int y=0;y<(height/8);y++) {
			  for (int x=0;x<(w/8);x++) {
				  _map.add(map.get(j));
				  j++;
		       }
			  j=j+((width/8)-(w/8));
	      }
		  map = _map;
	  }
	  width = w;
	  if (h>height) {
		  for (int i=0;i<(h/8)-(height/8);i++) {
			  for(int j = 0;j<(width/8);j++) map.add(0);
		  }
	 } else if (h<height) {
		 Vector<Integer> _map = new Vector<Integer>();
		 for (int i=0;i<(w/8)*(h/8);i++)
		 _map.add(map.get(i));
		 map = _map;
	  }
	  height = h;
	  offsetPaint.flush();
	  offsetPaint=createImage(width*zoom +1, height*zoom +1);
	  selection.flush();
      selection = new BufferedImage(width*zoom + 1, height*zoom + 1, BufferedImage.TYPE_INT_ARGB);
	  if (grid.getWidth()<width*zoom+1 || grid.getHeight()<height*zoom+1) {
		  grid.flush();
		  grid = new BufferedImage(width*zoom + 1, height*zoom + 1, BufferedImage.TYPE_INT_ARGB);
		  Graphics g = grid.getGraphics();
		  g.setColor(new Color(0, 0, 0, 50));
		  for (int x = 0; x <width*zoom; x=x+(8*zoom)) {
			  for (int y=0;y<height*zoom;y=y+(8*zoom)) {
				  g.drawRect(x, y, 8*zoom, 8*zoom);
			  }
		  }
	  }
	  toRefresh=true;
	  repaint();
  }
	public void mouseClicked(MouseEvent e) {
		if (image !=null && image.getImage() != null && map!=null && image.getTile().size()>0) {
			if (e.getX()>=0 && e.getX() <width*zoom && e.getY() >= 0 && e.getY() < height*zoom) {
				int i=0;
			    Graphics ofg = offsetPaint.getGraphics();
				while((image.getTile().get(i++)) == -1) {
					if (i+1==image.getTile().size()) break;
				}
				i--;
				for (int v : image.getTile()) {
					if (v==-1) continue;
					int _x = (v-image.getTile().get(i)) % (image.getImage().getWidth()/(8*zoom));
					int _y = (v-image.getTile().get(i)) / (image.getImage().getWidth()/(8*zoom));
					
					if (_x+(e.getX() / (8*zoom))>=(width*zoom)/(8*zoom)) continue;
					if (_y+(e.getY() / (8*zoom))>=(height*zoom)/(8*zoom)) continue;
					int j = ((e.getX() / (8*zoom))+_x + (((e.getY()/(8*zoom))+_y)*((width*zoom)/(8*zoom))));
					map.set(j, v);
					ofg.drawImage( image.getImage().getSubimage((v % (image.getImage().getWidth()/(8*zoom)))*(8*zoom), (v / (image.getImage().getWidth()/(8*zoom))) * (8*zoom), (8*zoom), (8*zoom)), (j%(width/8))*(8*zoom), (j/(width/8))*(8*zoom), null);
				}
				refreshSelection=true;
				hystoryMem();
				repaint();
			}
		}	
	}
	public void mouseExited(MouseEvent e) {
		Graphics2D g2 = (Graphics2D) selection.getGraphics();
		g2.setBackground(new Color(0, 0, 0, 0));
		g2.clearRect(0, 0, 100*8*zoom, 100*8*zoom);
		refreshSelection = true;
		repaint();
	}
	public void hystoryMem() {
		Vector<Integer> clone = new Vector<Integer>();
		for (int a : map) clone.add(a);
		hystory.add(new HystoryElement(clone, width/8, height/8));
		hystoryRedo.clear();
		hystoryRedo.trimToSize();
	}
	public void mouseReleased(MouseEvent e) {
		if (dragging) {
			hystoryMem();
			dragging=false;
		}
	}
	public void mouseDragged(MouseEvent e) {
		if (image !=null && image.getImage() != null && map!=null && image.getTile().size()>0) {
			if (e.getX()>=0 && e.getX() <width*zoom && e.getY() >= 0 && e.getY() < height*zoom) {
				int i=0;
			    Graphics ofg = offsetPaint.getGraphics();
				while((image.getTile().get(i++)) == -1) {
					if (i+1==image.getTile().size()) break;
				}
				i--;
				for (int v : image.getTile()) {
					if (v==-1) continue;
					int _x = (v-image.getTile().get(i)) % (image.getImage().getWidth()/(8*zoom));
					int _y = (v-image.getTile().get(i)) / (image.getImage().getWidth()/(8*zoom));
					
					if (_x+(e.getX() / (8*zoom))>=(width*zoom)/(8*zoom)) continue;
					if (_y+(e.getY() / (8*zoom))>=(height*zoom)/(8*zoom)) continue;
					int j = ((e.getX() / (8*zoom))+_x + (((e.getY()/(8*zoom))+_y)*((width*zoom)/(8*zoom))));
					map.set(j, v);
					ofg.drawImage( image.getImage().getSubimage((v % (image.getImage().getWidth()/(8*zoom)))*(8*zoom), (v / (image.getImage().getWidth()/(8*zoom))) * (8*zoom), (8*zoom), (8*zoom)), (j%(width/8))*(8*zoom), (j/(width/8))*(8*zoom), null);
				}
				refreshSelection(e);
				refreshSelection=true;
				dragging=true;
				repaint();
			}
		}
	}
	public void refreshSelection(MouseEvent e) {
		if (image !=null && image.getImage() != null && map!=null) {
			if (e.getX()>=0 && e.getX() <width*zoom && e.getY() >= 0 && e.getY() < height*zoom) {
				Vector<Integer> tileSelected = image.getTile();
				int w=0, width=0, height=0, x=-1, y=-1;
				if (tileSelected.size()>1) {
				for (int v : tileSelected) {
					if (v==-1) {
						if(w!=0) {
							width=w;
							height++;
						}
					w=0;
					} else  {
						if (x==-1) x=v%(image.getImage().getWidth()/(8*zoom));
						if (y==-1) y=v/(image.getImage().getWidth()/(8*zoom));
						w++;
						if (w >= image.getImage().getWidth()/(8*zoom))  {
							height++;
							width = w;
							w=0;
						}
					}
				}
				if ((x+width)>image.getImage().getWidth()/(8*zoom)) width = (image.getImage().getWidth()/(8*zoom))-x;
				if ((y+height)>image.getImage().getHeight()/(8*zoom)) height = (image.getImage().getHeight()/(8*zoom))-y;
				} else if (tileSelected.size()==1) {
					width=1;height=1;
					x=tileSelected.get(0) %(image.getImage().getWidth()/(8*zoom));
					y=tileSelected.get(0) /(image.getImage().getWidth()/(8*zoom));
				}
				Graphics2D g2 = (Graphics2D) selection.getGraphics();
				g2.setBackground(new Color(0, 0, 0, 0));
				g2.clearRect(0, 0, 100*8*zoom, 100*8*zoom);
				g2.setColor(new Color(255, 0, 0));
				g2.drawRect((e.getX()/(8*zoom))*(8*zoom), (e.getY()/(8*zoom))*(8*zoom), width*(8*zoom), height*(8*zoom));
			} else {
				Graphics2D g2 = (Graphics2D) selection.getGraphics();
				g2.setBackground(new Color(0, 0, 0, 0));
				g2.clearRect(0, 0, 100*8*zoom, 100*8*zoom);
			}
		}
	}
	public void mouseMoved(MouseEvent e) {
		refreshSelection(e);
		refreshSelection=true;
		repaint();
	}
	public void mouseEntered(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
}