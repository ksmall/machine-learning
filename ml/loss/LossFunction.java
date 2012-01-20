package ml.loss;

import ml.instance.Label;
import ml.instance.Prediction;

public interface LossFunction {
	
	// could argue Label, but might want to calculate norms at the such? (decided to argue this way)
	//public double loss(Instance instance, Prediction prediction);
	public double loss(Label label, Prediction prediction);
}
