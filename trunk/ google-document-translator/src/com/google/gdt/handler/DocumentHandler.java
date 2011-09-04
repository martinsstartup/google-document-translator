/*
 * DocumentHandler.java
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

package com.google.gdt.handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.google.gdt.main.PreferenceModel;
import com.google.gdt.ui.ProgressLevel;
import com.google.gdt.util.AjaxTranslator;
import com.google.gdt.util.HttpTranslator;
import com.google.gdt.util.Translator;
import com.google.gdt.util.TranslatorType;

/**
 * This class provides implementations of some {@link Handler} common operations
 * 
 * @author Sanmoy
 *
 */
public abstract class DocumentHandler implements Handler
{
	/**
	 * boolean to check whether the handler is interrupted
	 */
	public boolean isInterrupted = false;
	
	/**
	 * output file for the translated document
	 */
	public String outputFile;
	
	/**
	 * translator instance, depends on the  {@link TranslatorType}
	 */
	public Translator translator;
	
	/**
	 * {@link PreferenceModel} instance, holds all user preference (from language, to language, translator type etc)
	 */
	public PreferenceModel preferenceModel;
	
	/*
	 * static logger instance
	 */
	private static Logger logger = Logger.getLogger("DocumentHandler.class");
	
	/*
	 * fetches the singleton instance of preferenceModel and initialises the translator instance according to user preference 
	 */
	public DocumentHandler() 
	{
		preferenceModel = PreferenceModel.getInstance();
		if(TranslatorType.AJAX==preferenceModel.getTranslatorType())
		{
			translator = new AjaxTranslator();
			logger.log(Level.INFO, "TranslatorType : AJAX");
		}
		else
		{
			translator = new HttpTranslator();
			logger.log(Level.INFO, "TranslatorType : HTTP");
		}
	}
	
	/**
	 * sets the interrupt
	 * 
	 * @param interrupt
	 */
	@Override
	public void setInterrupt(boolean interrupt) 
	{
		isInterrupted = interrupt;
		logger.log(Level.INFO, "setInterrupt : "+interrupt);
	}
	
	/**
	 * gets the interrupt status
	 * 
	 * @return isInterrupted
	 */
	@Override
	public boolean getInterrupt() 
	{
		return isInterrupted;
	}
	
	/**
	 * get the output file (translated file) name according to the original file location and translation language
	 * 
	 * @param inputFile
	 * @return outputFile
	 * @throws FileNotFoundException
	 */
	public String getOuputFileName(String inputFile) throws FileNotFoundException 
	{
		logger.log(Level.INFO, "inputFile : "+inputFile);
		
		String extension = inputFile.substring(inputFile.lastIndexOf("."));
		String rawFilename = inputFile.substring(0, inputFile.lastIndexOf("."));
		outputFile = rawFilename+"_"+preferenceModel.getToLanguage().toString()+extension;
		
		logger.log(Level.INFO, "outputFile : "+outputFile);
		
		return outputFile;
	}
	
}
