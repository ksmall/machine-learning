package ml.learn.online;

import org.w3c.dom.Element;

import ml.instance.BinaryPrediction;
import ml.instance.Feature;
import ml.instance.FeatureVector;
import ml.instance.Prediction;
import core.function.Input;
import core.function.LinearFunction;
import core.function.RealOutput;
import core.io.SimpleWriter;
import core.text.DOM_Parser;

public abstract class PrimalLTU extends MarginErrorDriven {

	/** addition of a bias element (defaults to true) **/
	public boolean bias;
	protected double biasValue;
	
    public PrimalLTU(int identifier, LinearFunction<Feature,FeatureVector> function) {
    	super(identifier, function);
    	this.bias = true;
    	this.biasValue = 0.0;
    }
    
    /**
     * Default constructor
     */
    public PrimalLTU(LinearFunction<Feature,FeatureVector> function) {
    	this(1, function);
    }

    /*
    protected PrimalLTU(PrimalLTU ltu) {
    	//super(ltu.identifier, ltu.iterations, ltu.shuffle, ltu.threshold, ltu.positiveGamma, ltu.negativeGamma);
    	super(ltu);
    	w = ltu.w.deepCopy();
    }
    */
    
    public PrimalLTU(Element xml) {
    	super(xml);
    	bias = Boolean.parseBoolean(DOM_Parser.getNodeValue(xml, "bias"));
    	if (bias)
    		biasValue = Double.parseDouble(DOM_Parser.getNodeValue(xml, "biasValue"));
    }

	public void xmlBody(SimpleWriter out) {
		out.println("<bias>" + bias + "</bias>");
		if (bias)
			out.println("<biasValue>" + biasValue + "</biasValue>");
		super.xmlBody(out);
	}
    
    /**
     * Returns the dot product of the weight vector with a given Instance.
     * 
     * @param example	the Instance which will be used to calculate the dot product with
     * 						the weight vector
     * @return	the resulting dot product
     */
    /*
    public double score(Instance example) {
    	return w.dot(((SingleInstance) example).features);
    }
    
    public double trainScore(Instance example) {
    	return score(example);
    }
    */
    
    @SuppressWarnings("unchecked")
	public void reset() {
    	super.reset();
    	((LinearFunction<Feature,FeatureVector>) function).clear(); // = new SparseWeightVector();
    	biasValue = 0.0;
    }

    // overrides just for the purposes of the explicit bias element (under the "LTU" hierarchy)
    public Prediction evaluate(Input input) {
    	//double value = ((RealOutput) function.evaluate(input)).value(); //score(example);
    	double value = ((RealOutput) function.evaluate(input)).value() + (bias ? biasValue : 0.0);
    	return new BinaryPrediction(value >= threshold, identifier, Math.abs(value - threshold));
    }
    
    /*
    @SuppressWarnings("unchecked")
	public PrimalLTU copy() {
    	PrimalLTU copy = (PrimalLTU) clone();
    	copy.function = ((LinearFunction<Feature,FeatureVector>) this.function).copy();
    	return copy;
    }

    @SuppressWarnings("unchecked")
	public PrimalLTU<T> deepCopy() {
    	PrimalLTU<T> copy = (PrimalLTU<T>) clone();
    	copy.function = ((LinearFunction<Feature,FeatureVector>) this.function).deepCopy();
		return copy;
    }	
    */
}
