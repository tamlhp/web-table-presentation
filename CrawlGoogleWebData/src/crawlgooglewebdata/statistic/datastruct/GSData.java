/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic.datastruct;

import dataprocessing.sampling.SData;

/**
 *
 * @author Tuan Chau
 */
public class GSData extends SData{
    public long numberOfSources;
    public int numberOfTable;
    public float averageOfAttribute;
    public float averageOfRow;
    public String name;
    public GSData(String name, int numberOfSources){
        super();
        this.name = name;
        this.numberOfSources = numberOfSources;
    }
    
    public GSData(String name){
        super();
        this.name = name;
    }
    
}
