package ch.epfl.modularity.utils;

public class MetricUtils {
	public static double distance(byte[] v1, byte[] v2, int type) {
		int d = 0;
		for (int i = 0; i < v1.length; i++) {
			d += (v1[i] - v2[i]) * (v1[i] - v2[i]);
		}
		return Math.sqrt(d);
	}

	public static double[] getCol(double [][]matrix, int i){
		double []result = new double[matrix[0].length];
		for(int j = 0; j < result.length; i++){
			result[j] = matrix[j][i];
		}

		return result;
	}

	public static byte[] getCol(byte [][]matrix, int j){
		byte []result = new byte[matrix.length];
//		Log.o("MetricUtil", "result length = " + result.length);
		for(int i = 0; i < result.length; i++){
			result[i] = matrix[i][j];
		}

		return result;
	}
}
