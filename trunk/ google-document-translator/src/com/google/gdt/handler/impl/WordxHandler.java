/*
 * WordxHandler.java
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.google.gdt.handler.DocumentHandler;
import com.google.gdt.main.PreferenceModel;
import com.google.gdt.ui.ProgressLevel;
import com.google.gdt.util.AjaxTranslator;
import com.google.gdt.util.HttpTranslator;
import com.google.gdt.util.Translator;
import com.google.gdt.util.TranslatorType;

/**
 * 
 * @author Sanmoy
 *
 */
public class WordxHandler extends DocumentHandler
{

	private static Logger logger = Logger.getLogger("WordxHandler.class");
	
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
		
		XWPFDocument xwpfDocument = new XWPFDocument(inputStream);
		
		List<XWPFParagraph> paragraphs = xwpfDocument.getParagraphs();
		int paragraphCount = paragraphs.size();
		
		pLevel.setTrFileName(outPutFile);
		pLevel.setValue(0);
		pLevel.setStringPainted(true);
		
		List<XWPFTable> xwpftables = xwpfDocument.getTables();
		int count=0;
		//start translating table 
		int tablecount = 0;
		for(XWPFTable xwpfTable:xwpftables)
		{
			boolean traverseTable = false;
			count=0;
			tablecount++;
			pLevel.setString("Translating table : "+tablecount);
			
			List<XWPFTableRow> xwpfTableRows = xwpfTable.getRows();
			for(XWPFTableRow xwpfTableRow:xwpfTableRows)
			{
				if(!traverseTable)
				{
					pLevel.setMaxValue(xwpfTableRows.size());
					traverseTable=true;
				}
				
				List<XWPFTableCell> xwpfTableCells = xwpfTableRow.getTableCells();
				
				for(XWPFTableCell xwpfTableCell:xwpfTableCells)
				{
					for(XWPFParagraph xParagraph:xwpfTableCell.getParagraphs())
					{
						if(isInterrupted)
						 {
							 outputStream.close();
							 new File(outputFile).delete();
							 pLevel.setString("cancelled");
							 return;
						 }
						
						translateParagraph(xParagraph);
					}
				}
				count++;
				pLevel.setValue(count);
			}
			pLevel.setValue(0);
		}
		//table translation complete, start translating remaining document
		pLevel.setString(null);
		pLevel.setValue(0);
		pLevel.setMaxValue(paragraphCount);
		count = 0;
		
		for(XWPFParagraph xParagraph:paragraphs)
		{
			if(isInterrupted)
			 {
				 outputStream.close();
				 new File(outputFile).delete();
				 pLevel.setString("cancelled");
				 return;
			 }
			translateParagraph(xParagraph);
			count++;
			pLevel.setValue(count);
		}
		xwpfDocument.write(outputStream);
		outputStream.close();
		pLevel.setString("done");
	}
	
	/**
	 * translate paragraph by paragraph
	 * @param xParagraph
	 */
	private void translateParagraph(XWPFParagraph xParagraph)
	{
		for(XWPFRun xwpfRun : xParagraph.getRuns())
		{
			String inputText = xwpfRun.getText(0);
			String translatedTxt = inputText;
			//in http post method, all key value pairs are seperated with &
			if(preferenceModel.getTranslatorType()==TranslatorType.HTTP)
				inputText = inputText.replaceAll("&", "and");
			try
			{
				translatedTxt = translator.translate(inputText);
			}
			catch (Exception e) 
			{
				logger.log(Level.SEVERE, " cannot translate the text : "+inputText,e);
				xwpfRun.setText(translatedTxt, 0);
				continue;
			}
			
			xwpfRun.setText(translatedTxt+" ", 0);
		}
	}

}
