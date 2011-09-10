/*
 * GdtJob.java
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

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.google.gdt.handler.DocumentHandler;
import com.google.gdt.handler.impl.ExcelHandler;
import com.google.gdt.handler.impl.ExcelxHandler;
import com.google.gdt.handler.impl.PdfHandler;
import com.google.gdt.handler.impl.PowerpointHandler;
import com.google.gdt.handler.impl.PowerpointxHandler;
import com.google.gdt.handler.impl.TextHandler;
import com.google.gdt.handler.impl.WordHandler;
import com.google.gdt.handler.impl.WordxHandler;
import com.google.gdt.ui.ProgressLevel;

/**
 * A GdtJob will be created for each file to be handled and one handler will be attached
 * 
 * @author Sanmoy
 *
 */
public class GdtJob extends Thread {

	private File file ;
	
	private ProgressLevel progressLevel;
	
	private DocumentHandler handler;
	
	public boolean isAlreadyTriggered = false;
	
	private static Logger logger = Logger.getLogger("GdtJob.class");
	
	public DocumentHandler getHandler() {
		return handler;
	}

	public void setHandler(DocumentHandler handler) {
		this.handler = handler;
	}

	public GdtJob(File file,ProgressLevel progressLevel) {
		this.file = file;
		this.progressLevel = progressLevel;
	}
	
	@Override
	public void run() {
		isAlreadyTriggered = true;
		String fileExtension = file.getName();
		logger.log(Level.SEVERE, "FileName : "+file.getName());
		try 
		{
			if(fileExtension.endsWith("xls"))
			{
				handler = new ExcelHandler();
				handler.handle(file.getAbsolutePath(),progressLevel);
			}
			if(fileExtension.endsWith("doc"))
			{
				handler = new WordHandler();
				handler.handle(file.getAbsolutePath(),progressLevel);
			}
			if(fileExtension.endsWith("ppt"))
			{
				handler = new PowerpointHandler();
				handler.handle(file.getAbsolutePath(),progressLevel);
			}
			if(fileExtension.endsWith("xlsx"))
			{
				handler = new ExcelxHandler();
				handler.handle(file.getAbsolutePath(),progressLevel);
			}
			if(fileExtension.endsWith("docx"))
			{
				handler = new WordxHandler();
				handler.handle(file.getAbsolutePath(),progressLevel);
			}
			if(fileExtension.endsWith("pptx"))
			{
				handler = new PowerpointxHandler();
				handler.handle(file.getAbsolutePath(),progressLevel);
			}
			if(fileExtension.endsWith("txt"))
			{
				handler = new TextHandler();
				handler.handle(file.getAbsolutePath(), progressLevel);
			}
			if(fileExtension.endsWith("pdf"))
			{
				handler = new PdfHandler();
				handler.handle(file.getAbsolutePath(), progressLevel);
			}
		} 
		catch (InvalidFormatException e)
		{
			logger.log(Level.SEVERE, "Exception came GdtJob run", e);
		} 
		catch (IOException e) 
		{
			logger.log(Level.SEVERE, "Exception came GdtJob run", e);
		}
	}
	
}
