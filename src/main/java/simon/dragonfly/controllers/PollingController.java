package simon.dragonfly.controllers;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import simon.dragonfly.utility.PollingUtility;

@RestController
@Tag(name = "Polling Controller", description = "APIs for monitoring progress and polling")
public class PollingController {

    @Autowired
    private PollingUtility pollingUtility;

    @GetMapping("/progress/{id}")
    @Operation(summary = "Check progress of optimization", description = "This endpoint allows you to check the progress of an ongoing optimization process by polling until the best solution value is reached.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Best value reached"),
            @ApiResponse(responseCode = "500", description = "Error while monitoring progress")
    })
    public ResponseEntity<String> checkProgress(@PathVariable String id)
            throws InterruptedException, ExecutionException {

        Double bestValue = pollingUtility.pollUntilBestSolutionValueIsReached(id);
        try {
            String successMessage = "Best value reached, id %s, value: %s".formatted(id, String.valueOf(bestValue));
            System.out.println(successMessage);
            return new ResponseEntity<>(successMessage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while monitoring progress.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}