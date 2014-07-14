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
public class KMedoidClustering<T> {

    private double[][] distances;
    private List<Pattern<T>> listPatterns;
    private int k;
    private Pattern[] medoids;

    public KMedoidResult cluster(List<Pattern<T>> listPatterns, double[][] distances, int k) {
        this.listPatterns = listPatterns;
        this.distances = distances;
        this.k = k;

        medoids = chooseInitialMedoids();

        int iterations = 0;
        Pattern<T>[] newMedoids = null;

        HashMap<Integer, List<Pattern<T>>> clusterPatterns;
        do {
            iterations++;
            clusterPatterns = new HashMap<>();
            for (Pattern<T> medoid : medoids) {
                List<Pattern<T>> list = new ArrayList<>();
                clusterPatterns.put(medoid.clusterId, list);
                list.add(medoid);
            }

            for (Pattern p : listPatterns) {
                double min = Double.MAX_VALUE;
                if (!containsPattern(medoids, p)) {
                    for (Pattern<T> medoid : medoids) {
                        double distance = distances[medoid.id][p.id];

                        if (distance < min) {
//                            System.out.println("change cluster > from: " + p.clusterId + "   to: " + medoid.clusterId);
                            min = distance;
                            p.clusterId = medoid.clusterId;
                        }
                    }
                    clusterPatterns.get(p.clusterId).add(p);
                }
                
            }
            newMedoids = calculateNewMedoids(clusterPatterns);
            if(iterations > 5){
                return null;
            }
        } while (!stopIterations(newMedoids));
//        System.out.println("number of itegration: " + iterations);
        return new KMedoidResult(medoids, clusterPatterns);
    }

    private Pattern<T>[] calculateNewMedoids(HashMap<Integer, List<Pattern<T>>> clusterPatterns) {
        Pattern<T>[] newMedoids = new Pattern[k];
        int i = 0;
        for (Integer cluster : clusterPatterns.keySet()) {
            newMedoids[i] = getMedoid(clusterPatterns.get(cluster));
            newMedoids[i].clusterId = i;
            i++;
        }

        return newMedoids;
    }

    private Pattern<T> getMedoid(List<Pattern<T>> clusterPatterns) {
        double minTotalDistance = Double.MAX_VALUE;
        Pattern<T> medoid = null;
        for (Pattern<T> candidatePattern : clusterPatterns) {
            double totalDistance = 0;
            for (Pattern clusterPattern : clusterPatterns) {
                totalDistance += distances[candidatePattern.id][clusterPattern.id];
            }

            if (totalDistance < minTotalDistance) {
                minTotalDistance = totalDistance;
                medoid = candidatePattern;
            }
        }

        return medoid;
    }

    private Pattern<T>[] chooseInitialMedoids() {
        Pattern[] medoidPatterns = new Pattern[k];
        Set<Pattern> set = new HashSet<>();
//        for(Pattern p:listPatterns){
//            p.clusterId = (int)(Math.random() * k);
//        }
        while (true) {
            set.add(listPatterns.get((int) (Math.random() * listPatterns.size())));
            if (set.size() >= k) {
                break;
            }
        }

        medoidPatterns = set.toArray(medoidPatterns);
        for (int i = 0; i < medoidPatterns.length; i++) {
            medoidPatterns[i].clusterId = i;
        }
        
        return medoidPatterns;
    }

    private boolean containsPattern(Pattern[] patterns, Pattern pattern) {
        for (Pattern p : patterns) {
            if (p == pattern) {
                return true;
            }
        }
//        System.out.println("contain: false");
        return false;
    }

    private boolean stopIterations(Pattern[] newMedoids) {
        boolean stop = true;

        for (int i = 0; i < k; i++) {
            if (!containsPattern(newMedoids, medoids[i])) {
                stop = false;
            }
            medoids[i] = newMedoids[i];
            medoids[i].clusterId = i;
        }
//        System.out.println("stop: " + stop);
        return stop;
    }

    public class KMedoidResult {

        public Pattern[] medoids;
        public HashMap<Integer, List<Pattern<T>>> clusteredPatterns;

        public KMedoidResult(Pattern[] medoids, HashMap<Integer, List<Pattern<T>>> clusteredPatterns) {
            this.medoids = medoids;
            this.clusteredPatterns = clusteredPatterns;
        }
        
        public List<Integer> getMedoidIds(){
            List<Integer> list = new ArrayList<>();
            for(Pattern p:medoids){
                list.add(p.id);
            }
            return list;
        }
    }

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


        KMedoidClustering<SSchema> clustering = new KMedoidClustering<>();
        KMedoidClustering.KMedoidResult result = clustering.cluster(listPatterns, distances, 10);
        Map<Integer, List> map = result.clusteredPatterns;
        for (Integer k : map.keySet()) {
            System.out.println(map.get(k).size());
        }
    }
}
