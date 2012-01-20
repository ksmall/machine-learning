package ml.extraction;

import core.function.Input;

public abstract class Extractor implements Cloneable {

	/** a description for the Extractor */
    public String identifier;
    
    public boolean testing;

    /**
     * the default constructor
     * 
     * @param identifier	the Extractor description
     */
    public Extractor(String identifier) {
    	this.identifier = new String(identifier);
    	testing = false;
    }
        
    /**
     * Extracts a FeatureVector from the given object
     * 
     * @param o			the given Object
     * @param lexicon	a Lexicon for which integers are associated with Feature descriptions
     * @return	the extracted FeatureVector
     */
    public abstract Input extract(String source, Object o, Lexicon lexicon);
    
    public Input extract(Object o, Lexicon lexicon) {
    	return extract(toString(), o, lexicon);
    }
        
    public String toString() {
    	return identifier;
    }
    
    public Object clone() {
    	Extractor clone = null;
    	try {
    		clone = (Extractor) super.clone();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	return clone;
    }

	public int hashCode() {
		return toString().hashCode();
	}

}
