package ml.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import ml.extraction.Lexicon;
import ml.instance.Label;
import ml.instance.Prediction;
import ml.loss.LossFunction;
import core.Globals;

public class LossEvaluator extends LabelEvaluator {

	// basically LossFunction is stateless
	protected LossFunction lossFunction;
	protected HashMap<String,Double> condLoss;		// on a per label basis for analysis
	protected HashMap<String,Double> condTotal;
	protected HashMap<Integer,String> labels;		// to make the labels readable
	protected double loss;
	protected int total;

	public LossEvaluator(LossFunction lossFunction, Lexicon labels) {
		this.lossFunction = lossFunction;
		this.labels = labels.invert();
		reset();
	}

	public void evaluate(Label label, Prediction prediction) {
	//public void evaluate(Feature correct, Prediction predicted) {
		double currentLoss = lossFunction.loss(label, prediction);
		String labelID = labels.get(label.identifier());
		
		// attribute loss to appropriate label (or polarity)
		if (!label.isPositive())
			labelID = new String("!" + labelID);
		addLoss(labelID, currentLoss);
		
		// calculate total loss
		loss += currentLoss;
		total++;
		
		// generate analysis field for total loss 
		addLoss("total", currentLoss);
	}

	// basically puts loss in correct element of HashMap
	protected void addLoss(String labelID, double currentLoss) {
		Double correctLoss = condLoss.get(labelID);
		if (correctLoss == null)
			correctLoss = new Double(0.0);
		correctLoss = new Double(correctLoss.doubleValue() + currentLoss);
		condLoss.put(labelID, correctLoss);
		Double correctTotal = condTotal.get(labelID);
		if (correctTotal == null)
			correctTotal = new Double(0.0);
		correctTotal = new Double(correctTotal.doubleValue() + 1.0);
		condTotal.put(labelID, correctTotal);
	}
	
	public double calculate() {
		return loss / total;
	}

	public String analysis() {
		StringBuilder result = new StringBuilder();
		ArrayList<String> keys = new ArrayList<String>(condLoss.keySet());
		Collections.sort(keys);
		//for (String cond : condLoss.keySet()) {
		for (String cond : keys) {
			result.append("[" + cond + "]=" + condLoss.get(cond) + "/" + condTotal.get(cond) + "(" + 
				Globals.formatPercent.format(condLoss.get(cond) / condTotal.get(cond)) + ") ");
		}
		return result.toString();
	}

	public void reset() {
		this.condLoss = new HashMap<String,Double>();
		this.condTotal = new HashMap<String,Double>();
		loss = 0.0;
		total = 0;		
	}

}
