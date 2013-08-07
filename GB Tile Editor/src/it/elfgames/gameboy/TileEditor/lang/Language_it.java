package it.elfgames.gameboy.TileEditor.lang;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// Italy language
public class Language_it extends Language {
    public Object handleGetObject(String key) {
        if (key.equals("cancelKey")) return "Annulla";
        
        //if (key.equals("file")) return "File";
	     	if (key.equals("open")) return "Apri";
	     	if (key.equals("import")) return "Importa mappa...";
	     	if (key.equals("save")) return "Salva con nome...";
	     	if (key.equals("saveComp")) return "Salva compresso con nome...";
	     	if (key.equals("close")) return "Chiudi";
	     if (key.equals("edit")) return "Modifica";
	     	if (key.equals("undo")) return "Annulla";
	     	if (key.equals("redo")) return "Ripeti";
	     if (key.equals("view")) return "Visualizza";
	     	if (key.equals("grid")) return "Mostra/Nascondi griglia";
	     if (key.equals("lang")) return "Lingua";
	     if (key.equals("help")) return "Aiuto";
	     	if (key.equals("about")) return "About";
     	
        if (key.equals("errBigImage")) return "Le immagini con dimensioni superiori a 256x256 non sono supportate.";
        if (key.equals("errPropImage")) return "L'immagine deve avere altezza e larghezza divisibili per 8.";
        if (key.equals("errCantOpen")) return "Impossibile aprire il file.";
        if (key.equals("errCantSave")) return "Impossibile salvare l'immagine nella posizione indicata.";
        if (key.equals("errSpriteHeight")) return "L'immagine deve avere altezza divisibile per 16.";
        return super.handleGetObject(key);
    }

    protected Set<String> handleKeySet() {
        return new HashSet<String>(Arrays.asList(
       		 //various
       		 "cancelKey",
       		 //Menus
       		 "open", "import", "save", "saveComp", "close",
       		 "edit", "undo", "redo",
       		 "view", "grid",
       		 "help", "about",
       		 //errors
       		 "errBigImage", "errPropImage", "errCantOpen", "errCantSave", "errSpriteHeight"));
    }
    private static final String NAME = "Italiano";
	public String getName() {
		return NAME;
	}
}
