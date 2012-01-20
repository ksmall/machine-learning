package ml.instance;

public class RealPrediction extends Label implements Prediction {

	public RealPrediction(int identifier, double strength) {
		super(identifier, strength);
	}

	public Label winner() {
		return this;
	}
}
