package core.function;

import core.io.XML;

// not extractors, but mathematical component of a system
public interface Function extends XML {

	public Output evaluate(Input input);
	
}
