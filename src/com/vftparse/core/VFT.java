package com.vftparse.core;

import java.io.File;

import org.jdom2.Document;
import org.jdom2.Element;

import com.coremedia.iso.IsoFile;

import com.vftparse.io.*;
import com.vftparse.parse.*;

/**
 * <h1>The VFT core</h1>
 * <p>VFT is a static class that implements methods to parse
 * and elaborate xml file, jdom Element and Document objects, and Tree objects.
 * 
 * @author Saverio Meucci
 */
public class VFT {

	/**
	 * This method serves as an interface for the cli app to parse
	 * a mp4-like video container into a xml file.
	 * @param url The path of the video file.
	 * @param xmlDestinationPath The folder where to save the resulting xml file.
	 * @throws Exception
	 */
	public static void parse(String url, String xmlDestinationPath) throws Exception {
		try {
			if (url.toLowerCase().endsWith(".mp4") || url.toLowerCase().endsWith(".mov")) {
				FileReaderSaver fileSaver = new FileReaderSaver(url, xmlDestinationPath);
				//create an ISOFILE using the FileReaderSaver
				IsoFile isoFile = fileSaver.getIsoFile();
				Element root = parser(isoFile);
				//save the content of the container on a xml file using the saveOnFile function of the FileReaderSaver class
				fileSaver.saveOnFile(new Document(root));
				System.out.println("XML file '" + fileSaver.getDestinationPath() + "' created.");
			} else {
				System.err.println("Wrong input format. The input file should be a video file (mp4 or mov).");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println("Could not parse video: " + url);
		}
	}
	
	/**
	 * This method is used to parse a IsoFile into a jdom Element.
	 *  @param isoFile The IsoFile representing the container of the video file.
	 *  @return A jdom Element.
	 */
	public static Element parser(IsoFile isoFile) throws Exception {
		Element root = new Element("root");
		//TODO modelName and phoneBrandName should be read from the console during the parsing phase
		root.setAttribute("modelName", "phoneBrandName");
		BoxParser boxparser = new BoxParser(isoFile);
		boxparser.getBoxes(null, root);
		return root;
	}
	
	/**
	 * This method servers as an interface for the cli app to
	 * parse a directory of video file containers. It also considers sufolders
	 * and recreates the same folder structure for the outputted xml files.
	 * @param datasetPath The path of the folder that needs to be parsed.
	 * @param outputPath The folder where the xml files will be saved.
	 * @throws Exception
	 */
	public static void batch(String datasetPath, String outputPath) throws Exception {
		File datasetFolder = new File(datasetPath);
		if (!datasetFolder.exists() || !datasetFolder.isDirectory()) {
			System.err.println("Could not find the dataset folder at '" + datasetPath + "'");
		} else if (!new File(outputPath).exists() || !new File(outputPath).isDirectory()) {
			System.err.println("Could not find the output folder at '" + outputPath + "'");
		} else {
			System.out.println("Batch parsing the dataset at '" + datasetPath + "'");
			parseDirectory(datasetFolder, outputPath);
		}
	}
	
	/**
	 * This method is used to parse a directory of video file containers. It also considers subfolders
	 * and recreates the same folder structure for the outputted xml files.
	 * @param folder A File objects representing the folder under consideration.
	 * @param outputPath The path where to save the xml files.
	 * @throws Exception
	 */
	public static void parseDirectory(File folder, String outputPath) throws Exception {
		File[] files = folder.listFiles();
		for (File f: files) {
			if (f.isFile() && !f.getName().startsWith(".")) {
				parse(f.getAbsolutePath(), outputPath);
			} else if (f.isDirectory() && !f.getName().toLowerCase().endsWith(".not")) {
				File subfolder = new File(f.getAbsolutePath());
				new File(outputPath + "/" + f.getName()).mkdir();
				parseDirectory(subfolder, outputPath + "/" + f.getName());
			}
		}
	}
}