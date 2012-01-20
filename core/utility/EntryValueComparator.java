package core.utility;

import java.util.Comparator;

public class EntryValueComparator<T extends Comparable<T>,U extends Comparable<U>> implements Comparator<Entry<T,U>> {

	public boolean descending;
	
	public EntryValueComparator(boolean descending) {
		this.descending = descending;
	}
	
	public EntryValueComparator() {
		this(true);
	}
    	
	public int compare(Entry<T,U> e1, Entry<T,U> e2) {
		return descending ? e2.value.compareTo(e1.value) : e1.value.compareTo(e2.value);
	}
}
