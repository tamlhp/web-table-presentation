package crawlgooglewebdata.experiment;

public class IDGenerator {
	private static int intID = 0;
	public static int nextIntID(){
		intID++;
		return intID;
	}
	
	public static void reset() {
		intID = 0;
	}
}
