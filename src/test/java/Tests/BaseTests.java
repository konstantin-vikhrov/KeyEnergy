package Tests;

import AzurePages.HomePage;
import com.google.common.io.Files;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;

import java.io.File;
import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTests {
    private WebDriver driver;
    protected HomePage homePage;
    protected final String resourceFolderPath = "C:\\Users\\konstantin.vikhrov\\IdeaProjects\\KeyEnergy\\src\\test\\resources";
    //By xPathContains = By.xpath("//div[contains(text(), 'powered')]");
    //By xPathStartsWith = By.xpath("//div[starts-with(@class, 'powered')]");
    //By xPathIndex = By.xpath("(//div/img) [last()]");
    //By xPathImages_ChildrenOfDivsOrAs = By.xpath("//div[child::img] | //a[child::img]");

    @BeforeAll
    void setUp() {
        //System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
        //driver = new ChromeDriver(getChromeOptions());
        driver = new EdgeDriver();
        driver.get("https://portal.azure.com/#home");
        driver.manage().window().maximize();
        homePage = new HomePage(driver);
        //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
    }

    @AfterAll
    void tearDown() {
        driver.quit();
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