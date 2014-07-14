/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tuan Chau
 */
public class Utils {

    public static File[] getFiles(String dir) {
        File folder = new File(dir);
        return folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isFile()) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public static List<String> readLimit(String filename, int limit) {
        List<String> result = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while(line != null && limit > 0){
                result.add(line.trim());
                limit--;
                line = reader.readLine();
            }
        } catch (Exception e) {
        }
        return result;
    }
    
    public static List<String> readLimit(File file, int limit) {
        List<String> result = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while(line != null && limit > 0){
                result.add(line.trim());
                limit--;
                line = reader.readLine();
            }
        } catch (Exception e) {
        }
        return result;
    }
}
