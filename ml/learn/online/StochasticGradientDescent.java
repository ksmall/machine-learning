package ml.learn.online;

import org.w3c.dom.Element;

import ml.instance.Feature;
import ml.instance.FeatureVector;
import ml.instance.Instance;
import ml.instance.Label;
import ml.instance.Prediction;
import ml.instance.RealPrediction;
import core.function.Function;
import core.function.Input;
import core.function.LinearFunction;
import core.function.RealOutput;
import core.io.FactoryXML;
import core.io.SimpleWriter;
import core.text.DOM_Parser;

public class StochasticGradientDescent extends OnlineLearner<Instance> {

	protected Function function;
	protected double rate;
	
    public StochasticGradientDescent(int identifier, Function function) {
    	super(identifier);
    	this.function = function;
    	rate = 0.001;
    }
    
    public StochasticGradientDescent(Function function) {
    	this(1, function);
    }
     
    public StochasticGradientDescent(Element xml) {
    	super(xml);
    	rate = Double.parseDouble(DOM_Parser.getNodeValue(xml, "rate"));
    	// maybe shouldn't assume at position 1 (iterate and check is element node)
    	// e.g. if (nNode.getNodeType() == Node.ELEMENT_NODE)
    	function = (Function) FactoryXML.newInstance(DOM_Parser.getElement(xml, "parameters").getChildNodes().item(1));
    }
    
	public void xmlBody(SimpleWriter out) {
		super.xmlBody(out);
		out.println("<rate>" + rate + "</rate>");
		out.println("<parameters>");
		function.xml(out);
		out.println("</parameters>");
	}

    
    // really for cases where inference changes a local label
    // just LMS for time being
    // inefficiencies added for BinaryLabel (which one would think shouldn't occur really)
    @SuppressWarnings("unchecked")
	public void train(Input input, Label prediction) {
    	super.train(input, prediction);  // will advance time
    	Instance instance = (Instance) input;
    	Label winner = ((Prediction) prediction).winner();
    	double gradient = (instance.label().isPositive() ? instance.label().strength() : -instance.label().strength()) -
    		(winner.isPositive() ? winner.strength() : -winner.strength());
//		double y = instance.label().isPositive() ? instance.label().strength() : -instance.label().strength();
		//double y = instance.label().isPositive() ? 1.0 : 0.0;
//		double yhat = winner.isPositive() ? winner.strength() : -winner.strength();
//		double gradient = -y * Math.exp(-y * yhat);
    	((LinearFunction<Feature,FeatureVector>) function).add(FeatureVector.staticScale(rate * gradient, instance));
    }
    
    /*
    public void train(Input input, Function predictor) {
    	train(input, evaluate(input).winner());
    }
    */
    
    public void train(Input input) {
    	train(input, evaluate(input).winner());
    }
    
    public Prediction evaluate(Input input) {
    	double value = ((RealOutput) function.evaluate(input)).value(); //score(example);
    	return new RealPrediction(identifier, value);
    }
    
    /*
    public BinaryPrediction trainEvaluate(Instance example) {
    	double score = trainScore(example);
    	BinaryLabel label = new BinaryLabel(score >= threshold, identifier,
				Math.abs(score - threshold));
    	return new Prediction(label);
    }
    */
    
    /**
     * The abstract method for LTU promotion (for false negatives)
     * 
     * @param example	the Instance for which the weight vector is to be promoted
     */
 //   public abstract void promote(Instance instance);
    
    /**
     * The abstract method for LTU demotion (for false positives)
     * 
     * @param example	the Instance for which the weight vector is to be demoted
     */
   // public abstract void demote(Instance instance);

    // TODO - check if this actually does anything of value
    public StochasticGradientDescent copy() {
    	return (StochasticGradientDescent) clone();
    }
}
