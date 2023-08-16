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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Spider Status Controller", description = "APIs for checking the status of Spider service")
public class SpiderStatusController {

    @Value("${service.url.status}")
    private String serviceUrl;

    @GetMapping("/spiderStatus")
    @Operation(summary = "Check Spider status", description = "This endpoint allows you to check the status of the Spider service.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spider is online"),
            @ApiResponse(responseCode = "500", description = "Error getting Spider status")
    })
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