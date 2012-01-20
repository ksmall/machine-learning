package ml.learn.online;

import org.w3c.dom.Element;

import ml.instance.Feature;
import ml.instance.FeatureVector;
import ml.instance.Instance;
import core.function.LinearFunction;
import core.io.SimpleWriter;
import core.text.DOM_Parser;

public class Perceptron extends PrimalLTU {

	/** the learning rate for promotions */
    public double promotionRate;
    /** the learning rate for demotions */
    public double demotionRate;
	//public Pair<Double> rate; // promotion is a, demotion is b
    
    public Perceptron(int identifier, LinearFunction<Feature,FeatureVector> function) {
    	super(identifier, function);
    	//this.rate = new Pair<Double>(0.1, 0.1);
    	this.promotionRate = 0.1;
    	this.demotionRate = 0.1;
    }

    public Perceptron(LinearFunction<Feature,FeatureVector> function) {    
    	this(1, function);
    }
    
    /*
    public Perceptron(Perceptron p) { 
    	super(p);
    	this.promotionRate = p.promotionRate;
    	this.demotionRate = p.demotionRate;    	
    }
    */
    
    public Perceptron(Element xml) {
    	super(xml);
    	//String[] r = DOM_Parser.getNodeValue(xml, "rate").split(",");
    	//rate = new Pair<Double>(Double.parseDouble(r[0]), Double.parseDouble(r[1]));
    	promotionRate = Double.parseDouble(DOM_Parser.getNodeValue(xml, "promotionRate"));
    	demotionRate = Double.parseDouble(DOM_Parser.getNodeValue(xml, "demotionRate"));
    }
    
	public void xmlBody(SimpleWriter out) {
		//out.println("<rate>" + rate.a + "," + rate.b + "</rate>");
		out.println("<promotionRate>" + promotionRate + "</promotionRate>");
		out.println("<demotionRate>" + demotionRate + "</demotionRate>");
		super.xmlBody(out);
	}
    
    /**
     * Promotes the weight vector (false negative) for the given example
     * 
     * @param instance	the given instance
     */
    @SuppressWarnings("unchecked")
	public void promote(Instance instance) {
    	//System.out.println("Promoting " + identifier);
    	//System.out.println("b: " + w);
    	((LinearFunction<Feature,FeatureVector>) function).add(FeatureVector.staticScale(promotionRate, instance));
    	if (bias) 
    		biasValue += promotionRate;
    	//System.out.println("a: " + w);
    }

    /**
     * Demotes the weight vector (false positive) for the given example
     * 
     * @param example	the given example
     */
    @SuppressWarnings("unchecked")
	public void demote(Instance instance) {
    	//System.out.println("Demoting " + identifier);
    	//System.out.println("b: " + w);
    	((LinearFunction<Feature,FeatureVector>) function).add(FeatureVector.staticScale(-demotionRate, instance));
    	if (bias) 
    		biasValue -= demotionRate;
    	//System.out.println("a: " + w);
    }

    /*
    public Perceptron copy() {
    	Perceptron copy = (Perceptron) clone();
    	copy.w = this.w.copy();
    	return copy;
    }
    
    public Perceptron deepCopy() {
    	return new Perceptron(this);
    }
	*/
}
