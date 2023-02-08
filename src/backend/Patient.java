/** 
 * Patient.java
 * author: Matt Ellis
 * Patient attributes include:
 * 		a unique patient id
 *  	the patient's response to treatment
 *  	the patient's predicted response to treatment
 *  	a list of the patient's normalized protein expression level for 4776 proteins
 * Note: The patient's predicted response is calculated using their 3698th and 3259th protein levels
 */
package backend;

import java.util.ArrayList;

public class Patient {	
	private String id;
	private String rspns;
	private String predRspns;
	private ArrayList<Double> proteinLvl;
	private static final int PROTEIN1 = 3698;
	private static final int PROTEIN2 = 3259;

	public Patient() {
		proteinLvl = new ArrayList<Double>();
	}

	public Patient(String id, ArrayList<Double> proteinLvl) {	
		// constructor used while adding new patients from a csv file to a patientCollection
		this();
		this.id = id;
		this.proteinLvl = proteinLvl;				
		predictResponse();
		rspns = "unk"; 
	}

	public Patient(String id, String rspns, String predRspns, ArrayList<Double> proteinLvl) {
		// constructor used while reading existing patients from patientCollection csv file 
		this();
		this.id = id;
		this.rspns = rspns;
		this.predRspns = predRspns;
		this.proteinLvl = proteinLvl;
	}

	public void predictResponse() {
		predRspns = Predictor.predict(proteinLvl.get(PROTEIN1 - 1), proteinLvl.get(PROTEIN2 - 1));
	}

	public String getId() {
		return id;
	}

	public String getResponse() {
		return rspns;
	}

	public void setResponse(String rspns) {
		this.rspns = rspns;
	}

	public ArrayList<Double> getProteinLvl() {
		return proteinLvl;
	}

	public void setProteinLvl(ArrayList<Double> proteinLvl) {
		this.proteinLvl = proteinLvl;
	}

	public void setPredict(String predRspns) {
		this.predRspns = predRspns;
	} 

	public String getPredict() {
		return predRspns; 
	}

	public String toString() {
		String toReturn = "[Id] " + getId() + ", [Response] " + getResponse() + ", [Predicted] " + getPredict()
				+ ", [Protein " + PROTEIN1 + "] " + proteinLvl.get(PROTEIN1 - 1) + ", [Protein " + PROTEIN2 + "] "
				+ proteinLvl.get(PROTEIN2 - 1) + "\n";
		return toReturn;
	}

}
