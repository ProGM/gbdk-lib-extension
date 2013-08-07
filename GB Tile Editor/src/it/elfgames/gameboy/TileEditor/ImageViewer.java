package it.elfgames.gameboy.TileEditor;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class ImageViewer extends Panel implements MouseListener, MouseMotionListener {
	public static final int COLOR_MAP[] = {0xFF000000, 0xFFB0B0B0, 0xFF808080, 0xFFc8d0d0};
	
	private static final long serialVersionUID = 1L;
	private BufferedImage image, cache;
    private Vector<Integer> tileSelected;
    private boolean tileset, toRefresh;
    private Image offsetPaint;
    private int x0, y0, zoom;
	public ImageViewer() {
		zoom = 1;
		x0 = y0 = 0;
		tileset = toRefresh = false;
		image = cache = null;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	public void setImage(BufferedImage im) {
		  image = im;
		  cache = im;
		  if (tileSelected==null) {
			  tileSelected = new Vector<Integer>();
		  }
		  repaint();
	}
	public Vector<Integer> getTile() {
		return tileSelected;
	}
	public void setTileset(boolean b) {
		tileset = b;
		repaint();
	}
	public void setZoom(int zoom) {
		this.zoom = zoom;
		offsetPaint=createImage((256*zoom)+1, (256*zoom)+1);
		image = (BufferedImage) createImage(cache.getWidth()*zoom, cache.getHeight()*zoom);
		Graphics g = image.getGraphics();
		g.drawImage(cache, 0, 0, image.getWidth(), image.getHeight(), null);
		toRefresh=true;
		repaint();
	}
	public BufferedImage getImage() {
		return image;
	}
	public BufferedImage getCachedImage() {
		return cache;
	}
	public void update(Graphics g) {
		if (offsetPaint==null) offsetPaint=createImage(257, 257);
		if (toRefresh) {
			g.clearRect(0, 0, 600, 600);
			toRefresh = false;
		}
		Graphics g2 = offsetPaint.getGraphics();
		g2.clearRect(0, 0, 600, 600);
		if (image!=null) {
			g2.drawImage( image, 0, 0, null);
			if (tileset) {
				g2.setColor(new Color(255, 0, 0));
				int w=0, width=0, height=0, x=-1, y=-1;
				if (tileSelected.size()>1) {
					for (int v : tileSelected) {
						if (v==-1) {
							if(w!=0) {
								width=w;
								height++;
							}
						w=0;
						} else {
							if (x==-1)
								x=v%(image.getWidth()/(8*zoom));
							if (y==-1)
								y=v/+(image.getWidth()/(8*zoom));
							w++;
							if (w >= image.getWidth()/(8*zoom))  {
								height++;
								width = w;
								w=0;
							}
						}
					}
					if ((x+width)>image.getWidth()/(8*zoom))
						width = (image.getWidth()/(8*zoom))-x;
					if ((y+height)>image.getHeight()/(8*zoom))
						height = (image.getHeight()/(8*zoom))-y;
				} else if (tileSelected.size()==1) {
					width=1;height=1;
					x=tileSelected.get(0) %(image.getWidth()/(8*zoom));
					y=tileSelected.get(0) /(image.getWidth()/(8*zoom));
				}
				g2.drawRect(x*(8*zoom), y*(8*zoom), width*(8*zoom), height*(8*zoom));
			}
		}
		paint(g);
	}
	public void paint(Graphics g) {
		if (offsetPaint!=null) g.drawImage(offsetPaint, 0, 0, null);
	}
	public void mouseClicked(MouseEvent e) {
		if (image !=null && tileset) {
			if (e.getX()>=0 && e.getX() <image.getWidth() && e.getY() >= 0 && e.getY() < image.getHeight()) {
				tileSelected = new Vector<Integer>();
				x0 = (e.getX()/(8*zoom));
				y0 = (e.getY()/(8*zoom));
				tileSelected.add(x0 + (y0*(image.getWidth()/(8*zoom))));
				repaint();
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		if (image !=null && tileset) {
			if (e.getX()>=0 && e.getX() <=image.getWidth() && e.getY() >= 0 && e.getY() <= image.getHeight()) {
				x0=e.getX()/(8*zoom);
				y0=e.getY()/(8*zoom);
			}
		}
		
	}
	public void mouseDragged(MouseEvent e) {
		if (image !=null && tileset) {
			if (e.getX()>=0 && e.getX() <image.getWidth() && e.getY() >= 0 && e.getY() < image.getHeight()) {
				tileSelected = new Vector<Integer>();
				int XM = Math.abs((e.getX()/(8*zoom))-x0)+1;
				int YM = Math.abs((e.getY()/(8*zoom))-y0)+1;
				int signx=((e.getX()/(8*zoom))-x0);
				int signy=((e.getY()/(8*zoom))-y0);
				
				for (int i=0;i<XM*(YM*(image.getWidth()/(8*zoom)));i++) tileSelected.add(-1);
				if (signx>=0 && signy>=0) {
					for (int x=0; x<XM;x++) {
						for (int y=0;y<YM;y++) {
							tileSelected.set(x+(y*(image.getWidth()/(8*zoom))), x+x0+((y+y0)*(image.getWidth()/(8*zoom))));// = (e.getX() / 8) + ((e.getY()/8)*(image.getWidth()/8));
						}
					}
				} else if (signx<0 && signy>0) {
					for (int x=0; x<XM;x++) {
						for (int y=0;y<YM;y++) {
							tileSelected.set(x+(y*(image.getWidth()/(8*zoom))), x+(e.getX()/(8*zoom))+((y+y0)*(image.getWidth()/(8*zoom))));// = (e.getX() / 8) + ((e.getY()/8)*(image.getWidth()/8));
						}
					}
				} else if (signx>0 && signy<0) {
					for (int x=0; x<XM;x++) {
						for (int y=0;y<YM;y++) {
							tileSelected.set(x+(y*(image.getWidth()/(8*zoom))), x+x0+((y+(e.getY()/(8*zoom)))*(image.getWidth()/(8*zoom))));// = (e.getX() / 8) + ((e.getY()/8)*(image.getWidth()/8));
						}
					}
				} else {
					for (int x=0; x<XM;x++) {
						for (int y=0;y<YM;y++) {
							tileSelected.set(x+(y*(image.getWidth()/(8*zoom))), x+(e.getX()/(8*zoom))+((y+(e.getY()/(8*zoom)))*(image.getWidth()/(8*zoom))));// = (e.getX() / 8) + ((e.getY()/8)*(image.getWidth()/8));
						}
					}
				}
		      repaint();
			}
		}
		
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
}