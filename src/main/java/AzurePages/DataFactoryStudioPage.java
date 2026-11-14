package AzurePages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DataFactoryStudioPage extends Page {
    private final By authorButton = By.xpath("//button[@name='authoring']");
    private final By pipelineListTitle = By.xpath("//div[@aria-label='Pipelines']");
    private final By pipelineListItem = By.xpath("//div[@aria-label='BoA']");
    String separator = "/";
    //[@data-parent-name='BoA'][@data-parent-name='DailyFile']

    public DataFactoryStudioPage (WebDriver driver) { super(driver); }

    public void openPipelineList() {
        switchToAdfTab();
        clickElement(authorButton);
        clickElement(pipelineListTitle);
    }

    public void clickPipelineListItem(String item) {
        String[] parts = item.split(separator);

        for (String part : parts) {
            clickElement(part);
            scrollIntoView(part);
        }
    }

    private void switchToAdfTab() {
        String original = driver.getWindowHandle();
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> d.getWindowHandles().size() > 1);
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(original)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }
}
