package backend;

/**
 * This class provides a method for predicting a patients response to treatment.
 *
 * @author Dr. Mark Doderer - University of Central Arkansas
 * @author Matt Ellis
 */
public class Predictor {

    /**
     * This treatment response predictor was generated using the Weka J48 Decision Tree
     * algorithm on the original patient data set provided by researchers at the
     * University of Arkansas for Medical Sciences. It was discovered that proteins
     * 3698 and 3259 could be used to predict a patient's response to treatment.
     *
     * @param p1 patient's expression level of protein 3698
     * @param p2 patient's expression level of protein 3259
     * @return a String indicating the predicted response. "predDp" meaning disease
     * progression or "predCR" meaning complete response
     */
    public static String predict(double p1, double p2) {
        if (p1 <= 20.903959) {
            return "predDP";
        } else {
            if (p2 <= 22.058599) {
                return "predCR";
            } else {
                return "predDP";
            }
        }
    }
} 