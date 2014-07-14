/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata.statistic;

import static crawlgooglewebdata.statistic.Static1_Counter.BASE_DIR;
import static crawlgooglewebdata.statistic.Static1_Counter.keywords;
import crawlgooglewebdata.statistic.datastruct.GSData;
import dataprocessing.sampling.SRow;
import dataprocessing.sampling.SSchema;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.FileUtils;
import utils.GsonUtil;

/**
 *
 * @author Tuan Chau
 */
public class Static3_domain {

    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);

        for (String keyword : keywords) {
            StringBuilder builder = new StringBuilder();
            System.out.println("keyword: " + keyword);
//            builder.append(keyword).append("\r\n");
            GSData data = GsonUtil.fromGson(new File(BASE_DIR + keyword + ".json"), GSData.class);
            List<SSchema> schemas = data.getSchemas();
            for (SSchema s : schemas) {
                builder.append(s.name).append("\r\n");
                builder.append(Arrays.toString(s.getAttributes().toArray())).append("\r\n");
                System.out.println(s.name);
                System.out.println(Arrays.toString(s.getAttributes().toArray()));
//                SRow row = s.getRow(0);
                builder.append(s.getRow(0)).append("\r\n");
                builder.append(s.getRow(1)).append("\r\n");
                builder.append(s.getRow(2)).append("\r\n\r\n");
//                System.out.println(row);
//                String domain = scanner.nextLine();
//                domain = keyword + "_" + domain;
//                s.domain = domain;
            }

            System.out.println("===========================");
            System.out.println("");
            builder.append("\r\n\r\n");
            String str = GsonUtil.toJson(data);
            FileUtils.write(BASE_DIR + keyword + ".json", str);
            FileUtils.write(BASE_DIR + "/domain_" + keyword + ".txt", builder.toString());
        }

    }
}
