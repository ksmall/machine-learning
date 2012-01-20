package ml.loss;

import ml.instance.Label;
import ml.instance.Prediction;

public class ZeroOneLoss implements LossFunction {

	public double loss(Label label, Prediction prediction) {
		return compute(label, prediction);
	}
	
	public static double compute(Label label, Prediction prediction) {
		Label winner = prediction.winner();
		return (label.equals(winner) && (label.isPositive() == winner.isPositive())) ? 0.0 : 1.0;		
	}
}
