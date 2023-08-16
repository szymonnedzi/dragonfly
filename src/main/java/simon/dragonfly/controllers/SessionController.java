package simon.dragonfly.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import simon.dragonfly.staticHelpers.GSONHelper;
import simon.dragonfly.utility.SessionUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

@RestController
@Tag(name = "Session Controller", description = "APIs for managing sessions")
public class SessionController {

    @Autowired
    private SessionUtility sessionUtility;

    @GetMapping("/session")
    @Operation(summary = "Create and monitor a session", description = "This endpoint allows you to create and monitor a session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session established and ready"),
            @ApiResponse(responseCode = "500", description = "Error checking session status")
    })
    public ResponseEntity<String> createAndMonitorSession() throws InterruptedException, ExecutionException {
        HttpResponse<String> response = sessionUtility.initiateSession();
        String id = GSONHelper.getID(response);
        Boolean isReady = GSONHelper.getIsReady(response);
        while (!isReady) {
            System.out.println("Session with ID: %s established, is ready: %s".formatted(id, isReady));
            isReady = sessionUtility.awaitSessionReady(id, isReady);
        }
        System.out.println("Session with ID: %s established, is ready: %s".formatted(id, isReady));

        try {
            return new ResponseEntity<>("Session with ID: %s established, is ready: %s".formatted(id, isReady),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error checking session status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}