package ml.distance;

import ml.instance.FeatureVector;

public class RandomDistance extends Distance {

	public double calculate(FeatureVector v1, FeatureVector v2) {
		return Distance.random();
	}

}
