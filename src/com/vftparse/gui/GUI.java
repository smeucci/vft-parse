package com.vftparse.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import static com.vftparse.core.VFT.*;

public class GUI extends JPanel {
	
	private static final long serialVersionUID = 1L;
	public String inputFile;
	public String outputFolder;
	
	public GUI() {
		add(createContent());
	}

	private Container createContent() {
	    JPanel result = new JPanel();
	    result.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
	
	    // Create the layout
	    GroupLayout layout = new GroupLayout( result );
	    result.setLayout( layout );
	    layout.setAutoCreateGaps( true );
	
	    // Create the components we will put in the form
	    JLabel inputFileLabel = new JLabel( "Input File:" );
	    JTextField inputFileTextField = new JTextField( 20 );
	    inputFileTextField.setEditable(false);
	    JButton openFileButton = new JButton("Select File");
	    openFileButton.addActionListener(new OpenFileChooser(inputFileTextField));
	    
	    JLabel outputFolderLabel = new JLabel( "Output Folder:" );
	    JTextField outputFolderTextField = new JTextField( 20 );
	    outputFolderTextField.setEditable(false);
	    JButton openFolderButton = new JButton("Select Folder");
	    openFolderButton.addActionListener(new OpenFolderChooser(outputFolderTextField));
	    
	    JLabel parseLabel = new JLabel();
	    JButton parse = new JButton("Parse");
	    parse.addActionListener(new Parse(parseLabel));
	
	    // Horizontally, we want to align the labels and the text fields
	    // along the left (LEADING) edge
	    layout.setHorizontalGroup( layout.createSequentialGroup()
	    		.addGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING )
                        .addComponent( inputFileLabel )
                        .addComponent( outputFolderLabel ))
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING )
                        .addComponent( inputFileTextField )
                        .addComponent( outputFolderTextField )
                        .addComponent( parseLabel ))
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING )
                        .addComponent( openFileButton )
                        .addComponent( openFolderButton )
                        .addComponent( parse ) )
	    );
	
	    // Vertically, we want to align each label with his textfield
	    // on the baseline of the components
	    layout.setVerticalGroup( layout.createSequentialGroup()
	    		.addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )	    				
                        .addComponent( inputFileLabel )
                        .addComponent( inputFileTextField )
                        .addComponent( openFileButton ) )
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
                        .addComponent( outputFolderLabel )
                        .addComponent( outputFolderTextField )
                        .addComponent( openFolderButton ) )
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
                        .addComponent( parse )
                        .addComponent( parseLabel ) )
	    );
	
	    return result;
	}
	  
	private class OpenFileChooser implements ActionListener {
		JTextField field;
		
		public OpenFileChooser(JTextField field) {
			this.field = field;
		}
		
	    public void actionPerformed(ActionEvent e) {
	    	try {
	    		JFileChooser fileChooser = new JFileChooser();
	    		fileChooser.setFileFilter(new TxtFileFilter());
	    		int n = fileChooser.showOpenDialog(GUI.this);
	    		if (n == JFileChooser.APPROVE_OPTION) {
	    			File f = fileChooser.getSelectedFile();
	    			inputFile = f.getAbsolutePath();
	    			field.setText(inputFile);
	    	    }
	    	} catch (Exception ex) {}
	    }
	}
	
	private class OpenFolderChooser implements ActionListener {
		JTextField field;
		
		public OpenFolderChooser(JTextField field) {
			this.field = field;
		}
		
	    public void actionPerformed(ActionEvent e) {
	    	try {
	    		JFileChooser folderChooser = new JFileChooser();
	    		folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    		folderChooser.setAcceptAllFileFilterUsed(false);
	    		int n = folderChooser.showOpenDialog(GUI.this);
	    		if (n == JFileChooser.APPROVE_OPTION) {
	    			File f = folderChooser.getSelectedFile();
	    			outputFolder = f.getAbsolutePath();
	    			field.setText(outputFolder);
	    	    }
	    	} catch (Exception ex) {}
	    }
	}
	
	private class Parse implements ActionListener {
		JLabel label;
		
		public Parse(JLabel label) {
			this.label = label;
		}
		
		public void actionPerformed(ActionEvent e) {
			try {
				parse(inputFile, outputFolder);
				label.setText("Done.");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	private class TxtFileFilter extends FileFilter {
		public boolean accept(File file) {
			if (file.isDirectory()) return true;
			String fname = file.getName().toLowerCase();
			return fname.toLowerCase().endsWith("mp4") || fname.toLowerCase().endsWith("mov");
		}

		public String getDescription() {
			return "*.mp4, *.mov files";
		}
	}
	
	private static void displayJFrame() {
		JFrame frame = new JFrame("vft-parse");
		GUI gui = new GUI();
		frame.getContentPane().add(gui);
	    //frame.pack();
	    frame.setVisible(true);
	    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    frame.setSize(new Dimension(500, 150));
	    frame.setLocationRelativeTo(null);
	}
	
	public static void run() {
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		displayJFrame();
	    	}
	    });
	}
	
}
