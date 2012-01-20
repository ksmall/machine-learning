package core.sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import core.Globals;

// TODO - note that this can behave like a ListSample, but keeps around a bunch of doubles
// TODO - this really isn't kept up very well (so, it needs to be verified before used)
public class WeightedSample<T> { //implements AccessibleSample<T> {

	protected ArrayList<Event<T>> sample;
	protected double wMin;
	protected double wMax;
	protected double wSum;
	protected Iterator<T> iterator;
	
	public WeightedSample() {
		sample = new ArrayList<Event<T>>();
		wMin = Double.POSITIVE_INFINITY;
		wMax = Double.NEGATIVE_INFINITY;
		wSum = 0.0;
	}
	
	public boolean add(T outcome, double weight) {
		if (weight < wMin)
			wMin = weight;
		else if (weight > wMax)
			wMax = weight;
		wSum += weight;
		return sample.add(new Event<T>(outcome, weight));
	}

	public boolean add(T outcome) {
		return add(outcome, 1.0);
	}
	
	public T get(int index) {
		return sample.get(index).outcome;
	}
	
	public void reset() {
		iterator = iterator();
	}

	public void finish() {
		sample.trimToSize();
		reset();
	}

	// TODO - consider a more efficient way of doing this...
	// --- should be able to do a binary search
	public T draw() {
		double random = Globals.nextDouble() * wSum;
		double sum = 0.0;
		for (Event<T> event : sample) {
			sum += event.weight;
			if (random < sum)
				return event.outcome;
		}
		return null;  // should never get here
	}
	
	public boolean hasNext() {
		return iterator.hasNext();
	}

	public T next() {
		if (!iterator.hasNext())
			return null;
		return iterator.next();
	}

	public void remove() {
		iterator.remove();
	}
	
	public void shuffle() {
		Collections.shuffle(sample, Globals.rng);
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {

			protected Iterator<Event<T>> it = sample.iterator();
			
			public boolean hasNext() {
				return it.hasNext();
			}

			public T next() {
				if (!it.hasNext())
					throw new NoSuchElementException();
				return it.next().outcome;				
			}

			public void remove() {
				it.remove();
			}

		};
	}
}
