/*
 * PowerpointHandler.java
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

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.google.gdt.handler.DocumentHandler;
import com.google.gdt.ui.ProgressLevel;
import com.google.gdt.util.TranslatorType;

/**
 * 
 * @author Sanmoy
 *
 */
public class PowerpointHandler extends DocumentHandler
{
	private static Logger logger = Logger.getLogger("PowerpointHandler.class");

	/**
	 * 
	 * @param inputFile
	 * @param pLevel
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	@Override
	public void handle(String inputFile, ProgressLevel pLevel)
			throws IOException, InvalidFormatException 
	{
		InputStream inputStream = new FileInputStream(inputFile);
		String outPutFile = getOuputFileName(inputFile);
		OutputStream outputStream = new FileOutputStream(outPutFile);

		SlideShow aSlideShow = new SlideShow(inputStream);
		Slide[] pptSlides = aSlideShow.getSlides();
		
		pLevel.setValue(0);
		pLevel.setMaxValue(pptSlides.length);
		pLevel.setStringPainted(true);
		pLevel.setTrFileName(outPutFile);
		
		int pBarUpdate = 0;
		
		for (Slide aSlide : pptSlides) 
		{
			TextRun[] textRuns = aSlide.getTextRuns();
			for (TextRun aTextRun : textRuns) 
			{
				if(isInterrupted)
				 {
					 outputStream.close();
					 new File(outPutFile).delete();
					 pLevel.setString("cancelled");
					 return;
				 }
				String inputText = "";
				try
				{
					inputText= aTextRun.getText();
					//in http post method, all key value pairs are seperated with &
					if(preferenceModel.getTranslatorType()==TranslatorType.HTTP)
						inputText = inputText.replaceAll("&", "and");
					if(inputText.matches("\\s+"))//if the string is empty
						continue;
					String translatedTxt = inputText;
					if(inputText.contains("\n"))
					{
						inputText = inputText.replaceAll("[\\n]", " newline ");
						translatedTxt = translator.translate(inputText);
						translatedTxt = translatedTxt.replaceAll("newline", "\n");
					}
					else
					{
						translatedTxt = translator.translate(inputText);
					}
					aTextRun.setText(translatedTxt);
				}
				catch (Exception e) 
				{
					logger.log(Level.SEVERE, "Input File : "+inputFile+" cannot translate the text : "+inputText,e);
//						aRichTextRun.setText(translatedTxt);
					continue;
				}
			}
			pBarUpdate++;
			pLevel.setValue(pBarUpdate);
		}

		aSlideShow.write(outputStream);
		inputStream.close();
		outputStream.close();
		
		pLevel.setValue(pptSlides.length);
		pLevel.setString("done");
	}
	
}
