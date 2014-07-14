/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic;

import crawlgooglewebdata.statistic.datastruct.GSData;
import dataprocessing.sampling.SSchema;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.FileUtils;
import utils.GsonUtil;

/**
 *
 * @author Tuan Chau
 */
public class Static3_updateDomainInfo {
    public static void update(String keyword){
        GSData data = GsonUtil.fromGson(new File(Static1_Counter.BASE_DIR + keyword + ".json"), GSData.class);
        
        try {
            
            BufferedReader reader = new BufferedReader(new FileReader(Static1_Counter.BASE_DIR + "domain_" + keyword + ".txt"));
            String line = reader.readLine().trim();
            SSchema schema = null;
            boolean getSchema = true;
            Set<String> domains = new HashSet<>();
            while(line != null){
                if(line.isEmpty()){
                    getSchema = true;
                }
                else if(line.startsWith(">")){
                    schema.domain = keyword + "_" + line.substring(1);
//                    System.out.println(line.substring(1));
                    domains.add(line);
                }
                else if(getSchema){
                    schema = data.getSchema(line);
                    
                    getSchema = false;
                }
                
                line = reader.readLine();
            }
            System.out.println("number of domains = " + domains.size());
            for(String s:domains){
                System.out.println(s);
            }
            FileUtils.write(Static1_Counter.BASE_DIR + keyword + ".json", GsonUtil.toJson(data));
        } catch (IOException ex) {
            Logger.getLogger(Static3_updateDomainInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[]args){
        update("phone");
    }
}
