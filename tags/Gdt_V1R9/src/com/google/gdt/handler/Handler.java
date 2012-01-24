/*
 * Handler.java
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

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.google.gdt.ui.ProgressLevel;

/**
 * Defines the responsibility of all Handlers. All handlers should take a input file for translation, and 
 * generate the translated output file. In the course of translation process, handlers should update the 
 * progress level accordingly. 
 * 
 * @author Sanmoy
 *
 */
public interface Handler 
{
	/**
	 * Override this method for different different file type.
	 * 
	 * @param inputFile
	 * @param pLevel
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	void handle(String inputFile,ProgressLevel pLevel) throws IOException, InvalidFormatException ; 
	
	/**
	 * sets the interrupt
	 * 
	 * @param interrupt
	 */
	void setInterrupt(boolean interrupt);
	
	/**
	 * gets the interrupt status
	 * 
	 * @return isInterrupted
	 */
	boolean getInterrupt();
	
}
