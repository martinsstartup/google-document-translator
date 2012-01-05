/*
 * GdtFileChooser.java
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

package com.google.gdt.ui;

import java.io.File;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * @author Sanmoy
 *
 */
public class GdtFileChooser extends JFileChooser {

	/*
	 * 
	 */
	private final String DESCRIPTION = ".xls, .ppt, .doc, .xlsx, .pptx, .docx, .txt";
	
	private final String FILTER[] = {"xls","ppt","doc","xlsx","pptx","docx","txt"};
	
	private static final long serialVersionUID = -5800919218517480139L;
	
	private static Logger logger = Logger.getLogger("GdtFileChooser.class");
	
	public GdtFileChooser(File currentDirectoryPath)
	{
		super(currentDirectoryPath);
		this.setAcceptAllFileFilterUsed(false);
		initComponents();
	}

	public GdtFileChooser() {
		initComponents();
	}
	
	private void initComponents()
	{
		FileFilter fileFilter = new FileNameExtensionFilter(DESCRIPTION,FILTER);
		
		addChoosableFileFilter(fileFilter);
		setMultiSelectionEnabled(true);
		logger.finest("FileChooser is called");
	}
}

