/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic;

import crawlgooglewebdata.statistic.datastruct.Static1_CountResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.FileUtils;

/**
 *
 * @author Tuan Chau
 */
public class Static1_Counter {
    public static final String BASE_DIR = "D:/epfl/fusion/";
    public static final String FILE_RESOURCE_STATISTIC = "statistic.txt";
    public static final String[] keywords = {"author", "address", "type", "name", "country", "title", "year", "location", "model", "date", "color", "gender", "price", "organization", "company", "phone", "category", "size", "budget", "city"};
    
    public static Static1_CountResult staticKeyword(String keyword){
        File[] files = Utils.getFiles(BASE_DIR + keyword);
        Static1_CountResult result = new Static1_CountResult();
        result.keyword = keyword;
        result.numberOfTable = files.length;
        
        for(File f: files ){
            List<String> list = FileUtils.getList(f, "\n");
            result.averageOfRow += list.size();
            String[]arr = list.get(0).split(",");
            result.averageOfAttribute += arr.length;
        }
        
        
        result.averageOfAttribute /= (float)result.numberOfTable;
        result.averageOfRow /= (float) result.numberOfTable;
        return result;
    }
    
    public static void main(String[]args){
        List<String> list = FileUtils.getList(BASE_DIR + FILE_RESOURCE_STATISTIC, "\n");
        Map<String, Long> map = new HashMap<>();
        for(String s:list){
            String[]arr = s.split("\t");
            long l = Long.parseLong(arr[1].replace(",", "").trim());
            map.put(arr[0], l);
        }
        
        List<Static1_CountResult> result = new ArrayList<>();
        for(String keyword: keywords){
//            System.out.println("keyword: " + keyword);
            Static1_CountResult r = staticKeyword(keyword);
            r.numberOfSource = map.get(keyword);
            result.add(r);
        }
        
        int numberOfTable = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("keyword,table,source,avg row, avg attribute\r\n");
        for(Static1_CountResult r: result){
            numberOfTable += r.numberOfTable;
            System.out.println("keyword: " + r.keyword);
            System.out.println("number of table: " + r.numberOfTable);
            System.out.println("number of source: " + r.numberOfSource);
            System.out.println("average of row: " + r.averageOfRow);
            System.out.println("average of attribute: " + r.averageOfAttribute);
            System.out.println("");
            builder.append(r.keyword).append(",");
            builder.append(r.numberOfTable).append(",");
            builder.append(r.numberOfSource).append(",");
            builder.append(r.averageOfRow).append(",");
            builder.append(r.averageOfAttribute).append("\r\n");
        }
        
        System.out.println("total table: " + numberOfTable );
        try {
            FileUtils.write(BASE_DIR + "static_12.csv", builder.toString());
        } catch (Exception ex) {
            Logger.getLogger(Static1_Counter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
