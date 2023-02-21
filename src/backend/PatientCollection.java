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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class creates and maintains a collection of patients.
 * It maintains an updated record of the collection in the private data member "FILENAME".
 * <p>
 * Patients are recorded on a single line in the file with the format:
 * response,prediction,id,protein1,protein2, ... ,protein4776
 * <p>
 * New patients are added to the collection by reading a csv file with format:
 * id,protein1,protein2, ... ,protein4776
 * <p>
 * New patient's predictions are calculated when the patient is added to the collection
 *
 * @author Matt Ellis
 */

public class PatientCollection implements PatientCollectionADT {

    private static final String DELIMITER = ",";
    private static final int NO_PROTEINS = 4776;
    private static final int NEW_PATIENT_NO_DATA_PTS = 4777;
    private static final String FILENAME = "./src/backend/PatientCollection.csv";
    private final HashMap<String, Patient> patientMap;

    /**
     * Builds a patient collection from a maintained local file
     */
    public PatientCollection() {
        patientMap = new HashMap<String, Patient>();
        readFile(FILENAME);
    }

    /**
     * Builds a patient collection from a csv file passed as a parameter
     *
     * @param fn a csv file
     */
    public PatientCollection(String fn) {
        patientMap = new HashMap<String, Patient>();
        readFile(fn);
    }

    /**
     * Given a patient's id returns the patient from the collection
     *
     * @param id patient's id
     * @return the Patient or null if Patient does not exist
     */
    @Override
    public Patient getPatient(String id) {
        return patientMap.get(id);
    }

    /**
     * Given a patient's id removes the patient from the collection
     *
     * @param id patient's id
     * @return the Patient or null if Patient does not exist
     */
    @Override
    public Patient removePatient(String id) {
        return patientMap.remove(id);
    }

    /**
     * Sets the predicted treatment response field for the patient with given id.
     *
     * @param id     patient's id
     * @param result patient's predicted response
     */
    @Override
    public void setResultForPatient(String id, String result) {
        if (patientMap.containsKey(id)) {
            getPatient(id).setResponse(result);
        }
    }

    /**
     * Returns an ArrayList containing all the collection's patient ids
     *
     * @return list of patient ids
     */
    @Override
    public ArrayList<String> getIds() {
        ArrayList<Integer> intArray = new ArrayList<>();

        for (String s : patientMap.keySet()) {
            intArray.add(Integer.parseInt(s));
        }

        Collections.sort(intArray);

        ArrayList<String> strArray = new ArrayList<>();

        for (Integer i : intArray) {
            strArray.add("" + i);
        }

        return strArray;
    }

    /**
     * Imports patients to a collection from a file
     *
     * @param fileName a csv file
     * @return if an error occurs, returns the error
     */
    @Override
    public String addPatientsFromFile(String fileName) {
        String toReturn = "";
        int lineCount = 1;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line = "";

            // expected line format
            // id,protein1,protein2, ... ,protein4776
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
                    ArrayList<Double> proteinList = new ArrayList<>();
                    for (int i = 1; i < NO_PROTEINS + 1; i++) {
                        proteinList.add(Double.parseDouble(patientData[i]));
                    }
                    Patient p = new Patient(patientData[0], proteinList);
                    patientMap.put(patientData[0], p);
                }
                lineCount++;
            }
        } catch (Exception ee) {
            ee.printStackTrace();
            System.err.println(
                    "there was a problem with the file reader, try again.  either no such file or format error");
        } finally {
            try {
                br.close();
            } catch (IOException ie) {
                System.err.println("Error occurred while closing the BufferedReader");
                ie.printStackTrace();
            }
        }
        return toReturn;
    }

    /**
     * A string representation of a patient collection
     *
     * Only includes the 3698th and 3259th protein values
     *
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Set<String> keys = patientMap.keySet();
        ArrayList<Integer> ids = keys.stream().map(str -> Integer.parseInt(str)).collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(ids);
        for (Integer id : ids) {
            sb.append(getPatient(Integer.toString(id)));
        }
        return sb.toString();
    }

    /**
     * Reads stored patient collection data from csv file and adds to collection
     *
     * @param fileName csv file
     */
    private void readFile(String fileName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line = "";

            // expected line format
            // response,prediction,id,protein1,protein2, ... ,protein4776
            while ((line = br.readLine()) != null) {
                String[] patientData = line.split(DELIMITER);
                ArrayList<Double> proteinList = new ArrayList<>();
                for (int i = 3; i < NO_PROTEINS + 3; i++) {
                    proteinList.add(Double.parseDouble(patientData[i]));
                }
                Patient p = new Patient(patientData[2], patientData[0], patientData[1], proteinList);
                patientMap.put(patientData[2], p);
            }
        } catch (Exception e) {
            System.err.println(
                    "there was a problem with the file reader, try again.  either no such file or format error");
        } finally {
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    System.err.println("could not close BufferedReader");
                }
        }
    }

    /**
     * Passes FILENAME to doWrite method.
     * Saves collection data between runs.
     */
    public void writeFile() {
        doWrite(FILENAME);
    }

    /**
     * Overloaded method for testing doWrite method with an alternate file
     *
     * @param altFileName filename
     */
    public void writeFile(String altFileName) {
        doWrite(altFileName);
    }

    /**
     * Writes all the data in the patient collection to a csv file.
     * Each patient will be written to a single line.
     * Line format is:
     * response,prediction,id,protein1,protein2, ... ,protein4776
     *
     * @param fn file to be written to
     */
    private void doWrite(String fn) {
        try {
            FileWriter fw = new FileWriter(fn);
            BufferedWriter myOutfile = new BufferedWriter(fw);
            for (String key : patientMap.keySet()) {
                myOutfile.write(getPatient(key).getResponse() + DELIMITER);
                myOutfile.write(getPatient(key).getPredict() + DELIMITER);
                myOutfile.write(getPatient(key).getId() + DELIMITER);
                for (Double d : getPatient(key).getProteins()) {
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
