/*
 * HttpTranslator.java
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

package com.google.gdt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.google.gdt.main.PreferenceModel;

/**
 * Http translator uses http post method to send the content tobetranslated, and 
 * parse the response to get the translated output
 * 
 * @author Sanmoy
 *
 */
public class HttpTranslator implements Translator{

	/*
	 * 
	 */
	private PreferenceModel preferenceModel;
	
	/*
	 * 
	 */
	private static Logger logger = Logger.getLogger("HttpTranslator.class");
	
	/**
	 * 
	 */
	private String ametadata;
	
	/**
	 * 
	 */
	private String pmetadata; 
	
	public HttpTranslator() {
		preferenceModel = PreferenceModel.getInstance();
		logger.log(Level.INFO,"proxy url : "+System.getProperty("http.proxyHost"));
		logger.log(Level.INFO,"proxy port : "+System.getProperty("http.proxyPort"));
		logger.log(Level.INFO,"to language : "+preferenceModel.getToLanguage());
		logger.log(Level.INFO,"from language : "+preferenceModel.getFromLanguage());
		ametadata = TrConstants.AMETADATA.replaceAll("SourceLanguage", preferenceModel.getFromLanguage().toString());
		pmetadata = TrConstants.PMETADATA.replaceAll("Translatedlanguage", preferenceModel.getToLanguage().toString());
	}
	
	/**
	 * takes chinese text as input and translate it to english
	 * @param inputText
	 * @return translatedText
	 */
	@Override
	public String translate(String inputText) throws Exception
	{
		String translatedText = "";
		
		if(null==inputText||inputText.equals(""))
			return translatedText;
		 
		String body = ametadata + inputText
				+ pmetadata;
		
		String response = doPost(body);
		translatedText = parseResponse(response);
		return translatedText;
	}
	
	/**
	 * parse the response from google server and extracts the desired translated Text
	 * @param response
	 * @return translatedText
	 */
	private String parseResponse(String response) {
//		System.out.println(response);
		String translatedText = "";
		InputSource is = new InputSource(new StringReader(response));
		SAXReader reader = new SAXReader();
		Document doc = null;
		try 
		{
			doc = reader.read(is);
		} catch (DocumentException e) 
		{
			logger.log(Level.SEVERE, "Not able to parse response : "+response, e);
			return "";
		}
		
		Element root = doc.getRootElement();
		for(Iterator i = root.elementIterator();i.hasNext();)
		{
			Element element = (Element)i.next();
			translatedText+=element.getText();
		}
		return translatedText;
	}

	/**
	 * connects google server to get the translation
	 * @author sanmoy
	 * @param body
	 * @return response
	 */
	private String doPost(String body) throws IOException
	{
		// Send the request
		URL url = new URL(TrConstants.TRANSURL);
		URLConnection conn = url.openConnection();
		
		conn.setRequestProperty("User-Agent", TrConstants.UAGENT);
		conn.setDoOutput(true);
		
		// write parameters
		OutputStreamWriter writer = new OutputStreamWriter(conn
				.getOutputStream());
		writer.write(body);
		writer.flush();

		// Get the response
		StringBuffer answer = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn
				.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) 
		{
			answer.append(line);
		}
		writer.close();
		reader.close();

		// Output the response and truncate doctype
		String answerString = answer.toString().substring(15);
//		System.out.println(answerString);
		int beginIndex = answerString.indexOf("span id=result_box");
		int endIndex = answerString.lastIndexOf("</span></span>");
		String response = body.substring(TrConstants.AMETADATA.length(), body.length()-TrConstants.PMETADATA.length());
		try
		{
			response = answerString.substring(beginIndex+38, endIndex+14);
		}
		catch (Exception e)
		{
			logger.log(Level.SEVERE, "not able to translate the response : "+response,e);
			return "<span>"+response+"</span>";
		}
//		System.out.println("<span>"+response);
		return "<span>"+response;
	}
}
