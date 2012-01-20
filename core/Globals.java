package core;

import java.text.DecimalFormat;
import java.util.Random;

public class Globals {

	public static double sigmoid(double value) {
		return 1.0 / (1.0 + Math.exp(-value));
	}
	
	// text formatting stuff
	public static DecimalFormat formatPercent = new DecimalFormat("#0.00%");
	
    // RNG stuff
    public static long rngSeed = System.currentTimeMillis();
    public static Random rng = new Random(rngSeed);

    public static void seedRNG(long s) {
    	rngSeed = s;
    	rng = new Random(rngSeed);
    }
    
    public static double nextDouble() {
    	return rng.nextDouble();
    }
    
    public static int nextInt() {
    	return rng.nextInt();
    }
    
    public static int nextInt(int n) {
    	return rng.nextInt(n);
    }
    
    public static double nextGaussian() {
    	return rng.nextGaussian();
    }
    
    public static double nextGaussian(double mean, double stddev) {
    	return nextGaussian() * stddev + mean;
    }
    
    public static int nextGaussianInt(int mean, int stddev) {
    	return (int) Math.round(nextGaussian(mean, stddev));
    }
    
    public static int nextGaussianInt(int mean, double stddev) {
    	return (int) Math.round(nextGaussian(mean, stddev));
    }	
}
