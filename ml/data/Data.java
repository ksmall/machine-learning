package ml.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ml.extraction.Lexicon;
import ml.extraction.parse.Parser;
import ml.instance.Instance;
import ml.instance.Label;
import core.Identifiable;
import core.Identifier;
import core.data.DataSource;
import core.function.Input;
import core.io.SimpleWriter;
import core.sample.AccessibleSample;
import core.sample.SplitSample;
import core.utility.Histogram;

// TODO - eventually rid us of the DataHash bit
// using size of DataSource to determine folds (therefore, not in Sample - but assuming 1 to 1 mapping)
// I really do like this folds also able to be defined by sets direction
public class Data<T extends Input & Identifiable> implements AccessibleSample<T> {

	protected SplitSample<Identifier> sample;  // really, sampling strategy (even if technically allows nesting Data)
	protected SplitSample<Identifier> split; // for folds and the such (sort of wasteful, but allows shuffling)
	protected DataSource<Identifier,T> data;
	protected Iterator<T> iterator;			 // reset will set to current iterator
	
	protected Lexicon labels;
	protected HashMap<Integer,String> inverted;
	protected boolean testing;		// really for NestedData, but easier here and one bit won't kill anybody :)
	public SimpleWriter debug;
	
	// the sample is supposed to be empty (make note of this in any documentation)
	// data should also be empty (although it wouldn't matter for set versions)
	public Data(SplitSample<Identifier> sample, DataSource<Identifier,T> data, Lexicon labels) {
		this.sample = sample;
		this.data = data;
		this.split = null;
		this.testing = false;
		this.labels = labels;
		debug = null;
		//this.data = new DataHash<Identifier,T>();		// TODO = obviously fix this to be passed in
		all();  // assume we want to see all data
	}
	
	// TODO overload for 1 fold
	public Data(SplitSample<Identifier> sample, DataSource<Identifier,T> data, Lexicon labels, Parser<T> parser) {
		this(sample, data, labels);
		add(parser);
	}
	
	public int splits() {
		return sample.splits();
	}
	
	public void all() {
		//foldEnd = null;
		split = null;
		testing = false;
	}
	
	public void training(int fold) {
		split = sample.training(fold);
		testing = false;
	}
	
	public void training() {
		split = sample.training();
		testing = false;
	}
	
	public void testing(int fold) {
		split = sample.testing(fold);
		testing = true;
	}
	
	public void testing() {
		split = sample.testing();
		testing = true;
	}
	
	public void generateSplit(double test) {
		sample.generateSplit(test);
	}
	
	public void generateFolds(int folds) {
		sample.generateFolds(folds);
	}
	
	public boolean add(T outcome) {
		Identifier id = new Identifier(outcome.identifier());
		sample.add(id);
		data.put(id, outcome);
		return true;
	}
	
	public void add(Parser<T> parser) {
		for (T instance : parser)
			add(instance);	
		finish();  // actually ok if run multiple times
	}
	
	// TODO -- verify if this is what we actually want
	public T itemAt(int index) {
		return data.get(sample.itemAt(index));
	}

	// TODO -- verify this also
	public T get(Identifier id) {
		return data.get(id);
	}
	
	public void finish() {
		reset();
	}

	public T next() {
		if (!iterator.hasNext())
			return null;
		return iterator.next();
	}

	public void reset() {
		iterator = iterator();
	}

	// only prints out debug statement
	public void draw() {
		if (debug != null) {
			Histogram<String> labelDist = labelHistogram(iterator());
			debug.println("Training Dist: " + labelDist + "(" + labelDist.total() + ")");
		}
	}
	
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			
			protected Iterator<Identifier> it = (split == null) ? sample.iterator() : split.iterator();
			
			public boolean hasNext() {
				return it.hasNext();
			}

			public T next() {
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

	// this calculates the current label distribution of the data
	public Histogram<String> labelHistogram(Iterator<T> it) {
		Histogram<String> result = new Histogram<String>();
		iterator = it;
		Instance e = null;
		while ((e = (Instance) next()) != null) {
			//System.out.println(e);
			Label label = e.label();
			//System.out.println(label);
			String id = inverted.get(label.identifier());
			if (!label.isPositive())
				id = new String("!" + id);
			result.put(id);
		}
		return result;
	}
	
	// TODO - may eventually generate other sets requiring a new HashMap to ids or ArrayLists
	public void shuffle() {
		if (split == null)
			sample.shuffle();
		else
			split.shuffle();
	}
}
