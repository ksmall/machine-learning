package ml.evaluation;

import java.util.ArrayList;
import java.util.Collections;

import core.utility.Pair;

import ml.instance.Instance;
import ml.instance.Prediction;

// intended for ROC, AUC, PR, etc.
public class PredictionResults extends ArrayList<PredictionResult> implements Evaluator {

	private static final long serialVersionUID = 1L;

	public PredictionResults() {
		super();
	}
	
	// figure out what we want to do here
	public String analysis() {
		return null;
	}

	// TODO - figure out what we want to do here
	public double calculate() {
		return 0;
	}

	public void evaluate(Instance instance, Prediction prediction) {
		add(instance, prediction);
	}

	public void reset() {
		clear();
	}
	
	public void add(Instance instance, Prediction prediction) {
		add(new PredictionResult(instance.identifier(), instance.label(), prediction));
	}

	public ArrayList<Pair<Double>> PrecisionRecall() {
		ArrayList<Pair<Double>> result = new ArrayList<Pair<Double>>(this.size());
		Collections.sort(this);
		int total_pos = 0;
		for (PredictionResult r : this) {
			if (r.label.isPositive())
				total_pos++;
		}
		int true_pos = 0;
		int total = 0;
		for (PredictionResult r : this) {
			total++;
			if (r.label.isPositive())
				true_pos++;
			result.add(new Pair<Double>((double) true_pos / total_pos, (double) true_pos / total));
			if (true_pos == total_pos)
				break;
		}
		return result;
	}

	public ArrayList<Pair<Double>> InterpolatePR(ArrayList<Pair<Double>> pr, ArrayList<Double> points) {
		ArrayList<Pair<Double>> result = new ArrayList<Pair<Double>>();
		for (Double point : points) {
			double max = Double.NEGATIVE_INFINITY;
			for (Pair<Double> v : pr) {
				if ((v.a >= point) && (v.b > max))
					max = v.b;
			}
			result.add(new Pair<Double>(point,max));
		}
		return result;
	}

	public double MeanAveragePrecision(ArrayList<Pair<Double>> pr) {
		double result = 0.0;
		double last = 0.0;
		int count = 0;
		for (Pair<Double> v : pr) {
			if (v.a > last) {
				count++;
				result += v.b;
				last = v.a;
			}
		}
		//System.out.println(count);
		return result / count;
	}

	public ArrayList<Pair<Double>> ROC() {
		ArrayList<Pair<Double>> result = new ArrayList<Pair<Double>>(this.size());
		result.add(new Pair<Double>(0.0,0.0));
		Collections.sort(this);
		int total_pos = 0;
		int total_neg = 0;
		for (PredictionResult r : this) {
			if (r.label.isPositive())
				total_pos++;
			else
				total_neg++;
		}
		int true_pos = 0;
		int true_neg = total_neg;
		for (PredictionResult r : this) {
			if (r.label.isPositive())
				true_pos++;
			else 
				true_neg--;
			double tpr = (double) true_pos / total_pos;
			double fpr = 1.0 - ((double) true_neg / total_neg);
			result.add(new Pair<Double>(fpr, tpr));
		}
		return result;
	}

	public double AUC(ArrayList<Pair<Double>> roc) {
		double result = 0.0;
		for (int i = 1; i < roc.size(); i++) {
			double x0 = roc.get(i-1).a.doubleValue();
			double y0 = roc.get(i-1).b.doubleValue();
			double x1 = roc.get(i).a.doubleValue();
			double y1 = roc.get(i).b.doubleValue();
			result += (((y0+y1) / 2) * (x1 - x0));
		}
		return result;
	}
}
