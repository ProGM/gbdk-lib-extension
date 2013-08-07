package it.elfgames.gameboy.TileEditor.IO;

import it.elfgames.gameboy.TileEditor.ImageMapper;
import it.elfgames.gameboy.TileEditor.ImageViewer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import it.elfgames.gameboy.TileEditor.IO.Exceptions.CantImportException;
import it.elfgames.gameboy.TileEditor.IO.Exceptions.CantSaveException;
import it.elfgames.gameboy.TileEditor.IO.Exceptions.*;
import it.elfgames.gameboy.TileEditor.utils.Utils;

public class SaveLoadImport {
	
	/* 
	 * mode 1 = 8x8
	 * mode 2 = 8x16
	 */
    public static void saveImage(BufferedImage img, ImageViewer imViewer, ImageMapper imMapper, String path, int mode, boolean rle)
    		throws CantSaveException
    	{
    		String fn = generateCleanFileName(path);
    		
	    	StringBuffer output = new StringBuffer();
	    	//print data on the buffer
	    	printData(output, fn, img, mode, rle);
	    	
	    	//if it's a map, print also the map
        if (imMapper.isActive())
        		printMap(output, fn, imMapper);
	    	
        //cleans the filename
        int i = path.lastIndexOf(".");
        if (i > 0 && i < path.length())
        		path = path.substring(0, i);
        File file = new File(path+".h");
        
        //try to create it
        try {
        		if (!file.exists())
				file.createNewFile();
        		
	        FileWriter fstream = new FileWriter(path+".h");
	        BufferedWriter out = new BufferedWriter(fstream);
	        out.write(output.toString());
	        out.close();
		} catch (Exception e) {
			throw new CantSaveException();
		}
    }
    
	private static void printData(StringBuffer output, String fn, BufferedImage img, int mode, boolean rle) {
	    	printHeader(output, fn, "data", img.getWidth()/8, img.getHeight()/8);
	    int bytesCount=1;
        int prec_i1 = 0;
        int prec_i2 = 0;
        int repetitions_counter = 0;
	    for (int y = 0; y < img.getHeight(); y=y+8*mode) {
	        for (int x = 0; x < img.getWidth(); x=x+8) {
	          BufferedImage s = img.getSubimage(x, y, 8, 8*mode);
	          int i1 = 0;
	          int i2 = 0;
	          for (int y1=0;y1<8*mode;y1++) {
		        	  i1=0;
		        	  i2=0;
		        	  for (int x1=0;x1<8;x1++) {
	                  if (s.getRGB(x1, y1)==0xffb0b0b0) i1+= 0x01<<(7-x1);
	                  if (s.getRGB(x1, y1)==0xff808080) i2+=0x01<<(7-x1);
	                  if (s.getRGB(x1, y1)==0xff000000) {i1+=0x01<<(7-x1);i2+=0x01<<(7-x1); }
	           	  }
		          if (rle) {
			        	  	if (i1 == prec_i1 && i2 == prec_i2)
			        	  	{
				          	repetitions_counter++;
				        	  	if (repetitions_counter > 1) {
				        	  		if (repetitions_counter < 255) {
				        	  			continue;
				        	  		} else {
					        	  		appendValue(output, repetitions_counter, bytesCount);
					        	  		bytesCount++;
				        	  			repetitions_counter = 0;
				        	  		}
				        	  	}
			        	  	}
			        	  	else if (repetitions_counter > 0)
			        	  	{
			        	  		  appendValue(output, repetitions_counter+1, bytesCount);
				              bytesCount++;
				              repetitions_counter = 0;
			        	  	}
					    prec_i1 = i1;
					    prec_i2 = i2;
		        	  }
		          appendValue(output, i1, bytesCount);
		          
	              bytesCount++;
		          appendValue(output, i2, bytesCount);
		          bytesCount++;
	          }
	        }
	    }
	    if (repetitions_counter > 0) {
	  		  appendValue(output, repetitions_counter+1, bytesCount);
	  		  bytesCount++;
	    }
        if ((bytesCount%16) == 1) {
	    	    output.delete(output.length()-5, output.length()-1);
		    	output.trimToSize();
        } else {
	    	    output.deleteCharAt(output.length()-1);
		    	output.trimToSize();
        }
	    	output.append("\n\n");
	}
	
    private static void appendValue(StringBuffer output, int value, int bytesCount) {
        output.append(Utils.getHexString(value, 2));
        if ((bytesCount%16) == 0) {
          	  output.append("\n.db ");
        } else
      	  	output.append(",");
	}

