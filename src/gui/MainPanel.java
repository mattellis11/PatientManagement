/**
 * MainPanel.java
 * @author Matt Ellis
 * Implements a frontend gui for a patient collection
 */
package gui;

import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import backend.PatientCollection;

import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.SystemColor;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	private PatientCollection patCollection;
	private JTextArea patientCollectionTextArea;
	private JScrollPane patientCollectionScrollPane;
	private JComboBox<String> patientIdsComboBox;
	private JPanel controlPanel;
	private JPanel displayCollectionPanel;
	private JPanel modifyPatientPanel;
	private JLabel selectedPatientLabel;
	private JPanel greetPanel;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem exitMenuItem;
	private JRadioButton dp_radioButton;
	private JRadioButton cr_radioButton;
	private JButton updatePatientResponeButton;
	
	public MainPanel() {
		patCollection = new PatientCollection();
		
		setLayout(null);
		setPreferredSize(new Dimension(800,500));		
		
		/*
		 * greetPanel: shares space in frame with displayCollectionPanel and modifyPatientPanel
		 * functionality: visible when program begins and displays an icon image
		 */
		greetPanel = new JPanel();
		greetPanel.setLayout(null);
		greetPanel.setBounds(200, 0, 600, 500);
		greetPanel.setBackground (Color.WHITE);
		add(greetPanel);
		
		// icon displayed on the greetPanel at program startup
		JLabel greetIcon = new JLabel("");
		greetIcon.setIcon(new ImageIcon(MainPanel.class.getResource("/gui/pcm.png")));
		greetIcon.setBounds(71, 119, 449, 273);
		greetPanel.add(greetIcon);
		greetPanel.setVisible(true);
		
		/*
		 * controlPanel: panel always displayed on left side of the frame
		 * functionality: holds buttons to switch between patient management and
		 * 	display of entire patient collection as well as a button to add patients
		 * 	to the collection 
		 */
		controlPanel = new JPanel();
		controlPanel.setLayout(null);
		controlPanel.setBounds(0, 0, 200, 500);
		controlPanel.setBackground (Color.GRAY);
		add(controlPanel);
		
		// patientManagementButton: switches view to the modifyPatientPanel
		JButton patientManagementButton = new JButton("Patient Management");
		patientManagementButton.setBounds(10, 34, 180, 23);
		patientManagementButton.setFont(new Font("Verdana", Font.PLAIN, 11));
		patientManagementButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modifyPatientPanel.setVisible(true);
				modifyPatientPanel.setEnabled(true);
				displayCollectionPanel.setEnabled(false);
				displayCollectionPanel.setVisible(false);
				greetPanel.setVisible(false);
			}
		});		
		controlPanel.add(patientManagementButton);
		
		// displayPatientCollectionButton: switches view to the displayCollectionPanel
		JButton displayPatientCollectionButton = new JButton("Display Patient Collection");
		displayPatientCollectionButton.setBounds(10, 68, 180, 23);
		displayPatientCollectionButton.setFont(new Font("Verdana", Font.PLAIN, 11));
		displayPatientCollectionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				modifyPatientPanel.setVisible(false);
				modifyPatientPanel.setEnabled(false);
				displayCollectionPanel.setEnabled(true);
				displayCollectionPanel.setVisible(true);
				greetPanel.setVisible(false);
				
				// prints patient collection
				updatePatCollectionDisplay();
			}
		});
		controlPanel.add(displayPatientCollectionButton);

		// addPatientsFromFileButton: opens file chooser to import new patients to collection
		JButton addPatientsFromFileButton = new JButton("Add Patients From File");
		addPatientsFromFileButton.setBounds(10, 130, 180, 23);
		addPatientsFromFileButton.setFont(new Font("Verdana", Font.PLAIN, 11));
		addPatientsFromFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser("./backend");
				jfc.setDialogTitle("Select a file");
				jfc.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
				jfc.addChoosableFileFilter(filter);
				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String err = patCollection.addPatientsFromFile(jfc.getSelectedFile().toString());
					// if addition of patients had errors, display them
					if (err!="") {
						JOptionPane.showMessageDialog(null, err, "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				updatePatientListComboBox();
				updatePatCollectionDisplay();
			}
		});
		controlPanel.add(addPatientsFromFileButton);
				
		/* 
		 * menu bar located at top of the controlPanel. It holds options for loading a patient collection,
		 * saving a patient collection, and exiting the program
		*/
		menuBar = new JMenuBar();
		menuBar.setFont(new Font("Verdana", Font.PLAIN, 12));
		menuBar.setBounds(0, 0, 200, 23);
		controlPanel.add(menuBar);
		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
					
		// save the patient collection from the menu
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int response = JOptionPane.showConfirmDialog(null, "Do want to save?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {
					patCollection.writeFile();
				}
			}
		});			
		
		exitMenuItem = new JMenuItem("Exit (without saving)");
		exitMenuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(saveMenuItem);
		fileMenu.add(exitMenuItem);
		
		/*
		 * modifyPatientPanel: shares space with patientCollectionDisplayPanel and greetPanel
		 * functionality: includes a combo box of all patient ids in collection, displays selected patient info,
		 * 	allows modification of patient's response to treatment, and ability to remove a patient from the
		 *  collection 
		 */
		modifyPatientPanel = new JPanel();
		modifyPatientPanel.setLayout(null);
		modifyPatientPanel.setBackground(Color.LIGHT_GRAY);
		modifyPatientPanel.setBounds(200, 0, 600, 500);
		add(modifyPatientPanel);
		
		// patientIdsComboBox: selectable list of patient ids
		patientIdsComboBox = new JComboBox<String>();			
		patientIdsComboBox.setFont(new Font("Verdana", Font.PLAIN, 11));
		patientIdsComboBox.setBounds(113, 66, 56, 22);
		patientIdsComboBox.setModel(new DefaultComboBoxModel(patCollection.getIds().toArray()));	
		patientIdsComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTextForSelectedPatientLabel();
			}
		});
		modifyPatientPanel.add(patientIdsComboBox);
		
		// label which identifies combo box purpose
		JLabel comboBoxLabel = new JLabel("Select Patient ->");
		comboBoxLabel.setFont(new Font("Verdana", Font.PLAIN, 11));
		comboBoxLabel.setBounds(10, 66, 125, 22);
		modifyPatientPanel.add(comboBoxLabel);
		
		// label which displays patient information of id selected by combo box
		selectedPatientLabel = new JLabel();
		selectedPatientLabel.setFont(new Font("Verdana", Font.PLAIN, 10));
		selectedPatientLabel.setBounds(10, 148, 580, 33);
		modifyPatientPanel.add(selectedPatientLabel);
		setTextForSelectedPatientLabel();
		
		// label to identify displayed patient information
		JLabel patientInfoLabel = new JLabel("Patient info:");
		patientInfoLabel.setFont(new Font("Verdana", Font.PLAIN, 11));
		patientInfoLabel.setBounds(10, 123, 136, 33);
		modifyPatientPanel.add(patientInfoLabel);
		
		// radio button for selection of "dp", disease progression, as patient response
		dp_radioButton = new JRadioButton("DP");
		buttonGroup.add(dp_radioButton);
		dp_radioButton.setBounds(10, 223, 50, 23);
		modifyPatientPanel.add(dp_radioButton);
		
		// radio button for selection of "cr", complete response, as patient response
		cr_radioButton = new JRadioButton("CR");
		cr_radioButton.setSelected(true);
		buttonGroup.add(cr_radioButton);
		cr_radioButton.setBounds(10, 197, 50, 23);
		modifyPatientPanel.add(cr_radioButton);
		
		// button updates patient's response using radio button selection
		updatePatientResponeButton = new JButton("Update Patient's Response");
		updatePatientResponeButton.setFont(new Font("Verdana", Font.PLAIN, 11));
		updatePatientResponeButton.setBounds(68, 207, 204, 23);
		updatePatientResponeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String patId = patientIdsComboBox.getSelectedItem().toString();
				if(cr_radioButton.isSelected()) {
					patCollection.getPatient(patId).setResponse("CR");
				}
				else {
					patCollection.getPatient(patId).setResponse("DP");
				}
				setTextForSelectedPatientLabel();
				updatePatCollectionDisplay();
			}
		});
		modifyPatientPanel.add(updatePatientResponeButton);
		
		// button to delete patient from patient collection
		JButton deletePatientButton = new JButton("Delete Selected Patient from Collection ");
		deletePatientButton.setFont(new Font("Verdana", Font.PLAIN, 11));
		deletePatientButton.setBounds(10, 440, 279, 23);
		deletePatientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String id = patientIdsComboBox.getSelectedItem().toString();
				int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete Patient "+id+" from the collection?", "Confirm",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {
					patCollection.removePatient(id);
					updatePatientListComboBox();
					updatePatCollectionDisplay();
				}  
			}
		});
		modifyPatientPanel.add(deletePatientButton);
		
		// label that displays header at top of modifyPatientPanel
		JLabel modifyPatientPanelHeader = new JLabel("Patient Management");
		modifyPatientPanelHeader.setBackground(Color.LIGHT_GRAY);
		modifyPatientPanelHeader.setFont(new Font("Garamond", Font.PLAIN, 24));
		modifyPatientPanelHeader.setBounds(10, 10, 580, 33);
		modifyPatientPanel.add(modifyPatientPanelHeader);
		
		// begin program with modifyPatientPanel disabled and unseen
		modifyPatientPanel.setVisible(false);
		modifyPatientPanel.setEnabled(false);
				
		/*
		 * displayCollectionPanel: shares space with modifyPatientPanel and greetPanel
		 * functionality: displays complete patient collection in a Jtextarea, and allows
		 * 	for printing of the patient collection
		 */
		displayCollectionPanel = new JPanel();
		displayCollectionPanel.setLayout(null);		
		displayCollectionPanel.setBackground(Color.LIGHT_GRAY);
		displayCollectionPanel.setBounds(200, 0, 600, 500);
		add(displayCollectionPanel);
		
		// scrollpane for patient collection data
		patientCollectionScrollPane = new JScrollPane();
		patientCollectionScrollPane.setBounds(10, 60, 580, 429);
		displayCollectionPanel.add(patientCollectionScrollPane);
		
		// textarea for patient collection data
		patientCollectionTextArea = new JTextArea();
		patientCollectionTextArea.setFont(new Font("Noto Sans", Font.PLAIN, 11));
		patientCollectionTextArea.setEditable(false);
		patientCollectionTextArea.setBackground(SystemColor.control);
		patientCollectionScrollPane.setViewportView(patientCollectionTextArea);
		
		// title header at top of the displayCollectionPanel
		JLabel displayCollectionPanelHeader = new JLabel("Patient Collection");
		displayCollectionPanelHeader.setBackground(SystemColor.control);
		displayCollectionPanelHeader.setVerticalAlignment(SwingConstants.BOTTOM);
		displayCollectionPanelHeader.setFont(new Font("Garamond", Font.PLAIN, 24));
		displayCollectionPanelHeader.setBounds(10, 10, 407, 30);
		displayCollectionPanel.add(displayCollectionPanelHeader);
		
		// enables print functionality of displayed patient collection
		JButton printCollectionButton = new JButton("Print");
		printCollectionButton.setBounds(501, 14, 76, 30);
		printCollectionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				PrinterJob job = PrinterJob.getPrinterJob();
		         job.setPrintable(patientCollectionTextArea.getPrintable(null, null));
		         boolean ok = job.printDialog();
		         if (ok) {
		             try {
		                  job.print();
		             } catch (PrinterException ex) {
		              /* The job did not successfully complete */
		             }
		         }
			}
		});
		displayCollectionPanel.add(printCollectionButton);
		
		// begin program with displayCollectionPanel disabled and unseen
		displayCollectionPanel.setVisible(false);
		displayCollectionPanel.setEnabled(false);		
		
	}
	
	private void updatePatientListComboBox() {
		// updates the patient ids listed in the combo box 
		patientIdsComboBox.setModel(new DefaultComboBoxModel(patCollection.getIds().toArray()));
		setTextForSelectedPatientLabel();
	}
	
	private void setTextForSelectedPatientLabel() {
		// updates the displayed patient information for the patient whose id is
		// selected in the combo box
		String id = (String) patientIdsComboBox.getSelectedItem();
		if (id!=null) {
			selectedPatientLabel.setText(patCollection.getPatient(id).toString());
		}
		// when collection is empty
		else {
			selectedPatientLabel.setText("");
		}		
	}
	
	private void updatePatCollectionDisplay() {
		// updates the patient collection data displayed in the displayCollectionPanel
		patientCollectionTextArea.setText(patCollection.toString());
	}
}
