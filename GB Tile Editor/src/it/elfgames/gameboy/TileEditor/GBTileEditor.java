package it.elfgames.gameboy.TileEditor;

import it.elfgames.gameboy.TileEditor.TileMain.MapMode;
import it.elfgames.gameboy.TileEditor.TileMain.SModeSaving;
import it.elfgames.gameboy.TileEditor.TileMain.SpriteBackgroundMode;
import it.elfgames.gameboy.TileEditor.TileMain.ZoomSaving;
import it.elfgames.gameboy.TileEditor.utils.Utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

public class GBTileEditor extends JFrame {
	public static final int ZOOM_1X = 0;
	public static final int ZOOM_2X = 1;
	public static final int MODE_8x8 = 2;
	public static final int MODE_8x16 = 3;
	private static final long serialVersionUID = -8975915804849652748L;
	private JRadioButton z1x;
	private JRadioButton z2x;
	private JRadioButton m8x8;
	private JRadioButton m8x16;
	private JSpinner mapWidth;
	private JSpinner mapHeight;
	private MenuBar menuBar;

	public GBTileEditor(ImageViewer imViewer, ImageMapper imMapper) {
		super(Utils.lang("title"));
        this.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        
        JPanel sprbkg = setupEditingModePanel();
        JPanel zoomRadio = setupZoomPanel();
        
        JPanel upMenu = new JPanel();
        upMenu.setLayout(new GridLayout(0, 1));
        upMenu.add(sprbkg);
        upMenu.add(zoomRadio);
        
        JPanel jplRadio = setupSpriteModePanel();
        JPanel bkgRadio = setupMapSizePanel();
        
        menuBar = new MenuBar();
        this.setJMenuBar(menuBar);
        
        JScrollPane editorScroll = new JScrollPane(imMapper);
        editorScroll.setBackground(new Color(0, 0, 0, 0));
        imMapper.setSpanel(editorScroll);
        
        JPanel imPanel = new JPanel();
        imPanel.setLayout(new GridLayout(0, 2));
        imPanel.add(imViewer);
        imPanel.add(editorScroll);
        
        JPanel modePanel = new JPanel();
        modePanel.setLayout(new GridLayout(2, 1));
        modePanel.add(jplRadio);
        modePanel.add(bkgRadio);
        
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(upMenu, BorderLayout.NORTH);
        this.getContentPane().add(imPanel);
        this.getContentPane().add(modePanel, BorderLayout.SOUTH);
        
        this.pack();
        this.setVisible(true);
	}
	private JPanel setupMapSizePanel() {
	    	mapWidth = new JSpinner(new SpinnerNumberModel(32, 20, 100, 1));
	    	mapHeight = new JSpinner(new SpinnerNumberModel(32, 18, 100, 1));
	    	ChangeListener act2 = new MapMode();
	    	mapWidth.addChangeListener(act2);
	    	mapHeight.addChangeListener(act2);
		mapWidth.setEnabled(false);
	    mapHeight.setEnabled(false);
	    JPanel bkgRadio = new JPanel();
	    bkgRadio.add(new JLabel(Utils.lang("width")));
	    bkgRadio.add(mapWidth);
	    bkgRadio.add(new JLabel(Utils.lang("height")));
	    bkgRadio.add(mapHeight);
		return bkgRadio;
	}
	private JPanel setupSpriteModePanel() {
        ActionListener act = new SModeSaving();
        m8x8 = new JRadioButton(Utils.lang("8x8"));
        m8x8.setSelected(true);
        m8x16 = new JRadioButton(Utils.lang("8x16"));
        m8x8.setActionCommand("8");
        m8x16.setActionCommand("16");
        m8x8.addActionListener(act);
        m8x16.addActionListener(act);
        ButtonGroup group = new ButtonGroup();
        group.add(m8x8);
        group.add(m8x16);
        JPanel jplRadio = new JPanel();
        jplRadio.setLayout(new GridLayout(0, 4));
        jplRadio.setBounds(0, 0, 256, 20);
        jplRadio.add(m8x8);
        jplRadio.add(m8x16);
		return jplRadio;
	}
	private JPanel setupZoomPanel() {
        ActionListener act3 = new ZoomSaving();
        z1x = new JRadioButton(Utils.lang("1x"));
        z1x.setEnabled(false);
        z1x.setSelected(true);
        z2x = new JRadioButton(Utils.lang("2x"));
        z2x.setEnabled(false);
        z1x.setActionCommand("1");
        z2x.setActionCommand("2");
        z1x.addActionListener(act3);
        z2x.addActionListener(act3);
        ButtonGroup group3 = new ButtonGroup();
        group3.add(z1x);
        group3.add(z2x);
        JPanel zoomRadio = new JPanel();
        zoomRadio.setBounds(0, 0, 256, 20);
        zoomRadio.add(z1x);
        zoomRadio.add(z2x);
		return zoomRadio;
	}
	private JPanel setupEditingModePanel() {
        ActionListener act1 = new SpriteBackgroundMode();
        JRadioButton spr = new JRadioButton(Utils.lang("spr"));
        spr.setSelected(true);
        JRadioButton bkg = new JRadioButton(Utils.lang("bkg"));
        spr.setActionCommand("spr");
        bkg.setActionCommand("bkg");
        spr.addActionListener(act1);
        bkg.addActionListener(act1);
        ButtonGroup group1 = new ButtonGroup();
        group1.add(spr);
        group1.add(bkg);
        JPanel sprbkg = new JPanel();
        sprbkg.setBounds(0, 0, 256, 20);
        sprbkg.add(spr);
        sprbkg.add(bkg);
        return sprbkg;
	}
	public void setZoom(int zoom) {
		switch (zoom) {
		case ZOOM_1X:
			z1x.setSelected(true);
			z2x.setSelected(false);
			break;
		case ZOOM_2X:
			z1x.setSelected(false);
			z2x.setSelected(true);
			break;
		default:
			break;
		}
	}
	public void setZoomEnabled(boolean value) {
		z1x.setEnabled(value);
		z2x.setEnabled(value);
		if (value) {
			z1x.setSelected(true);
			z2x.setSelected(false);
		}
	}
	public void setSpriteModeSelectorEnabled(boolean value) {
		m8x8.setEnabled(value);
		m8x16.setEnabled(value);
		if (value) {
			m8x8.setSelected(true);
			m8x16.setSelected(false);
		}
	}
	public void setSpriteMode(int mode) {
		switch (mode) {
		case MODE_8x8:
			m8x8.setSelected(true);
			m8x16.setSelected(false);
			break;
		case MODE_8x16:
			m8x8.setSelected(false);
			m8x16.setSelected(true);
			break;
		default:
			break;
		}
	}
	public void setMapSizeEnabled(boolean value) {
		mapWidth.setEnabled(value);
		mapHeight.setEnabled(value);
	}
	public int getMapWidth() {
		return Integer.parseInt(mapWidth.getValue().toString());
	}
	public int getMapHeight() {
		return Integer.parseInt(mapHeight.getValue().toString());
	}
	public void setMapWidth(int width) {
		System.out.println(width);
		mapWidth.setValue(width);
	}
	public void setMapHeight(int height) {
		System.out.println(height);
		mapHeight.setValue(height);
	}
	public MenuBar getGBMenuBar() {
		return menuBar;
	}
}
