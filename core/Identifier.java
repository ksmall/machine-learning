package core;

import core.function.Input;

// used for SplitSample, but not entirely clear this is the ideal way to do this
// TODO - can't we just extend String
public class Identifier implements Identifiable, Input {

	public String identifier;
	
	public Identifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String identifier() {
		return identifier;
	}
	
	public int hashCode() {
		return identifier.hashCode();
	}
	
	public boolean equals(Object o) {
		return identifier.equals(((Identifier) o).identifier());
	}
	
	public String toString() {
		return identifier();
	}
}
