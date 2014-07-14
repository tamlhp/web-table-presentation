/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic.cluster.kmedoid;

/**
 *
 * @author Tuan Chau
 */
public class Pattern<T> {
    public int id;
    public int clusterId;
    public T data;
    
    public Pattern(int id, T data){
        this.id = id;
        this.data = data;
    }
}
