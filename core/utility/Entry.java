package core.utility;

import java.util.Comparator;

public class Entry<T extends Comparable<T>, U extends Comparable<U>> {
	
	public T key;
	public U value;
	
	public Entry(T key, U value) {
		this.key = key;
		this.value = value;
	}

    public class keyComparator implements Comparator<Entry<T,U>> {
    	public int compare(Entry<T,U> e1, Entry<T,U> e2) {
    		return e1.key.compareTo(e2.key);
    	}
    }

    /*
    public int hashCode() {
    	return key.hashCode();
    }
    
    @SuppressWarnings("unchecked")
	public boolean equals(Object o) {
    	return ((Entry<T,U>) o).key.equals(key);
    }
    */
    
    public String toString() {
    	return new String(key + ":" + value);
    }
}
