package ml.learn.online;

import ml.instance.Label;
import core.function.Input;

public interface Online {

	public void train(Input input, Label target);
	//public void train(Input input, Function predictor);
	public void train(Input input);
	
}
