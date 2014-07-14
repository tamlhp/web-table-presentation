/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic.cluster.sumarization;

import crawlgooglewebdata.statistic.cluster.kmedoid.KMedoidClustering;
import crawlgooglewebdata.statistic.cluster.kmedoid.Pattern;
import static crawlgooglewebdata.statistic.cluster.sumarization.ClusterTuple.summarization;
import crawlgooglewebdata.statistic.metric.JaccardListStringSimilarity;
import dataprocessing.sampling.SData;
import dataprocessing.sampling.SRow;
import dataprocessing.sampling.SSchema;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import utils.GsonUtil;

/**
 * check with number of attribute changed
 * @author Tuan Chau
 */
public class CheckTupleWithKMedoid2 {

    public static void main(String[] args) {
        SData data = GsonUtil.fromGson(new File("D:/epfl/fusion/phone.json"), SData.class);
        List<SSchema> list = data.getSchemas();
        List<SSchema> list100 = new ArrayList<>();
        int count = 0;
        Set<Integer> selectedIndexes = new HashSet<>(Arrays.asList(18));
        for (SSchema s : list) {
            if (s.getNumberRows() >= 20) {
                System.out.println(count + "\t" + s.getNumberRows());
//                list100.add(s);
            }
            if (selectedIndexes.contains(count)) {
                list100.add(s);
            }
            count++;
        }
        System.out.println("size\tattrs\t      time\t      IL\tPT");

        KMedoidClustering<SRow> kMedoidClustering = new KMedoidClustering<>();
        for (SSchema s : list100) {
//            System.out.println(s.getNumberRows());
            for (int i = 1; i <= s.numAttr; i++) {
                check(kMedoidClustering, s, i);
            }
//            break;
        }
    }

    public static void check(KMedoidClustering<SRow> clustering, SSchema schema, int numAtts) {
        List<SRow> rows = schema.getAllRows();
        List<List<String>> table = new ArrayList<>();
        List<Pattern<SRow>> patterns = new ArrayList<>();
        int count = 0;
        for (SRow row : rows) {
            table.add(row.getStringInstances(numAtts));
            patterns.add(new Pattern<>(count++, row));
        }

        double[][] distances = new double[rows.size()][rows.size()];
        JaccardListStringSimilarity jaccard = new JaccardListStringSimilarity();
        for (int i = 0; i < table.size(); i++) {
            for (int j = i + 1; j < table.size(); j++) {
                distances[i][j] = distances[j][i] = 1 - jaccard.getSimilarity(table.get(i), table.get(j));
            }
            distances[i][i] = 0;
        }
        int runtimeCount = 0;
        double[] times = new double[1];
        double[] ilsum = new double[1];
        double[] ptsum = new double[1];
        for (runtimeCount = 0; runtimeCount < 1000; runtimeCount++) {
            count = 0;
            for (int i = 10; i <= 10; i += 5) {
//                System.out.println("i = " + i + "   count = " + count);
                long t0 = System.nanoTime();
                KMedoidClustering.KMedoidResult result = clustering.cluster(patterns, distances, i);
                times[count] += System.nanoTime() - t0;
                if (result == null) {
//                    System.out.println("null: " + i);
                    count--;
                    i -= 5;
                } else {
                    List<Integer> dstar = result.getMedoidIds();
                    ilsum[count] += sumIL(distances, dstar);
                    ptsum[count] += sumPT(distances, dstar);
                }
                count++;
            }
        }
        for (int i = 0; i < times.length; i++) {
            times[i] /= runtimeCount;
            ilsum[i] /= runtimeCount;
            ptsum[i] /= runtimeCount;
        }
        for (int i = 0; i < times.length; i++) {
            System.out.printf("%4d\t%4d\t%10d\t%2.5f\t%2.5f\r\n", distances.length, numAtts, (long) times[i], ilsum[i], ptsum[i]);
        }

    }

    public static double sumIL(double[][] distances, Collection<Integer> dstar) {
        double sum = 0;
        Set<Integer> set = new HashSet<>(dstar);
        for (int i = 0; i < distances.length; i++) {
            if (!set.contains(i)) {
                sum += singleLinkage(distances, i, dstar);
            }
        }

        return sum;
    }

    public static double sumPT(double[][] distances, List<Integer> dstar) {
        double sum = 0;
        for (int i = 0; i < dstar.size() - 1; i++) {
            sum += (1 - distances[dstar.get(i)][dstar.get(i + 1)]);
        }

        return sum;
    }

    public static double singleLinkage(double[][] distances, int id, Collection<Integer> dstar) {
        double min = Double.MAX_VALUE;
        for (int i : dstar) {
            if (distances[i][id] < min) {
                min = distances[i][id];
            }
        }
        return min;
    }
}
