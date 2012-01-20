package ml.evaluation;

import ml.instance.Instance;
import ml.instance.Label;
import ml.instance.Prediction;

// NB: assuming binary classification here
public class ConfusionMatrix implements Evaluator {

	protected int TP;
	protected int TN;
	protected int FP;
	protected int FN;
	
	public ConfusionMatrix() {
		reset();
	}
	
	public void evaluate(Instance instance, Prediction prediction) {
		Label correct = instance.label();
		Label predicted = prediction.winner();
		if (correct.equals(predicted)) {
			if (correct.isPositive() && predicted.isPositive())
				TP++;
			else if (!correct.isPositive() && !predicted.isPositive())
				TN++;
			else if (correct.isPositive() && !predicted.isPositive())
				FN++;
			else if (!correct.isPositive() && predicted.isPositive())
				FP++;
			else {
				System.out.println("Completely confused: " + correct + "," + predicted);
				System.exit(1);
			}
		}
		else {
			System.out.println("Not a binary label: " + correct + "," + predicted);
			System.exit(1);
		}
	}

	// uses accuracy as a default
	public double calculate() {
		return ((double) TP + TN) / ((double) TP + TN + FP + FN);
	}

	public void reset() {
		TP = 0;
		TN = 0;
		FP = 0;
		FN = 0;
	}

	public String analysis() {
		return "TP:" + TP + " TN:" + TN + " FP:" + FP + " FN:" + FN;
	}

}
