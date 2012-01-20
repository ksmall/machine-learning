package ml.learn.online;

import ml.instance.BinaryPrediction;
import ml.instance.Instance;
import ml.instance.Label;
import ml.instance.Prediction;

import org.w3c.dom.Element;

import core.function.Function;
import core.function.Input;
import core.function.RealOutput;
import core.io.FactoryXML;
import core.io.SimpleWriter;
import core.text.DOM_Parser;

public abstract class MarginErrorDriven extends OnlineLearner<Instance> {

	protected Function function;
	/** the threshold which determines positive or negative classification */
    public double threshold;
    /** the positive side thickness of the separator used to determine updates */
    public double pThickness;
    /** the negative side thickness of the separator used to determine updates */
    public double nThickness;
    //public Pair<Double> thickness;  // a is positive, b is negative
        
    public MarginErrorDriven(int identifier, Function function) {
    	super(identifier);
    	this.function = function;
    	this.threshold = 0.0;
    	this.pThickness = 0.0;
    	this.nThickness = 0.0;
    }
    
    public MarginErrorDriven(Function function) {
    	this(1, function);
    }
    
    /*
    protected LinearThresholdUnit(LinearThresholdUnit ltu) {
    	this(ltu.identifier, ltu.iterations, ltu.shuffle, ltu.threshold, ltu.positiveGamma, ltu.negativeGamma);
    }
    */

    public MarginErrorDriven(Element xml) {
    	super(xml);
    	//String[] t = DOM_Parser.getNodeValue(xml, "thickness").split(",");
    	//thickness = new Pair<Double>(Double.parseDouble(t[0]), Double.parseDouble(t[1]));
    	threshold = Double.parseDouble(DOM_Parser.getNodeValue(xml, "threshold"));
    	pThickness = Double.parseDouble(DOM_Parser.getNodeValue(xml, "pThickness"));
    	nThickness = Double.parseDouble(DOM_Parser.getNodeValue(xml, "nThickness"));
    	// maybe shouldn't assume at position 1 (iterate and check is element node)
    	// e.g. if (nNode.getNodeType() == Node.ELEMENT_NODE)
    	function = (Function) FactoryXML.newInstance(DOM_Parser.getElement(xml, "parameters").getChildNodes().item(1));
    }
    
	public void xmlBody(SimpleWriter out) {
		super.xmlBody(out);
		out.println("<threshold>" + threshold + "</threshold>");
		out.println("<pThickness>" + pThickness + "</pThickness>");
		out.println("<nThickness>" + nThickness + "</nThickness>");
		//out.println("<thickness>" + thickness.a + "," + thickness.b + "</thickness>");
		out.println("<parameters>");
		function.xml(out);
		out.println("</parameters>");
	}
    
    // really for cases where inference changes a local label
    public void train(Input input, Label prediction) {
    	super.train(input, prediction);  // will advance time
    	Instance instance = (Instance) input;
    	
    	if (instance.label().isPositive() != prediction.isPositive()) {  // a labeling mistake
    		//System.out.println("making update");
    		if (instance.label().isPositive())
    			promote(instance);
    		else 
    			demote(instance);
    	}
    	else if (instance.label().isPositive() && (prediction.strength() < pThickness))
    		promote(instance);
    	else if (!instance.label().isPositive() && (prediction.strength() < nThickness))
    		demote(instance);
    }
    
    // TODO -- figure out what the deal is with predictor (as this presently makes no sense)
    /*
    public void train(Input input, Function predictor) {
    	train(input, evaluate(input).winner());
    }
    */
    
    public void train(Input input) {
    	//train(input, function);
    	train(input, evaluate(input).winner());
    }
    
    public Prediction evaluate(Input input) {
    	//double value = ((RealOutput) function.evaluate(input)).value(); //score(example);
    	double value = ((RealOutput) function.evaluate(input)).value();
    	return new BinaryPrediction(value >= threshold, identifier, Math.abs(value - threshold));
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
    public abstract void promote(Instance instance);
    
    /**
     * The abstract method for LTU demotion (for false positives)
     * 
     * @param example	the Instance for which the weight vector is to be demoted
     */
    public abstract void demote(Instance instance);
    
    // TODO - check if this actually does anything of value
    public MarginErrorDriven copy() {
    	return (MarginErrorDriven) clone();
    }
}
