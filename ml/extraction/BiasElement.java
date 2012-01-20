package ml.extraction;

import ml.instance.Feature;
import ml.instance.FeatureVector;

/**
 * Used simply to add an active feature corresponding to a bias element for
 * linear classifier algorithms.
 * 
 * @author ksmall
 */
public class BiasElement extends Extractor {

	public int id;
	
	/** the constructor */
    public BiasElement(int id, Lexicon lexicon) {
    	super("BiasElement");
    	this.id = id;
    	lexicon.put(identifier, id);
    }

    public BiasElement(Lexicon lexicon) {
    	//this(Integer.MAX_VALUE, lexicon);
    	super("BiasElement");
    	this.id = lexicon.get(identifier);
    }
    
    /** Extractor returns a single Feature associated with the bias element. */
    public FeatureVector extract(String source, Object o, Lexicon lexicon) {
    	//lexicon.get(source, toString(), id); // Integer.MAX_VALUE);
    	//return new FeatureVector(new Feature(lexicon.get(identifier))); // to add counts
    	return new FeatureVector(new Feature(id));
    }
}
