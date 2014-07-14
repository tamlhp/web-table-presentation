package ch.epfl.modularity.datastructure;

import scenewise.Constant;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.modularity.clustering.hierachy.LinkageStrategy;
import ch.epfl.modularity.matrix.DataMatrix;
import ch.epfl.modularity.matrix.IMetric;
import ch.epfl.modularity.utils.MetricUtils;

public class SAffinityMatrix implements IAffinityMatrix {

    private List<String> listSchemas;
    private Map<Point, String> mapAttributes = null;
    private byte[][] originalMatrix;
    private byte[][] orderedMatrix;
    private double[][] rowMatrix;
    private double[][] colMatrix;
    private String[] baseAttributeName;
    private int numCol, numRow;
    private DataMatrix dm;
    private Point pointGetAttNameHelper = new Point();
    private List<String> rowNames = null;

    public SAffinityMatrix(List<String> listSchemas, byte[][] matrix) throws Exception {
        if (listSchemas.size() != matrix[0].length) {
            throw new Exception("Size must be equal : " + listSchemas.size() + "  " + matrix[0].length);
        }
        dm = new DataMatrix(matrix);

        this.listSchemas = listSchemas;
        this.originalMatrix = matrix;
        numCol = matrix[0].length;
        numRow = matrix.length;


        orderedMatrix = new byte[numRow][numCol];
        orderMatrix(0, Constant.DISTANCE_TYPE_ORIGINAL);

        rowMatrix = new double[numRow][numRow];
        colMatrix = new double[numCol][numCol];

        recalColMatrix(Constant.METRIC_TYPE_JACARD);
        recalRowMatrix(Constant.METRIC_TYPE_JACARD);
    }

    public SAffinityMatrix(List<String> listSchemas, Map<Point, String> mapAttName, byte[][] matrix) throws Exception {
        this(listSchemas, matrix);
        this.mapAttributes = mapAttName;
    }

    public SAffinityMatrix(List<String> listSchemas, byte[][] matrix, List<String> rowNames) throws Exception {
        if (listSchemas.size() != matrix[0].length) {
            throw new Exception("Size must be equal : " + listSchemas.size() + "  " + matrix[0].length);
        }
        dm = new DataMatrix(matrix, listSchemas, rowNames);

        this.listSchemas = listSchemas;
        this.originalMatrix = matrix;
        numCol = matrix[0].length;
        numRow = matrix.length;

        orderedMatrix = new byte[numRow][numCol];
        orderMatrix(0, Constant.DISTANCE_TYPE_ORIGINAL);

        rowMatrix = new double[numRow][numRow];
        colMatrix = new double[numCol][numCol];

        recalColMatrix(Constant.METRIC_TYPE_JACARD);
        recalRowMatrix(Constant.METRIC_TYPE_JACARD);
        this.rowNames = rowNames;
    }

    @Override
    public List<String> getSchemaList() {
        return listSchemas;
    }

    @Override
    public byte[][] getMatrix() {
        return orderedMatrix;
    }

    public byte[][] getOriginalMatrix() {
        return originalMatrix;
    }

    /**
     *
     */
    @Override
    public String getAttributeName(int i, int j) {
        if (mapAttributes == null || mapAttributes.isEmpty()) {
            return "a" + dm.getColIndexAt(j) + "." + dm.getRowIndexAt(i);
        }
        pointGetAttNameHelper.setLocation(dm.getColIndexAt(j), dm.getRowIndexAt(i));
        return mapAttributes.get(pointGetAttNameHelper);
    }

    public String getAttributeName(Point p) {
        if (mapAttributes == null || mapAttributes.isEmpty()) {
            return "a" + dm.getColIndexAt(p.x) + "." + dm.getRowIndexAt(p.y);
        }
        pointGetAttNameHelper.setLocation(dm.getColIndexAt(p.x), dm.getRowIndexAt(p.y));
        return mapAttributes.get(pointGetAttNameHelper);
    }

    @Override
    public String getSchemaName(int j) {
        return listSchemas.get(dm.getColIndexAt(j));
    }

    @Override
    public boolean isAppear(int i, int j) {
        return i >= 0 && i < numRow && j >= 0 && j < numCol && orderedMatrix[i][j] > 0;
    }

    @Override
    public int getNumCol() {
        return numCol;
    }

    @Override
    public int getNumRow() {
        return numRow;
    }

