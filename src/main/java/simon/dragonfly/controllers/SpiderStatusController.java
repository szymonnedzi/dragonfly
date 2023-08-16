package simon.dragonfly.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import simon.dragonfly.staticHelpers.HTTPHelper;

@RestController
public class SpiderStatusController {

    @Value("${service.url.status}")
    private String serviceUrl;

    // Endpoint to see if Spider is on
    @GetMapping("/spiderStatus")
    public ResponseEntity<String> spiderStatus() {
        try {
            String spiderStatus = getSpiderStatus();
            return new ResponseEntity<>(spiderStatus, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error getting Spider status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getSpiderStatus() throws IOException, InterruptedException, ExecutionException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl))
                .GET()
                .build();

        HttpResponse<String> response = HTTPHelper.getResponseAsynch(httpClient, request);
        System.out.println("Spider status - OK");
        System.out.println(response.body());
        return response.body();
    }
}