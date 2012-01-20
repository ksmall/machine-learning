package core.sample;

import core.Identifiable;
import core.Identifier;

public interface AccessibleSample<T extends Identifiable> extends IterableSample<T> {

	public T itemAt(int index);		// item at position "index" (according to order added to sample)
	public T get(Identifier id);	// item with identifier "id"
	public void shuffle();
	
}
