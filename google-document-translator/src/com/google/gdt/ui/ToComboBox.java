/*
 * ToComboBox.java
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

package com.google.gdt.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JComboBox;

import com.google.gdt.main.PreferenceModel;
import com.google.gdt.util.Language;

/**
 * 
 * @author Sanmoy
 *
 */
public class ToComboBox extends JComboBox {

	private PreferenceModel preferenceModel;
	
	private static Logger logger = Logger.getLogger("ToComboBox.class");
	
	public ToComboBox() 
	{
		// TODO Auto-generated constructor stub
		initComponents();
		preferenceModel = PreferenceModel.getInstance();
		String toLangString = preferenceModel.getToLanguage().name();
		this.setSelectedItem(toLangString);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				toComboBoxActionPerformed(e);
			}
		});
	}

	protected void toComboBoxActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String toLangString = this.getSelectedItem().toString();
		preferenceModel.setToLanguage(Language.valueOf(toLangString));
	}

	private void initComponents()
	{
		// TODO Auto-generated method stub
		 for (Language language : Language.values()) 
		 {
			 if(language.toString().equals(""))
			 {
				 continue;
			 }
			 else
			 {
				 this.addItem(language.name());
			 }
		 }
	}
	
}
