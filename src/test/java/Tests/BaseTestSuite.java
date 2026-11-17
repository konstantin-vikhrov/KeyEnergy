package Tests;

import AzurePages.DataFactoryPage;
import AzurePages.DataFactoryStudioPage;
import AzurePages.HomePage;
import com.google.common.io.Files;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;

import java.io.File;
import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTestSuite {
    private WebDriver driver;
    HomePage homePage;
    final String azureHomeURL = "https://portal.azure.com/#home";
    final String resourceFolderPath = "C:\\Users\\konstantin.vikhrov\\IdeaProjects\\KeyEnergy\\src\\test\\resources";
    public final String localQaFolder = "C:\\Users\\konstantin.vikhrov\\OneDrive - Enroute\\Documents";
    final int pipelineExecutionStep = 2;
    final String storageRoot = "integrations";
    DataFactoryStudioPage dataFactoryStudioPage;

    @BeforeAll
    void setUp() {
        //driver = new ChromeDriver(getChromeOptions());
        driver = new EdgeDriver();
        driver.get(azureHomeURL);
        driver.manage().window().maximize();
        homePage = new HomePage(driver);
        //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
    }

    @AfterAll
    void tearDown() {
        driver.quit();
    }

    protected void openADFPipeline(String pipelineFolder, String pipelineName) {
        DataFactoryPage dataFactoryPage = homePage.clickDataFactoryLink();
        dataFactoryStudioPage = dataFactoryPage.clickLaunchStudioButton();
        dataFactoryStudioPage.openPipelineList();
        dataFactoryStudioPage.clickPipelineListItem(pipelineFolder);
        dataFactoryStudioPage.clickElement(pipelineName);
    }

    public void setGlobalParameter(String paramName, String paramValue) {

    }

    public void setPipelineConfig(String localFolder, String fileName) {

    }

    protected void testPipelineExecution() {


        //Assertions.assertTrue(redirectPage.isHereLinkVisible());
    }

    @Test
    @Order(2)
    void testAuditRecords() {

    }

    @Test
    @Order(3)
    void testFailureNotification() {

    }

    protected void takeScreenshot() {
        //takes screenshot of what is visible.
        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        //Takes screenshot of the entire webpage.
        //File fullPageScreenshot = ((FirefoxDriver) driver).getFullPageScreenshotAs(OutputType.FILE);
        try {
            Files.move(screenshot, new File(resourceFolderPath + screenshot.getName()));
        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
        System.out.println("Screenshot saved in " + screenshot.getAbsolutePath());
        driver.findElement(By.id("ExampleID")).getScreenshotAs(OutputType.FILE);
    }

    protected void navigateBack(int numOfPagesToGoBack) {
        for (int i = 0; i < numOfPagesToGoBack; ++i) {
            driver.navigate().back();
        }
    }

    protected ChromeOptions getChromeOptions () {
        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("disable-infobars", "--headless=new");
        return chromeOptions;
    }

    protected void setCookie() {
        Cookie cookie = new Cookie.Builder("TAU", "123")
                .domain("https://the-internet.herokuapp.com/")
                .build();
        driver.manage().addCookie(cookie);
    }
}