/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.epfl.modularity.matrix;

/**
 * 
 * @author kubin
 */
public class Matrix {

	private byte[][] matrix;
	private byte[][] tmatrix;
	private int numrow;
	private int numcol;

	public Matrix(byte[][] matrix) {
		this.matrix = matrix;
		numrow = matrix.length;
		numcol = matrix[0].length;
		tmatrix = new byte[numcol][numrow];

		for (int j = 0; j < numcol; j++) {
			for (int i = 0; i < numrow; i++) {
				tmatrix[j][i] = matrix[i][j];
			}
		}
	}

	public byte[] getRow(int index) {
		if (index < 0 || index >= matrix.length) {
			throw new ArrayIndexOutOfBoundsException("index must between: [0," + (numrow - 1) + "], index = " + index);
		}
		return matrix[index];
	}

	public byte[] getCol(int index) {
		if (index < 0 || index >= numcol) {
			throw new ArrayIndexOutOfBoundsException("index must between: [0," + (numcol - 1) + "], index = " + index);
		}
		return tmatrix[index];
	}

	public int getNumCol() {
		return numcol;
	}

	public int getNumRow() {
		return numrow;
	}

	public byte getValue(int r, int c) {
		if (r < 0 || c < 0 || r >= numrow || c >= numcol) {
			throw new ArrayIndexOutOfBoundsException("r and must between [0," + (numrow - 1) + "] and [0,"
					+ (numcol - 1) + "], r=" + r + ", c=" + c);
		}
		return matrix[r][c];
	}

	public byte[][] getMatrix() {
		return matrix;
	}

	@Override
	public Matrix clone() {
		byte[][] m = new byte[numrow][numcol];
		for (int i = 0; i < numrow; i++) {
			System.arraycopy(this.matrix[i], 0, m[i], 0, numcol);
		}
		return new Matrix(m);
	}

	public void swapRow(int r1, int r2, boolean swapTMatrix) {
		if (r1 == r2) {
			return;
		}
		swapRow(matrix, r1, r2);
		if (swapTMatrix) {
			swapCol(tmatrix, r1, r2);
		}
	}

	public void swapCol(int c1, int c2, boolean swapTMatrix) {
		if (c1 == c2) {
			return;
		}
		swapCol(matrix, c1, c2);
		if (swapTMatrix) {
			swapRow(tmatrix, c1, c2);
		}
	}

	private void swapRow(byte[][] matrix, int r1, int r2) {
		byte[] tmp = matrix[r1];
		matrix[r1] = matrix[r2];
		matrix[r2] = tmp;
	}

	private void swapCol(byte[][] matrix, int c1, int c2) {
		byte t = 0;
		for (int i = 0; i < numrow; i++) {
			t = matrix[i][c1];
			matrix[i][c1] = matrix[i][c2];
			matrix[i][c2] = t;
		}
	}

	public void newRowOrder(int[] order) {
		byte[][] n = new byte[numrow][numcol];
		for (int i = 0; i < numrow; i++) {
			for (int j = 0; j < numcol; j++) {
				n[i][j] = matrix[order[i]][j];
			}
		}
		matrix = n;
	}

	public void newColOrder(int[] order) {
		byte[][] n = new byte[numrow][numcol];
		for (int i = 0; i < numrow; i++) {
			for (int j = 0; j < numcol; j++) {
				n[i][j] = matrix[i][order[j]];
			}
		}
		matrix = n;
	}

}
