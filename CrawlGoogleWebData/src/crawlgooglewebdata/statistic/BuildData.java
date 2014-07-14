/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic;

import static crawlgooglewebdata.statistic.Static1_Counter.*;
import crawlgooglewebdata.statistic.datastruct.GSData;
import crawlgooglewebdata.statistic.datastruct.GSSchema;
import dataprocessing.sampling.SInstance;
import dataprocessing.sampling.SRow;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.FileUtils;
import utils.GsonUtil;

/**
 *
 * @author Tuan Chau
 */
public class BuildData {

    public static GSSchema buildSchema(File file) {
        List<String> list = FileUtils.getList(file, "\n");
        List<String> atts = parseCsv(list.get(0));

        GSSchema schema = new GSSchema(file.getName(), atts);
        System.out.println(file.getAbsolutePath());
        for (int i = 1; i < list.size(); i++) {
            List<String> arr = parseCsv(list.get(i));
            if (arr.size() != atts.size()) {
                continue;
            }
            List<SInstance> instance = new ArrayList<>();
            System.out.println(list.get(i));
            for (String s : arr) {
                instance.add(new SInstance(s, -1));
            }
            SRow row = new SRow(i - 1, instance);

            schema.addInstances(row);
        }
        return schema;
    }

    private static List<String> parseCsv(String line) {
        if (line.endsWith(",")) {
            line += " ";
        }
        String[] arr = line.split(",");
        System.out.println(arr.length);
        System.out.println(Arrays.toString(arr));
        boolean opened = false;
        List<String> list = new ArrayList<>();
        String last = "";
        for (int i = 0; i < arr.length; i++) {
//            arr[i] = arr[i].trim();
            if (!opened) {
                if (arr[i].trim().startsWith("\"")) {
                    opened = true;
                    last = arr[i];
                } else {
                    list.add(arr[i]);
                }
            } else {
                last += "," + arr[i];
                if (arr[i].trim().endsWith("\"")) {
                    opened = false;
                    list.add(last);
                    last = "";
                }
            }
        }
//        if(line.trim().endsWith(",")){
//            list.add("");
//        }
        return list;
    }

    public static GSData buildData(String keyword) {
        GSData data = new GSData(keyword);
        File[] files = Utils.getFiles(BASE_DIR + keyword);
        for (File f : files) {
            GSSchema schema = buildSchema(f);
            data.addSchema(schema);
            data.numberOfTable++;
            data.averageOfAttribute += schema.getNumberOfAttribute();
            data.averageOfRow += schema.getNumberOfRow();
        }

        data.averageOfAttribute /= (float) (data.numberOfTable);
        data.averageOfRow /= (float) data.numberOfTable;

        return data;
    }

    public static void builDataJson() {
        List<String> list = FileUtils.getList(BASE_DIR + FILE_RESOURCE_STATISTIC, "\n");
        Map<String, Long> map = new HashMap<>();
        for (String s : list) {
            String[] arr = s.split("\t");
            long l = Long.parseLong(arr[1].replace(",", "").trim());
            map.put(arr[0], l);
        }

        for (String keyword : keywords) {
            GSData data = buildData(keyword);
            data.numberOfSources = map.get(keyword);
            String str = GsonUtil.toJson(data);
            FileUtils.write(BASE_DIR + keyword + ".json", str);
        }

    }

    public static void main(String[] args) {
        builDataJson();
//        System.out.println(Arrays.toString(parseCsv(",1,2,\"3, 4 , 5\",,,,,, ").toArray()));

    }
}
