/*
 * WordHandler.java
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

package com.google.gdt.handler.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.google.gdt.handler.DocumentHandler;
import com.google.gdt.ui.ProgressLevel;

/**
 * 
 * @author Sanmoy
 *
 */
public class WordHandler extends DocumentHandler
{
	private static Logger logger = Logger.getLogger("WordHandler.class");
	
	/**
	 * 
	 * @param inputFile
	 * @param pLevel
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	@Override
	public void handle(String inputFile,ProgressLevel pLevel) throws IOException, InvalidFormatException 
	{
		String outPutFile = getOuputFileName(inputFile);
		OutputStream outputStream = new FileOutputStream(outPutFile);
		InputStream inputStream = new FileInputStream(inputFile);
		
		HWPFDocument hDocument = new HWPFDocument(inputStream);
		XWPFDocument xwpfDocument = new XWPFDocument(inputStream);
		
		Range range = hDocument.getRange();
		
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		for(int i =0;i<range.numParagraphs();i++)
		{
			String inputText = "";
			try
			{
				Paragraph paragraph = range.getParagraph(i);
				paragraphs.add(paragraph);
				
				String translatedTxt = inputText;
				if(inputText.contains("\n")||inputText.contains("\r")||inputText.contains("\t")||inputText.contains("\n"))
				{
//					inputText = inputText.replaceAll("[\\n]", " newline ");
//					translatedTxt = Translator.Translate(inputText);
//					translatedTxt = translatedTxt.replaceAll("newline", "\n");
					continue;
				}
				else
				{
					translatedTxt = translator.translate(inputText);
				}
//				paragraph.replaceText(inputText, translatedTxt);
				paragraph.insertAfter(translatedTxt);
			}
			catch(IllegalArgumentException e)
			{
				logger.log(Level.SEVERE, "Input File : "+inputFile+" cannot translate the text : "+inputText,e);
			} 
			catch (Exception e) 
			{
				logger.log(Level.SEVERE, "Input File : "+inputFile+" cannot translate the text : "+inputText,e);
			}
		}
		
		hDocument.write(outputStream);
		outputStream.close();
		
	}
	
}
