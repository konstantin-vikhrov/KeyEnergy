package AzurePages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DataFactory {
    private WebDriver driver;
    private final By alertButton = By.cssSelector("[onclick='jsAlert()']");
    private final By results = By.id("result");

    public DataFactory (WebDriver driver) {
        this.driver = driver;
    }

    public void clickAlertButton () {
        driver.findElement(alertButton).click();
        driver.switchTo().alert().accept();
    }
}
