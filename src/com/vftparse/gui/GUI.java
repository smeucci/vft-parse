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
	public String input;
	public String outputFolder;
	public boolean isFolder;
	
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
	    JLabel parseLabel = new JLabel();
	    JButton parseButton = new JButton("Parse");
	    parseButton.addActionListener(new Parse(parseLabel));
	    
	    JLabel inputFileLabel = new JLabel( "Input File:" );
	    JTextField inputFileTextField = new JTextField( 20 );
	    inputFileTextField.setEditable(false);
	    JButton inputFileButton = new JButton("Select File");
	    
	    JLabel inputFolderLabel = new JLabel( "Input Folder:" );
	    JTextField inputFolderTextField = new JTextField( 20 );
	    inputFolderTextField.setEditable(false);
	    JButton inputFolderButton = new JButton("Select Folder");
	    
	    inputFileButton.addActionListener(new InputFileChooser(inputFileTextField, inputFolderTextField, parseLabel));
	    inputFolderButton.addActionListener(new InputFolderChooser(inputFolderTextField, inputFileTextField, parseLabel));
	    
	    JLabel outputFolderLabel = new JLabel( "Output Folder:" );
	    JTextField outputFolderTextField = new JTextField( 20 );
	    outputFolderTextField.setEditable(false);
	    JButton outputFolderButton = new JButton("Select Folder");
	    outputFolderButton.addActionListener(new OutputFolderChooser(outputFolderTextField, parseLabel));
	
	    // Horizontally, we want to align the labels and the text fields
	    // along the left (LEADING) edge
	    layout.setHorizontalGroup( layout.createSequentialGroup()
	    		.addGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING )
                        .addComponent( inputFileLabel )
                        .addComponent( inputFolderLabel )
                        .addComponent( outputFolderLabel ))
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING )
                        .addComponent( inputFileTextField )
                        .addComponent( inputFolderTextField )
                        .addComponent( outputFolderTextField )
                        .addComponent( parseLabel ))
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING )
                        .addComponent( inputFileButton )
                        .addComponent( inputFolderButton )
                        .addComponent( outputFolderButton )
                        .addComponent( parseButton ) )
	    );
	
	    // Vertically, we want to align each label with his textfield
	    // on the baseline of the components
	    layout.setVerticalGroup( layout.createSequentialGroup()
	    		.addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )	    				
                        .addComponent( inputFileLabel )
                        .addComponent( inputFileTextField )
                        .addComponent( inputFileButton ) )
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )	    				
                        .addComponent( inputFolderLabel )
                        .addComponent( inputFolderTextField )
                        .addComponent( inputFolderButton ) )       
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
                        .addComponent( outputFolderLabel )
                        .addComponent( outputFolderTextField )
                        .addComponent( outputFolderButton ) )
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
                        .addComponent( parseButton )
                        .addComponent( parseLabel ) )
	    );
	
	    return result;
	}
	  
	private class InputFileChooser implements ActionListener {
		private JTextField field;
		private JLabel parseLabel;
		private JTextField inputFolderField;
		
		public InputFileChooser(JTextField field, JTextField inputFolderField, JLabel parseLabel) {
			this.field = field;
			this.parseLabel = parseLabel;
			this.inputFolderField = inputFolderField;
		}
		
	    public void actionPerformed(ActionEvent e) {
	    	try {
	    		parseLabel.setText("");
	    		JFileChooser fileChooser = new JFileChooser();
	    		fileChooser.setFileFilter(new TxtFileFilter());
	    		int n = fileChooser.showOpenDialog(GUI.this);
	    		if (n == JFileChooser.APPROVE_OPTION) {
	    			File f = fileChooser.getSelectedFile();
	    			isFolder = false;
	    			input = f.getAbsolutePath();
	    			field.setText(input);
	    			inputFolderField.setText("");
	    	    }
	    	} catch (Exception ex) {}
	    }
	}
	
	private class InputFolderChooser implements ActionListener {
		private JTextField field;
		private JLabel parseLabel;
		private JTextField inputFileField;
		
		public InputFolderChooser(JTextField field, JTextField inputFileField, JLabel parseLabel) {
			this.field = field;
			this.parseLabel = parseLabel;
			this.inputFileField = inputFileField;
		}
		
	    public void actionPerformed(ActionEvent e) {
	    	try {
	    		parseLabel.setText("");
	    		JFileChooser folderChooser = new JFileChooser();
	    		folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    		folderChooser.setAcceptAllFileFilterUsed(false);
	    		int n = folderChooser.showOpenDialog(GUI.this);
	    		if (n == JFileChooser.APPROVE_OPTION) {
	    			File f = folderChooser.getSelectedFile();
	    			isFolder = true;
	    			input = f.getAbsolutePath();
	    			field.setText(input);
	    			inputFileField.setText("");
	    	    }
	    	} catch (Exception ex) {}
	    }
	}
	
	private class OutputFolderChooser implements ActionListener {
		private JTextField field;
		private JLabel parseLabel;
		
		public OutputFolderChooser(JTextField field, JLabel parseLabel) {
			this.field = field;
			this.parseLabel = parseLabel;
		}
		
	    public void actionPerformed(ActionEvent e) {
	    	try {
	    		parseLabel.setText("");
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
				if (isFolder) {
					batch(input, outputFolder);
				} else {
					parse(input, outputFolder);
				}
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
	    frame.setSize(new Dimension(550, 180));
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
