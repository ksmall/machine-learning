package core.function;

import java.util.Iterator;
import java.util.List;

import core.Copyable;

public interface LinearFunction<T,U extends List<T>> extends Copyable<LinearFunction<T,U>>, Function, Iterable<T> {

	public double dot(U input);
	public RealOutput evaluate(U input);
	public Double put(int key, double value);
	
	public void add(U vector);
	public void add(U vector, double scale);
	public void add(LinearFunction<T,U> parameters);
	public void add(LinearFunction<T,U> parameters, double scale);
	
	public void clear();
    
	public Iterator<T> iterator();
	public Double get(int id);

	public double norm(double p);
    public void scale(double scalar);
    
	public LinearFunction<T,U> copy();
	public LinearFunction<T,U> deepCopy();	
	
	//public List<T> parameters();	
}
