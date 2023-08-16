package simon.dragonfly.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import simon.dragonfly.staticHelpers.GSONHelper;
import simon.dragonfly.utility.SessionUtility;

import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

@RestController
public class SessionController {

    @Autowired
    private SessionUtility sessionUtility;

    @GetMapping("/session")
    public ResponseEntity<String> createSession() throws InterruptedException, ExecutionException {
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