package it.elfgames.gameboy.TileEditor.utils;

import java.io.File;
import javax.swing.filechooser.*;

public class Hfilter extends FileFilter {
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.h)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "*.h (GBDK image files)";
    }
}