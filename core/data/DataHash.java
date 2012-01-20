package core.data;

import java.util.HashMap;

import core.function.Input;

public class DataHash<T,U extends Input> extends HashMap<T,U> implements DataSource<T,U> {

	private static final long serialVersionUID = 1L;

	public boolean contains(T key) {
		return containsKey(key);
	}
	
}
