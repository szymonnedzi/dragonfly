package simon.dragonfly.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Dragonfly Controller")
public class DragonFlyController {

    private static final Logger logger = LoggerFactory.getLogger(DragonFlyController.class);

    @GetMapping("/")
    @Operation(summary = "Check the status of the Dragonfly application", description = "This endpoint allows you to check if the Dragonfly application is online.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dragonfly is online"),
            @ApiResponse(responseCode = "500", description = "An error occurred while checking Dragonfly status")
    })
    public ResponseEntity<String> checkAppStatus() {
        try {
            logger.info("Checking Dragonfly status...");
            return ResponseEntity.ok("Dragonfly is online");
        } catch (Exception e) {
            logger.error("Error checking Dragonfly status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while checking Dragonfly status");
        }
    }
}
