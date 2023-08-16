package simon.dragonfly.controllers;

import java.net.http.HttpResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import simon.dragonfly.model.DeliveryOrder;
import simon.dragonfly.staticHelpers.GSONHelper;
import simon.dragonfly.utility.CSVUtility;
import simon.dragonfly.utility.JSONUtility;
import simon.dragonfly.utility.OptimizationUtility;
import simon.dragonfly.utility.PollingUtility;
import simon.dragonfly.utility.SessionUtility;
import simon.dragonfly.utility.SolutionUtility;

@RestController
@Tag(name = "Full Process Controller", description = "APIs for handling the full processing of data")

public class FullProcessController {

    @Autowired
    private CSVUtility csvUtility;
    @Autowired
    private SessionUtility sessionUtility;
    @Autowired
    private OptimizationUtility optimizationUtility;
    @Autowired
    private PollingUtility pollingUtility;
    @Autowired
    private SolutionUtility solutionUtility;
    @Autowired
    private JSONUtility jsonUtility;

    @Operation(summary = "Perform the full data processing", description = "This endpoint performs the full data processing, including CSV handling, session establishment, optimization, polling, solution retrieval, saving results, session deletion, and presenting results.")
    @ApiResponse(responseCode = "200", description = "Data processing completed successfully")
    @ApiResponse(responseCode = "500", description = "Error working with Dragonfly")
    @PostMapping(value = "/fullProcess", consumes = { "multipart/form-data" })
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        // Hardcoded but can easily be made customizeable
        String id = "test-695";

        // Post CSV
        System.out.println("Attaching orders start");
        List<DeliveryOrder> orders = csvUtility.processCoordinates(file);
        csvUtility.attachCoordinatesToInput(orders);
        System.out.println("Attaching orders end");

        // Establish session
        System.out.println("Establishing session: start");
        HttpResponse<String> sessionResponse = sessionUtility.initiateSession();
        Boolean isReady = GSONHelper.getIsReady(sessionResponse);
        while (!isReady) {
            System.out.println("Session with ID: %s established, is ready: %s".formatted(id, isReady));
            isReady = sessionUtility.awaitSessionReady(id, isReady);
        }
        System.out.println("Establishing session: end");

        // Initiate optimization
        System.out.println("Starting optimizaton: start");
        optimizationUtility.startOptimization(id);
        System.out.println("Starting optimizaton: end");

        // Poll until the value ceases to improve
        pollingUtility.pollUntilBestSolutionValueIsReached(id);

        // Stop optimization
        System.out.println("Stopping optimizaton: start");
        optimizationUtility.stopOptimization(id);
        System.out.println("Stopping optimizaton: end");

        // Fetch results
        HttpResponse<String> response = solutionUtility.getSolution(id);

        // Save results
        // Local for now, database later?
        jsonUtility.saveJsonStringToFile(response.body());

        // Delete session
        System.out.println("Deleting session: start");
        sessionUtility.deleteSession(id);
        System.out.println("Delete session: end");

        // Present results
        String result = jsonUtility.readJSONFileAsString();

        try {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error working with Dragonfly", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
