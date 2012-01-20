package ml.learn.online;

import ml.instance.BinaryPrediction;
import ml.instance.Feature;
import ml.instance.FeatureVector;
import ml.instance.Label;
import ml.instance.Prediction;

import org.w3c.dom.Element;

import core.function.Input;
import core.function.LinearFunction;
import core.function.RealOutput;
import core.io.FactoryXML;
import core.io.SimpleWriter;
import core.text.DOM_Parser;

public class DirectAveragedPerceptron extends Perceptron {

    /** the final hypothesis **/
    protected LinearFunction<Feature,FeatureVector> s;
    protected double sBiasValue;
    
    public DirectAveragedPerceptron(int identifier, LinearFunction<Feature,FeatureVector> function) {
    	super(identifier, function);
    	s = function.deepCopy();
    	sBiasValue = 0.0;
    }
    
    public DirectAveragedPerceptron(LinearFunction<Feature,FeatureVector> function) {
    	this(1, function);
    }

    /*
    public AveragedPerceptron(AveragedPerceptron p) {
    	super(p);
    	this.s = p.s.deepCopy();
    }
    */
    
    @SuppressWarnings("unchecked")
	public DirectAveragedPerceptron(Element xml) {
    	super(xml);
    	// maybe shouldn't assume at position 1 (iterate and check is element node)
    	s =  (LinearFunction<Feature, FeatureVector>) FactoryXML.
    			newInstance(DOM_Parser.getElement(xml, "averaged_parameters").getChildNodes().item(1));
    	if (bias)
    		sBiasValue = Double.parseDouble(DOM_Parser.getNodeValue(xml, "averaged_biasValue"));
    }
    
	public void xmlBody(SimpleWriter out) {
		super.xmlBody(out);
		out.println("<averaged_parameters>");
		s.xml(out);
		out.println("</averaged_parameters>");
		if (bias)
			out.println("<averaged_biasValue>" + sBiasValue + "</averaged_biasValue>");
	}
    
    public Prediction evaluate(Input input) {
    	double value = ((RealOutput) s.evaluate(input)).value() + (bias ? sBiasValue : 0.0); //score(example);
    	return new BinaryPrediction(value >= threshold, identifier, Math.abs(value - threshold));
    }
    
    // necessary to ensure updates are made from evaluation using w
    public void train(Input input) {
    	train(input, super.evaluate(input).winner());
    }
    
    public void reset() {
    	super.reset();
    	s.clear();
    	sBiasValue = 0.0;
    }
    
    @SuppressWarnings("unchecked")
	public void train(Input input, Label prediction) {
    	super.train(input, prediction);
    	s.add((LinearFunction<Feature,FeatureVector>) function);
    	sBiasValue += biasValue;
    }
    
    // TODO - give explanation for "+ 1.0" other than "matches shortcut method" (don't actually know)
	public void finish() {
    	super.finish();
    	double local_time = time + 1.0;
    	s.scale(1.0 / local_time);
    	sBiasValue /= local_time;
    }
    
    /*
    public AveragedPerceptron copy() {
    	AveragedPerceptron copy = (AveragedPerceptron) super.copy();
    	copy.s = this.s.copy();
    	return copy;
    }
    
    public AveragedPerceptron deepCopy() {
    	return new AveragedPerceptron(this);
    }
	*/
}
