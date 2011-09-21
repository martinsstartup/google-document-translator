/*
 * ExcelHandler.java
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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.gdt.handler.DocumentHandler;
import com.google.gdt.ui.ProgressLevel;
import com.google.gdt.util.TranslatorType;

/**
 * Handles excel files for translation 
 * 
 * @author Sanmoy
 *
 */
public class ExcelHandler extends DocumentHandler{

	private static Logger logger = Logger.getLogger("ExcelHandler.class");
	
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
		InputStream is = new FileInputStream(inputFile);
		
		Workbook wb = WorkbookFactory.create(is);
		List<Sheet> sheets = getSheets(wb);
		
		pLevel.setTrFileName(outPutFile);
		
		//iterate over sheet
		for(int index =0;index<sheets.size();index++)
		{
			Sheet sheet = sheets.get(index);
			
			if(sheets.size()>1)
			{
				pLevel.setString("Translating sheet "+(index+1)+"/"+sheets.size());
			}
			
			int firstRowNum = sheet.getFirstRowNum();
			int lastRowNum = sheet.getLastRowNum();
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
			//iterate over row
			for ( Row row : sheet )
			{
				//iterate over cells
				for ( Cell cell : row)
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
						//in http post method, all key value pairs are seperated with &
						if(preferenceModel.getTranslatorType()==TranslatorType.HTTP)
							inputText = inputText.replaceAll("&", "and");
						try
						{
							if(inputText.matches("\\s+"))//if the string is empty
								continue;
							if(inputText.contains("\n"))
							{
								inputText = inputText.replaceAll("[\\n]", " gdtnewline ");
								translatedTxt = translator.translate(inputText);
								translatedTxt = translatedTxt.replaceAll("gdtnewline", "\n");
							}
							else
							{
								translatedTxt = translator.translate(inputText);
							}
							cell.setCellValue(translatedTxt);
						}
						catch (Exception e) 
						{
							logger.log(Level.SEVERE, "Input File : "+inputFile+" cannot translate the text : "+inputText,e);
							continue;
						}
		             }
				}//cell iteration ends
				pBarUpdate++;
				pLevel.setValue(pBarUpdate);
			}//row iteration ends
			pLevel.setValue(rowCount);
		}
		pLevel.setString("done");
		
		wb.write(outputStream);
		outputStream.close();
	}

	/**
	 * returns the list of sheets in a workbook  
	 * 
	 * @param wb
	 * @return sheets
	 */
	private static List<Sheet> getSheets(Workbook wb) 
	{
		List<Sheet> sheets = new ArrayList<Sheet>();
		for(int i =0;i<20;i++)
		{
			try
			{
				Sheet sheet = wb.getSheetAt(i);
				sheets.add(sheet);
			}
			catch(IllegalArgumentException e)
			{
				break;
			}
		}
		return sheets;
	}
}
