package ml.loss;

import ml.instance.Label;
import ml.instance.Prediction;

// TODO - surely, this can be sped up -- but it is java anyway
public class SquaredError implements LossFunction {

	public double loss(Label label, Prediction prediction) {
		return compute(label, prediction);
	}
	
	public static double compute(Label label, Prediction prediction) {
		Label winner = prediction.winner();
		double predict = winner.isPositive() ? winner.strength() : -winner.strength();
		double correct = label.isPositive() ? label.strength() : -label.strength();
		return Math.pow(correct - predict, 2);
	}	
}
