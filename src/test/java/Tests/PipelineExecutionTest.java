package Tests;

import AzurePages.DataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PipelineExecutionTest extends BaseTests {
    @Test
    void validateHereLink() {
        DataFactory dataFactoryPage = homePage.clickDataFactoryLink();
        //Assertions.assertTrue(redirectPage.isHereLinkVisible());
    }
}
