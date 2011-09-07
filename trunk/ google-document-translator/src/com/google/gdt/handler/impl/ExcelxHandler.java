/*
 * ExcelxHandler.java
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
public class ExcelxHandler extends DocumentHandler
{

	private static Logger logger = Logger.getLogger("ExcelxHandler.class");
	
	/**
	 * 
	 * @param inputFile
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	@Override
	public void handle(String inputFile,ProgressLevel pLevel) throws IOException, InvalidFormatException
	{
		String outPutFile = getOuputFileName(inputFile);
		OutputStream outputStream = new FileOutputStream(outPutFile);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputFile);
		List<XSSFSheet> sheets = getSheets(xssfWorkbook);
		
		pLevel.setTrFileName(outPutFile);
		
		for(int index=0; index<sheets.size();index++)
		{
			XSSFSheet xssfsheet = sheets.get(index);
			
			if(sheets.size()>1)
			{
				pLevel.setString("Translating sheet "+index+"/"+sheets.size());
			}
			
			int firstRowNum = xssfsheet.getFirstRowNum();
			int lastRowNum = xssfsheet.getLastRowNum();
			int rowCount = lastRowNum - firstRowNum;
			// check for empty sheet, don't perform any operation
			if(rowCount==0)
			{
				continue;
			}
			pLevel.setValue(0);
			pLevel.setMaxValue(rowCount);
			pLevel.setStringPainted(true);
			
			int pBarUpdate = 0;
			//iterate for rows
			for ( int rowindex = firstRowNum; rowindex<=lastRowNum; rowindex++  )
			{
				XSSFRow xssfRow = xssfsheet.getRow(rowindex);
				//iterate for cells
				for ( Cell cell : xssfRow)
				{
					if(isInterrupted)
					 {
						 outputStream.close();
						 new File(outputFile).delete();
						 pLevel.setString("cancelled");
						 return;
					 }
					
					 if(cell.getCellType()==Cell.CELL_TYPE_STRING)
		             {
						String inputText = cell.getStringCellValue();
						String translatedTxt = inputText;
						try
						{
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
						}
						catch (Exception e) 
						{
							logger.log(Level.SEVERE, "Input File : "+inputFile+" cannot translate the text : "+inputText,e);
							cell.setCellValue(translatedTxt);
							continue;
						}
						
						cell.setCellValue(translatedTxt);
		             }
				}
				pBarUpdate++;
				pLevel.setValue(pBarUpdate);
			}
			pLevel.setValue(rowCount);
		}
		
		xssfWorkbook.write(outputStream);
		outputStream.close();
		
		pLevel.setString("done");
	}

	/**
	 * 
	 * @param xssfWorkbook
	 * @return
	 */
	private List<XSSFSheet> getSheets(XSSFWorkbook xssfWorkbook) 
	{
		List<XSSFSheet> xssfSheets = new ArrayList<XSSFSheet>();
		for (int i = 0; i < xssfWorkbook.getNumberOfSheets(); i++) 
		{
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(i);
			xssfSheets.add(xssfSheet);
		}
		return xssfSheets;
	}

}
