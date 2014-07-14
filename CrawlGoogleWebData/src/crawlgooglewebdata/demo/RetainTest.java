/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.demo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Tuan Chau
 */
public class RetainTest {
    public static void main(String[]args){
        String s1 = "0 2 4 4 5 6 5 2 3 4 10 11 12";
        String s2 = "3 4 5 6 4 6 7 2 4 5 3 5 6 9 8";
        
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        
        for(String s: s1.split(" ")){
            set1.add(s);
        }
        
        for(String s: s2.split(" ")){
            set2.add(s);
        }
        
        int union = set1.size() + set2.size();
        
        HashSet<String> ss1 = new HashSet<>(set1);
        
        boolean retain = ss1.retainAll(set2);
        
        System.out.println(set1.size());
        System.out.println(set2.size());
        System.out.println(retain);
        System.out.println(ss1.size());
        
        System.out.println(Arrays.toString(set1.toArray()));
        System.out.println(Arrays.toString(set2.toArray()));
        System.out.println(Arrays.toString(ss1.toArray()));
    }
}
