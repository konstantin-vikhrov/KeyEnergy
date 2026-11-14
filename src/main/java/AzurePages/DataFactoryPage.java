package AzurePages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DataFactoryPage extends Page {
    private final By launchStudioButton = By.xpath("//a[@aria-label='Open Azure Data Factory Studio']"); ////

    public DataFactoryPage(WebDriver driver) {
        super(driver);
    }

    public DataFactoryStudioPage clickLaunchStudioButton () {
        clickElement(launchStudioButton);
        return new DataFactoryStudioPage(driver);
    }
}
