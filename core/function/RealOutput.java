package core.function;

public class RealOutput implements Output {

	protected Double value;
	
	public RealOutput(Double value) {
		this.value = value;
	}	
	
	public double value() {
		return value.doubleValue();
	}
}
