package ml.instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import core.Copyable;
import core.function.Input;

/**
 * FeatureVector performs the basic operations on a set of features for use in
 * learning algorithms.  Should consider making a sparse and fixed size vector
 * version.
 * 
 * @author ksmall
 */
public class FeatureVector extends ArrayList<Feature> implements Copyable<FeatureVector>, Iterable<Feature>, Input {

	private static final long serialVersionUID = 1L;
	public static final byte UNSORTED = 0;
	public static final byte ID_SORTED = 1;
	public static final byte STRENGTH_SORTED = 2;

    /** keeps sorting status so we don't repetitively re-sort (mostly for distances) */
    protected byte sortStatus;
    
    /**
     * The primary FeatureVector constructor.  Creates an empty list for which 
     * features will be added.
     */
    public FeatureVector() {
    	super();
    	sortStatus = UNSORTED;
    }

    public FeatureVector(int size) {
    	super(size);
    	sortStatus = UNSORTED;    	
    }
    
    /**
     * A constructor which also takes a Feature from which the vector is started.
     * 
     * @param f	the initial feature
     */
    public FeatureVector(Feature f) {
    	this();
    	add(f);
    }
    
    /**
     * The copy constructor
     * 
     * @param v		the FeatureVector to be copied
     */
    public FeatureVector(FeatureVector v) {
    	this();
    	for (Feature feature : v)
    		add(feature.deepCopy());
    	this.sortStatus = v.sortStatus;
    }
    
    /**
     * Clears the FeatureVector, resulting in an empty vector.
     */
    public void clear() {
    	super.clear();
    	sortStatus = UNSORTED;
    }

    /**
     * Adds the specified Feature to the FeatureVector.
     *  
     * @param f	the Feature to be added
     */
    // TODO - other add forms
    public boolean add(Feature f) {
    	sortStatus = UNSORTED;
    	return super.add(f);
    }

    /**
     * Adds the input FeatureVector to the current FeatureVector
     * 
     * @param v	the FeatureVector to be added
     */
    public boolean addAll(FeatureVector v) {
    	sortStatus = UNSORTED;
    	return super.addAll(v);
    }

    /**
     * Returns the specified Feature in the Feature vector as defined by
     * Feature equality (identifier) or null if the Feature does not exist.
     * 
     * @param f	the Feature to be retrieved
     * @return	the Feature if it exists, else null
     * 
     * TODO: Should check if id-sorted and do a binary search here.
     */
    // TODO - think about if we actually need this -- should be something like indexOf
    /*
    public Feature get(Feature f) {
    	for (Feature current : features) {
    		if (f.equals(current))
    			return current;    		
    	}
    	return null;
    }
    */
            
    /**
     * Scales the FeatureVector by the specified scalar.
     * 
     * @param scalar	the specified scalar
     */
    /*
    public void scale(double scalar) {
    	ArrayList<Feature> scaled = new ArrayList<Feature>(features.size());
    	for (Feature f : features)
    		scaled.add(new FeatureReal(f.identifier(), f.strength() * scalar, f.isVisible()));
    	features = scaled;
    }
    */
    
    // TODO - definite potential here, but postponing for the moment
    /*
    public void add(FeatureVector v) {
    	FeatureVector result = new FeatureVector(respectHidden);
    	idSort();
    	v.idSort();
    	Iterator<Feature> it = iterator();
    	boolean done = false;
    	Feature current = null;
    	if (it.hasNext())
    		current = it.next();
    	else
    		done = true;
    	Iterator<Feature> v_it = v.iterator();
    	boolean v_done = false;
    	Feature v_current = null;
    	if (v_it.hasNext())
    		v_current = v_it.next();
    	else
    		v_done = true;    	
    	while (!done || !v_done) {
    		//System.out.println(current + "," + v_current);
    		if (done) {
    			result.addFeature(v_current.deepCopy());
    	    	if (v_it.hasNext())
    	    		v_current = v_it.next();
    	    	else
    	    		v_done = true;    			
    		}
    		else if (v_done) {
    			result.addFeature(current);
    	    	if (it.hasNext())
    	    		current = v_it.next();
    	    	else
    	    		done = true;    			
    		}
    		else if (v_current.identifier() == current.identifier()) {
    			//current = new FeatureReal(current.identifier(), f.strength() + current.strength());
    			double strength = v_current.strength() + current.strength();
    			if (strength != 0.0)
    				result.addFeature(new FeatureReal(current.identifier(), strength));
    	    	if (it.hasNext())
    	    		current = it.next();
    	    	else
    	    		done = true;
    	    	if (v_it.hasNext())
    	    		v_current = v_it.next();
    	    	else
    	    		v_done = true;    			
    		}
    		else if (v_current.identifier() < current.identifier()) {
    			result.addFeature(v_current.deepCopy());
    	    	if (v_it.hasNext())
    	    		v_current = v_it.next();
    	    	else
    	    		v_done = true;    			
    		}
    		else if (v_current.identifier() > current.identifier()) {
    			result.addFeature(current);
    	    	if (it.hasNext())
    	    		current = it.next();
    	    	else
    	    		done = true;    			    			
    		}
    	}
    	//addFeatures(added);
    	features = result.features;
    }
    */
    
