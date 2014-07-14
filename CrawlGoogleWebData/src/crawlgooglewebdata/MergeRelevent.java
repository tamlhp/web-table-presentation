/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata;

import crawlgooglewebdata.statistic.datastruct.GSData;
import dataprocessing.sampling.SSchema;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.FileUtils;
import utils.GsonUtil;

/**
 *
 * @author Tuan Chau
 */
public class MergeRelevent {

    public static void main(String[] args) {
        GSData data = GsonUtil.fromGson(new File("D:/epfl/fusion/phone.json"), GSData.class);
        List<String> list = FileUtils.getList("D:/epfl/fusion/phone_table_name.txt", "\n");

        List<SSchema> schemas = data.getSchemas();
        for (SSchema s : schemas) {
            int countdown = 0;
            if (s.name.contains("(1)")) {
                countdown = 1;
            } else if (s.name.contains("(2)")) {
                countdown = 2;
            } else if (s.name.contains("(3)")) {
                countdown = 3;
            } else if (s.name.contains("(4)")) {
                countdown = 4;
            } else if (s.name.contains("(5)")) {
                countdown = 5;
            } else if (s.name.contains("(6)")) {
                countdown = 6;
            } else if (s.name.contains("(7)")) {
                countdown = 7;
            } else if (s.name.contains("(8)")) {
                countdown = 8;
            } else if (s.name.contains("(9)")) {
                countdown = 9;
            } else{
                countdown = 0;
            }

            for (int i = 0; i < list.size(); i++) {
                if (s.name.startsWith(list.get(i))) {
                    if (countdown <= 0) {
                        s.relevant = 1.0 - (double) i / 100d;
                        System.out.println(s.relevant);
                    }
                    countdown--;
                }
            }

        }

        String str = GsonUtil.toJson(data);
        FileUtils.write("D:/epfl/fusion/phone_2.json", str);
    }
}
