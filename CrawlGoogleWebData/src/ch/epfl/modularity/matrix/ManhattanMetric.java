package ch.epfl.modularity.matrix;

public class ManhattanMetric implements IMetric {

	@Override
	public double distance(byte[] arr1, byte[] arr2) {
		if (arr1.length != arr2.length) {
			throw new IllegalArgumentException("two arrays must same length");
		}
		double result = 0;
		for (int i = 0; i < arr1.length; i++) {
			result += Math.abs(arr1[i] - arr2[i]);
		}

		return result;
	}

}
