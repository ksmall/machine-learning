package core.sample;

public interface IterableSample<T> extends Sample<T>, Iterable<T> {

	public boolean add(T outcome);
}
