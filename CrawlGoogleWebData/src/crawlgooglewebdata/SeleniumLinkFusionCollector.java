/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author Tuan Chau
 */
public class SeleniumLinkFusionCollector {

    private WebDriver driver;
    private List<String> keywords;
    private int curPage = 1;
    private SeleniumFusionTableCollector fusionTableCollector;

    public SeleniumLinkFusionCollector(WebDriver driver, SeleniumFusionTableCollector fusionTableCollector) {
        this.driver = driver;
        this.fusionTableCollector = fusionTableCollector;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void run() {
        login();
        start();
        if (keywords != null) {
            for (String keyword : keywords) {
                fusionTableCollector.setKeyword(keyword);
                fusionTableCollector.waitForChangingDownloadFolder();
                putKeyword(keyword);

                for (int i = 0; i < 10; i++) {
                    for (int j = 1; j <= 10; j++) {
                        sleep(5000);
                        System.out.printf("keyword: %s\tpage: %d\trow: %d\t\n", keyword, i, j);
                        String href = import2Fusion(j);
                        //TODO put to selenium fusion table
                        if (href != null) {
                            fusionTableCollector.openTable(keyword, href);
                        }
                    }
                    nextPage();
                }
            }
        }
    }

    public void login() {
        driver.get("https://accounts.google.com/ServiceLogin");
        WebElement form = driver.findElement(By.id("gaia_loginform"));
        WebElement email = form.findElement(By.id("Email"));
        WebElement password = form.findElement(By.id("Passwd"));
        email.sendKeys(Config.username);
        password.sendKeys(Config.password);
        form.submit();
    }

    public void start() {
        driver.get("http://research.google.com/tables");
    }

    public void putKeyword(String keyword) {
//        driver.get("http://research.google.com/tables?hl=en&ei=g-mXUr3DMsSFrQHS7oG4CQ&q=" + keyword);
        WebElement form = driver.findElement(By.id("gbqf"));

        WebElement textbox = form.findElement(By.id("gbqfq"));
        textbox.clear();
        textbox.sendKeys(keyword);

        form.submit();
        WebElement desc = driver.findElement(By.className("desc"));
        while (true) {
            if (desc == null) {
                desc = driver.findElement(By.className("desc"));
            }

            List<WebElement> list = desc.findElements(By.tagName("b"));
            if (!list.isEmpty()) {
                if (list.get(3).getText().equals(keyword)) {
                    break;
                }
            }

            sleep(100);
        }

        curPage = 1;
    }

    public String import2Fusion(int id) {
        WebElement li = driver.findElement(By.id("li_" + id));
        WebElement impLink = li.findElement(By.id("imp_link_" + id));
        impLink.click();
        System.out.println("impLink clicked");
        sleep(2000);
        int count = 0;
        while (true) {
            try {
                WebElement btnOk = li.findElement(By.id("btn_ok_" + id));
                btnOk.click();
                System.out.println("import btn clicked");
                break;
            } catch (Exception e) {
                System.out.println("got exception");
                count++;
                sleep(1500);
                if(count > 20){
                    driver.navigate().refresh();
                    return null;
                }
            }
        }
        System.out.println("waiting for import...");
        WebElement seeTableA = waitingImport(li, id);
        if (seeTableA != null) {
            String href = seeTableA.getAttribute("href");

            System.out.println("href: " + id + " = " + href);
            return href;
        }
        return null;
    }

    private WebElement waitingImport(WebElement li, int id) {
        WebElement imp = li.findElement(By.id("imp_" + id));
        int count = 0;
        while (true) {
            String errorCheck = imp.getText().trim().toLowerCase();
            if(errorCheck.contains("error") || errorCheck.contains("please try again later")){
                return null;
            }
            try {
                WebElement statusDiv = imp.findElement(By.className("status-txt"));

                if (statusDiv != null) {
                    String str = statusDiv.getText().trim().toLowerCase();
                    System.out.println(">>" + str);
                    if (str.contains("see table")) {
                        WebElement a = statusDiv.findElement(By.tagName("a"));
                        if (a != null) {
                            return a;
                        }
                    } else if (str.contains("error") || str.contains("please try again later")) {
                        return null;
                    }
                }
            } catch (Exception e) {
                sleep(1500);
            }
            count++;
            if(count >= 30){
                driver.navigate().refresh();
                return null;
            }
        }
    }

    private void nextPage() {
        WebElement navbar = driver.findElement(By.id("navbar"));
        List<WebElement> list = navbar.findElements(By.tagName("a"));
        list.get(list.size() - 1).click();
        waitForNextPage();
        curPage += 10;
    }

    private void waitForNextPage() {
        while (true) {
            int currentPage = getCurrentPage();
            if (currentPage > this.curPage) {
                break;
            }
        }
    }

    private int getCurrentPage() {
        WebElement desc = driver.findElement(By.className("desc"));
        while (true) {
            if (desc == null) {
                desc = driver.findElement(By.className("desc"));
            }
            try {
                WebElement bFirst = desc.findElement(By.tagName("b"));
                if (bFirst != null) {
                    String str = bFirst.getText();
                    return Integer.parseInt(str);
                }
            } catch (Exception e) {
                sleep(600);
            }
        }
    }

    public void stop() {
        driver.quit();
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(SeleniumLinkFusionCollector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {


        System.setProperty("webdriver.chrome.driver", "E:\\Softwares\\Windows\\Dev\\Java tool\\Library\\Selenium\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();

        SeleniumFusionTableCollector collector = new SeleniumFusionTableCollector(new ChromeDriver());
        SeleniumLinkFusionCollector selenium = new SeleniumLinkFusionCollector(new ChromeDriver(), collector);
        //"author", "address", "type", "name", "country", "title", "year", "location", "model", "date", "color", "gender", "price", "organization", "company", "phone", "category", "size", "budget", "city"
        //year, color
        //remove email
        //model inside date folder
        List<String> keywords = Arrays.asList("year", "color");

        selenium.setKeywords(keywords);
        try {
            selenium.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        selenium.import2Fusion(2);
        selenium.stop();
    }
}
