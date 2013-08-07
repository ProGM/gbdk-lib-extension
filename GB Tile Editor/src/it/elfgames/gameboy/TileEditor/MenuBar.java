package it.elfgames.gameboy.TileEditor;

import it.elfgames.gameboy.TileEditor.TileMain.*;
import it.elfgames.gameboy.TileEditor.utils.Utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

//to implement change language
//import javax.swing.JRadioButtonMenuItem;
//import it.elfgames.gameboy.TileEditor.lang.Language;
//import java.util.Vector;
public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 7943256006505231465L;
	private JMenuItem importMap;
	
	public MenuBar() {
        this.setOpaque(true);
        this.addFocusListener(new RefreshMenu());
        this.setPreferredSize(new Dimension(200, 20));
        JMenu file = setupFileMenu();
        JMenu edit = setupEditMenu();
        JMenu view = setupViewMenu();
        //TO IMPLEMENT
        //JMenu lang = setupLangMenu();
        
        JMenu help = setupHelpMenu();
        
        this.add(file);
        this.add(edit);
        this.add(view);
        //TO IMPLEMENT
        //this.add(lang);
        this.add(help);
	}
/*
 * TO IMPLEMENT
	private JMenu setupLangMenu() {
        JMenu languageMenu = new JMenu(Utils.lang("lang"));
        Vector<Language> langs = Utils.getLangs();
        for (int i = 0; i < langs.size(); ++i) {
        		JRadioButtonMenuItem language = new JRadioButtonMenuItem(langs.get(i).getName());
        		language.addActionListener(new ChangeLanguage(langs.get(i).getName()));
        		if (langs.get(i) == Utils.getActiveLanguage())
        			language.setSelected(true);
        		else
        			language.setSelected(false);
        		languageMenu.add(language);
        }
		return languageMenu;
	}
*/
	private JMenu setupHelpMenu() {
        JMenu help = new JMenu(Utils.lang("help"));
        JMenuItem about = new JMenuItem(Utils.lang("about"));
        about.addActionListener(new About());
        help.add(about);
		return help;
	}

	private JMenu setupViewMenu() {
        JMenu view = new JMenu(Utils.lang("view"));
        JMenuItem grid = new JMenuItem(Utils.lang("grid"));
        grid.addActionListener(new GridV());
        view.add(grid);
		return view;
	}

	private JMenu setupEditMenu() {
        JMenu edit = new JMenu(Utils.lang("edit"));
        JMenuItem undo = new JMenuItem(Utils.lang("undo"));
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        undo.addActionListener(new Undo());
        JMenuItem redo = new JMenuItem(Utils.lang("redo"));
        redo.addActionListener(new Redo());
        edit.add(undo);
        edit.add(redo);
		return edit;
	}

	private JMenu setupFileMenu() {
        JMenu file = new JMenu(Utils.lang("file"));
        file.setMnemonic(KeyEvent.VK_ALT);
        JMenuItem apri = new JMenuItem(Utils.lang("open"));
        apri.addActionListener(new OpenL());
        importMap = new JMenuItem(Utils.lang("import"));
        importMap.addActionListener(new ImportL());
        importMap.setEnabled(false);
        JMenuItem chiudi = new JMenuItem(Utils.lang("close"));
        chiudi.addActionListener(new CloseL());
        JMenuItem salva = new JMenuItem(Utils.lang("save"));
        salva.addActionListener(new SaveL(false));
        JMenuItem salvaRLE = new JMenuItem(Utils.lang("saveComp"));
        salvaRLE.addActionListener(new SaveL(true));
        file.add(apri);
        file.add(importMap);
        file.add(chiudi);
        file.add(salva);
        file.add(salvaRLE);
        return file;
	}
	
	/*
	 * Getters
	 *
	 */
	public JMenuItem getImportMap() {
		return importMap;
	}
}
