package AzurePages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.locators.RelativeLocator;

public class HomePage {
    private WebDriver driver;
    private final By dataFactory = By.xpath("//a[@aria-label='adf-integrations-qat-scus-001']");

    public HomePage (WebDriver driver) {
        this.driver = driver;
    }

    public DataFactory clickDataFactoryLink()  {
        driver.findElement(dataFactory).click();
        return new DataFactory(driver);
    }
/*
    public RedirectPage clickRedirectPageLink () {
        driver.findElement(redirectLink).click();
        return new RedirectPage(driver);
    }
 */
}