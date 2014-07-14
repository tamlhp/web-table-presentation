package ch.epfl.modularity.matrix;

public class HammingMetric implements IMetric {

	@Override
	public double distance(byte[] arr1, byte[] arr2) {
		if (arr1.length != arr2.length) {
			throw new IllegalArgumentException("two arrays must same length");
		}
		int count = 0;
		for(int i = 0; i < arr1.length; i++){
			if(arr1[i] != arr2[i]){
				count++;
			}
		}
		return count;
	}

}
