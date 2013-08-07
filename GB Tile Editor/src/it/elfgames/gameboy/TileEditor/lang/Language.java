package it.elfgames.gameboy.TileEditor.lang;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.HashSet;


 // default (English language, United States)
 public class Language extends ResourceBundle {
     public Object handleGetObject(String key) {
         if (key.equals("title")) return "Gameboy Tile Editor";
    	 
         if (key.equals("okKey")) return "Ok";
         if (key.equals("cancelKey")) return "Cancel";
         //Menu
         if (key.equals("file")) return "File";
         	if (key.equals("open")) return "Open";
         	if (key.equals("import")) return "Import map...";
         	if (key.equals("save")) return "Save as...";
         	if (key.equals("saveComp")) return "Save compressed as...";
         	if (key.equals("close")) return "Close";
         if (key.equals("edit")) return "Edit";
         	if (key.equals("undo")) return "Undo";
         	if (key.equals("redo")) return "Redo";
         if (key.equals("view")) return "View";
         	if (key.equals("grid")) return "Show/Hide grid";
         if (key.equals("lang")) return "Language";
         if (key.equals("help")) return "Help";
         	if (key.equals("about")) return "About";
         		if (key.equals("aboutText")) return "GB Tile editor v 1.0\n created by ProGM\n www.rpg2s.net\n2013";
         
         //labels
         if (key.equals("width")) return "width: ";
         if (key.equals("height")) return "height: ";
         if (key.equals("8x8")) return "Sprites 8x8 mode";
         if (key.equals("8x16")) return "Sprites 8x16 mode";
         if (key.equals("1x")) return "Zoom 1x";
         if (key.equals("2x")) return "Zoom 2x";
         if (key.equals("spr")) return "Sprites editing";
         if (key.equals("bkg")) return "Background editing";
         
         //errors
         if (key.equals("errBigImage")) return "Image must be 256x256px or lower.";
         if (key.equals("errPropImage")) return "Image sizes must be multiples of 8.";
         if (key.equals("errCantOpen")) return "Can't open the file.";
         if (key.equals("errCantSave")) return "Unable to save file in that position.";
         if (key.equals("errSpriteHeight")) return "Image height must be a multiple of 16.";
         return null;
     }

    public Enumeration<String> getKeys() {
         return Collections.enumeration(keySet());
    }

    protected Set<String> handleKeySet() {
         return new HashSet<String>(Arrays.asList(
        		 //title of the application
        		 "title",
        		 //various
        		 "okKey", "cancelKey",
        		 //Menus
        		 "file", "open", "import", "save", "saveComp", "close",
        		 "edit", "undo", "redo",
        		 "view", "grid",
        		 "lang",
        		 "help", "about","aboutText",
        		 //labels
        		 "width", "height", "8x8", "8x16", "1x", "2x", "spr", "bkg",
        		 //errors
        		 "errBigImage", "errPropImage", "errCantOpen", "errCantSave", "errSpriteHeight"));
    }
    private static final String NAME = "English";
	public String getName() {
		return NAME;
	}
 }
