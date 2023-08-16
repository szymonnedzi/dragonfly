package simon.dragonfly.controllers;

import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

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
public class OptimizationController {

    @Autowired
    private OptimizationUtility optimizationUtility;

    private static final Logger logger = LoggerFactory.getLogger(OptimizationController.class);

    @PostMapping("/optimization/{id}/start")
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
    public ResponseEntity<String> optiEnd(@PathVariable String id) throws InterruptedException, ExecutionException {
        HttpResponse<String> response = optimizationUtility.stopOptimization(id);
        if (response.statusCode() == 200) {
            String successMessage = String.format("Optimization for session %s stopped", id);
            return ResponseEntity.ok(successMessage);
        } else {
            logger.error("Error stopped optimization for session {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error stopped optimization. Investigate.");
        }
    }
}