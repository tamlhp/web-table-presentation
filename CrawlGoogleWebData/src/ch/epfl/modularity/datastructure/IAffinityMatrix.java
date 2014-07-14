package ch.epfl.modularity.datastructure;

import java.awt.Point;
import java.util.List;
import java.util.Set;

public interface IAffinityMatrix {
	public List<String> getSchemaList();
	public byte[][]getMatrix();
	public String getAttributeName(int i, int j);
	public String[]getBaseAttributeName();
	public boolean isAppear(int i, int j);
	public int getNumRow();
	public int getNumCol();
	public int findAtt(String query, Set<Point> result);
	public List<Schema> createSchemas(List<Point> listPoint);
	public void orderMatrix(int metricType, int linkageType);
	public String getSchemaName(int i);
	public void writeMatrix(String filename);
}
