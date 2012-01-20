package ml.extraction.parse;

import java.util.Iterator;
import java.util.NoSuchElementException;

import ml.extraction.InputGenerator;
import core.function.Input;
import core.io.SimpleReader;

public class LineParser<T extends Input> extends Parser<T> {

	// comment is a "comment out" start of line
	public String comment;
	
	public LineParser(String datafile, InputGenerator<T> generator) {
		super(datafile, generator);
	}

	public T next() {
		String line = null;
		try {
			line = reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (line != null) {
			//System.out.println(line);
			if ((comment != null) && (line.startsWith(comment)))
				return next();
			else
				return generator.generate(line);
		}
		else
			return null;
	}

	public Iterator<T> iterator() {
		
		return new Iterator<T>() {
			
			protected SimpleReader br = new SimpleReader(datafile);

			String line = null;
					
			public boolean hasNext() {
				line = br.readLine();
				if (line == null)
					return false;
				else
					return true;
			}

			public T next() {
				if (line != null) {
					String result = line;
					line = null;
					return generator.generate(result);
				}
				else {
					line = br.readLine();
					if (line == null)
						throw new NoSuchElementException();
					String result = line;
					line = null;
					return generator.generate(result);					
				}
			}
			
			public void remove() {
				// unsupported  throw
			}
		};
	}

}
