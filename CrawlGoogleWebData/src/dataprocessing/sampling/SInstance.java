package dataprocessing.sampling;

public class SInstance {
	public String value;
	public int catId;
//    public int aid;
	
	public SInstance() {
		value = "";
		catId = -1;
	}
	
	public SInstance(String value, int catId) {
		this.value = value;
		this.catId = catId;
	}
}
