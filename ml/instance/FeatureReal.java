package ml.instance;

/**
 * A Feature class with a real-numbered value associated with each Feature.
 * 
 * @author ksmall
 */

public class FeatureReal extends Feature {
	
    /**
     * The value (real number) of this specific Feature
     */
    protected double strength;

    /**
     * The primary constructor
     * 
     * @param identifier  	the feature identifier
     * @param strength		the real-numbered value
     */
    public FeatureReal(int identifier, double strength) {
    	super(identifier);
    	this.strength = strength;
    }
    
    /**
     * A constructor which assumes by default that the strength is 1.0
     * 
     * @param identifier  	the feature identifier
     */
    public FeatureReal(int identifier) {
    	this(identifier, 1.0);
    }
    
    /**
     * The copy constructor.  Note that in this case, the copy constructor can also
     * be used to "promote" a Feature to a FeatureReal
     * 
     * @param f		the FeatureReal to be copied
     */
    public FeatureReal(Feature f) {
    	this(f.identifier, f.strength());
    }

    public double strength() {
    	return strength;
    }
    
    /**
     * Returns a String representation of the form {@code identifier(strength)} 
     * followed by an {@code *} if this Feature instance is hidden.  Note that
     * if the strength is 1.0, the form is {@code identifier} for compactness.
     * 
     * @return		the {@code String} representation of this FeatureReal
     */
    public String toString() {
    	String result = Integer.toString(identifier);
    	if (strength != 1.0)
    		result += "(" + strength + ")";
    	return result;
    }
        
    public FeatureReal copy() {
    	return new FeatureReal(this);
    }
    
	public FeatureReal deepCopy() {
		return copy();
	}
}