    public double dot(FeatureVector v) {
    	return dot(this, v);
    }

    public static double dot(FeatureVector v1, FeatureVector v2) {
    	double result = 0.0;
    	v1.idSort();
    	v2.idSort();
    	Iterator<Feature> it1 = v1.iterator();
    	Iterator<Feature> it2 = v2.iterator();
    	Feature f1 = null;
    	Feature f2 = null;
    	if (it1.hasNext())
    		f1 = it1.next();
    	if (it2.hasNext())
    		f2 = it2.next();
    	while (true) {
    		if (f2.identifier() == f1.identifier()) {
    			result += f1.strength() * f2.strength();
    			if (it1.hasNext())
    				f1 = it1.next();
    			else 
    				break;
    			if (it2.hasNext())
    				f2 = it2.next();
    			else
    				break;
    		}
    		else if (f2.identifier() < f1.identifier()) {
    			if (it2.hasNext())
    				f2 = it2.next();
    			else
    				break;
    		}
    		else { // f1 < f2
    			if (it1.hasNext())
    				f1 = it1.next();
    			else 
    				break;
    		}
    	}
    	return result;
    }

    
    // created for MatchMaker, but maybe useful in general
    // note that strength values are thrown away (set to 1.0)
    public static FeatureVector intersection(FeatureVector v1, FeatureVector v2) {
    	FeatureVector result = new FeatureVector();
    	v1.idSort();
    	v2.idSort();
    	Iterator<Feature> it1 = v1.iterator();
    	Iterator<Feature> it2 = v2.iterator();
    	Feature f1 = null;
    	Feature f2 = null;
    	if (it1.hasNext())
    		f1 = it1.next();
    	if (it2.hasNext())
    		f2 = it2.next();
    	while (true) {
    		if (f2.identifier() == f1.identifier()) {
    			result.add(new Feature(f1.identifier));
    			if (it1.hasNext())
    				f1 = it1.next();
    			else 
    				break;
    			if (it2.hasNext())
    				f2 = it2.next();
    			else
    				break;
    		}
    		else if (f2.identifier() < f1.identifier()) {
    			if (it2.hasNext())
    				f2 = it2.next();
    			else
    				break;
    		}
    		else { // f1 < f2
    			if (it1.hasNext())
    				f1 = it1.next();
    			else 
    				break;
    		}
    	}
    	result.trimToSize();
    	return result;
    }

    // created for MatchMaker, but maybe useful in general
    // note that this has the effect of taking the union
    // also note absolute value
    public static FeatureVector difference(FeatureVector v1, FeatureVector v2) {
    	FeatureVector result = new FeatureVector();
    	//System.out.println(v1);
    	//System.out.println(v2);
    	v1.idSort();
    	v2.idSort();
    	Iterator<Feature> it1 = v1.iterator();
    	Iterator<Feature> it2 = v2.iterator();
    	Feature f1 = null;
    	Feature f2 = null;
    	if (it1.hasNext())
    		f1 = it1.next();
    	if (it2.hasNext())
    		f2 = it2.next();
    	boolean finish_v1 = false;
    	boolean finish_v2 = false;
    	while (true) {
    		if (f2.identifier() == f1.identifier()) {
    			result.add(new FeatureReal(f1.identifier, Math.abs(f1.strength() - f2.strength())));
    			if (it1.hasNext())
    				f1 = it1.next();
    			else {
    				finish_v2 = true;
    				break;
    			}
    			if (it2.hasNext())
    				f2 = it2.next();
    			else {
    				finish_v1 = true;
    				break;
    			}
    		}
    		else if (f2.identifier() < f1.identifier()) {
    			result.add(new FeatureReal(f2.identifier(), Math.abs(f2.strength())));
    			if (it2.hasNext())
    				f2 = it2.next();
    			else {
    				finish_v1 = true;
    				break;
    			}
    		}
    		else { // f1 < f2
    			result.add(new FeatureReal(f1.identifier(), Math.abs(f1.strength())));
    			if (it1.hasNext())
    				f1 = it1.next();
    			else {
    				finish_v2 = true;
    				break;
    			}
    		}
    	}
    	if (finish_v1) {
    		//System.out.println("finishing v1");
    		while (it1.hasNext()) {
    			f1 = it1.next();
    			result.add(new FeatureReal(f1.identifier(), Math.abs(f1.strength())));
    		}
    	}
    	if (finish_v2) {
    		//System.out.println("finishing v2");
    		while (it2.hasNext()) {
    			//System.out.println("actually inside");
    			f2 = it2.next();
    			result.add(new FeatureReal(f2.identifier(), Math.abs(f2.strength())));
    		}
    	}
    	result.trimToSize();
    	//System.out.println("R: " + result);
    	return result;
    }
    
