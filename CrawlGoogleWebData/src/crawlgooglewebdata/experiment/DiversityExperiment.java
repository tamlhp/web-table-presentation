/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.experiment;

import com.csvreader.CsvWriter;
import crawlgooglewebdata.statistic.cluster.kmedoid.GreedyDiversifyingAlgorithm;
import crawlgooglewebdata.statistic.cluster.kmedoid.KMedoidClustering;
import crawlgooglewebdata.statistic.cluster.kmedoid.Pattern;
import crawlgooglewebdata.statistic.datastruct.GSData;
import crawlgooglewebdata.statistic.metric.ISimilarity;
import crawlgooglewebdata.statistic.metric.JaccardListStringSimilarity;
import dataprocessing.sampling.SSchema;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import utils.GsonUtil;

/**
 *
 * @author sonthanh
 */
public class DiversityExperiment {

    private String filterResultFileName;
    private GSData data;
    private double w;

    public DiversityExperiment(GSData data, double w) {
        this.filterResultFileName = this.getClass().getSimpleName() + w + ".csv";
        this.w = w;
        this.data = data;
    }

    public void run() {
        List<SSchema> schemas = data.getSchemas();
        List<Pattern<SSchema>> listPatterns = new ArrayList<>();
        double[][] M = new double[schemas.size()][schemas.size()];

        double[] topRelevantScores = new double[schemas.size()];
        for (int i = 0; i < topRelevantScores.length; i++) {
            topRelevantScores[i] = schemas.get(i).relevant;
        }
        Arrays.sort(topRelevantScores);

        //Compute similarity matrix
        //TODO: distance matrix or similarity ???
        ISimilarity similarity = new JaccardListStringSimilarity();
        for (int i = 0; i < schemas.size(); i++) {
            listPatterns.add(new Pattern<>(i, schemas.get(i)));

            for (int j = i + 1; j < schemas.size(); j++) {
                M[i][j] = M[j][i] = similarity.getSimilarity(schemas.get(i).getAttributes(), schemas.get(j).getAttributes());
            }
            M[i][i] = 1;
        }

        int min = 5;
        int max = 98;

        Map<Integer, ExperimentResult> expRes = new HashMap<>();
        for (int k = min; k <= max; k ++) {
            ExperimentResult res = new ExperimentResult();
            
            double highestRelevance = 0;
            int n = topRelevantScores.length;
            for (int i = 1; i <= k; i++) {
                highestRelevance += topRelevantScores[n - i];
            }
            
            double start = System.nanoTime();
            greedyRun(schemas, M, k, w, highestRelevance, res);
            double end = System.nanoTime();
            double time = end - start;
            clusterRun(listPatterns, M, k, highestRelevance, res);

            double clusterSrecall = res.getClusterSrecall();
            double greedyScrecall = res.getGreedyScrecall();
            double srecall = (greedyScrecall - clusterSrecall) / clusterSrecall;

            double clusterNrelevance = res.getClusterNrelevance();
            double greedyNrelevance = res.getGreedyNrelevance();
            double nrelevance = (greedyNrelevance - clusterNrelevance) / clusterNrelevance;

            res.setTopk(k);
            res.setSrecall(srecall);
            res.setNrelevance(nrelevance);
            res.setGreedyTime(time);
            writeResult(res);
        }
    }

    public static void main(String[] args) {
        //TODO add domain to data
        GSData data = GsonUtil.fromGson(new File("E:\\Git\\mappingvisualization\\CrawlGoogleWebData\\data\\phone_2.json"), GSData.class);
        double w = 2;
        DiversityExperiment exp = new DiversityExperiment(data,w);
        for (int i = 0; i < 10; i++) {
            exp.run();

        }

    }

    public void writeResult(ExperimentResult expRes) {
        boolean alreadyExists = new File(filterResultFileName).exists();
        CsvWriter csvOutput;
        try {
            csvOutput = new CsvWriter(
                    new FileWriter(filterResultFileName, true), ',');

            // if the file didn't already exist then we need to write out the
            // header line
            if (!alreadyExists) {
                csvOutput.write("ExpID");
                csvOutput.write("TopK");
                csvOutput.write("Diversity(clsuterSrecall)");
                csvOutput.write("Diversity(greedySrecall)");
                csvOutput.write("Diversity(srecall)");
                csvOutput.write("Relevance(clsuterNrelevance)");
                csvOutput.write("Relevance(greedyNrelevance)");
                csvOutput.write("Relevance(relevance)");
                csvOutput.write("Time(greedyTime)");
                csvOutput.endRecord();
            }
            csvOutput.write(String.valueOf(expRes.getId()));
            csvOutput.write(String.valueOf(expRes.getTopk()));
            csvOutput.write(String.valueOf(expRes.getClusterSrecall()));
            csvOutput.write(String.valueOf(expRes.getGreedyScrecall()));
            csvOutput.write(String.valueOf(expRes.getSrecall()));
            csvOutput.write(String.valueOf(expRes.getClusterNrelevance()));
            csvOutput.write(String.valueOf(expRes.getGreedyNrelevance()));
            csvOutput.write(String.valueOf(expRes.getNrelevance()));
            csvOutput.write(String.valueOf(expRes.getGreedyTime()));
            csvOutput.endRecord();

            csvOutput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Write Result to " + filterResultFileName);
    }

    private void greedyRun(List<SSchema> schemas, double[][] M, int k, double w, double highestRelevance, ExperimentResult res) {
        GreedyDiversifyingAlgorithm gd = new GreedyDiversifyingAlgorithm(schemas, M, k, w);
        Map<Integer, Double> selectionResult = gd.runGreedySelection();

        Set<SSchema> relatedTables = new HashSet<>();
        for (Integer sid : selectionResult.keySet()) {
            relatedTables.add(schemas.get(sid));
        }

        double relevance = 0;
        Set<String> domain = new HashSet<>();
        for (SSchema table : relatedTables) {
            domain.add(table.domain);
            relevance += table.relevant;
        }

        double nrelevance = relevance / highestRelevance;
        //            System.out.println("#" + k + "\t" + ((float) domain.size() / 18.0));
        double srecall = (float) domain.size() / 18.0;
        res.setGreedyNrelevance(nrelevance);
        res.setGreedyScrecall(srecall);
    }

    private void clusterRun(List<Pattern<SSchema>> listPatterns, double[][] M, int k, double highestRelevance, ExperimentResult res) {
        KMedoidClustering<SSchema> clustering = new KMedoidClustering<>();
        KMedoidClustering.KMedoidResult result = null;
        do {
            result = clustering.cluster(listPatterns, M, k);
            if (result != null) {
                Map<Integer, List> map = result.clusteredPatterns;
                int total = 0;
                for (Integer i : map.keySet()) {
                    total += map.get(i).size();
//            System.out.println(map.get(k).size());
                }

//            System.out.println("total = " + total);
                double relevance = 0;
                Set<String> domain = new HashSet<>();
                for (Pattern<SSchema> p : result.medoids) {
                    domain.add(p.data.domain);
                    relevance += p.data.relevant;
                }

//                System.out.println("#" + i + "\t" + ((float) domain.size() / 18.0));
                double nrelevance = relevance / highestRelevance;
                double srecall = (float) domain.size() / 18.0;
                res.setClusterNrelevance(nrelevance);
                res.setClusterSrecall(srecall);
            }
        } while (result == null);
    }
}
