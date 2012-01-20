package experiment;

import java.util.HashMap;

import ml.data.Data;
import ml.evaluation.Evaluator;
import ml.extraction.Lexicon;
import ml.instance.Instance;
import ml.instance.Label;
import ml.learn.Learner;
import core.function.Input;
import core.utility.Histogram;

// TODO - decide where this whole enterprise goes
public class Experiment {

	protected HashMap<Integer,String> labels;

	public Experiment(Lexicon labelLexicon) {
		labels = labelLexicon.invert();
	}

	// assumes a train/test split already determined (could add second as sugar)
	public double run(Learner<Instance> learner, Evaluator evaluator,
			Data<Instance> data, boolean analysis) {
		learner.reset();
		//data.generateSplit(pTest);
		data.training();
		/*
		if (analysis) {
			Histogram<String> trainDist = labelDistribution(data, labels);
			System.out.println("Training Dist: " + trainDist + "(" + trainDist.total() + ")");
		}
		*/
		learner.train(data);
		data.testing();
		if (analysis) {
			Histogram<String> testDist = labelDistribution(data, labels);
			System.out.println("Testing Dist: " + testDist + "(" + testDist.total() + ")");
		}
		for (Instance instance : data) {
			//System.out.println(learner.evaluate(instance));
			evaluator.evaluate(instance, learner.evaluate(instance));
		}
		if (analysis)
			System.out.println(evaluator.analysis());
		return evaluator.calculate();		
	}
			
	// eventually analysis to have stddev 
	public double crossValidation(Learner<Instance> learner, Evaluator evaluator,
			Data<Instance> data, boolean analysis) {
		double total = 0.0;
		for (int i = 0; i < data.splits(); i++) {
			evaluator.reset();
			total += crossValidation(learner, evaluator, data, i, analysis);
		}
		return total / data.splits();
	}
	
	// presumes fold generation has already occurred
	public double crossValidation(Learner<Instance> learner, Evaluator evaluator,
			Data<Instance> data, int fold, boolean analysis) {
		learner.reset();
		//data.generateSplit(pTest);
		data.training(fold);
		if (analysis) {
			System.out.println("*** Fold " + fold + " ***");
			//Histogram<String> trainDist = labelDistribution(data, labels);
			//System.out.println("Training Dist: " + trainDist + "(" + trainDist.total() + ")");
		}
		learner.train(data);
		data.testing(fold);
		if (analysis) {
			Histogram<String> testDist = labelDistribution(data, labels);
			System.out.println("Testing Dist: " + testDist + "(" + testDist.total() + ")");
		}
		for (Input instance : data) {
			evaluator.evaluate((Instance) instance, learner.evaluate((Instance) instance));
		}
		if (analysis)
			System.out.println(evaluator.analysis());
		return evaluator.calculate();			
	}
	
	
	// the case where we have two samples (sort of old school code base)
	/*
	public double run(Learner<Input> learner, Evaluator evaluator,
			Sample<Input> train, IterableSample<Input> test, boolean analysis) {
		if (analysis) {
			Histogram<String> trainDist = labelDistribution(train, labels);
			System.out.println("Training Dist: " + trainDist + "(" + trainDist.total() + ")");
			Histogram<String> testDist = labelDistribution(test, labels);
			System.out.println("Testing Dist: " + testDist + "(" + testDist.total() + ")");
		}
		learner.reset();
		train.reset(); // TODO -- make sure that we actually want to do this
		learner.train(train);
		test.reset();
		for (Input instance : test) {
			evaluator.evaluate((Instance) instance, learner.evaluate((Instance) instance));
		}
		return evaluator.calculate();
	}
	*/
	
	//public static Histogram<String> labelDistribution(Sample<Input> sample, HashMap<Integer,String> labels) {
	// TODO - not quite sure why this isn't done with a for loop 
	public static Histogram<String> labelDistribution(Data<Instance> data, HashMap<Integer,String> labels) {
		Histogram<String> result = new Histogram<String>();
		data.reset();
		Instance e = null;
		while ((e = (Instance) data.next()) != null) {
			Label label = e.label();
			String id = labels.get(label.identifier());
			if (!label.isPositive())
				id = new String("!" + id);
			result.put(id);
		}
		return result;
	}
}
