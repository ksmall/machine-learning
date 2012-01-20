package core.utility;

import java.util.HashMap;

public class StopWatch {
    
    public HashMap<String,Long> stopWatches;

    public StopWatch() {
    	stopWatches = new HashMap<String,Long>();
    }

    public void startTime(String identifier) {
    	stopWatches.put(identifier, new Long(System.currentTimeMillis()));
    }
    
    public long elapsedTime(String identifier) {
	Long startTime = (Long) stopWatches.get(identifier);
	if (startTime == null)
	    return 0L;
	else 
	    return (System.currentTimeMillis() - startTime.longValue());
    }

    public String printTime(String identifier) {
	return new String(identifier + ": " + 
			  (elapsedTime(identifier) / 1000) + "s");
    }
}
	    
