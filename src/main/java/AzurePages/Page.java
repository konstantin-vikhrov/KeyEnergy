package AzurePages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Page {
    protected WebDriver driver;
    private Duration seconds = Duration.ofSeconds(30);

    public Page (WebDriver driver) {
        this.driver = driver;
    }

    public void clickElement(By by) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        //wait.until(ExpectedConditions.visibilityOfElementLocated(by)).click();
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    public void clickElement(String label) {
        By by = By.xpath("//div[@data-sa-idt='" + label + "']");
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        //wait.until(ExpectedConditions.visibilityOfElementLocated(by)).click();
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    protected void scrollIntoView(String label) {
        By by = By.xpath("//div[@data-sa-idt='" + label + "']");
        WebElement element = driver.findElement(by);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
