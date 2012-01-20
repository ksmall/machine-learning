package ml.extraction;

public class OrderedIdentifier implements IdentifierExtractor {

	protected int id;
	
	public OrderedIdentifier(int start) {
		id = start;
	}
	
	public String extract(Object o) {
		return Integer.toString(id++);
	}
}
