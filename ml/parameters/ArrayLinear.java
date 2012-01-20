package ml.parameters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Element;

import ml.instance.Feature;
import ml.instance.FeatureReal;
import ml.instance.FeatureVector;
import core.function.Input;
import core.function.LinearFunction;
import core.function.Output;
import core.function.RealOutput;
import core.io.SimpleWriter;

public class ArrayLinear implements LinearFunction<Feature,FeatureVector> {

	protected double[] values;
	
	public ArrayLinear(int length) {
		values = new double[length];
	}

	public ArrayLinear(ArrayLinear a) {
		this(a.values.length);
		for (int i = 0; i < a.values.length; i++)
			values[i] = a.values[i];
	}
	
    public void xml(SimpleWriter out) {
		out.println("<" + getClass().getName() + ">");
		for (Feature f : this)
			out.println(f);
		out.println("</" + getClass().getName() + ">");
    }
	
    // assuming in order (as is done in general) -- note potential use in training is Sparse and eval in Array
    public ArrayLinear(Element xml) {
    	String[] f = xml.getFirstChild().getTextContent().trim().split("\n");
    	values = new double[f.length];
    	for (int i = 0; i < f.length; i++)
    		values[i] = Feature.parseFeature(f[i]).strength();
    }
    
	// replicate for *slight* efficiency in removing multiplication
	public void add(FeatureVector vector) {
    	for (Feature f : vector)
    		values[f.identifier()] += f.strength();
	}

	public void add(FeatureVector vector, double scale) {
    	for (Feature f : vector)
    		values[f.identifier()] += f.strength() * scale;
	}

	public void add(LinearFunction<Feature,FeatureVector> parameters) {
    	for (Feature f : parameters)
    		values[f.identifier()] += f.strength();
	}

	public void add(LinearFunction<Feature,FeatureVector> parameters, double scale) {
    	for (Feature f : parameters)
    		values[f.identifier()] += f.strength() * scale;
	}

	public double dot(FeatureVector input) {
    	double result = 0.0;
    	for (Feature f : input)
    		result += get(f.identifier()) * f.strength();
    	return result;
	}

	public RealOutput evaluate(FeatureVector input) {
		return new RealOutput(dot(input));
	}

	public Double get(int id) {
		return (id < values.length) ? values[id] : 0.0;
	}

	// avoid using within this class just due to creating of Feature objects
	public Iterator<Feature> iterator() {
		return new Iterator<Feature>() {

			int i = 0;
			
			public boolean hasNext() {
				return (i < values.length) ? true : false;
			}

			public Feature next() {
				if (!hasNext())
					throw new NoSuchElementException();
				return new FeatureReal(i, values[i++]);				
			}

			// just set to 0 -- never used and defeats purpose
			// known lack of error checking on 0
			public void remove() {
				values[i-1] = 0.0;
			}

		};
	}

	public double norm(double p) {
    	double result = 0.0;
    	for (int i = 0; i < values.length; i++)
    		result += Math.pow(Math.abs(values[i]), p);
    	return Math.pow(result, (1.0 / p));
	}

	// no error checking
	public Double put(int key, double value) {
		values[key] = value;
		return new Double(value);
	}

	public void scale(double scalar) {
    	for (int i = 0; i < values.length; i++)		
    		values[i] *= scalar;
	}	

	public Output evaluate(Input input) {
		return new RealOutput(dot((FeatureVector) input));	}

	// sort of puzzling as the same length might be odd
	public void clear() {
		Arrays.fill(values, 0.0);
	}

	public double max(HashSet<Integer> exclusion, double threshold, boolean above) {
		System.out.println("ArrayLinear: not implmented");
		System.exit(1);
		return 0.0;
	}

	public double mean(HashSet<Integer> exclusion, double threshold, boolean above) {
		System.out.println("ArrayLinear: not implmented");
		System.exit(1);
		return 0.0;
	}

	public LinearFunction<Feature, FeatureVector> copy() {
		return new ArrayLinear(this);
	}

	public LinearFunction<Feature, FeatureVector> deepCopy() {
		return copy();
	}
}
