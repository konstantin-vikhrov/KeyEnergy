package Tests.iScout;

import Tests.BaseTestSuite;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class ListenSaveResponse187622 extends BaseTestSuite {
    private final String adoWorkItem = "1839";
    private final String adfFolder = "iScout/GetFormResponses/187622";
    private final String azureStorageFolder = "iScout/getFormResponses/187622";
    private final String localFolder = "iScout/getFormResponses/187622";
    private final String requestJSON = "ID_38080446.json";

    @Test
    @Order(0)
    void testCase00() {
        int adoTestCase = 2193;
        setGlobalParameter("gp_iscoutApiUrl", "https://api.novaraflex.com/v1");
        openADFPipeline(adfFolder, "pl_iScout_ListenSaveResponse187622");
        setPipelineConfig(localQaFolder + "\\" + adoWorkItem, requestJSON);
    }

    @Test
    @Order(1)
    void testCase01() {
        int adoTestCase = 2196;

    }
}
