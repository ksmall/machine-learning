package ml.evaluation;

import ml.instance.Instance;
import ml.instance.Prediction;

public interface Evaluator {

	public void evaluate(Instance instance, Prediction prediction);
	
	// has to be able to calculate at the moment and continue getting new data
	public double calculate();
	
	public void reset();
	
	public String analysis();
	
}
