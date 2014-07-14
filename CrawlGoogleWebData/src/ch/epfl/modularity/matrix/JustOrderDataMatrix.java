/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.epfl.modularity.matrix;

import ch.epfl.modularity.clustering.hierachy.Cluster;
import ch.epfl.modularity.clustering.hierachy.ClusteringAlgorithm;
import ch.epfl.modularity.clustering.hierachy.DefaultClusteringAlgorithm;
import ch.epfl.modularity.clustering.hierachy.LinkageStrategy;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tuan Chau
 */
public class JustOrderDataMatrix {

    protected int[] colOrder;
    protected int[] rowOrder;

    public JustOrderDataMatrix() {
    }

    public int[][] justOrder(byte[][] matrix, IMetric metric, LinkageStrategy ls) {
        try {
            Matrix mmatrix = new Matrix(matrix);
            double [][]rowmatrix = generateRowMatrix(mmatrix, metric);
            double [][]colmatrix = generateColMatrix(mmatrix, metric);
            
            rowOrder = new int[mmatrix.getNumRow()];
            colOrder = new int[mmatrix.getNumCol()];

            OrderThread rowThread = new OrderThread(rowmatrix, rowOrder, ls);
            OrderThread colThread = new OrderThread(colmatrix, colOrder, ls);

            rowThread.start();
            colThread.start();
            rowThread.join();
            colThread.join();

            return new int[][]{rowOrder, colOrder};
        } catch (InterruptedException ex) {
            Logger.getLogger(JustOrderDataMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    protected double[][] generateRowMatrix(Matrix matrix, IMetric metric) {
        double[][] rowmatrix = new double[matrix.getNumRow()][matrix.getNumRow()];

        for (int i = 0; i < matrix.getNumRow(); i++) {
            for (int j = 0; j <= i; j++) {
                rowmatrix[i][j] = rowmatrix[j][i] = metric.distance(matrix.getRow(i), matrix.getRow(j));
            }
        }

        return rowmatrix;

    }

    protected double[][] generateColMatrix(Matrix matrix, IMetric metric) {
        double[][] colmatrix = new double[matrix.getNumCol()][matrix.getNumCol()];

        for (int i = 0; i < matrix.getNumCol(); i++) {
            for (int j = 0; j <= i; j++) {
                colmatrix[i][j] = colmatrix[j][i] = metric.distance(matrix.getCol(i), matrix.getCol(j));
            }
        }
        return colmatrix;
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
