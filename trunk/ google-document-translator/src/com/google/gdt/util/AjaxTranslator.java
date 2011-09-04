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

import java.util.logging.Logger;

import com.google.api.translate.Translate;
import com.google.gdt.main.PreferenceModel;

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
	
	public AjaxTranslator() {
		// TODO Auto-generated constructor stub
		preferenceModel = PreferenceModel.getInstance();
	}
	@Override
	public String translate(String inputText) throws Exception
	{
		Translate.setHttpReferrer("http://code.google.com/p/google-document-translator/");
		return Translate.execute(inputText,
				preferenceModel.getFromLanguage(),
				preferenceModel.getToLanguage());
	}

}
