/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Tuan Chau
 */
public class Config {

    public static final String username = "tunachauict";
    public static final String password = "#1099kuBinmeotukydethuong*";
    //"author", "address", "type", "name", "country", "title", "year", "location", "model", "date", "color", "gender", "price", "organization", "email", "company", "phone", "category", "size", "budget"
    public static List<String> keywords = Arrays.asList("year", "location", "model", "date", "color", "gender", "price", "organization", "email", "company", "phone", "category", "size", "budget");
    public static final String SAVE_DIR = "D:\\workspace\\assemblagit\\mappingvisualization\\CrawlGoogleWebData\\data";
    public static final String CHROME_DRIVER = "D:\\workspace\\assemblagit\\mappingvisualization\\CrawlGoogleWebData\\tool\\chromedriver.exe";//"E:\\Softwares\\Windows\\Dev\\Java tool\\Library\\Selenium\\chromedriver.exe"
}
