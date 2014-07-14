package dataprocessing.sampling;

public class SCategory {
	public String value;
	public int id;
	public int frequency;
	
	public SCategory() {
		id = -1;
		value = "";
		frequency = 0;
	}
	
	public SCategory(int id, String value ) {
		this.id = id;
		this.value = value;
		frequency = 0;
	}
	
	public int increaseFrequency() {
		frequency++;
		return frequency;
	}
	
	public int decreaseFrequency() {
		frequency--;
		return frequency;
	}
}
