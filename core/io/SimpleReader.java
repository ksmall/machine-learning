package core.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

// doesn't extend so we can catch exceptions
// TODO - consider reset
public class SimpleReader {

	protected BufferedReader br;
	
	public SimpleReader(Reader r) {
		br = new BufferedReader(r);
	}

	public SimpleReader(String file, String encoding) {
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} /*finally {
			close();
		}*/
	}
	
	public SimpleReader(String file) {
		this(file, "UTF-8");
	}
	
	// TODO - verify the finally here
	public String readLine() {
		String result = null;
		try {
			result = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} /*finally {
			close();
		}*/
		return result;
	}
	
	public void close() {
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
