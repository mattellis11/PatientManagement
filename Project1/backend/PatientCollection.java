/** 
 * PatientCollection.java
 * author: Matt Ellis
 * Implements the PatientCollectionADT interface
 * It maintains an updated record of the collection in the private data member "fileName"
 * Patients are recorded on a single line in the file with the format: 
 * response,prediction,id,protein1,protein2, ... ,protein4776
 * New patients are added to the collection by reading a csv file with format:
 * id,protein1,protein2, ... ,protein4776
 * New patient's predictions are calculated when the patient is added to the collection
 * 
 * 
 */
package backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PatientCollection implements PatientCollectionADT {

	private static final String DELIMITER = ",";
	private static final int PROTEINS = 4776;
	private static final int NEW_PATIENT_NO_DATA_PTS = 4777;
	private static final String FILENAME = "./backend/PatientCollection.csv";
	private HashMap<String, Patient> patientMap;

	public PatientCollection() {
		// default constructor
		// reads in patients from maintained local file
		patientMap = new HashMap<String, Patient>();
		readFile(FILENAME);
	}

	public PatientCollection(String fn) {
		// alternate constructor
		// builds a Patient Collection from file passed as parameter
		patientMap = new HashMap<String, Patient>();
		readFile(fn);
	}

	@Override
	public Patient getPatient(String id) {
		if (patientMap.containsKey(id)) {
			return (Patient) patientMap.get(id);
		}
		return null;
	}

	@Override
	public Patient removePatient(String id) {
		if (patientMap.containsKey(id)) {
			return (Patient) patientMap.remove(id);
		}
		return null;
	}

	@Override
	public void setResultForPatient(String id, String result) {
		if (patientMap.containsKey(id)) {
			getPatient(id).setResponse(result);
		}
	}

	@Override
	public ArrayList<String> getIds() {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for(String s: patientMap.keySet()) {
			arr.add(Integer.parseInt(s));
		}
		Collections.sort(arr);
		ArrayList<String> ar = new ArrayList<String>();
		for(Integer i:arr) {
			ar.add(""+i);
		}
				
		return ar;
	}

	@Override
	public String addPatientsFromFile(String fileName) {
		// expected line format
		// id,protein1,protein2, ... ,protein4776
		String toReturn = "";
		BufferedReader br = null;
		int lineCount = 1;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String line = "";
			while ((line = br.readLine()) != null) {
				// split line read from file into String[]
				String[] patientData = line.split(DELIMITER);
				// check for missing data
				if (patientData.length != NEW_PATIENT_NO_DATA_PTS) {
					toReturn += "**Error** - Line " + lineCount
							+ " - Format Error - line contains incorrect number of data elements.\n";
				}
				// check if patient id already exists
				else if (patientMap.containsKey(patientData[0])) {
					toReturn += "**Error** - Line " + lineCount + " - Patient id, " + patientData[0]
							+ ", already exists in record.\n";
				}
				// build new patient and add to collection
				else {
					ArrayList<Double> proteinList = new ArrayList<Double>();
					for (int i = 1; i < PROTEINS + 1; i++) {
						proteinList.add(Double.parseDouble(patientData[i]));
					}
					Patient p = new Patient(patientData[0], proteinList);
					patientMap.put(patientData[0], p);
				}
				lineCount++;
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException ie) {
				System.out.println("Error occured while closing the BufferedReader");
				ie.printStackTrace();
			}
		}
		return toReturn;
	}

	@Override
	public String toString() {
		String toReturn = "";
		for (String key : patientMap.keySet()) {
			toReturn += getPatient(key);
		}
		return toReturn;
	}

	private String readFile(String fileName) {
		// read stored patient collection data from csv file and add to patient
		// collection
		// expected line format
		// response,prediction,id,protein1,protein2, ... ,protein4776		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] patientData = line.split(DELIMITER);				
				ArrayList<Double> proteinList = new ArrayList<Double>();
				for (int i = 3; i < PROTEINS + 3; i++) {
					proteinList.add(Double.parseDouble(patientData[i]));
				}
				Patient p = new Patient(patientData[2], patientData[0], patientData[1], proteinList);
				patientMap.put(patientData[2], p);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				br = new BufferedReader(
						new InputStreamReader(this.getClass().getResourceAsStream(fileName.substring(1))));
				String line = "";
				while ((line = br.readLine()) != null) {
					String[] patientData = line.split(DELIMITER);
					ArrayList<Double> proteinList = new ArrayList<Double>();
					for (int i = 3; i < PROTEINS + 3; i++) {
						proteinList.add(Double.parseDouble(patientData[i]));
					}
					Patient p = new Patient(patientData[2], patientData[0], patientData[1], proteinList);
					patientMap.put(patientData[2], p);
				}
			} catch (Exception e2) {
				System.err.println(
						"there was a problem with the file reader, try again.  either no such file or format error");
			} finally {
				if (br != null)
					try {
						br.close();
					} catch (IOException e2) {
						System.err.println("could not close BufferedReader");
					}
			}
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					System.err.println("could not close BufferedReader");
				}
		}
		return null;
	}

	public void writeFile() {
		// overloaded method: this calls doWrite with file used to read data
		// use this for saving data between runs
		doWrite(FILENAME);
	}

	public void writeFile(String altFileName) {
		// overloaded method: this calls doWrite with different file name
		// use this for testing write
		doWrite(altFileName);
	}

	private void doWrite(String fn) {
		// this method writes all of the data in the patient collection to a csv file
		// file name is passed in from the writeFile methods
		// each patient will be written to a single line
		// line format is:
		// response,prediction,id,protein1,protein2, ... ,protein4776
		try {
			FileWriter fw = new FileWriter(fn);
			BufferedWriter myOutfile = new BufferedWriter(fw);
			for (String key : patientMap.keySet()) {
				myOutfile.write(getPatient(key).getResponse() + DELIMITER);
				myOutfile.write(getPatient(key).getPredict() + DELIMITER);
				myOutfile.write(getPatient(key).getId() + DELIMITER);
				for (Double d : getPatient(key).getProteinLvl()) {
					myOutfile.write(d + DELIMITER);
				}
				myOutfile.newLine();
			}
			myOutfile.flush();
			myOutfile.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Didn't save to " + fn);
		}
	}
}