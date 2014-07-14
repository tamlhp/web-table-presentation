/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataprocessing.sampling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utils.FileUtils;
import utils.GsonUtil;

/**
 *
 * @author Tuan Chau
 */
public class BuildSampling {

    public static void main(String[] args) throws IOException {
        File dir = new File("D:/epfl/schema/sampling");
        File[] files = dir.listFiles();
        SData data = new SData();
        for (File f : files) {
            data.addSchema(parseFile(f, data));
        }
        
        String str = GsonUtil.toJson(data);
        FileUtils.write("D:/epfl/schema/sampling/sampling_data.json", str);
    }

    private static SSchema parseFile(File file, SData data) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        String[] arr = line.split("\t");
        List<String> atts = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            if (i % 2 == 0) {
                data.addAttribute(arr[i], i / 2);
                atts.add(arr[i]);
            }
        }
        SSchema schema = new SSchema(file.getName().replace(".csv", ""), atts);
        line = reader.readLine();
        int count = 0;
        while (line != null) {
            arr = line.split("\t");
            SInstance[] ins = new SInstance[arr.length / 2];
            for(int i = 0; i < arr.length; i++){
                int cid = data.getCatId(arr[i + 1], i/2);
                ins[i/2] = new SInstance(arr[i], cid);
                
                i++;
            }
            schema.addInstances(count++, ins);
            line = reader.readLine();
        }


        return schema;
    }
}
