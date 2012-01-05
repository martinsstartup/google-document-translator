package com.google.gdt.ui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class RecentFileMenuItem extends JMenuItem {

	private String fileName ;
	
	public RecentFileMenuItem(String filename) {
		this.fileName = filename;
		this.setText(new File(filename).getName());
		this.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(
							new File(fileName).getParentFile());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Cannot open folder",
							"Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
	}
}
