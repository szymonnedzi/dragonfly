package simon.dragonfly.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//Why does this documentation not work nice?
@Api(tags = "Dragonfly Controller", description = "APIs for checking the status of Dragonfly application")
public class DragonFlyController {

    private static final Logger logger = LoggerFactory.getLogger(DragonFlyController.class);

    @GetMapping("/")
    @ApiOperation(value = "Check the status of the Dragonfly application",
                  notes = "This endpoint allows you to check if the Dragonfly application is online.",
                  response = ResponseEntity.class)
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
