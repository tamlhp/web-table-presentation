/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic.cluster.sumarization;

import ch.epfl.modularity.clustering.hierachy.AverageLinkageStrategy;
import ch.epfl.modularity.clustering.hierachy.Cluster;
import ch.epfl.modularity.clustering.hierachy.CompleteLinkageStrategy;
import ch.epfl.modularity.clustering.hierachy.DefaultClusteringAlgorithm;
import ch.epfl.modularity.clustering.hierachy.LinkageStrategy;
import ch.epfl.modularity.clustering.hierachy.SingleLinkageStrategy;
import crawlgooglewebdata.statistic.metric.JaccardListStringSimilarity;
import dataprocessing.sampling.SData;
import dataprocessing.sampling.SRow;
import dataprocessing.sampling.SSchema;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import utils.GsonUtil;

/**
 *
 * @author Tuan Chau
 */
public class ClusterTuple2 {

    public static void main(String[] args) {
        SData data = GsonUtil.fromGson(new File("D:/epfl/fusion/phone.json"), SData.class);
        List<SSchema> list = data.getSchemas();
        List<SSchema> list100 = new ArrayList<>();
        int count = 0;
        Set<Integer> selectedIndexes = new HashSet<>(Arrays.asList(18));
        for (SSchema s : list) {
            if (s.getNumberRows() >= 20) {
                System.out.println(count + "\t" + s.getNumberRows() + "\t" + s.numAttr);
//                list100.add(s);
            }
            if (selectedIndexes.contains(count)) {
                list100.add(s);
            }
            count++;
        }
        System.out.println("size\tatts\ttime\tIL\tPT");
        for (SSchema s : list100) {
            for (int i = 1; i <= s.numAttr; i++) {
                summarization(s, false, new AverageLinkageStrategy(), i);
            }
//            break;
        }
    }

    public static void summarization(SSchema schema, boolean random, LinkageStrategy linkage, int numInst) {
        List<SRow> rows = schema.getAllRows();
        List<List<String>> table = new ArrayList<>();
        for (SRow row : rows) {
            table.add(row.getStringInstances(numInst));
        }

        double[][] distances = new double[rows.size()][rows.size()];
        JaccardListStringSimilarity jaccard = new JaccardListStringSimilarity();
        for (int i = 0; i < table.size(); i++) {
            for (int j = i + 1; j < table.size(); j++) {
                distances[i][j] = distances[j][i] = 1 - jaccard.getSimilarity(table.get(i), table.get(j));
            }
            distances[i][i] = 0;
        }

        LinkageStrategy singleLinkage = linkage;
        DefaultClusteringAlgorithm clusteringAlgorithm = new DefaultClusteringAlgorithm();

        Cluster cluster = clusteringAlgorithm.performClustering(distances, singleLinkage);
        List<Cluster> leafs = cluster.getChildrenLeaf();
        for (int i = 0; i < leafs.size(); i++) {
            leafs.get(i).setNewSSN(i);
        }
//        System.out.println("get k clusters");
        int runtimeCount = 0;
        long times[] = new long[1];
        double ils[] = new double[1];
        double pts[] = new double[1];
        for (runtimeCount = 0; runtimeCount < (random ? 100 : 1); runtimeCount++) {

            for (int i = 10; i <= 10; i += 5) {
                long t0 = System.nanoTime();
                List<Cluster> kclusters = random ? getRandomKClusters(leafs, i) : getKCluster(cluster, i);
                kclusters = selectKLeafs(cluster, clusteringAlgorithm, kclusters);
                times[(i - 10) / 5] = System.nanoTime() - t0;
                if (!random) {
                    Collections.sort(kclusters, new Comparator<Cluster>() {

                        @Override
                        public int compare(Cluster o1, Cluster o2) {
                            return o1.getNewSSN() - o2.getNewSSN();
                        }
                    });
                }

                Set<Cluster> setClusterLeafs = new HashSet<>(kclusters);

                for (Cluster c : leafs) {
                    if (!setClusterLeafs.contains(c)) {
                        ils[(i - 10) / 5] += singleLinkage(clusteringAlgorithm, c, kclusters);
                    }
                }
                double pt = 0;
                for (int j = 0; j < kclusters.size() - 1; j++) {
                    pt += 1 - clusteringAlgorithm.distanceClusters(kclusters.get(j), kclusters.get(j + 1));
                }

                pts[(i - 10) / 5] += pt;
            }
        }
//        System.out.println("runtime count = " + runtimeCount);
        for (int i = 0; i < times.length; i++) {
            times[i] /= runtimeCount;
            pts[i] /= runtimeCount;
            ils[i] /= runtimeCount;
        }
        for (int i = 0; i < times.length; i++) {
            System.out.printf("%4d\t%4d\t%10d\t%2.5f\t%2.5f\r\n", leafs.size(), numInst, times[i], ils[i], pts[i]);
        }
    }

