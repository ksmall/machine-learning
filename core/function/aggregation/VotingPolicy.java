package core.function.aggregation;

import java.util.ArrayList;

import core.function.Function;
import core.function.Output;

public abstract class VotingPolicy extends ArrayList<Function> {
	
	private static final long serialVersionUID = 1L;

	public abstract Output vote();
	
}
