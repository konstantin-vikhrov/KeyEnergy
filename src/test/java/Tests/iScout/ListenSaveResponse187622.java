package Tests.iScout;

import Tests.BaseTestSuite;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class ListenSaveResponse187622 extends BaseTestSuite {
    private final String adoWorkItem = "1839";
    private final String folderADF = "iScout/GetFormResponses/187622";
    private final String folderAzureStorage = "iScout/getFormResponses/187622";
    private final String requestJSON = "ID_38080446.json";
    private final String pl_iScout_ListenSaveResponse187622 = "pl_iScout_ListenSaveResponse187622";

    @Test
    @Order(0)
    void testCase00() {
        int adoTestCase = 2193;
        openADFPipeline(folderADF, pl_iScout_ListenSaveResponse187622);
    }

    @Test
    @Order(1)
    void testCase01() {
        int adoTestCase = 2196;

    }
}
