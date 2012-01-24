package com.google.gdt.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GdtDropTargetListener extends DropTargetAdapter
{
	private final Set<String> extnSet = new HashSet<String>();
	
	private List<File> fileList;
	
	public GdtDropTargetListener() {
		// TODO Auto-generated constructor stub
		extnSet.add("doc");
		extnSet.add("xls");
		extnSet.add("ppt");
		extnSet.add("txt");
//		extnSet.add("pdf");
		extnSet.add("docx");
		extnSet.add("xlsx");
		extnSet.add("pptx");
	}
	@Override
	public void dragEnter(DropTargetDragEvent dtde) 
	{
		Transferable transferable = dtde.getTransferable();
		try 
		{
            fileList =
                (List<File>)transferable.getTransferData(DataFlavor.javaFileListFlavor);

            for (File file : fileList)
            {
            	if(!extnSet.contains(getFileExtension(file)))
            	{
            		dtde.rejectDrag();
            	}
            }
        } 
		catch (UnsupportedFlavorException e) 
        {
			dtde.rejectDrag();
        } 
		catch (IOException e) 
        {
			dtde.rejectDrag();
        }
		
	}
	@Override
	public void drop(DropTargetDropEvent dtde) 
	{
		MainJFrame.getInstance().createGdtJobs((File[]) fileList.toArray());
	}
	
	private String getFileExtension(File file)
	{
		int dot = file.getName().lastIndexOf(".");
		return file.getName().substring(dot+1);
	}
}