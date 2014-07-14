package ch.epfl.modularity.matrix;

public class Jaccard1Metric implements IMetric{

	@Override
	public double distance(byte[] arr1, byte[] arr2) {
		if (arr1.length != arr2.length) {
			throw new IllegalArgumentException("two arrays must same length");
		}
		int c11 = 0;
		int c10 = 0;
		int c01 = 0;

		double d = 0;
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] == 1) {
				if (arr2[i] == 1) {
					c11++;
				} else {
					c10++;
				}
			} else {
				if (arr2[i] == 1) {
					c01++;
				}
			}
		}

		return (double) (c10 + c01) / (double) (c11 + c10 + c01);
	}

}
