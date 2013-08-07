package it.elfgames.gameboy.TileEditor.history;
import java.util.Vector;


public class HystoryElement {
	private Vector<Integer> map;
	private int width;
	private int height;
	public HystoryElement(Vector<Integer> _map, int _width, int _height) {
		map = _map;
		width = _width;
		height=_height;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public Vector<Integer> getMap() {
		return map;
	}
}
