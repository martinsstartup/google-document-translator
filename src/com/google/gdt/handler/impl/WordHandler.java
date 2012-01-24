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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.google.gdt.handler.DocumentHandler;
import com.google.gdt.ui.ProgressLevel;
import com.google.gdt.util.TranslatorType;

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
		Range range = hDocument.getRange();
		
		pLevel.setTrFileName(outPutFile);
		pLevel.setValue(0);
		pLevel.setStringPainted(true);
		pLevel.setMaxValue(range.numParagraphs());
		int count = 0;
		for(int i =0;i<range.numParagraphs();i++)
		{
			Paragraph paragraph = range.getParagraph(i);
			int numCharRuns = paragraph.numCharacterRuns();
			for (int j = 0; j < numCharRuns; j++) 
			{
				if (isInterrupted) 
				{
					outputStream.close();
					new File(outPutFile).delete();
					pLevel.setString("cancelled");
					return;
				}
				CharacterRun charRun = paragraph.getCharacterRun(j);
				String inputText = charRun.text();
				if ((null == inputText)
						|| (inputText.trim().equals("")))
					continue;
				String translatedTxt = inputText;
				//in http post method, all key value pairs are seperated with &
				if(preferenceModel.getTranslatorType()==TranslatorType.HTTP)
					inputText = inputText.replaceAll("&", "and");
				try
				{
					if(inputText.matches("\\s+"))//if the string is empty
						continue;
					if(inputText.contains("\n")||inputText.contains("\r")||inputText.contains("\t"))
					{
						translatedTxt = inputText.replaceAll("[\\n]", " gdtnewline ");
						translatedTxt = translatedTxt.replaceAll("[\\r]", " gdtfromline ");
						translatedTxt = translatedTxt.replaceAll("[\\t]", " gdttabline ");
						translatedTxt = translator.translate(translatedTxt);
						translatedTxt = translatedTxt.replaceAll("gdtnewline", "\n");
						translatedTxt = translatedTxt.replaceAll("gdtfromline", "\r");
						translatedTxt = translatedTxt.replaceAll("gdttabline", "\t");
						if(!inputText.equals(translatedTxt))
						charRun.replaceText(inputText, translatedTxt);
					}
					else
					{
						translatedTxt = translator.translate(inputText);
						if(!inputText.equals(translatedTxt))
						charRun.replaceText(inputText, translatedTxt.toString());
					}
				}
				catch (Exception e) 
				{
					logger.log(Level.SEVERE, "Input File : "+inputFile+" cannot translate the text : "+inputText,e);
				}
			}
			count++;
			pLevel.setValue(count);
		}
		pLevel.setString("done");	
		hDocument.write(outputStream);
		outputStream.close();
	}
}