    /**
     * Trims the FeatureVector to size (in an effort to save space).
     */
    public void finish() {
    	trimToSize();
    }
    
    /**
     * Sorts the FeatureVector by identifier order.
     */
    public void idSort() {
    	if (sortStatus != ID_SORTED)
    		Collections.sort(this, new idComparator());
    	sortStatus = ID_SORTED;
    }

    /**
     * Sorts the FeatureVector by strength order (used for predictions).
     */
    public void strengthSort() {
    	if (sortStatus != STRENGTH_SORTED)
    		Collections.sort(this, new strengthComparator());
    	sortStatus = STRENGTH_SORTED;
    }

    /**
     * Returns the maximum strength valued Feature.
     * @return	the maximum strength valued Feature
     */
    public Feature strengthMax() {
    	return Collections.min(this, new strengthComparator());
    }

    /**
     * Returns the minimum strength valued Feature.
     * @return	the minimum strength valued Feature
     */
    public Feature strengthMin() {
    	return Collections.max(this, new strengthComparator());
    }
    
    /**
     * Calculates the p-norm of the FeatureVector.
     * 
     * @param p	the specified norm
     * @return	the value of the p-norm
     */
    // TODO - consider caching value
    public double norm(double p) {
    	double result = 0.0;
    	for (Feature feature : this)
    		result += Math.pow(Math.abs(feature.strength()), p);    		
    	result = Math.pow(result, (1.0 / p));
    	return result;
    }

    /**
     * Calculates the 2-norm of the FeatureVector
     * 
     * @return	the value of the 2-norm
     */
    public double norm() {
    	return norm(2);
    }
    
    /**
     * Takes a FeatureVector and scalar, returning a new scaled FeatureVector.
     * This is widely used for additive online learning algorithms (Perceptron).
     * Assumes that any scaling results in a FeatureReal vector.
     * 
     * @param scalar	the value used to scale the input FeatureVector
     * @param v	the FeatureVector to be scaled
     * @return	a scaled FeatureVector
     */
    public static FeatureVector staticScale(double scalar, FeatureVector v) {
    	FeatureVector result = new FeatureVector();
    	for (Feature f : v)
    		result.add(new FeatureReal(f.identifier(), f.strength() * scalar));
    	return result;
    }
    
    /**
     * Used to sort Features in ascending order of Feature identifiers.
     */
    public class idComparator implements Comparator<Feature> {
    	public int compare(Feature f1, Feature f2) {
    		return f1.identifier() - f2.identifier();
    	}
    }
    
    /**
     * Used to sort Features in descending order by Feature strength values
     */
    public class strengthComparator implements Comparator<Feature> {

    	/**	respect hidden status of Features */
    	public boolean descending;
	
    	/**
    	 * the comparator constructor
    	 * 
    	 * @param visibility	indicates if Feature hidden status is respected
    	 */
    	public strengthComparator(boolean descending) {
    		this.descending = descending;
    	}
	
    	public strengthComparator() {
    		this(true);
    	}
    	
    	public int compare(Feature f1, Feature f2) {
    			return descending ? -Double.compare(f1.strength(), f2.strength()) :
    				Double.compare(f1.strength(), f2.strength());
    	}
    }
    
	public FeatureVector copy() {
		FeatureVector result = new FeatureVector();
		result.addAll(this);
		result.sortStatus = sortStatus;
		return result;
	}

	public FeatureVector deepCopy() {
		return new FeatureVector(this);
	}
}
