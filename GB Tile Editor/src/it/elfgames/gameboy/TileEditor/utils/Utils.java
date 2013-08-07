package it.elfgames.gameboy.TileEditor.utils;
import it.elfgames.gameboy.TileEditor.lang.Language;
import it.elfgames.gameboy.TileEditor.lang.Language_it;

import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;

/* Utils.java is used by FileChooserDemo2.java. */
public class Utils {
    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String h = "h";
    public final static String tif = "tif";
    public final static String png = "png";
    public final static String bmp = "bmp";
    
    private static Vector<Language> languages;
    private static Language language;
    /*
     * Get the extension of a file.
     */
    public static String getHexString(int number, int minDigits) {
    		String s = Integer.toHexString(number);
    		while (s.length() < minDigits)
    			s = "0" + s;
    		
    		return "0x"+s;
    }
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    public static String removeExtension(String s) {
        String ext = null;
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(0, i-1);
        }
        return ext;
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Utils.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    public static void setupLanguages() {
    		languages = new Vector<Language>();
    		languages.add(new Language());
    		languages.add(new Language_it());
    		Utils.language = languages.get(0);
    }
    
    public static void setActiveLang(String name) {
		for (int i = 0; i < languages.size(); ++i)
			if (languages.get(i).getName().equals(name))
				language = languages.get(i);
    }
    public static String lang(String word) {
    		return language.getString(word);
    }
	public static Language getActiveLanguage() {
		return language;
	}
	public static Vector<Language> getLangs() {
		return languages;
	}
}