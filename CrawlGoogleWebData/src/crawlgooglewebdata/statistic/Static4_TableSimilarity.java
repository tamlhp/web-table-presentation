/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic;

import static crawlgooglewebdata.statistic.Static1_Counter.*;
import crawlgooglewebdata.statistic.datastruct.GSData;
import crawlgooglewebdata.statistic.metric.ISimilarity;
import crawlgooglewebdata.statistic.metric.JaccardListStringSimilarity;
import dataprocessing.sampling.SSchema;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
public class Static4_TableSimilarity {

    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();

        for (String keyword : keywords) {
            GSData data = GsonUtil.fromGson(new File(BASE_DIR + keyword + ".json"), GSData.class);
            List<SSchema> schemas = data.getSchemas();
            ISimilarity distance = new JaccardListStringSimilarity();
            int size = schemas.size();
            List<Double> listSim = new ArrayList<>();
            Map<Double, Integer> map = new HashMap<>();
            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    double sim = distance.getSimilarity(schemas.get(i).getAttributes(), schemas.get(j).getAttributes());
                    listSim.add(sim);
                    if (map.containsKey(sim)) {
                        map.put(sim, map.get(sim) + 1);
                    } else {
                        map.put(sim, 1);
                    }
                }
            }

            Collections.sort(listSim);
            List<Double> keyList = new ArrayList<>(map.keySet());
            Collections.sort(keyList);
//            for(Double d: keyList){
//                System.out.println(d + ": " + map.get(d));
//            }
            builder.append(keyword).append(":\r\n");
            builder.append("   value,  count,  total\r\n");
            for (int i = keyList.size() - 1; i >= 0; i--) {
                int count = 0;
                for (int j = i; j < keyList.size(); j++) {
                    count += map.get(keyList.get(j));
                }

                builder.append(String.format("%3f, %6d, %6d\r\n", keyList.get(i), map.get(keyList.get(i)), count));
                System.out.println(keyList.get(i) + " : " + count + "  -  " + map.get(keyList.get(i)));
            }
            builder.append("\r\n=======================\r\n");
//            break;
        }
        FileUtils.write(BASE_DIR + "statistic4_similarity_count.txt", builder.toString());
    }
}
