/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author sonthanh
 */
public class GreedyDiversifyingAlgorithm {
    private List<SSchema> schemas;
    private double[][] M;
    private int topk;
    private double w;

    public GreedyDiversifyingAlgorithm(List<SSchema> schemas, double[][] M, int topk, double w) {
        this.schemas = schemas;
        this.M = M;
        this.topk = topk;
        this.w = w;
    }

    public Map<Integer, Double> runGreedySelection() {
        //Compute relevance score for each table
        //TODO: compute relevance score based on the ranking of the original source
        double[] r = new double[schemas.size()];
        for (int i = 0; i < r.length; i++) {
//             r[i] = 1.0;
            r[i] = schemas.get(i).relevant;
        }

        double[] q = new double[schemas.size()];
        //Compute weight factor q = \sum Mij * rj
        for (int i = 0; i < q.length; i++) {
            for (int j = 0; j < M[i].length; j++) {
                q[i] += M[i][j] * r[j];
            }
        }

        //Init ranking score
//        int w = 4;
        double[] s = new double[schemas.size()];
        for (int i = 0; i < s.length; i++) {
            s[i] = w * q[i] * r[i];
        }

        Set<Integer> candidateTables = new HashSet<>();
        for (int i = 0; i < schemas.size(); i++) {
            candidateTables.add(i);
        }

        //Step 2: Greedy Selection
//        System.out.println("Table name is unique ?:" + (candidateTables.size() == schemas.size()));
        Map<Integer, Double> res = new HashMap<>();

        Set<Integer> suggestTables = new HashSet<>();
        for (int j = 0; j < topk; j++) {
            if (j >= schemas.size()) {
                break;
            }

            // Select the next concept
            double maxConceptUtility = Double.NEGATIVE_INFINITY;
            Integer nextCandidate = -1;

            for (Integer c : candidateTables) {
                if (!suggestTables.contains(c)) {
                    if (maxConceptUtility < s[c]) {
                        maxConceptUtility = s[c];
                        nextCandidate = c;
                    }
                }
            }
            assert nextCandidate != -1 : candidateTables.size();
            suggestTables.add(nextCandidate);
            res.put(nextCandidate, s[nextCandidate]);

            for (int i = 0; i < s.length; i++) {
                s[i] -= 2 * r[nextCandidate]
                        * M[i][nextCandidate] * r[i];
            }
        }

        return res;
    }
}
