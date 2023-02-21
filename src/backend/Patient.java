package backend;

import java.util.ArrayList;

/**
 * Patient attributes include:
 * <ul><li>a unique patient id</li>
 * <li>the patient's predicted response to treatment</li>
 * <li>the patient's response to treatment</li>
 * <li>a list of the patient's normalized protein expression level for 4776 proteins</li></ul>
 * <p>
 * Note: The patient's predicted response is calculated using their 3698th and 3259th protein levels
 *
 * @author Matt Ellis
 */
public class Patient {
    private String id;
    private String rspns;
    private String predRspns;
    private ArrayList<Double> proteins;
    private static final int PROTEIN1 = 3698;
    private static final int PROTEIN2 = 3259;

    public Patient() {
        proteins = new ArrayList<>();
    }

    /**
     * Constructor used when adding new patients from a file
     *
     * @param id       patient id
     * @param proteins patient's protein levels
     */
    public Patient(String id, ArrayList<Double> proteins) {
        this();
        setId(id);
        setProteins(proteins);
        predictResponse();
        setResponse("unk"); // new patient hasn't yet received treatment
    }

    /**
     * Constructor used when reading existing patients from a collection csv file
     *
     * @param id        patient id
     * @param rspns     patient's treatment response
     * @param predRspns patient's predicated response
     * @param proteins  patient's protein levels
     */
    public Patient(String id, String rspns, String predRspns, ArrayList<Double> proteins) {
        this();
        setId(id);
        setResponse(rspns);
        setPredict(predRspns);
        setProteins(proteins);
    }

    /**
     * Predicts patient's response to treatment
     */
    public void predictResponse() {
        predRspns = Predictor.predict(proteins.get(PROTEIN1 - 1), proteins.get(PROTEIN2 - 1));
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<Double> getProteins() {
        return proteins;
    }

    public void setProteins(ArrayList<Double> proteins) {
        this.proteins = proteins;
    }

    public void setPredict(String predRspns) {
        this.predRspns = predRspns;
    }

    public String getPredict() {
        return predRspns;
    }

    public String toString() {
        String toReturn = "[Id] " + getId() + ", [Response] " + getResponse() + ", [Predicted] " + getPredict()
                + ", [Protein " + PROTEIN1 + "] " + proteins.get(PROTEIN1 - 1) + ", [Protein " + PROTEIN2 + "] "
                + proteins.get(PROTEIN2 - 1) + "\n";
        return toReturn;
    }

}
