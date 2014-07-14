/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic.cluster.kmedoid;

import crawlgooglewebdata.statistic.datastruct.GSData;
import crawlgooglewebdata.statistic.metric.ISimilarity;
import crawlgooglewebdata.statistic.metric.JaccardListStringSimilarity;
import dataprocessing.sampling.SSchema;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import utils.GsonUtil;

/**
 *
 * @author Tuan Chau
 */
public class CheckDomainWithKMedoid {

    public static void main(String[] args) {
        GSData data = GsonUtil.fromGson(new File("D:/epfl/fusion/phone.json"), GSData.class);
        List<SSchema> schemas = data.getSchemas();
        List<Pattern<SSchema>> listPatterns = new ArrayList<>();
        double[][] distances = new double[schemas.size()][schemas.size()];

        ISimilarity similarity = new JaccardListStringSimilarity();
        for (int i = 0; i < schemas.size(); i++) {
            listPatterns.add(new Pattern<>(i, schemas.get(i)));

            for (int j = i + 1; j < schemas.size(); j++) {
                distances[i][j] = distances[j][i] = 1 - similarity.getSimilarity(schemas.get(i).getAttributes(), schemas.get(j).getAttributes());
            }
            distances[i][i] = 0;
        }
        System.out.println("k\ts-recall");

        
        double[] srecall = new double[10];
        int runtimeCount = 0;
        KMedoidClustering<SSchema> clustering = new KMedoidClustering<>();
        for (runtimeCount = 0; runtimeCount < 100; runtimeCount++) {
            int count = 0;
            for (int i = 5; i <= 50; i += 5) {

                KMedoidClustering.KMedoidResult result = clustering.cluster(listPatterns, distances, i);
                if (result == null) {
                    i--;
                } else {
                    Map<Integer, List> map = result.clusteredPatterns;
                    int total = 0;
                    for (Integer k : map.keySet()) {
                        total += map.get(k).size();
//            System.out.println(map.get(k).size());
                    }

//            System.out.println("total = " + total);

                    Set<String> domain = new HashSet<>();
                    for (Pattern<SSchema> p : result.medoids) {
                        domain.add(p.data.domain);
                    }
                    srecall[count++] += (float) domain.size() / 18.0;
//                    System.out.println("#" + i + "\t" + ((float) domain.size() / 18.0));
                }
            }
        }
        
        for(int i = 0; i < srecall.length; i++){
            srecall[i] /= runtimeCount;
            System.out.println("#" + (5 * (i + 1)) + "\t" + srecall[i]);
        }
        
    }
}
