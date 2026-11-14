package AzurePages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends Page {
    private final By dataFactory = By.xpath("//a[@aria-label='adf-integrations-qat-scus-001']");

    public HomePage(WebDriver driver) { super(driver); }

    public DataFactoryPage clickDataFactoryLink()  {
        clickElement(dataFactory);
        return new DataFactoryPage(driver);
    }
}