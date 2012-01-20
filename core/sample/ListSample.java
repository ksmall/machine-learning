package core.sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import core.Globals;
import core.Identifiable;
import core.Identifier;
import core.function.Input;

public class ListSample<T extends Input & Identifiable> extends ArrayList<T> implements SplitSample<T> {

	private static final long serialVersionUID = 1L;

	protected Iterator<T> iterator;
	
	protected int[] foldEnd;
	protected HashMap<String,Integer> membership;
	protected HashSet<Integer> setID;
	protected int totalDefined;
	
	public ListSample() {
		super();
		reset();
	}
	
	public T next() {
		if (!iterator.hasNext())
			return null;
		return iterator.next();
	}

	public void reset() {
		iterator = iterator();
		foldEnd = null;
		membership = null;
		setID = null;
		totalDefined = 0;
	}
	
	public void clear() {
		super.clear();
		reset();
	}
	
	public void finish() {
		trimToSize();
	}
	
	public void shuffle() {
		Collections.shuffle(this, Globals.rng);
	}

	// retrieves sample which has set ids (doesn't check for undefined ids)
	// can use undefined to get remainder (note that if negated, still adds to set)
	// TODO - decide on semantics of not undefined (should just be inverse probably)
	public SplitSample<T> contains(Set<Integer> ids, boolean negate) {
		ListSample<T> result = new ListSample<T>();
		//for (String key : membership.keySet()) {
		for (T item : this) {
			Integer id = membership.get(item.identifier());
			if (id == null) {
				if (ids.contains(SplitSample.undefined))
					result.add(item); // adding current item if undefined to be added
			}
			else if ((!negate && ids.contains(id)) ||
					(negate && !ids.contains(id)))
				result.add(item);  //.add(get(id));
		}
		return result;
	}
	
	public SplitSample<T> contains(Set<Integer> ids) {
		return contains(ids, false);
	}

	// TODO - note that foldEnd could also be interpreted to mean the start of the next fold
	public void generateFolds(int folds) {
		//System.out.println("here");
		foldEnd = new int[folds];
		int base = size() / folds;
		int remainder = size() % folds;
		for (int i = 0; i < foldEnd.length; i++)
			foldEnd[i] = (i + 1) * base;
		for (int i = 1; i <= remainder; i++)
			foldEnd[i-1] += i;
		for (int i = remainder; i < foldEnd.length; i++)
			foldEnd[i] += remainder;
	}

	// TODO - note that doesn't allow you to add ids to more than one set
	public void generateSet(int id, Set<String> set) {
		if (membership == null) {
			membership = new HashMap<String,Integer>();
			totalDefined = 0;
		}
		if (setID.contains(id))
			throw new IllegalArgumentException("SplitSample: set identifier " + id + " already exists");
		else
			setID.add(id);
		for (String key : set) {
			if (membership.containsKey(key))
				throw new IllegalArgumentException("SplitSample: " + key + " already assigned to set " + membership.get(key));
			membership.put(key, id);
		}
		totalDefined += set.size();
	}

	public void generateSplit(double test) {
		foldEnd = new int[2];
		int testSize = 0;
		if (test >= 1.0)
			testSize = (int) Math.round(test);
		else 
			testSize = (int) Math.round(test * size());
		//System.out.println(testSize);
		foldEnd[0] = size() - testSize;
		foldEnd[1] = size();
	}

	public int splits() {
		if (foldEnd == null) {
//			System.out.println("here 2");
			if (membership == null)
				return 1;
			else 
				return ((totalDefined == size()) ? setID.size() : (setID.size() + 1));
		}
		else 
			return foldEnd.length;
	}

	public SplitSample<T> testing(int fold) {
		ListSample<T> result = new ListSample<T>();
		int counter = (fold == 0) ? 0 : foldEnd[fold-1];
		while (counter < size()) {
			result.add(itemAt(counter));
			counter++;
			if (counter == foldEnd[fold])
				counter = size();
		}
		result.trimToSize();
		return result;
	}

	public SplitSample<T> testing() {
		return testing(foldEnd.length - 1);
	}

	// add exception for generating folds not called
	// TODO - need to eventually add copyable (or even deep copyable) here (seriously)
	public SplitSample<T> training(int fold) {
		ListSample<T> result = new ListSample<T>();
		int counter = (fold == 0) ? foldEnd[0] : 0;
		int currentFold = (fold == 0) ? 1 : 0;
		while (counter < size()) {
			result.add(itemAt(counter));
			counter++;
			if (counter == foldEnd[currentFold]) {
				currentFold++;
				if (currentFold == fold) {  // checking if in testing fold
					counter = foldEnd[fold];
					currentFold++;
				}
			}
		}
		result.trimToSize();
		return result;
	}

	public SplitSample<T> training() {
		return training(foldEnd.length - 1);
	}

	public T itemAt(int index) {
		return super.get(index);
	}

	// TODO -- decide what to do here
	public T get(Identifier id) {
		System.out.println("get(String id) not really implemented for ListSample");
		return null;
	}
}
