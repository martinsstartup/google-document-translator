/*
 * PreferenceModel.java
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

package com.google.gdt.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.translate.Language;
import com.google.gdt.util.TranslatorType;

/**
 * 
 * @author Sanmoy
 *
 */
public class PreferenceModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2122592331376649950L;

	private static final String PREFERENCE_PATH = System.getProperty("user.home") +"/.gdtPreference";
	
	private static PreferenceModel preferenceModel;
	
	private String proxyURL;
	
	private Integer proxyPort;
	
	private boolean isProxyEnable;
	
	private TranslatorType translatorType;
	
	private Language fromLanguage;
	
	private Language toLanguage;
	
	private List<String> recentFiles;
	
	private String lastLocation;
	
	private static Logger logger = Logger.getLogger("PreferenceModel.class");
	
	public static synchronized PreferenceModel getInstance() 
	{
		if(null==preferenceModel)
		{
			File file = new File(PREFERENCE_PATH);
			FileInputStream fiStream = null;
			ObjectInputStream oStream = null;
			
			if(file.exists())//initialize with the saved value
			{
				try 
				{
					fiStream = new FileInputStream(file);
					oStream = new ObjectInputStream(fiStream);
					preferenceModel = (PreferenceModel) oStream.readObject();
					if((null!=preferenceModel.getProxyURL()) && (null!=preferenceModel.getProxyPort()))
					{
						System.setProperty("http.proxyHost", preferenceModel.getProxyURL());
						System.setProperty("http.proxyPort", Integer.valueOf(preferenceModel.getProxyPort()).toString());
					}
				} 
				catch (FileNotFoundException e) 
				{
					logger.log(Level.SEVERE, "preferencemodel getInstance() load from saved object", e);
					preferenceModel = new PreferenceModel();
				} 
				catch (IOException e) 
				{
					logger.log(Level.SEVERE, "preferencemodel getInstance() load from saved object", e);
					preferenceModel = new PreferenceModel();
				} 
				catch (ClassNotFoundException e) 
				{
					// TODO Auto-generated catch block
					logger.log(Level.SEVERE, "preferencemodel getInstance() load from saved object", e);
					preferenceModel = new PreferenceModel();
				}
				finally
				{
					if(null!=fiStream)
					{
						try 
						{
							fiStream.close();
						} 
						catch (IOException e) 
						{
							logger.log(Level.SEVERE, "preferencemodel getInstance() try to close the serialized object input stream", e);
						}
					}
					if(null!=oStream)
					{
						try 
						{
							oStream.close();
						} 
						catch (IOException e) 
						{
							logger.log(Level.SEVERE, "preferencemodel getInstance() try to close the serialized object output stream", e);
						}
					}
				}
			}
			else //initialise with default value
			{
				preferenceModel = new PreferenceModel();
			}
		}
		return preferenceModel;
	}
	
	private PreferenceModel() 
	{
			proxyURL=System.getProperty("http.proxyHost");
			if(null!=System.getProperty("http.proxyPort"))
			{
				proxyPort=new Integer(System.getProperty("http.proxyPort"));
			}
			isProxyEnable=false;
			translatorType = TranslatorType.HTTP;
			fromLanguage=Language.AUTO_DETECT;
			toLanguage=Language.ENGLISH;
			recentFiles = new ArrayList<String>(4);
			lastLocation = "";
	}
	
	public void save() 
	{
		File file = new File(PREFERENCE_PATH);
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream oStream = null;
		
		try 
		{
			fileOutputStream = new FileOutputStream(file);
			oStream = new ObjectOutputStream(fileOutputStream);
			oStream.writeObject(preferenceModel);
		}
		catch (FileNotFoundException e) 
		{
			logger.log(Level.SEVERE, "preferencemodel save() ");
			preferenceModel = new PreferenceModel();
		} 
		catch (IOException e) 
		{
			logger.log(Level.SEVERE, "preferencemodel save() ");
			preferenceModel = new PreferenceModel();
		} 
		finally
		{
			if(null!=oStream)
			{
				try 
				{
					oStream.close();
				} 
				catch (IOException e) 
				{
					logger.log(Level.SEVERE, "preferencemodel save() try to close the serialized object output stream", e);
				}
			}
				
		}
	}

	public String getProxyURL() {
		return proxyURL;
	}

	public void setProxyURL(String proxyURL) {
		this.proxyURL = proxyURL;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public boolean isProxyEnable() {
		return isProxyEnable;
	}

	public void setProxyEnable(boolean isProxyEnable) {
		this.isProxyEnable = isProxyEnable;
	}

	public TranslatorType getTranslatorType() {
		return translatorType;
	}

	public void setTranslatorType(TranslatorType translatorType) {
		this.translatorType = translatorType;
	}

	public Language getFromLanguage() {
		return fromLanguage;
	}

	public void setFromLanguage(Language fromLanguage) {
		this.fromLanguage = fromLanguage;
	}

	public Language getToLanguage() {
		return toLanguage;
	}

	public void setToLanguage(Language toLanguage) {
		this.toLanguage = toLanguage;
	}

	public List<String> getRecentFiles() {
		return recentFiles;
	}

	public void setRecentFiles(String recentFile) {
		if(recentFiles.contains(recentFile))
			return;
		if(recentFiles.size()<=4)
		{
			recentFiles.add(recentFile);
		}
		else
		{
			recentFiles.set(0, recentFiles.get(1));
			recentFiles.set(1, recentFiles.get(2));
			recentFiles.set(2, recentFiles.get(3));
			recentFiles.set(3, recentFile);
		}
	}

	public String getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(String lastLocation) {
		this.lastLocation = lastLocation;
	}
	
}

