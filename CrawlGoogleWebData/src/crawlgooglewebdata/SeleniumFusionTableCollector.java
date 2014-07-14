/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlgooglewebdata;

import java.io.File;
import java.util.Scanner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author Tuan Chau
 */
public class SeleniumFusionTableCollector {

    private WebDriver driver;
    private String keyword;

    public SeleniumFusionTableCollector(WebDriver driver) {
        this.driver = driver;
        login();
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;

        File dir = new File(Config.SAVE_DIR + keyword);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        System.out.println("Use this directory:");
        System.out.println(dir.getAbsolutePath());
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("download.directory_upgrade=true");
//        options.addArguments("download.default_directory", dir.getAbsolutePath());
//        options.addArguments("download.default_directory=" + dir.getAbsolutePath());
//        options.setExperimentalOptions("prefs", "{\"download.default_directory\":\"" + dir.getAbsolutePath() + "\"}");
//        driver = new ChromeDriver(options);
    }

    public void openTable(final String keyword, final String href) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (driver) {
                    driver.get(href);
                    waitForLoading();
                    download();
                }
            }
        });

        thread.start();
    }

    private void waitForLoading() {

        while (true) {
            SeleniumLinkFusionCollector.sleep(2000);
            try {
                WebElement div = driver.findElement(By.className("gwt-SplitLayoutPanel"));
                if (div != null) {
                    break;
                }
            } catch (Exception e) {
            }
        }
    }

    private void download() {

        WebElement file = driver.findElement(By.id("gwt-uid-114"));
        file.click();
        SeleniumLinkFusionCollector.sleep(1000);
        WebElement download = driver.findElement(By.id("gwt-uid-126"));
        download.click();
        SeleniumLinkFusionCollector.sleep(1000);
        WebElement modalDialog = driver.findElement(By.className("ft-modal-dialog"));
        WebElement btnRow = modalDialog.findElement(By.className("button-row"));

        WebElement downloadBtn = btnRow.findElement(By.tagName("div"));
        downloadBtn.click();

//        WebElement downloadBtn = driver.findElement(By.className("GL4F4JIDOG"));
//        downloadBtn.click();
        SeleniumLinkFusionCollector.sleep(5000);
    }

    public void waitForChangingDownloadFolder() {
        System.out.println("Change download folder for keyword: " + keyword);
        System.out.println("then enter something into console.");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
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

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", Config.CHROME_DRIVER);
        SeleniumFusionTableCollector collector = new SeleniumFusionTableCollector(new ChromeDriver());
        collector.setKeyword("author");
        collector.waitForChangingDownloadFolder();
        collector.openTable("author", "https://www.google.com/fusiontables/data?docid=1MG2IOdUrYBo2WJ_c3h7-gzC-fIs6g_nimrmTDaE&ei=Qo-dUqzmMceGrAGt44CYCQ#rows:id=1");
        SeleniumLinkFusionCollector.sleep(20000);
        collector.driver.quit();
    }
}
