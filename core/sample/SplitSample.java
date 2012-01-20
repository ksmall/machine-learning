package core.sample;

import java.util.Set;

import core.Identifiable;

// this can handle splits, folds, and disjoint sets
// convention is that we are specifying test sets and remainder is training
public interface SplitSample<T extends Identifiable> extends AccessibleSample<T> {
	
	public static int undefined = -1;
	
	// percentage here is holdout data (note that >=1 is assumed to be a count)
	// secondly, convention is to round the percentage to get to a whole number
	public void generateSplit(double test);
	
	public void generateFolds(int folds);
	
	public void generateSet(int id, Set<String> set);
	
	public SplitSample<T> training(int fold);
	// assumes "last" fold for testing data
	public SplitSample<T> training();
	
	public SplitSample<T> testing(int fold);
	public SplitSample<T> testing();
	
	// will either be #folds, 2 (train/test), or #sets (+1 if not fully defined)
	public int splits();
	
	// returns a sample with all members having one of the ids as defined
	// note Remainder allows us to include undefined members (useful for defining a test set, etc.)
	public SplitSample<T> contains(Set<Integer> ids, boolean negate);
	public SplitSample<T> contains(Set<Integer> ids);
}
