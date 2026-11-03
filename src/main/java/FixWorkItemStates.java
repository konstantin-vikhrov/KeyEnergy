import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FixWorkItemStates {
    final static String url = "https://portal.azure.com/#home";
    //ADF: https://adf.azure.com/en/home?factory=%2Fsubscriptions%2F053f6612-136f-4767-acbf-15de82d70895%2FresourceGroups%2Frg-integrations-qat-scus-001%2Fproviders%2FMicrosoft.DataFactory%2Ffactories%2Fadf-integrations-qat-scus-001
    //

    public static void main(String[] args) {
        final String tableLocator = "//table[@aria-label='Query Results Table']/tbody";
        final By newQueryButton = By.xpath("//button/span[text()='New query']");
        final By runQueryButton = By.xpath("//button/span[text()='Run query']");
        final By queryTable = By.xpath(tableLocator);
        final By queryRows = By.xpath(tableLocator + "/tr[@data-row-index]");
        final By rowTotalCount = By.className("query-summary-result-count");

        WebDriver driver = new EdgeDriver();
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.visibilityOfElementLocated(newQueryButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(runQueryButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(queryTable));
        List<WebElement> rows = new ArrayList<WebElement>();
        String str = driver.findElement(rowTotalCount).getText();
        int total = Integer.parseInt(str.replace("1 of ", "").replace(" work items",""));
        int processedRowCount = 0;
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement rowCell;
        String state;
        while (processedRowCount < total) {
            rows = driver.findElements(queryRows);
            for (int i = 0; i < rows.size(); ++i) {
                state = driver.findElement(By.xpath("(" + tableLocator + "/tr[@data-row-index='" + String.valueOf(i) + "']/td[7])")).getAttribute("textContent");
                switch (state) {
                    case "Closed": {

                    }
                    case "Active": {

                    }
                    case "Build in progress": {

                    }
                }
                if (state.equals("Closed")) {
                    rowCell = driver.findElement(queryRows);
                }
            }
            rows = driver.findElements(queryRows);
            //js.executeScript("arguments[0].scrollIntoView(true);", rows.get(rowCount - 1));
        }
    }
}
