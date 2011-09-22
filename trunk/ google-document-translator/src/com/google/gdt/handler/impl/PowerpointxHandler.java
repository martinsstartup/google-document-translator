/*
 * PowerpointxHandler.java
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.google.gdt.handler.DocumentHandler;
import com.google.gdt.ui.ProgressLevel;

/**
 * 
 * @author Sanmoy
 *
 */
public class PowerpointxHandler extends DocumentHandler
{
	private static Logger logger = Logger.getLogger("PowerpointxHandler.class");
	
	private final int BUFFER = 2048;
	/**
	 * 
	 * @param inputFile
	 * @param pLevel
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	@Override
	public void handle(String inputFile,ProgressLevel pLevel) throws IOException, InvalidFormatException {
		String outPutFile = getOuputFileName(inputFile);
		ZipInputStream zis = new ZipInputStream(new FileInputStream(inputFile));
		ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(outPutFile));
		
		int slideCount = getSlideCount(inputFile);
		pLevel.setValue(0);
		pLevel.setMaxValue(slideCount);
		pLevel.setStringPainted(true);
		pLevel.setTrFileName(outPutFile);
		ZipEntry zipEntry;
		int pBarUpdate = 0;
		while((zipEntry=zis.getNextEntry())!=null)
		{
			if(zipEntry.getName().contains("ppt/slides/slide"))
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    byte[] buffer = new byte[BUFFER];
			    int len;
			    while ((len = zis.read(buffer)) > 0 ) {
			        baos.write(buffer, 0, len);
			    }
			    baos.flush();

			    InputStream clone = new ByteArrayInputStream(baos.toByteArray()); 
				SAXReader saxReader = new SAXReader();
				Document doc = null;
				try 
				{
					doc = saxReader.read(clone);
					List<Node> nodes = doc.selectNodes("//p:sld/p:cSld/p:spTree/p:sp/p:txBody/a:p/a:r/a:t");
					for(Node node : nodes)
					{
						if (isInterrupted) 
						{
							zis.close();
							zout.close();
							new File(outPutFile).delete();
							pLevel.setString("cancelled");
							return;
						}
						String inputText = "";
						try
						{
							inputText = node.getText();
							String translatedText = translator.translate(inputText);
							node.setText(translatedText);
						}
						catch (Exception e) 
						{
							logger.log(Level.SEVERE, "Translation fails for the inputText : "+inputText, e);
						}
					}
				} 
				catch (DocumentException e) 
				{
					logger.log(Level.SEVERE, "cannot parse slide", e);
				}
				
				zout.putNextEntry(new ZipEntry(zipEntry.getName()));
				byte data[] = doc.asXML().getBytes("UTF8");
				zout.write(data,0,data.length);
				pBarUpdate++;
				pLevel.setValue(pBarUpdate);
			}
			else
			{
				zout.putNextEntry(new ZipEntry(zipEntry.getName()));
				int len;
				byte data[] = new byte[BUFFER];
				while((len=zis.read(data,0,BUFFER))!=-1)
				{
					zout.write(data,0,len);
				}
			}
		}
		zis.close();
		zout.close();
		pLevel.setValue(slideCount);
		pLevel.setString("done");
	}
	
	private int getSlideCount(String inputFile)
	{
		int count = 0;
		ZipInputStream zis = null;
		ZipEntry zipEntry;
		try 
		{
			zis = new ZipInputStream(new FileInputStream(inputFile));
			while((zipEntry=zis.getNextEntry())!=null)
			{
				if(zipEntry.getName().contains("ppt/slides/slide"))
				{
					count++;
				}
			}
		} 
		catch (IOException e) 
		{
			logger.log(Level.SEVERE, "cannot count the number of slides ", e);
		}
		finally
		{
			if(null!=zis)
			{
				try 
				{
					zis.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		return count;
	}
	
}
