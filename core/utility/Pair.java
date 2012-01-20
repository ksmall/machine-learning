package core.utility;

public class Pair<T> {
	
	public T a;
	public T b;
	
	public Pair(T a, T b) {
		this.a = a;
		this.b = b;
	}
	
	public String toString() {
		return new String("[" + a + "," + b + "]");
	}
}
