/**
 * Predictor.java
 * author: Dr. Mark Doderer, University of Central Arkansas
 * This predictor was generated from the original data using Weka’s J48 Decision Tree algorithm.
 * The parameters for prediction are the 3698th and 3259th protein values in that order.
 * "predDP" predicts disease progression
 * "predCR" predicts complete response
 */
package backend;

public class Predictor { 
	
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