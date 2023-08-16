package simon.dragonfly.controllers;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import simon.dragonfly.utility.JSONUtility;
import simon.dragonfly.utility.SolutionUtility;

@RestController
public class SolutionController {

    private static final Logger logger = LoggerFactory.getLogger(SolutionController.class);

    @Autowired
    private SolutionUtility solutionUtility;

    @Autowired
    private JSONUtility jsonUtility;

    @PostMapping("/solution/{id}/save ")
    public ResponseEntity<String> getSolution(@PathVariable String id) throws InterruptedException, ExecutionException {
        HttpResponse<String> response = solutionUtility.getSolution(id);
        jsonUtility.saveJsonStringToFile(response.body());

        try {
            String successMessage = "Solution saved successfully";
            logger.info(successMessage);
            return ResponseEntity.ok(response.body());
        } catch (Exception e) {
            String errorMessage = "Error saving solution. Investigate.";
            logger.error(errorMessage, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @GetMapping("/solution/")
    public ResponseEntity<String> checkAppStatus() throws IOException {
        String solutionJson = jsonUtility.readJSONFileAsString();
        try {
            return ResponseEntity.ok(solutionJson);
        } catch (Exception e) {
            String errorMessage = "Error reading solution file";
            logger.error(errorMessage, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
}