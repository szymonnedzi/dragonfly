package simon.dragonfly.controllers;

import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import simon.dragonfly.utility.OptimizationUtility;

@RestController
@Tag(name = "Optimization Controller", description = "APIs for starting and stopping optimization sessions")
public class OptimizationController {

    @Autowired
    private OptimizationUtility optimizationUtility;

    private static final Logger logger = LoggerFactory.getLogger(OptimizationController.class);

    @PostMapping("/optimization/{id}/start")
    @Operation(summary = "Start optimization for a session", description = "This endpoint allows you to start optimization for a specific session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Optimization started successfully"),
            @ApiResponse(responseCode = "500", description = "Error starting optimization")
    })
    public ResponseEntity<String> optiStart(@PathVariable String id) throws InterruptedException, ExecutionException {
        HttpResponse<String> response = optimizationUtility.startOptimization(id);
        if (response.statusCode() == 200) {
            String successMessage = String.format("Optimization for session %s started", id);
            return ResponseEntity.ok(successMessage);
        } else {
            logger.error("Error starting optimization for session {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error starting optimization. Investigate.");
        }
    }

    @PostMapping("/optimization/{id}/end")
    @Operation(summary = "Stop optimization for a session", description = "This endpoint allows you to stop optimization for a specific session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Optimization stopped successfully"),
            @ApiResponse(responseCode = "500", description = "Error stopping optimization")
    })
    public ResponseEntity<String> optiEnd(@PathVariable String id) throws InterruptedException, ExecutionException {
        HttpResponse<String> response = optimizationUtility.stopOptimization(id);
        if (response.statusCode() == 200) {
            String successMessage = String.format("Optimization for session %s stopped", id);
            return ResponseEntity.ok(successMessage);
        } else {
            logger.error("Error stopping optimization for session {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error stopping optimization. Investigate.");
        }
    }
}