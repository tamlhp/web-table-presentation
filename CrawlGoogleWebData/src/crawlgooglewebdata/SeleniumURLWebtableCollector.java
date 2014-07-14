/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata;

import static crawlgooglewebdata.SeleniumLinkFusionCollector.sleep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author Tuan Chau
 */
public class SeleniumURLWebtableCollector {
    private int curPage = 1;
    private WebDriver driver;
    private List<String> keywords;
    
    public SeleniumURLWebtableCollector(WebDriver driver){
        this.driver = driver;
    }
    
    public Map<String, List<String>> run(){
        login();
        start();
        Map<String, List<String>> map = new HashMap<>();
        if(keywords != null){
            for(String keyword : keywords){
                List<String> list = new ArrayList<>();
                map.put(keyword, list);
                putKeyword(keyword);
                
                for(int i = 0; i < 10; i++){
                    for(int j = 1; j <= 10; j++){
                        SeleniumLinkFusionCollector.sleep(5000);
                        try{
                        list.add(getLink(j));
                        }
                        catch(Exception e){
                            
                        }
                    }
                    
                    nextPage();
                }
            }
        }
        
        return map;
    }
    
    public String getLink(int id){
        WebElement li = driver.findElement(By.id("li_" + id));
        WebElement a = driver.findElement(By.tagName("a"));
        return a.getAttribute("href");
    }
    
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
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
    
    public static void main(String[]args){
        System.setProperty("webdriver.chrome.driver", "E:\\Softwares\\Windows\\Dev\\Java tool\\Library\\Selenium\\chromedriver.exe");
        List<String> keywords = Arrays.asList("author", "address", "type", "name", "country", "title", "year", "location", "model", "date", "color", "gender", "price", "organization", "email", "company", "phone", "category", "size", "budget");
        
        SeleniumURLWebtableCollector collector = new SeleniumURLWebtableCollector(new ChromeDriver());
        collector.run();
        
        
    }
}
