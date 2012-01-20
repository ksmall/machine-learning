package ml.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ml.extraction.Lexicon;
import ml.extraction.parse.Parser;
import ml.instance.Instance;
import ml.instance.Label;
import core.Globals;
import core.Identifier;
import core.data.DataSource;
import core.io.SimpleWriter;
import core.sample.SplitSample;
import core.utility.Histogram;

// TODO make more efficient at some point by creating a HashMap from id to label (or DataSource or whatever)
// shuffle will shuffle the current draw, reset will just reset the iterator, draw will actually draw the nested sample
// NOTE: note that until draw() is called, this will just return everything
public class NestedData extends Data<Instance> {

	protected HashMap<String,Double> dist;
	protected ArrayList<Identifier> currentDraw;
	
	public SimpleWriter debug;
		
	public NestedData(SplitSample<Identifier> sample, DataSource<Identifier,Instance> data, Lexicon labels) {
		super(sample, data, labels);
		currentDraw = null;
	}
	
	// TODO overload for 1 fold
	public NestedData(SplitSample<Identifier> sample, DataSource<Identifier,Instance> data, Lexicon labels, 
			Parser<Instance> parser) {
		super(sample, data, labels, parser);
		currentDraw = null;
	}

	// assumption is that if it sums to 1.0, it is a distribution
	// otherwise if all integer values >= 0.0, assume just counts (note no error checking necessarily here)
	public void setDistribution(Lexicon labelLexicon, HashMap<String,Double> dist) {
		this.dist = new HashMap<String,Double>();
		this.labels = labelLexicon;
		inverted = labelLexicon.invert();
		//System.out.println(inverted);
		for (String key : dist.keySet()) {
			if (!labels.containsKey(key.replace("!", "")))
				throw new NoSuchElementException();
			else
				this.dist.put(key, dist.get(key));
		}
	}

	// assume just one label
	public void balance(Lexicon labelLexicon) {
		HashMap<String,Double> balanced = new HashMap<String,Double>();
		String id = labelLexicon.keySet().iterator().next();
		balanced.put(id, 0.5);
		balanced.put("!" + id, 0.5);
		setDistribution(labelLexicon, balanced);
	}
	
	public void draw() {
		Histogram<String> original = labelHistogram(super.iterator());
		Histogram<String> modified = modifiedHistogram(original);
		if (debug != null) {
			debug.println("Unmodified Dist: " + original + "(" + original.total() + ")");
			debug.println("Nested(" + dist + "): " + modified + "(" + modified.total() + ")");
		}
		currentDraw = allocate(original, modified);
	}
		
	// TODO - think about error checking here
	public void shuffle() {
		Collections.shuffle(currentDraw, Globals.rng);
	}
	
	public Iterator<Instance> iterator() {
		if (testing || (currentDraw == null))
			return super.iterator();
		else {
			return new Iterator<Instance>() {
				
				// ArrayList<Identifier> included = allocate(original, modified);
				//protected Iterator<Identifier> it = (split == null) ? sample.iterator() : split.iterator();
				protected Iterator<Identifier> it = currentDraw.iterator();
				
				public boolean hasNext() {
					return it.hasNext();
				}

				public Instance next() {
					if (!it.hasNext())
						throw new NoSuchElementException();
					Identifier key = it.next();
					if (!data.contains(key))
						throw new NoSuchElementException();
					return data.get(key);				
				}

				// note that this would have fold calculation implications (and won't universally remove if folds)
				public void remove() {
					it.remove();
				}
			};
		}
	}
		
	public ArrayList<Identifier> allocate(Histogram<String> original, Histogram<String> modified) {
		ArrayList<Identifier> result = new ArrayList<Identifier>();
		Histogram<String> allocated = new Histogram<String>();
		iterator = super.iterator();
		Instance e = null;
		while ((e = (Instance) next()) != null) {
			Label label = e.label();
			String id = inverted.get(label.identifier());
			if (!label.isPositive())
				id = new String("!" + id);
			if (modified.containsKey(id) && (modified.get(id) > 0.0) &&
					(!allocated.containsKey(id) || (allocated.get(id) < modified.get(id)))) {
				allocated.put(id);
				result.add(new Identifier(e.identifier()));
			}
		}
		result.trimToSize();
		return result;
	}
		
	// this calculates the desired label distribution of the data for sub sampling
	public Histogram<String> modifiedHistogram(Histogram<String> current) {
		String dominated = findDominated(current);
		Histogram<String> result = new Histogram<String>();
		result.put(dominated, current.get(dominated));
		for (String c : dist.keySet()) {
			if (!c.equals(dominated))
				result.put(c, (int) Math.round(current.get(dominated) * (dist.get(c) / dist.get(dominated))));
		}

		return result;
	}
	
	public String findDominated(Histogram<String> current) {
		Histogram<String> constrainedCount = new Histogram<String>();
		ArrayList<String> key = new ArrayList<String>(current.keySet());
		for (int i = 0; i < key.size(); i++) {
			for (int j = i + 1; j < key.size(); j++) {
				//System.out.println("" + i + "," + j);
				if ((dist.get(key.get(i)) > 0.0) && ((dist.get(key.get(j)) > 0.0))) {
					//System.out.println("" + i + "," + j);
					String constrained = findConstrained(key.get(i), key.get(j), current);
					//System.out.println(pair);
					if (constrained != null)
						constrainedCount.put(constrained);
				}
			}
		}
		//System.out.println(loserCount + "=" + loserCount.max());
		return constrainedCount.max();

	}
	
	// assumes access to global "desired"
	public String findConstrained(String a, String b, Histogram<String> current) {
		double totalDist = dist.get(a) + dist.get(b);
		int totalCount = current.get(a) + current.get(b);
		int remainderA = (int) Math.round(current.get(a) / (dist.get(a) / totalDist)) - totalCount; 
		int remainderB = (int) Math.round(current.get(b) / (dist.get(b) / totalDist)) - totalCount; 
		//System.out.println("" + temp1 + "," + temp2);
		if (remainderB > remainderA)
			return a;
		else if (remainderA > remainderB)
			return b;
		else
			return null;
	}

}
