package crawlgooglewebdata.experiment;

import java.util.HashSet;
import java.util.Set;

public class ExperimentResult {

    private int id;
    private int topk;
    private double srecall;
    private double clusterSrecall;
    private double greedyScrecall;
    private double nrelevance;
    private double clusterNrelevance;
    private double greedyNrelevance;
    private double greedyTime;

    public double getNrelevance() {
        return nrelevance;
    }

    public void setNrelevance(double nrelevance) {
        this.nrelevance = nrelevance;
    }

    public double getClusterNrelevance() {
        return clusterNrelevance;
    }

    public void setClusterNrelevance(double clusterNrelevance) {
        this.clusterNrelevance = clusterNrelevance;
    }

    public double getGreedyNrelevance() {
        return greedyNrelevance;
    }

    public void setGreedyNrelevance(double greedyNrelevance) {
        this.greedyNrelevance = greedyNrelevance;
    }
    
    public double getClusterSrecall() {
        return clusterSrecall;
    }

    public void setClusterSrecall(double clusterSrecall) {
        this.clusterSrecall = clusterSrecall;
    }

    public double getGreedyScrecall() {
        return greedyScrecall;
    }

    public void setGreedyScrecall(double greedyScrecall) {
        this.greedyScrecall = greedyScrecall;
    }

    public int getTopk() {
        return topk;
    }

    public void setTopk(int topk) {
        this.topk = topk;
    }

    public double getSrecall() {
        return srecall;
    }

    public void setSrecall(double srecall) {
        this.srecall = srecall;
    }

    public ExperimentResult() {
        this.id = IDGenerator.nextIntID();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getGreedyTime() {
        return greedyTime;
    }

    public void setGreedyTime(double greedyTime) {
        this.greedyTime = greedyTime;
    }
}
