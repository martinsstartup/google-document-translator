/*
 * Gdt.java
 *
 * Copyright (C) 2011,  Sanmoy Ray
 * 
 * This file is part of google-document-translator.
 *
 * Google Document Translator is not a product from Google. Neither it is endorsed nor it is supported by Google.
 * This is an open source and free software. you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or(at your option) any later version.
 * 
 * Google Document Translator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Google Document Translator.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.google.gdt.main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.ToolTipManager;

import com.google.gdt.ui.MainJFrame;

/**
 * 
 * @author sanmoy ray
 *
 */
public class Gdt {

	private static Logger logger = Logger.getLogger("Gdt.class");
	
	/**
	 * 
	 * @param args
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws SecurityException, IOException {
//		System.setProperty("file.encoding", "UTF-8");
		String pattern = "gdt%g.log";
	    int limit = 1000000; // 1 Mb
	    int numLogFiles = 3;
	    FileHandler fh = new FileHandler(pattern, limit, numLogFiles,true);
	    
	    fh.setFormatter(new SimpleFormatter());
	    logger.addHandler(fh);
	    logger.setLevel(Level.INFO);
	    
		ToolTipManager.sharedInstance().setInitialDelay(0);
		java.awt.EventQueue.invokeLater(new Runnable() 
		{
            public void run() 
            {
            	MainJFrame mFrame = MainJFrame.getInstance();
            	mFrame.setSize(676, 450);
            	mFrame.setResizable(false);
            	mFrame.setLocationByPlatform(true);
            	mFrame.setTitle("Google-Document-Translator");
            	mFrame.setVisible(true);
            }
        });
	}
}
