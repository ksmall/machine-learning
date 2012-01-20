package ml.parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ml.instance.Feature;
import ml.instance.FeatureReal;
import ml.instance.FeatureVector;

import org.w3c.dom.Element;

import core.function.Input;
import core.function.LinearFunction;
import core.function.RealOutput;
import core.io.SimpleWriter;

public class SparseLinear extends HashMap<Integer,Double> implements LinearFunction<Feature,FeatureVector> {

	private static final long serialVersionUID = 1L;

	public SparseLinear() {
		super();
	}
	
    /**
     * The copy constructor
     * TODO - double check this is a "deep" copy (probably is due to Double)
     * 
     * @param v		the SparseWeightVector to be copied
     */
    public SparseLinear(SparseLinear v) {
    	this();
    	putAll(v);
    }

    public SparseLinear(Element xml) {
    	this();
    	String[] f = xml.getFirstChild().getTextContent().trim().split("\n");
    	for (int i = 0; i < f.length; i++)
    		put(Feature.parseFeature(f[i]));
    }
    
    public void xml(SimpleWriter out) {
    	ArrayList<Integer> ids = new ArrayList<Integer>(keySet());
    	Collections.sort(ids);
		out.println("<" + getClass().getName() + ">");
		for (Integer id : ids)
			out.println(new FeatureReal(id, get(id)));
		out.println("</" + getClass().getName() + ">");
    }

    /**
     * Adds a FeatureVector to the current sparse weight vector (for Perceptron-like
     * learning algorithms)
     * 
     * @param vector	the input feature vector to be added to the weight vector
     */
    public void add(FeatureVector vector, double scale) {
    	for (Feature f : vector) {
    		Integer id = new Integer(f.identifier());
    		double weight = f.strength() * scale;
    		if (containsKey(id))
    			weight += get(id).doubleValue();
    		put(id, new Double(weight));
    	}
    }
    
    public void add(FeatureVector vector) {
    	add(vector, 1.0);
    }
    
	public void add(LinearFunction<Feature,FeatureVector> parameters, double scale) {
    	for (Feature f : parameters) {
    		Integer id = new Integer(f.identifier());
    		double weight = f.strength() * scale;
    		if (containsKey(id))
    			weight += get(id).doubleValue();
    		put(id, new Double(weight));
    	}
	}
	
	public void add(LinearFunction<Feature,FeatureVector> parameters) {
    	add(parameters, 1.0);
	}
    
	public RealOutput evaluate(FeatureVector input) {
		return new RealOutput(dot(input));
	}
	
	public double dot(FeatureVector input) {
    	double result = 0.0;
    	for (Feature f : input) {
    		Integer id = new Integer(f.identifier());
    		if (containsKey(id))
    			result += get(id).doubleValue() * f.strength();
    	}
    	return result;	
    }

	public Double get(int id) {
		//Double value = super.get(id);
		//System.out.println(id + "," + value);
    	//return (value == null) ? null : value.doubleValue();
		return super.get(id);
	}
	
	public Iterator<Feature> iterator() {
		
		return new Iterator<Feature>() {

			protected Iterator<Integer> it = keySet().iterator();
			
			public boolean hasNext() {
				return it.hasNext();
			}

			public Feature next() {
				if (!it.hasNext())
					throw new NoSuchElementException();
				Integer identifier = it.next();
				return new FeatureReal(identifier, get(identifier));				
			}

			public void remove() {
				it.remove();
			}

		};
	}

	public double norm(double p) {
    	double result = 0.0;
    	for (Feature f : this)
    		result += Math.pow(Math.abs(f.strength()), p);
    	return Math.pow(result, (1.0 / p));
	}

	public Double put(int key, double value) {
    	return super.put(key, value); 
    }

	public Double put(Feature f) {
		return put(f.identifier(), f.strength());
	}
	
	public void scale(double scalar) {
    	for (Integer id : keySet()) {
    		double value = scalar * get(id);
    		put(id, new Double(value));
    	}
	}

	public RealOutput evaluate(Input input) {
		return new RealOutput(dot((FeatureVector) input));
	}

	public LinearFunction<Feature,FeatureVector> copy() {
		return new SparseLinear(this);
	}

	public LinearFunction<Feature,FeatureVector> deepCopy() {
		return copy();
	}
}
