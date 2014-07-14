/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.epfl.modularity.matrix;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import ch.epfl.modularity.clustering.hierachy.Cluster;
import ch.epfl.modularity.clustering.hierachy.ClusteringAlgorithm;
import ch.epfl.modularity.clustering.hierachy.DefaultClusteringAlgorithm;
import ch.epfl.modularity.clustering.hierachy.LinkageStrategy;

/**
 * 
 * @author kubin
 */
public class DataMatrix {

	protected Matrix matrix;
	protected Matrix orderedMatrix;
	protected double[][] rowmatrix;
	protected double[][] colmatrix;
	protected int[] colOrder;
	protected int[] rowOrder;
	protected List<String> colName;
	protected List<String> rowName;
	protected DefaultClusteringAlgorithm dca;

	public DataMatrix(byte[][] matrix) {
		this.matrix = new Matrix(matrix);
		dca = new DefaultClusteringAlgorithm();
		rowOrder = new int[matrix.length];
		colOrder = new int[matrix[0].length];

		for (int i = 0; i < rowOrder.length; i++) {
			rowOrder[i] = i;
		}
		for (int i = 0; i < colOrder.length; i++) {
			colOrder[i] = i;
		}
	}

	public DataMatrix(byte[][] matrix, List<String> colName, List<String> rowName) {
		this(matrix);
		this.colName = colName;
		this.rowName = rowName;
	}

	protected void generateRowColMatrix(IMetric metric) {
		rowmatrix = new double[matrix.getNumRow()][matrix.getNumRow()];
		colmatrix = new double[matrix.getNumCol()][matrix.getNumCol()];

		for (int i = 0; i < matrix.getNumRow(); i++) {
			for (int j = 0; j <= i; j++) {
				rowmatrix[i][j] = rowmatrix[j][i] = metric.distance(matrix.getRow(i), matrix.getRow(j));
			}
		}

		for (int i = 0; i < matrix.getNumCol(); i++) {
			for (int j = 0; j <= i; j++) {
				colmatrix[i][j] = colmatrix[j][i] = metric.distance(matrix.getCol(i), matrix.getCol(j));
			}
		}

	}

	public double[][] getRowMatrix() {
		return rowmatrix;
	}

	public double[][] getColMatrix() {
		return colmatrix;
	}

	public byte[][] order(IMetric metric, LinkageStrategy ls) {
		try {
			generateRowColMatrix(metric);
			rowOrder = new int[matrix.getNumRow()];
			colOrder = new int[matrix.getNumCol()];
			this.orderedMatrix = matrix.clone();

			OrderThread rowThread = new OrderThread(rowmatrix, rowOrder, ls);
			OrderThread colThread = new OrderThread(colmatrix, colOrder, ls);
			long t0 = System.currentTimeMillis();
			rowThread.start();
			colThread.start();
			rowThread.join();
			colThread.join();
			t0 = System.currentTimeMillis() - t0;
			System.out.println("order time = " + t0);
			this.orderedMatrix.newRowOrder(rowOrder);
			this.orderedMatrix.newColOrder(colOrder);

			printMatrix();

			return this.orderedMatrix.getMatrix();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// TODO change cluster name to cluster ssn id
	public static String[] createClusterName(int size) {
		String[] arr = new String[size];
		for (int i = 0; i < size; i++) {
			arr[i] = "" + i;
		}

		return arr;
	}

	public int getRowIndexAt(int index) {
		return rowOrder[index];
	}

	public int getColIndexAt(int index) {
		return colOrder[index];
	}

	public void resetRowColOrder() {
		for (int i = 0; i < rowOrder.length; i++) {
			rowOrder[i] = i;
		}
		for (int i = 0; i < colOrder.length; i++) {
			colOrder[i] = i;
		}
	}

	public void printMatrix() {
		if (rowName == null || colName == null) {
			printMatrix2();
		} else {
			printMatrix1();
		}
	}

	protected void printMatrix1() {
		int maxRowNameLength = 0;
		for (String str : rowName) {
			if (maxRowNameLength < str.length()) {
				maxRowNameLength = str.length();
			}
		}

		int maxColNameLength = 4;
		for (String str : colName) {
			if (maxColNameLength < str.length()) {
				maxColNameLength = str.length();
			}
		}

		System.out.printf("%" + maxRowNameLength + "s", " ");
		for (int i = 0; i < colName.size(); i++) {
			System.out.printf(" %" + maxColNameLength + "s", colName.get(colOrder[i]));
		}

		System.out.println();
		byte[][] m = orderedMatrix.getMatrix();
		for (int i = 0; i < m.length; i++) {
			System.out.printf("%" + maxRowNameLength + "s", rowName.get(rowOrder[i]));
			for (int j = 0; j < m[i].length; j++) {
				System.out.printf(" %" + maxColNameLength + "d", m[i][j]);
			}
			System.out.println();
		}

	}

	protected void printMatrix2() {
		byte[][] m = this.orderedMatrix.getMatrix();
		System.out.print("    ");
		for (int i : colOrder) {
			System.out.printf("%4d", i);
		}
		System.out.println();
		for (int i = 0; i < m.length; i++) {
			System.out.printf("%4d", rowOrder[i]);
			for (int j = 0; j < m[i].length; j++) {
				System.out.printf("%4d", m[i][j]);
			}
			System.out.println();
		}
	}

	public void writeFile(String filename) throws IOException {
		if (!filename.endsWith(".csv")) {
			filename += ".csv";
		}
		
		if(rowName == null || colName == null){
			writeFile2(filename);
		}
		else{
			writeFile1(filename);
		}
	}

	protected void writeFile1(String filename) throws IOException {
		PrintWriter writer = new PrintWriter(filename);

		for (int i = 0; i < colName.size(); i++) {
			writer.write("," + colName.get(colOrder[i]));
		}
		writer.write("\n");
		byte[][] m = this.orderedMatrix.getMatrix();
		for (int i = 0; i < m.length; i++) {
			writer.write(rowName.get(rowOrder[i]));
			for (int j = 0; j < m[i].length; j++) {
				writer.write("," + m[i][j]);
			}
			writer.write("\n");
		}
		
		writer.close();
	}
	
	protected void writeFile2(String filename) throws IOException {
		PrintWriter writer = new PrintWriter(filename);

		for (int i:colOrder) {
			writer.write("," + i);
		}
		writer.write("\n");
		byte[][] m = this.orderedMatrix.getMatrix();
		for (int i = 0; i < m.length; i++) {
			writer.write(rowOrder[i]);
			for (int j = 0; j < m[i].length; j++) {
				writer.write("," + m[i][j]);
			}
			writer.write("\n");
		}
		
		writer.close();
	}
        
        public int[]getRowOrder(){
            return rowOrder;
        }

        public int[]getColOrder(){
            return colOrder;
        }
        
	public class OrderThread extends Thread {

		private int[] orderArray;
		private double[][] matrix;
		private LinkageStrategy ls;
		private ClusteringAlgorithm clusteringAlgorithm;

		public OrderThread(double[][] matrix, int[] orderArray, LinkageStrategy ls) {
			this.orderArray = orderArray;
			this.matrix = matrix;
			this.ls = ls;
			clusteringAlgorithm = new DefaultClusteringAlgorithm();
		}

		@Override
		public void run() {
			Cluster c = clusteringAlgorithm.performClustering(matrix, ls);

			List<Cluster> list = c.getChildrenLeaf();

			for (int i = 0; i < orderArray.length; i++) {
				orderArray[i] = list.get(i).getSSN();
			}

		}
	}
}
