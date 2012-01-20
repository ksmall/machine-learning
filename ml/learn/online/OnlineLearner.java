package ml.learn.online;

import java.util.HashSet;

import ml.data.Data;
import ml.data.NestedData;
import ml.instance.Label;
import ml.learn.Learner;

import org.w3c.dom.Element;

import core.Identifiable;
import core.function.Input;
import core.io.SimpleWriter;
import core.sample.Sample;
import core.text.DOM_Parser;

public abstract class OnlineLearner<T extends Input & Identifiable> extends Learner<T> implements Online {

	public int iterations;
	public boolean shuffle;
	protected int time;
	
	public OnlineLearner(int identifier) {
		super(identifier);
		iterations = 1;
		shuffle = false;
		time = 0;
	}
	
	public OnlineLearner() {
		this(1);
	}
	
	/*
	public OnlineLearner(OnlineLearner l) {
		this(l.identifier);
		this.time = l.time;
	}
	*/

    public OnlineLearner(Element xml) {
    	super(xml);
    	iterations = Integer.parseInt(DOM_Parser.getNodeValue(xml, "iterations"));
    	shuffle = Boolean.parseBoolean(DOM_Parser.getNodeValue(xml, "shuffle"));
    	time = Integer.parseInt(DOM_Parser.getNodeValue(xml, "time"));
    }
	
	public void xmlBody(SimpleWriter out) {
		super.xmlBody(out);
		out.println("<iterations>" + iterations + "</iterations>");
		out.println("<shuffle>" + shuffle + "</shuffle>");
		out.println("<time>" + time + "</time>");
	}
	
    public void train(Input input, Label prediction) {
    	advanceTime();
    }
    
    /*
    public void train(Input input, Function predictor) {
    	advanceTime();
    }
	*/
    
    public void train(Input input) {
    	advanceTime();
    }
    
    public void advanceTime() {
    	time++;
    }
    
    // TODO - something with weighted sample, or maybe sample in general
    /**
     * Trains an online algorithm for an entire Sample by presenting each Instance in the
     * Sample once to the OnlineLearner.
     * 
     * @param examples	the Sample of examples for training
     */
    /*
    public void train(AccessibleSample<T> input) {
    	System.out.println("here1");
    	staticTrain(this, input, iterations, shuffle);
    }
	*/
    
/*    
    public void train(Data<?> input) {
    	System.out.println("here2");
//    	staticTrain(this, input, iterations, shuffle);
    	//Online online = (Online) learner;
    	//input.draw();
    	for (int i = 0; i < iterations; i++) {
    		if (shuffle)
    			//Collections.shuffle(input);
    			input.shuffle();
    		for (Input instance : input)
    			train(instance);
    	}
   		finish();    	
    }
*/    
    
    /*
    public void train(WeightedSample<T> input) {
    	staticTrain(this, input, iterations);
    }
     */
    
    public void train(Sample<T> input) {
    	/*
    	System.out.println("here3 " + input.getClass().getName());
    	Class<?>[] inter = input.getClass().getInterfaces();
    	for (int i = 0; i < inter.length; i++)
    		System.out.println(inter[i].getClass().getName());
    	//staticTrain(this, input, iterations);
    	//Online online = (Online) learner;
    	Input instance = null;
    	for (int i = 0; i < iterations; i++) {
    		input.reset();
    		while ((instance = input.next()) != null)
    			train(instance);
    	}
   		finish();    	
   		*/
    	staticTrain(this, input, iterations, shuffle);
    }
    
    // TODO -- I am really unhappy about this, but want to make sure that it works
    public static void staticTrain(Learner<? extends Input> learner, Sample<? extends Input> input, 
    		int iterations, boolean shuffle) {
    	String name = input.getClass().getName();
    	Online online = (Online) learner;
    	HashSet<String> valid = new HashSet<String>();
    	valid.add("ml.data.Data");
    	valid.add("ml.data.NestedData");
    	if (!valid.contains(name)) {
    		System.out.println("Online training doesn't work for " + name);
    		System.exit(1);
    	}
    	else if (name.equals("ml.data.Data")) {
    		((Data<?>) input).draw();		// in this case, draw is used only to print debug if desired
        	for (int i = 0; i < iterations; i++) {
        		if (shuffle)
        			((Data<?>) input).shuffle();
        		for (Input instance : ((Data<?>) input))
        			online.train(instance);
        	}
       		learner.finish();    	
    	}
    	else if (name.equals("ml.data.NestedData")) {
        	((NestedData) input).draw();
        	for (int i = 0; i < iterations; i++) {
        		if (shuffle)
        			((NestedData) input).shuffle();
        		for (Input instance : ((NestedData) input))
        			online.train(instance);
        	}
       		learner.finish();    	
    	}
    }
    
    /*
    public static void staticTrain(Learner<? extends Input> learner, AccessibleSample<? extends Input> input, 
    		int iterations, boolean shuffle) {
    	Online online = (Online) learner;
    	for (int i = 0; i < iterations; i++) {
    		if (shuffle)
    			//Collections.shuffle(input);
    			input.shuffle();
    		for (Input instance : input)
    			online.train(instance);
    	}
   		learner.finish();    	
    }
    */

    /*
    public static void staticTrain(Learner<? extends Input> learner, NestedData input, 
    		int iterations, boolean shuffle) {
    	Online online = (Online) learner;
    	input.draw();
    	for (int i = 0; i < iterations; i++) {
    		if (shuffle)
    			//Collections.shuffle(input);
    			input.shuffle();
    		for (Input instance : input)
    			online.train(instance);
    	}
   		learner.finish();    	
    }
    */
    
    /*
    public static void staticTrain(Learner<? extends Input> learner, WeightedSample<? extends Input> input, int iterations) {
    	Online online = (Online) learner;
    	for (int i = 0; i < iterations; i++) {
    		online.train(input.draw());
    	}
   		learner.finish();    	
    }
	*/
    
    /*
    public static void staticTrain(Learner<? extends Input> learner, Sample<? extends Input> input, int iterations) {
    	Online online = (Online) learner;
    	Input instance = null;
    	for (int i = 0; i < iterations; i++) {
    		input.reset();
    		while ((instance = input.next()) != null)
    			online.train(instance);
    	}
   		learner.finish();    	
    }
    */
    
    public void reset() {
    	super.reset();
    	time = 0;
    }
    
    /*
    public OnlineLearner copy() {
    	return (OnlineLearner) clone();
    }
    */
	
}
