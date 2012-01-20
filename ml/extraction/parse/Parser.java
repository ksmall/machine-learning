package ml.extraction.parse;

import ml.extraction.InputGenerator;
import core.function.Input;
import core.io.SimpleReader;

public abstract class Parser<T extends Input> implements Iterable<T> {

	protected SimpleReader reader;
	protected String datafile;
	protected InputGenerator<T> generator;

	public Parser(String datafile, InputGenerator<T> generator) {
		this.generator = generator;
		this.datafile = datafile;
		open(datafile);
	}
	
	public void open(String datafile) {
		reader = new SimpleReader(datafile);
	}

	public void close() {
		reader.close();
	}
	
	// returns null when done
	public abstract T next();
}
