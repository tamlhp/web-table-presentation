/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic.metric;

import java.util.Collection;

/**
 *
 * @author Tuan Chau
 */
public interface ISimilarity<T> {
    public double getSimilarity(Collection<T> col1, Collection<T> col2);
}
