package ml.evaluation;

import ml.instance.Instance;
import ml.instance.Label;
import ml.instance.Prediction;

public abstract class LabelEvaluator implements Evaluator {

	public void evaluate(Instance instance, Prediction prediction) {
		evaluate(instance.label(), prediction);
	}
	
	public abstract void evaluate(Label label, Prediction prediction);
}
