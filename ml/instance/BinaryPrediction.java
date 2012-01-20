package ml.instance;

public class BinaryPrediction extends BinaryLabel implements Prediction {

    /**
     * The primary constructor
     * 
     * @param positive		the label polarity
     * @param identifier	the label identifier
     * @param strength		the label strength
     */
    public BinaryPrediction(boolean positive, int identifier, double strength) {
    	super(positive, identifier, strength);
    }

    /**
     * A constructor which assumes that the label is visible and the strength is 1.0 
     * (the standard binary supervised learning setting)
     * 
     * @param positive		the label polarity
     * @param identifier	the label identifier
     */
    public BinaryPrediction(boolean positive, int identifier) {
    	this(positive, identifier, 1.0);
    }

    /**
     * The copy constructor
     * 
     * @param b		the BinaryLabel to be copied
     */
    public BinaryPrediction(BinaryPrediction b) {
    	super(b);
    }

	public Label winner() {
		return this;
	}
}
