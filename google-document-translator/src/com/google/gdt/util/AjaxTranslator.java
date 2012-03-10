/*
 * AjaxTranslator.java
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import com.google.gdt.main.PreferenceModel;
import com.tecnick.htmlutils.htmlentities.HTMLEntities;

/**
 * Ajax translator uses google ajax apis for translation, which has been depricated 
 * and has been replaced by a paid version
 * 
 * @author Sanmoy
 *
 */
public class AjaxTranslator implements Translator 
{
	/*
	 * 
	 */
	private PreferenceModel preferenceModel;
	
	/*
	 * 
	 */
	private static Logger logger = Logger.getLogger("AjaxTranslator.class");
	
	/**
	 * Constants.
	 */
    private static final String
    		LANG_PARAM = "&langpair=",
    		TEXT_PARAM = "&q=",
    		PIPE_PARAM = "%7C",
    		URL = "https://www.googleapis.com/language/translate/v2?",
    		PARAMETERS = "key=#KEY#&source=#FROM#&target=#TO#&q=#TEXT#";
    
    protected static final String ENCODING = "UTF-8";
    
    protected static String key;
    
    protected static final String REFERER = "http://code.google.com/p/google-document-translator/";
	
	public AjaxTranslator() {
		preferenceModel = PreferenceModel.getInstance();
		key = preferenceModel.getApiKey();
		logger.log(Level.INFO,"proxy url : "+System.getProperty("http.proxyHost"));
		logger.log(Level.INFO,"proxy port : "+System.getProperty("http.proxyPort"));
		logger.log(Level.INFO,"to language : "+preferenceModel.getToLanguage());
		logger.log(Level.INFO,"from language : "+preferenceModel.getFromLanguage());
		
	}
	
	
	@Override
	public String translate(String inputText) throws Exception
	{
		return execute(inputText,
				preferenceModel.getFromLanguage(),
				preferenceModel.getToLanguage());
	}
	
	/**
     * Translates text from a given Language to another given Language using Google Translate.
     * 
     * @param text The String to translate.
     * @param from The language code to translate from.
     * @param to The language code to translate to.
     * @return The translated String.
     * @throws Exception on error.
     */
    public static String execute(final String text, final Language from, final Language to) throws Exception {
    	
    	final String parameters = PARAMETERS.replaceAll("#FROM#", from.toString()).replaceAll("#TO#", to.toString())
		.replaceAll("#TEXT#", URLEncoder.encode(text, ENCODING)).replaceAll("#KEY#", key);
    	final URL url = new URL(URL);
    	
    	final JSONObject json = retrieveJSON(url,parameters);
    	
    	return getJSONResponse(json);
    }
    
    /**
     * Forms an HTTP request, sends it using GET method and returns the result of the request as a JSONObject.
     * 
     * @param url The URL to query for a JSONObject.
     * @return The translated String.
     * @throws Exception on error.
     */
    protected static JSONObject retrieveJSON(final URL url) throws Exception {
    	try {
    		final HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
    		uc.setRequestProperty("referer", REFERER);
    		uc.setRequestMethod("GET");
    		uc.setDoOutput(true);
    		
    		try {
    			final String result = inputStreamToString(uc.getInputStream());
    			
    			return new JSONObject(result);
    		} finally { // http://java.sun.com/j2se/1.5.0/docs/guide/net/http-keepalive.html
    			uc.getInputStream().close();
    			if (uc.getErrorStream() != null) {
    				uc.getErrorStream().close();
    			}
    		}
    	} catch (Exception ex) {
    		throw new Exception("[google-api-translate-java] Error retrieving translation.", ex);
    	}
    }
    
    /**
     * Forms an HTTP request, sends it using POST method and returns the result of the request as a JSONObject.
     * 
     * @param url The URL to query for a JSONObject.
     * @param parameters Additional POST parameters
     * @return The translated String.
     * @throws Exception on error.
     */
    protected static JSONObject retrieveJSON(final URL url, final String parameters) throws Exception {
    	try {
    		final HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
    		uc.setRequestProperty("referer", REFERER);
    		uc.setRequestMethod("POST");
    		uc.setRequestProperty("X-HTTP-Method-Override","GET");
    		uc.setDoOutput(true);

			final PrintWriter pw = new PrintWriter(uc.getOutputStream());
			pw.write(parameters);
			pw.close();
			uc.getOutputStream().close();
    		
    		try {
    			final String result = inputStreamToString(uc.getInputStream());
    			
    			return new JSONObject(result);
    		} finally { // http://java.sun.com/j2se/1.5.0/docs/guide/net/http-keepalive.html
    			if (uc.getInputStream() != null) {
    				uc.getInputStream().close();
    			}
    			if (uc.getErrorStream() != null) {
    				uc.getErrorStream().close();
    			}
    			if (pw != null) {
    				pw.close();
    			}
    		}
    	} catch (Exception ex) {
    		throw new Exception("[google-api-translate-java] Error retrieving translation.", ex);
    	}
    }
    
    /**
     * Reads an InputStream and returns its contents as a String.
     * Also effects rate control.
     * @param inputStream The InputStream to read from.
     * @return The contents of the InputStream as a String.
     * @throws Exception on error.
     */
    private static String inputStreamToString(final InputStream inputStream) throws Exception {
    	final StringBuilder outputBuilder = new StringBuilder();
    	
    	try {
    		String string;
    		if (inputStream != null) {
    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
    			while (null != (string = reader.readLine())) {
    				outputBuilder.append(string).append('\n');
    			}
    		}
    	} catch (Exception ex) {
    		throw new Exception("[google-api-translate-java] Error reading translation stream.", ex);
    	}
    	
    	return outputBuilder.toString();
    }
    
    /**
     * Returns the JSON response data as a String. Throws an exception if the status is not a 200 OK.
     * 
     * @param json The JSON object to retrieve the response data from.
     * @return The responseData from the JSONObject.
     * @throws Exception If the responseStatus is not 200 OK.
     */
    private static String getJSONResponse(final JSONObject json) throws Exception {
    	//if (json.getString("responseStatus").equals("200"))
		final String translatedText = json.getJSONObject("data").getJSONArray("translations").getJSONObject(0).getString("translatedText");
		return HTMLEntities.unhtmlentities(translatedText);
    }

}
