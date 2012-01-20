package core.text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

public class Utility {

	// the form here is one word per line in the file
	public static HashSet<String> WordlistHash(String file, boolean lowercase) {
		HashSet<String> result = new HashSet<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				line.trim();
				result.add(lowercase ? line.toLowerCase() : line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result; 
	}
	
}
