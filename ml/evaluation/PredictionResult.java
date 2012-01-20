package ml.evaluation;

import ml.instance.Label;
import ml.instance.Prediction;

public class PredictionResult implements Comparable<PredictionResult> {

	protected String id;
	protected Prediction prediction; // use winner().identifier to get label, winner.strength() to get activation
	protected Label label; // don't forget to check polarity
	
	public PredictionResult(String id, Label label, Prediction prediction) {
		this.id = id;
		this.label = label;
		this.prediction = prediction;
	}
	
	public int compareTo(PredictionResult o) {
		return Double.compare(o.score(), score());
	}

	// checks if label matches
	public boolean correct() {
		return (prediction.winner().equals(label) && 
				(prediction.winner().isPositive() == label.isPositive()));
	}
	
	// just returns the activation value (not absolute value)
	public double score() {
		return (prediction.winner().isPositive() ? 1.0 : -1.0) * prediction.winner().strength();
	}
	
	public String toString() {
		return new String(id + " " + label + " " + prediction.winner());
	}
}
