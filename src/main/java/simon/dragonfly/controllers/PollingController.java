package simon.dragonfly.controllers;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import simon.dragonfly.utility.PollingUtility;

@RestController
public class PollingController {

    @Autowired
    private PollingUtility pollingUtility;

    @GetMapping("/progress/{id}")
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
