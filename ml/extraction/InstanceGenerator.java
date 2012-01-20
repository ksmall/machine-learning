package ml.extraction;

import java.util.ArrayList;
import java.util.HashSet;

import ml.instance.Feature;
import ml.instance.FeatureVector;
import ml.instance.Instance;
import ml.instance.Label;

public class InstanceGenerator implements InputGenerator<Instance> {

	// TODO - fix this at some point
	protected IdentifierExtractor idExtractor;
	/** the list of Extractor instances associated with feature extraction */
    protected ArrayList<Extractor> featureExtractors;
    /** the list of Extractor instances associated with label extraction */
    protected Extractor labelExtractor;
    // sequential model later
    /** the Lexicon of extracted Feature instances */
    protected Lexicon featureLexicon;
    /** the Lexicon of extracted Label instances */
    protected Lexicon labelLexicon;
    /** the default  numbering associated with the Instance objects extracted */
    //public int nextValue = 1;  // used only if none is provided
    public boolean deduplicate;
    
    /**
     * the constructor
     * 
     * @param featureLexicon	a Lexicon to associate generated features with integers
     * @param labelLexicon		a Lexicon to associate generated labels with integers
     */
    // TODO - decide if idExtractor default behavior is null or numerical
    public InstanceGenerator(Lexicon featureLexicon, Lexicon labelLexicon, 
    		Extractor labelExtractor, IdentifierExtractor idExtractor) {
    	featureExtractors = new ArrayList<Extractor>();
    	this.labelExtractor = labelExtractor;
    	this.idExtractor = idExtractor;
    	this.featureLexicon = featureLexicon;
    	this.labelLexicon = labelLexicon;
    	this.deduplicate = false;
    }
    
    public InstanceGenerator(Lexicon featureLexicon, Lexicon labelLexicon, 
    		Extractor labelExtractor) {
    	this(featureLexicon, labelLexicon, labelExtractor, new OrderedIdentifier(1));
    }
    
    /**
     * Adds a feature generating function (FGF) to the list of feature extractors
     * 
     * @param fgf	the feature extractor to be added
     */
    public void addFGF(Extractor fgf) {
    	featureExtractors.add(fgf);
    }

    /**
     * Generates an Instance using the current Extractor objects given an object in 
     * its native form and an id to be associated with the generated instsance.
     * 
     * @param o		the object for which an Instance to be generated
     * @param id	the id associated with the object for input; note that while
     * 				assigning two Instances the same id will likely not break the
     * 				system, it defeats the purpose of assigning them ids.  This issue
     * 				is left to the code calling this function.  If you don't want to 
     * 				handle this, use the other version of generate.
     * @return		the generated Instance
     */
    public Instance generateDirect(Object o, String id) {
    	Instance result = new Instance(id);
    	//System.out.println(o);
    	for (Extractor extractor : featureExtractors) {
    		//System.out.println(extractor);
    		//Input features = extractor.extract(o, featureLexicon);
    		result.addAll((FeatureVector) extractor.extract(o, featureLexicon));
    	}
    	result.label((Label) labelExtractor.extract(o, labelLexicon));
    	// deduplicate would go here (maybe in finish?)
    	result.finish();
    	return result;
    }

    // TODO - fix this in some way (probably option)
    public Instance generateDeduplicate(Object o, String id) {
    	Instance result = new Instance(id);
    	//System.out.println(o);
    	HashSet<Feature> features = new HashSet<Feature>();
    	for (Extractor extractor : featureExtractors) {
    		//System.out.println(extractor);
    		//Input features = extractor.extract(o, featureLexicon);
    		//result.addAll((FeatureVector) extractor.extract(o, featureLexicon));
    		features.addAll((FeatureVector) extractor.extract(o, featureLexicon)); 
    	}
    	result.label((Label) labelExtractor.extract(o, labelLexicon));
    	// deduplicate would go here (maybe in finish?)
    	result.addAll(features);
    	result.finish();
    	return result;
    }
    
    public Instance generate(Object o, String id) {
    	if (deduplicate)
    		return generateDeduplicate(o, id);
    	else
    		return generateDirect(o, id);
    }
    
    /**
     * Generates an Instance using the current Extractor objects given an object in 
     * its native form.  The id for the newly generated Instance will be consecutive numbers.
     * 
     * @param o		the object for which an Instance to be generated
     * @return		the generated Instance
     */
    public Instance generate(Object o) {
    	return generate(o, idExtractor.extract(o));
    }
}