	private static void printMap(StringBuffer output, String fn, ImageMapper imMapper) {
        int bytesCount=1;
        if (imMapper.isActive())
        {
            printHeader(output, fn, "map", imMapper.getMode()[0]/8, imMapper.getMode()[1]/8);
	        	for (int v=0;v<imMapper.getMap().size();v++)
	        	{
	        		output.append(Utils.getHexString(imMapper.getMap().get(v), 2));
	        		if (v != imMapper.getMap().size()-1)
	        		{
		        		if (bytesCount==16)
		        		{
		        			output.append("\n.db ");
		        			bytesCount=0;
		        		} else
		        			output.append(",");
	        		}
	        		bytesCount++;
	        	}
        }
        output.append("\n\n");
	}

	private static void printHeader(StringBuffer output, String fn, String type, int w, int h) {
	    	output.append("#define "+fn.toUpperCase()+(type.equals("map") ? "_MAP" : "")+"_W ");
	    	output.append(w);
	    	output.append("\n#define "+fn.toUpperCase()+(type.equals("map") ? "_MAP" : "")+"_H ");
	    	output.append(h);
	    output.append("\n\nextern unsigned char "+fn+"_tile"+type+"[];\n");
	    output.append("\n\n.globl _"+fn+"_tile"+type+"\n");
	    output.append(".dw _"+fn+"_tile"+type+"\n");
	    output.append("\n_"+fn+"_tile"+type+":\n.db ");
	}

	private static String generateCleanFileName(String path) {
	    String fn = path;
	    int i = fn.lastIndexOf(".");
	    if (i > 0 && i < fn.length())
	    		fn = fn.substring(0, i);
	    i = path.lastIndexOf(File.separatorChar);
	    if (i > 0 && i < fn.length())
	    		fn = fn.substring(i, fn.length());
	    	fn = fn.replaceAll(" ", "_");
	    	fn = fn.replaceAll("[^A-Za-z0-9_]", "");
		return fn;
	}

	public static BufferedImage loadImage(String filename, ImageViewer imViewer, ImageMapper imMapper)
    					throws WrongSizeException, WrongProportionException, WrongImageTypeException
    	{
        BufferedImage img = null;
        try
        {
          img = ImageIO.read(new File(filename));
        }
        catch (Exception e)
        {
        		throw new WrongImageTypeException();
        }
        if (img.getWidth() > 256 || img.getHeight() > 256) {
	        	img = null;
	        	throw new WrongSizeException();
        }
        if (img.getWidth() % 8 != 0 || img.getHeight() % 8 != 0) {
	        	img = null;
	        	throw new WrongProportionException();
        }
        int cmap[] = ImageViewer.COLOR_MAP;
        IndexColorModel colorModel = new IndexColorModel(8, cmap.length, cmap, 0, true, -1, DataBuffer.TYPE_BYTE);
        BufferedImage image2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, colorModel);
        Graphics g = image2.getGraphics();  
        g.drawImage(img, 0, 0, null);  
        g.dispose();
        return image2;
    }
    
    public static void loadMap(String filename, ImageViewer imViewer, ImageMapper imMapper)
    		throws CantImportException
    	{
        FileReader fstream = null;
        try {
			fstream = new FileReader(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        BufferedReader in = new BufferedReader(fstream);
        try {
        		String s = null;
        		boolean loadMap = false;
        		Vector<String[]> elements = new Vector<String[]>();
        		int width = 32;
        		int height = 32;
			while((s = in.readLine()) != null) {
				if (s.matches(""))
					loadMap = false;
				if (s.matches("#define [0-9A-Za-z]+_MAP_W.*")) {
					s = s.substring(s.length()-2);
					width = Integer.parseInt(s);
					continue;
				}
				if (s.matches("#define [0-9A-Za-z]+_MAP_H.*")) {
					s = s.substring(s.length()-2);
					height = Integer.parseInt(s);
					continue;
				}
				if (loadMap) {
					s = s.replace("0x", "");
					s = s.replace(".db ", "");
					elements.add(s.split(","));
				}
				if (s.matches("_[a-z0-9A-Z_]+tilemap:"))
					loadMap = true;
			}
	    		Vector<Integer> elementsInt = new Vector<Integer>();
	    		for (int i = 0; i < elements.size(); ++i) {
	    			for (int j = 0; j < elements.get(i).length; ++j) {
	    				elementsInt.add(Integer.parseInt(elements.get(i)[j], 16));
	    			}
	    		}
	    		imMapper.setMap(elementsInt, width, height);
		} catch (IOException e) {
			throw new CantImportException();
		}
    }

}
