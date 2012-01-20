package ml.extraction;

import core.function.Input;

public interface InputGenerator<T extends Input> {

	T generate(Object o);
	
}
