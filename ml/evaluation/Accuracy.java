package ml.evaluation;

import java.util.ArrayList;
import java.util.Collections;

import core.Globals;

import ml.extraction.Lexicon;
import ml.loss.ZeroOneLoss;

// TODO - verify multiclass
public class Accuracy extends LossEvaluator {

	public Accuracy(Lexicon labels) {
		super(new ZeroOneLoss(), labels);
	}
	
	public double calculate() {
		return 1.0 - super.calculate();
	}
	
	public String analysis() {
		StringBuilder result = new StringBuilder();
		ArrayList<String> keys = new ArrayList<String>(condLoss.keySet());
		Collections.sort(keys);
		//for (String cond : condLoss.keySet()) {
		for (String cond : keys) {
			double complement = condTotal.get(cond).doubleValue() - condLoss.get(cond).doubleValue();
			result.append("[" + cond + "]=" + complement + "/" + condTotal.get(cond) + "(" + 
					Globals.formatPercent.format(complement / condTotal.get(cond)) + ") ");
		}
		return result.toString();
	}


}
