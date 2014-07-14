package ch.epfl.modularity.utils;

public class Utils {
	public static double[][] cloneMatrix(double [][]matrix){
		double [][]result = new double[matrix.length][];
		
		for(int i = 0 ; i < matrix.length; i++){
			result[i] = new double[matrix[i].length];
			for(int j = 0; j < matrix[i].length; j++){
				result[i][j] = matrix[i][j];
			}
		}
		
		return result;
	}
	
	public static byte[][] cloneMatrix(byte [][]matrix){
		byte [][]result = new byte[matrix.length][];
		
		for(int i = 0 ; i < matrix.length; i++){
			result[i] = new byte[matrix[i].length];
			for(int j = 0; j < matrix[i].length; j++){
				result[i][j] = matrix[i][j];
			}
		}
		
		return result;
	}
	
	
}
