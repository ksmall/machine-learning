package core.sample;

public interface Sample<T> {

	public void reset();
	public T next(); 
	public void finish();
}
