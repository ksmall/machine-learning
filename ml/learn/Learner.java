package ml.learn;

import java.lang.reflect.Field;
import java.util.HashMap;

import ml.instance.Prediction;

import org.w3c.dom.Element;

import core.Copyable;
import core.function.Function;
import core.function.Input;
import core.io.SimpleWriter;
import core.io.XML;
import core.sample.Sample;
import core.text.DOM_Parser;

public abstract class Learner<T extends Input> implements Cloneable, Copyable<Learner<T>>, Function, XML {

	/** 
	 * An identifier for the Learner; note  by default, this is set to 1 meaning
	 * a positive classification for a binary classification.  However, this will
	 * generally be reset or irrelevant depending on the learning algorithm.
	 */
    protected int identifier;
    protected boolean finished; 	// indicates that this is an "up-to-date" classifier

    protected Learner(int identifier) {
    	this.identifier = identifier;
    	finished = false;
    }
    
    protected Learner() {
    	this(1);
    }
    
    // TODO -- eventually do this with reflection
    public Learner(Element xml) {
    	identifier = Integer.parseInt(DOM_Parser.getNodeValue(xml, "identifier"));
    	finished = Boolean.parseBoolean(DOM_Parser.getNodeValue(xml, "finished"));
    }

    // TODO - actually finish this cleanly
    public void setParams(HashMap<String,Object> params) {
    	for (String param : params.keySet()) {
    		try {
    			Field f = getClass().getField(param);
    			String type = f.getType().getSimpleName();
    		//	System.out.println("name:" + type);
    			if (type.equals("int"))
    				f.setInt(this, (Integer) params.get(param));
    			else if (type.equals("double"))
    				f.setDouble(this, (Double) params.get(param));
    			else {
    				System.out.println("unknown parameters type: " + type + " for " + param);
    				System.exit(1);
    			}
    		//.setInt(this, value);
    		} catch (Exception e) {
    			e.printStackTrace();
    			System.exit(1);
    		}
    	}
    }
    
/*
    // TODO -- eventually make a file version of this
    protected Learner(Element xml) {
		try {
			Class<?> extractor = Class.forName(xml.getNodeName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Constructor<?> constructor = 
		//	extractor.getConstructor(new Class[] { Class.forName("org.w3c.dom.Element") });

    }
*/    
    
    /* TODO - later
    protected Learner(Learner learner) {
    	this(learner.identifier);
    }
    */
   
    // note that this is always near the constructors as I want this to be central to usability
    // TODO - add constructors that can take a file
	public void xml(SimpleWriter out) {
		//out.println("<Learner>");
		//out.println("<type>" + getClass().getName() + "</type>");
		//out.println("<learner>");
		out.println("<" + getClass().getName() + ">");
		out.println("<finished>" + finished + "</finished>");
		xmlBody(out);
		out.println("</" + getClass().getName() + ">");
		//out.println("</learner>");
		//out.println("</Learner>");
	}

	public void xmlBody(SimpleWriter out) {
		out.println("<identifier>" + identifier + "</identifier>");
	}

    
    /** 
     * Sets the identifier
     * 
     * @param identifier	the new identifier value	
     */
    public void identifier(int identifier) {
    	this.identifier = identifier;
    }

    /**
     * Trains the learner parameters using the provided data Sample
     * 
     * @param examples	the Sample of examples for training
     */
    public abstract void train(Sample<T> data);
        
    /**
     * Evaluate a new Instance based on the learned function
     * 
     * @param input	the Instance to be evaluated
     * @return	the resulting Prediction
     */
    public abstract Prediction evaluate(Input instance);
    
    /**
     * Evaluate a new Instance based on the learned function relative to
     * the hypothesis used when making predictions used for training
     * updates (think averaged Perceptron)
     * 
     * @param input	the Instance to be evaluated
     * @return	the resulting Prediction
     */
    /*
    public Prediction trainEvaluate(Input input) {
    	return evaluate(input);
    }
    */
    
    // TODO - figure out Evaluator bit
    /**
     * Evaluates a hypothesis relative to a testset.
     * 
     * @param test			the testing set
     * @param loss			the loss function for evaluation
     * @return				the cumulative loss
     */
    //public abstract double evaluate(Sample<Input> test, Evaluator evaluator, boolean analysis);
     
    // TODO - note that this is a hack, but not an awful one (but should be generalized)
    //public abstract double evaluate(FinitePoolSelectiveSample sample, int status, Evaluator evaluator);
    
    /**
     * Generally, we will do nothing; useful for averaged Perceptron and such.  Could be
     * thought of as wrapping up the algorithm for testing after training with multiple
     * data samples.
     *
     */
    public void finish() { 
    	finished = true; 
    }

    
    /**
     * Resets the learned parameters of the learning algorithm.  Intended for
     * running multiple experiments and such.
     */
	public void reset() {
		finished = false;
	}

    /**
     * returns a clone of the Learner
     */
	@SuppressWarnings("unchecked")
	public Object clone() {
    	Learner<T> clone = null;
    	try {
    		clone = (Learner<T>) super.clone();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	return clone;
    }
    
	@SuppressWarnings("unchecked")
	public Learner<T> copy() {
    	return (Learner<T>) clone();
    }
    
    public Learner<T> deepCopy() {
    	return copy();
    }	
    
}
