package ch.epfl.modularity.matrix;

public class Jaccard2Metric implements IMetric {

	@Override
	public double distance(byte[] arr1, byte[] arr2) {
		if (arr1.length != arr2.length) {
            throw new IllegalArgumentException("two arrays must same length");
        }
        int c11 = 0;
       
       
        for(int i = 0; i < arr1.length; i++){
        	if(arr1[i] == arr2[i]){
        		c11++;
        	}
        	
        }
        
        return (double)c11 / (double)(arr1.length + arr2.length - c11);
	}

}
