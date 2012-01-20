package ml.distance;

import core.Globals;
import ml.instance.FeatureVector;

public abstract class Distance {

	public abstract double calculate(FeatureVector v1, FeatureVector v2);
	
	public static double cosine(FeatureVector v1, FeatureVector v2) {
		return v1.dot(v2) / (v1.norm() * v2.norm());
	}
	
	public static double random() {
		return Globals.nextDouble();
	}
}