    public static Map<Double, Integer> countDistance(Cluster cluster) {
        Map<Double, Integer> result = new HashMap<>();
        countDistance(cluster, result);

        return result;
    }

    private static void countDistance(Cluster cluster, Map<Double, Integer> map) {
        if (map.containsKey(cluster.getDistance())) {
            map.put(cluster.getDistance(), map.get(cluster.getDistance()) + 1);
        } else {
            map.put(cluster.getDistance(), 1);
        }

        for (Cluster c : cluster.getChildren()) {
            countDistance(c, map);
        }
    }

    public static List<Cluster> getKCluster(Cluster cluster, int k) {
        List<Cluster> result = new ArrayList<>();
        List<Cluster> sortedList = new ArrayList<>();
        sortedList.add(cluster);
        double threshold = 1;
        if (k == 1) {
            return sortedList;
        } else {
            while (countWithThreshold(sortedList, threshold) < k) {
                if (!sortedList.isEmpty()) {
                    Cluster first = sortedList.remove(0);
                    if (first.isLeaf()) {
                        k--;
                        result.add(first);
//                        System.out.printf("leaf: distance = %3.2f  |  node height = %5d  |  k = %5d  |  result size = %5d\r\n", first.getDistance(), first.getHeight(), k, result.size());
                    } else {
                        List<Cluster> children = first.getChildren();
                        sortedList.addAll(children);
                        Collections.sort(sortedList);
//                        if(children.get(0).getDistance() < threshold && children.get(1).getDistance() < threshold){
//                            
//                        }

                        threshold = first.getDistance();
                    }
                } else {
                    break;
                }
            }

            for (int i = 0; i < k; i++) {
                result.add(sortedList.get(i));
            }
        }
        return result;
    }

    public static int countWithThreshold(List<Cluster> sortedList, double threshold) {
        int count = 0;
        for (Cluster c : sortedList) {
            if (c.getDistance().doubleValue() >= threshold) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    public static double getThreadhold(List<Cluster> sortedList, double threshold) {
        for (Cluster c : sortedList) {
            if (c.getDistance().doubleValue() < threshold) {
                return c.getDistance().doubleValue();
            }
        }
        return 0;
    }

    public static List<Cluster> selectKLeafs(Cluster tree, DefaultClusteringAlgorithm clusteringAlgorithm, List<Cluster> kClusters) {
        List<Cluster> result = new ArrayList<>();
        Cluster leftest = tree.getLeftestInSet(kClusters);
        kClusters.remove(leftest);
        if (leftest.isLeaf()) {
            result.add(leftest);
        } else {
            List<Cluster> leafs = leftest.getChildrenLeaf();
            leftest = leafs.get(leafs.size() / 2);
            result.add(leftest);
        }

        for (Cluster c : kClusters) {
            if (c.isLeaf()) {
                result.add(c);
            } else {
                result.add(argmin(clusteringAlgorithm, c, result));
            }
        }

        return result;
    }

    private static Cluster argmin(DefaultClusteringAlgorithm clusteringAlgorithm, Cluster root, List<Cluster> dstar) {
        List<Cluster> leafs = root.getChildrenLeaf();
        Cluster maxLeaf = leafs.get(0);
        double max = singleLinkage(clusteringAlgorithm, maxLeaf, dstar);
        for (int i = 1; i < leafs.size(); i++) {
            Cluster leaf = leafs.get(i);
            double d = singleLinkage(clusteringAlgorithm, leaf, dstar);
            if (d > max) {
                maxLeaf = leafs.get(i);
                max = d;
            }
        }
        return maxLeaf;
    }

    private static double singleLinkage(DefaultClusteringAlgorithm clusteringAlgorithm, Cluster leaf, List<Cluster> dstar) {
        double min = clusteringAlgorithm.distanceClusters(leaf, dstar.get(0));
        for (int i = 1; i < dstar.size(); i++) {
            double d = clusteringAlgorithm.distanceClusters(leaf, dstar.get(i));
            if (d < min) {
                min = d;
            }
        }
        return min;
    }

    public static List<Cluster> getRandomKClusters(List<Cluster> leafs, int k) {
        leafs = new ArrayList<>(leafs);
        List<Cluster> list = new ArrayList<>();
        while (k > 0) {
            int index = ((int) (Math.random() * 5564564)) % leafs.size();
            list.add(leafs.get(index));
            k--;
            leafs.remove(index);
        }

        return list;
    }

    public static void resetSSN(Cluster root) {

    }
}
