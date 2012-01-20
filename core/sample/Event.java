package core.sample;

public class Event<T> {

	public T outcome;
	public double weight;

	public Event(T outcome, double weight) {
		this.outcome = outcome;
		this.weight = weight;
	}
	
	public Event(T item) {
		this(item, 1.0);
	}
	
	// assumes only one outcome definition in a sample (so doesn't check weight)
	public boolean equals(Event<T> o) {
		return o.outcome.equals(outcome);
	}
}
