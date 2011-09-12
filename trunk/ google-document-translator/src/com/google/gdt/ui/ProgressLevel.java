/*
 * ProgressLevel.java
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

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.ToolTipManager;

/**
 * 
 * @author Sanmoy
 *
 */
public class ProgressLevel extends JPanel {

	private static Logger logger = Logger.getLogger("ProgressLevel.class");
	
	public static final int HEIGHT = 50;
	public static final int WIDTH = 620;
	
	private static final int DISPLAYLENGTH = 95;
	
	private JProgressBar jBar = new JProgressBar();
	private JLabel jLabel = new JLabel();
	
	private String filename;
	private String trFileName;
	
	public ProgressLevel(String filename) {
		this.filename = filename;
		initComponents(filename);
	}

	private void initComponents(String filename) {
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		int filenameLength = filename.length();
		
		if(filenameLength > DISPLAYLENGTH)
		{
			filename = filename.substring(0, 3)+"..."+filename.substring(filenameLength-(DISPLAYLENGTH-6),filenameLength);
		}
		
		setPreferredSize(new Dimension(620, HEIGHT));
		setMinimumSize(new Dimension(620, HEIGHT));
		setMaximumSize(new Dimension(620, HEIGHT));
		
		jLabel.setToolTipText("click for options");
		ToolTipManager.sharedInstance().registerComponent(jLabel);
		jLabel.setText(filename);
		jLabel.setPreferredSize(new Dimension(620, 20));
		jLabel.setMinimumSize(new Dimension(620, 20));
		jLabel.setMaximumSize(new Dimension(620, 20));
		jLabel.addMouseListener(new HGTpopupMenu());
		
//		jLabel.setBounds(5, 5, 640, 25);
		add(jLabel,BorderLayout.WEST);
//		add(jLabel);
		
		jBar.setPreferredSize(new Dimension(620,18));
		jBar.setMinimumSize(new Dimension(620, 18));
		jBar.setMaximumSize(new Dimension(620, 18));
		jBar.addMouseListener(new HGTpopupMenu());
		jBar.setToolTipText("click for options");
		ToolTipManager.sharedInstance().registerComponent(jBar);
		
//		jBar.setBounds(5, 30, 640, 25);
		add(jBar,BorderLayout.SOUTH);
//		add(jBar);
		
	}
	
	public void setValue(int progress) {
		jBar.setValue(progress);
	}
	
	public void setMaxValue(int maxValue) {
		jBar.setMaximum(maxValue);
	}
	
	public void setStringPainted(boolean bool)
	{
		jBar.setStringPainted(bool);
	}
	
	public void setString(String string)
	{
		jBar.setString(string);
	}
	
	public void setTrFileName(String trFileName) {
		this.trFileName = trFileName;
	}



	class HGTpopupMenu extends MouseAdapter
	{
		JPopupMenu jPopupMenu = new JPopupMenu();
		JMenuItem openFolderMenu = new JMenuItem();
		JMenuItem originalFileMenu = new JMenuItem();
		JMenuItem translatedFileMenu = new JMenuItem();
		JMenuItem removeMenu = new JMenuItem();
		
		@Override
		public void mouseClicked(MouseEvent e) 
		{
			if(!jBar.getString().equals("done"))
			{
				translatedFileMenu.setEnabled(false);
			}
			else
				translatedFileMenu.setEnabled(true);
			jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
		}
		
		public HGTpopupMenu() 
		{
			openFolderMenu.setText("Open Folder");
			originalFileMenu.setText("Original File");
			translatedFileMenu.setText("Translated File");
			removeMenu.setText("Remove");
			
			jPopupMenu.add(openFolderMenu);
			jPopupMenu.add(originalFileMenu);
			jPopupMenu.add(translatedFileMenu);
			jPopupMenu.add(removeMenu);
			
			translatedFileMenu.setEnabled(false);
			
			openFolderMenu.addActionListener(new ActionListener() 
			{
				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					try 
					{
						Desktop.getDesktop().open(new File(filename).getParentFile());
					} 
					catch (Exception e1) 
					{
						JOptionPane.showMessageDialog(null, "Cannot open folder", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			
			originalFileMenu.addActionListener(new ActionListener() 
			{
				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					try 
					{
						Desktop.getDesktop().open(new File(filename));
					} 
					catch (Exception e1) 
					{
						JOptionPane.showMessageDialog(null, "Cannot open file", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			
			translatedFileMenu.addActionListener(new ActionListener() 
			{
				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					try 
					{
						Desktop.getDesktop().open(new File(trFileName));
					} 
					catch (Exception e1) 
					{
						JOptionPane.showMessageDialog(null, "Cannot open file", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			
			removeMenu.addActionListener(new ActionListener() 
			{
			
				MainJFrame mainJFrame = MainJFrame.getInstance();
				
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					mainJFrame.removeGdtJobs(new File(filename));
					mainJFrame.getJpanel().remove(ProgressLevel.this);
				}
			});
		}

	}
}
