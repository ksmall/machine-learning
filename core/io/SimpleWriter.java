package core.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class SimpleWriter {

	protected PrintWriter out;
	
	public SimpleWriter(Writer out) {
		this.out = new PrintWriter(out, true);	// autoflush
	}

	public SimpleWriter(PrintStream out) {
		this.out = new PrintWriter(out, true);
	}
	
	public SimpleWriter(String file, String encoding) {
		try {
			out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), encoding), true);
			//out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)), true);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public SimpleWriter(String file) {
		this(file, "UTF-8");
	}
	
	// TODO - verify the finally here
	public void print(Object o) {
		out.print(o);
	}

	public void println(Object o) {
		out.println(o);
	}
	
	public void close() {
		out.close();
	}
}
