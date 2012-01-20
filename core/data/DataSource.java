package core.data;

public interface DataSource<T,U> {

	public U get(T key);
	public boolean contains(T key);
	public U put(T key, U value);
	public int size();
	
}
