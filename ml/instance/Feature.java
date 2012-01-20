package ml.instance;

import core.Copyable;

/**
 * The Feature class.
 * 
 * @author ksmall
 */

// TODO - figure out visibility issue at some point
public class Feature implements Cloneable, Copyable<Feature> {

	/**
	 * Used to uniquely identify this specific Feature in the Lexicon
	 */
    protected int identifier;

    /**
     * The primary constructor
     * 
     * @param identifier  	the feature identifier
     */
    public Feature(int identifier) {
    	this.identifier = identifier;
    }

    /**
     * The copy constructor
     * 
     * @param f		the Feature to be copied
     */
    public Feature(Feature f) {
    	this.identifier = f.identifier;
    }
    
    /**
     * Returns the identifier associated with this Feature
     * 
     * @return	the identifier
     */
    public int identifier() {
    	return identifier;
    }

    /**
     * Returns the strength associated with this Feature
     * 
     * @return	the strength
     */
    public double strength() {
    	return 1.0;
    }
    
    /**
     * Used to test equality between two Feature instances.  Equality is determined
     * exclusively by the {@code identifier} values.
     * 
     * @param o		the {@code Feature} instance to be compared for equality
     * @return 		a boolean value of {@code true} if equal, {@code false} otherwise
     */
    public boolean equals(Object o) {
    	return identifier == ((Feature) o).identifier;
    }
    
    // TODO - is this correct or do Integer objects have fancier hashCodes
    public int hashCode() {
    	return identifier;
    }
    
    /**
     * Returns the polarity of this Feature; used for Label instances
     *
     * @return		{@code true}
     */
    // TODO -- should this be moved to label?
    public boolean isPositive() {
    	return true;
    }

    /**
     * Returns a String representation of the form {@code identifier} 
     * followed by an {@code *} if this Feature instance is hidden
     * 
     * @return		the {@code String} representation of this Feature
     */
    public String toString() {
    	String result = Integer.toString(identifier);
    	return result;
    }
    
    // assuming 1(0.1)
    public static Feature parseFeature(String f) {
    	String[] fields = f.split("\\(");
    	if (fields.length == 1)
    		return new Feature(Integer.parseInt(fields[0]));
    	else
    		return new FeatureReal(Integer.parseInt(fields[0]), 
    				Double.parseDouble(fields[1].substring(0,fields[1].length()-1)));
    }

    
    /**
     * returns a clone of this instance
     * 
     *  @return		a clone of this instance
     */
    public Object clone() {
    	Feature clone = null;
    	try {
    		clone = (Feature) super.clone();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	return clone;
    }
  
    public Feature copy() {
    	return new Feature(this);
    }

	public Feature deepCopy() {
		return copy();
	}
}