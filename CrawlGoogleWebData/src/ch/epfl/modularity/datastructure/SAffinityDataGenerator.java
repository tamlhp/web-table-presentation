package ch.epfl.modularity.datastructure;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class SAffinityDataGenerator {

    public static SAffinityMatrix generateAffinityMatrixFromXML(File file) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(file);

        XPathExpression<Attribute> attExpression = XPathFactory.instance().compile("/data/sources/@numOfAtt",
                Filters.attribute());
        Attribute att = attExpression.evaluateFirst(doc);
        if (att == null) {
            throw new IOException("Wrong xml format");
        }
        int numOfAtts = att.getIntValue();

        XPathExpression<Element> schemaExpression = XPathFactory.instance().compile("/data/sources/source/schema",
                Filters.element());
        List<Element> schemasList = schemaExpression.evaluate(doc);
        int numOfSchemas = schemasList.size();

        byte[][] matrix = new byte[numOfAtts][numOfSchemas];
        ArrayList<String> listSchemasName = new ArrayList<String>();
        Map<Point, String> mapAttributes = new HashMap<Point, String>();

        for (int i = 0; i < numOfAtts; i++) {
            for (int j = 0; j < numOfSchemas; j++) {
                matrix[i][j] = 0;
            }
        }

        int j = 0;
        for (Element schema : schemasList) {
            List<Element> attributes = schema.getChildren("attribute");
            for (Element attribute : attributes) {
                Attribute mapID = attribute.getAttribute("mapID");
                if (mapID != null) {
                    int rowID = mapID.getIntValue() - 1;
                    Attribute attName = attribute.getAttribute("name");
                    assert attName != null;
                    if (attName != null) {
                        matrix[rowID][j] = 1;
                        mapAttributes.put(new Point(j, rowID), attName.getValue().trim());
                    }
                }
            }

            Attribute name = ((Element) schema.getParent()).getAttribute("name");
            listSchemasName.add(name.getValue().trim());
            j++;
        }

        return new SAffinityMatrix(listSchemasName, mapAttributes, matrix);
    }

    public static SAffinityMatrix genereateMatrixXMLs(File[] files) throws Exception {
        SAffinityMatrix[] amatrixs = new SAffinityMatrix[files.length];
        for (int i = 0; i < amatrixs.length; i++) {
            amatrixs[i] = generateAffinityMatrixFromXML(files[i]);
        }

        // merge
        int numRow = 0;
        int numCol = 0;
        for (SAffinityMatrix a : amatrixs) {
            numCol += a.getNumCol();
            numRow += a.getNumRow();
        }

        byte[][] matrix = new byte[numRow][numCol];
        ArrayList<String> listSchemas = new ArrayList<>();
        Map<Point, String> mapAttName = new HashMap<>();
        int row = 0;
        int col = 0;
        for (SAffinityMatrix a : amatrixs) {
            byte[][] subMatrix = a.getOriginalMatrix();
            for (int i = 0; i < subMatrix.length; i++) {
                for (int j = 0; j < subMatrix[i].length; j++) {
                    matrix[row + i][col + j] = subMatrix[i][j];
                }
            }

            List<String> listSchemaName = a.getSchemaList();
            listSchemas.addAll(listSchemaName);
            Map<Point, String> mapAName = a.getMapAttributes();
            for (Point p : mapAName.keySet()) {
                String name = mapAName.get(p);
                p.x += col;
                p.y += row;

                mapAttName.put(p, name);
            }

            row += a.getNumRow();
            col += a.getNumCol();
        }

        return new SAffinityMatrix(listSchemas, mapAttName, matrix);
    }

    public static SAffinityMatrix generateMatrixCSV(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        String[] arr = line.split(",");
        List<String> listSchemas = new ArrayList<>();
        for (int i = 1; i < arr.length; i++) {
            listSchemas.add(arr[i].trim());
        }
        List<String> listRows = new ArrayList<>();
        line = reader.readLine();
        List<byte[]> list = new ArrayList<byte[]>();
        while (line != null) {
            arr = line.split(",");
            listRows.add(arr[0].trim());
            byte[] bb = new byte[arr.length - 1];
            for (int i = 1; i < arr.length; i++) {
                bb[i - 1] = (byte) Float.parseFloat(arr[i].trim());
            }

            list.add(bb);

            line = reader.readLine();
        }
        byte[][] bytes = new byte[list.size()][];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = list.get(i);
        }
        reader.close();

        SAffinityMatrix matrix = new SAffinityMatrix(listSchemas, bytes, listRows);
        return matrix;
    }

    public static SAffinityMatrix generateMatrix2(File file) throws FileNotFoundException, IOException, Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        List<byte[]> list = new ArrayList<byte[]>();
        int numCol = 0;
        while (line != null) {
            line = line.trim();
            String[] arr = line.split("\t");
            byte[] bb = new byte[arr.length];
            for (int i = 0; i < arr.length; i++) {
                bb[i] = (byte) Float.parseFloat(arr[i]);
            }
            list.add(bb);

            line = reader.readLine();
        }
        numCol = list.get(0).length;
        ArrayList<String> listSchema = new ArrayList<String>();
        for (int i = 0; i < numCol; i++) {
            listSchema.add("S" + i);
        }
        byte[][] bytes = new byte[list.size()][];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = list.get(i);
        }
        reader.close();
        SAffinityMatrix matrix = new SAffinityMatrix(listSchema, bytes);
        return matrix;
    }
}
