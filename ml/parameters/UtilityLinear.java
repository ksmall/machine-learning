package ml.parameters;

import java.util.HashSet;

import ml.instance.Feature;
import ml.instance.FeatureVector;

import core.function.Function;
import core.function.LinearFunction;

public class UtilityLinear {
	
	@SuppressWarnings("unchecked")
	public static double max(Function w, HashSet<Integer> id, boolean inclusion) {
		double result = Double.NEGATIVE_INFINITY;
		for (Feature f : (LinearFunction<Feature,FeatureVector>) w) {
			if (((inclusion && id.contains(f.identifier())) || (!inclusion && !id.contains(f.identifier()))) &&
					(f.strength() > result))
				result = f.strength();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static double min(Function w, HashSet<Integer> id, boolean inclusion) {
		double result = Double.POSITIVE_INFINITY;
		for (Feature f : (LinearFunction<Feature,FeatureVector>) w) {
			if (((inclusion && id.contains(f.identifier())) || (!inclusion && !id.contains(f.identifier()))) &&
					(f.strength() < result))
				result = f.strength();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static double mean(Function w, HashSet<Integer> id, boolean inclusion, 
			double threshold, boolean above) {
		double result = 0.0;
		int counter = 0;
		for (Feature f : (LinearFunction<Feature,FeatureVector>) w) {
			if (((inclusion && id.contains(f.identifier())) || (!inclusion && !id.contains(f.identifier()))) &&
					((above && (f.strength() > threshold)) || (!above && (f.strength() < threshold)))) {
				result += f.strength();
				counter++;
			}
		}
		return result / counter;
	}

	@SuppressWarnings("unchecked")
	public static LinearFunction<Feature,FeatureVector> sum(Function a, Function b) {
		LinearFunction<Feature,FeatureVector> result = ((LinearFunction<Feature,FeatureVector>) a).deepCopy();
		result.add((LinearFunction<Feature,FeatureVector>) b);
		return result;
	}

}
