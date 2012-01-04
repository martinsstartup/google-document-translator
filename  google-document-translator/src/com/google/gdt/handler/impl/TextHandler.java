/*
 * TextHandler.java
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.google.gdt.handler.DocumentHandler;
import com.google.gdt.ui.ProgressLevel;
import com.google.gdt.util.TranslatorType;

/**
 * 
 * @author Sanmoy
 *
 */
public class TextHandler extends DocumentHandler 
{

	private static Logger logger = Logger.getLogger("TextHandler.class");
	
	private static final String LINE_SEPERATOR = System.getProperty("line.separator");
	
	@Override
	public void handle(String inputFile, ProgressLevel pLevel)
			throws IOException, InvalidFormatException {
		String outPutFile = getOuputFileName(inputFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),"UTF8"));
		BufferedWriter bw = new BufferedWriter(new FileWriter(outPutFile));
		
		pLevel.setTrFileName(outPutFile);
		int lineCount = getLineCount(inputFile);
		pLevel.setValue(0);
		pLevel.setMaxValue(lineCount);
		pLevel.setStringPainted(true);

		// translate line in a bunch of 100
		String inputText;
		String translatedText="";
		int count=0;
		// Read File Line By Line
		while ((inputText = br.readLine()) != null) 
		{
			if (isInterrupted) 
			{
				br.close();
				bw.close();
				new File(outPutFile).delete();
				pLevel.setString("cancelled");
				return;
			}
			try 
			{
				translatedText = inputText;
				if((null==inputText)||inputText.trim().equals(""))
				{
					bw.write(translatedText+LINE_SEPERATOR);
					count++;
					pLevel.setValue(count);
					continue;
				}
				if(preferenceModel.getTranslatorType()==TranslatorType.HTTP)
					inputText = inputText.replaceAll("&", "and");
				
				translatedText = translator.translate(inputText);
			} 
			catch (Exception e) 
			{
				logger.log(Level.SEVERE, "cannot translate the text : "+inputText,e);
			}
			bw.write(translatedText+LINE_SEPERATOR);
			count++;
			pLevel.setValue(count);
		}
		// Close the input stream
		br.close();
		bw.close();
		
		pLevel.setValue(lineCount);
		pLevel.setString("done");
	}

	private int getLineCount(String is) throws FileNotFoundException 
	{
		Reader reader = new FileReader(is);
		LineNumberReader lineNumberReader = new LineNumberReader(reader);;
		try {
			String line = "";
			while ((line = lineNumberReader.readLine()) != null) {
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "not able to close File reader : "+is, e);
			}
			try {
				lineNumberReader.close();
			} catch (IOException e) {
				logger.log(Level.SEVERE, "not able to close LineNumber reader : "+is, e);
			}
		}
		return lineNumberReader.getLineNumber();
	}

}
