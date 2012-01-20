package ml.instance;

import core.function.Input;

// input as this is fed into algorithms as part of an instance -- however, really for extractor
public class Label extends FeatureReal implements Input {

    public Label(int identifier, double strength) {
    	super(identifier,strength);
    }
    
    public Label(int identifier) {
    	super(identifier);
    }
    
    public Label(Feature f) {
    	super(f);
    }
    
    public Label copy() {
    	return new Label(this);
    }
    
	public Label deepCopy() {
		return copy();
	}
}
