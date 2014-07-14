/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic.metric;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Tuan Chau
 */
public class JaccardListStringSimilarity implements ISimilarity<String>{

    @Override
    public double getSimilarity(Collection col1, Collection col2) {
        Set<String> set1 = new HashSet<>(col1);
        Set<String> set2 = new HashSet<>(col2);
        
        int union = set1.size() + set2.size();
        set1.retainAll(set2);
        
        int intersection = set1.size();
        union -= intersection;
        
        return (double)(intersection) / (double) union;
    }
    
}
