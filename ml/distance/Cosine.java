package ml.distance;

import ml.instance.FeatureVector;

public class Cosine extends Distance {

	public double calculate(FeatureVector v1, FeatureVector v2) {
		return Distance.cosine(v1,v2);
	}
}
