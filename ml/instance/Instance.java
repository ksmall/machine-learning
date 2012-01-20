package ml.instance;

import core.Identifiable;
import core.function.Input;

// is essentially a FeatureVector with a label and identifier
public class Instance extends FeatureVector implements Input, Identifiable {

	private static final long serialVersionUID = 1L;

	/** a description of the instance */
    protected String identifier;
    /** the label */
    protected Label label;

    /**
     * the primary constructor
     * 
     * @param identifier	the Instance identifier
     */
    public Instance(String identifier) {
    	super();
    	this.identifier = identifier;
    	label = null;
    }

    public Instance(Instance instance) {
    	super(instance);	// note that this does deepCopy on the features
    	this.identifier = instance.identifier();
    	this.label = (instance.label == null) ? null : instance.label.deepCopy();
    }
        
    /**
     * Adds a label to the label FeatureVector
     * 
     * @param l	the label to be added
     */
    public void label(Label label) {
    	this.label = label.deepCopy();
    }

    public Label label() {
    	return label;
    }
    
    /**
     * Returns a hashcode based on the identifier of this Instance
     * 
     * @return	the hash code
     */
    public int hashCode() {
    	return identifier.hashCode();
    }

    /**
     * Trims the feature and label FeatureVector instances to size
     */
    public void finish() {
    	super.finish();
    }

    /**
     * Normalizes the feature FeatureVector to size 1 based on the stated norm.
     * 
     * @param p	the norm used for normalization
     */
    // TODO - later
    /*
    public void normalize(double p) {
    	if (features != null)
    		features.scale(1.0 / norm(p));
    }
    
    public double norm(double p) {
    	return features.norm(p);
    }
	*/
    
    /**
     * a String representation of this Instance
     */
    public String toString() {
    	String result = new String(identifier + "|[");
    	result += (label == null) ? "null" : label.toString();
    	result += "] " + super.toString();
    	return result;
    }

	public Instance copy() {
		Instance result = new Instance(this.identifier);
		result.addAll(this);
		result.sortStatus = this.sortStatus;
		return result;
	}

	public Instance deepCopy() {
		return new Instance(this);
	}

	public String identifier() {
		return identifier;
	}

	public boolean equals(Instance instance) {
		return identifier.equals(instance.identifier());
	}

    // for active learning debugging (doesn't do null stuff yet)
    /*
    public String activeToString() {
    	return new String(identifier + "|" + "[" + labels + 
    			"](" + confidence + ")");
    }	
    */
}