    @Override
    public int findAtt(String query, Set<Point> result) {
        result.clear();
        // test
        System.out.println("numRow = " + numRow + "   numCol = " + numCol);
        Point p = new Point();
        for (int i = 0; i < originalMatrix.length; i++) {
            for (int j = 0; j < originalMatrix[i].length; j++) {
                if (isAppear(i, j)) {
                    p.setLocation(j, i);
                    if (getAttributeName(p).contains(query)) {
                        result.add(new Point(j, i));
                    }
                }
            }
        }

        return result.size();
    }

    // Test
    @Override
    public List<Schema> createSchemas(List<Point> listPoint) {
        HashMap<Integer, Schema> map = new HashMap<>();
        for (Point p : listPoint) {
            if (map.containsKey(p.x)) {
                Schema s = map.get(p.x);
                s.addChild(new Attribute(s, getAttributeName(p.x, p.y)));
            } else {
                Schema s = new Schema(listSchemas.get(p.x));
                map.put(p.x, s);
                s.addChild(new Attribute(s, getAttributeName(p.x, p.y)));
            }
        }

        List<Schema> list = new ArrayList<>();
        for (Integer i : map.keySet()) {
            list.add(map.get(i));
        }
        return list;

    }

    @Override
    public void orderMatrix(int metricType, int type) {
        if (type == Constant.DISTANCE_TYPE_ORIGINAL) {
            orderedMatrix = originalMatrix;
            dm.resetRowColOrder();
            return;
        }

        LinkageStrategy ls = generateLS(type);
        IMetric metric = generateMetric(metricType);
        orderedMatrix = dm.order(metric, ls);

    }
    
    public int[]getOrderRow(){
        return dm.getRowOrder();
    }
    
    public int[]getOrderCol(){
        return dm.getColOrder();
    }

    @Override
    public void writeMatrix(String filename) {
        try {
            dm.writeFile(filename);
        } catch (IOException e) {
            // TODO throw ERROR Dialog
            e.printStackTrace();
        }
    }

    private LinkageStrategy generateLS(int type) {
        switch (type) {
            case Constant.DISTANCE_TYPE_AVERAGE_LINKAGE:
                return Constant.LS_AVERAGE;
            case Constant.DISTANCE_TYPE_COMPLETE_LINKAGE:
                return Constant.LS_COMPLETE;
            case Constant.DISTANCE_TYPE_SINGLE_LINKAGE:
                return Constant.LS_SINGLE;
            default:
                return null;
        }
    }

    private IMetric generateMetric(int type) {
        switch (type) {
            case Constant.METRIC_TYPE_JACCARD_1:
                return Constant.METRIC_JACCARD1;
            case Constant.METRIC_TYPE_JACCARD_2:
                return Constant.METRIC_JACCARD2;
            case Constant.METRIC_TYPE_MANHATTAN:
                return Constant.METRIC_MANHATTAN;
            case Constant.METRIC_TYPE_HAMMING:
                return Constant.METRIC_HAMMING;
            default:
                return null;
        }
    }

    private void recalRowMatrix(int metrictype) {
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < i; j++) {
                if (i == j) {
                    colMatrix[i][j] = 0;
                } else {
                    rowMatrix[j][i] = rowMatrix[i][j] = MetricUtils.distance(originalMatrix[i], originalMatrix[j],
                            metrictype);
                }
            }
        }
    }

    private void recalColMatrix(int metrictype) {
        for (int i = 0; i < numCol; i++) {
            for (int j = 0; j < i; j++) {
                if (i == j) {
                    colMatrix[i][j] = 0;
                } else {
                    colMatrix[j][i] = colMatrix[i][j] = MetricUtils.distance(MetricUtils.getCol(originalMatrix, i),
                            MetricUtils.getCol(originalMatrix, j), metrictype);
                }
            }
        }
    }

    public Map<Point, String> getMapAttributes() {
        return mapAttributes;
    }

    @Override
    public String[] getBaseAttributeName() {
        baseAttributeName = new String[numRow];
        if (rowNames != null) {
            for (int i = 0; i < numRow; i++) {
                baseAttributeName[i] = rowNames.get(dm.getRowIndexAt(i));
            }
        } else {
            for (int i = 0; i < numRow; i++) {
                for (int j = 0; j < numCol; j++) {
                    if (isAppear(i, j)) {
                        baseAttributeName[i] = getAttributeName(i, j);
                        break;
                    }
                }
            }
        }
        return baseAttributeName;
    }
}
