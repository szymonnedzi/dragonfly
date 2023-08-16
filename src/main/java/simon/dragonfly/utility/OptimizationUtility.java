package simon.dragonfly.utility;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import simon.dragonfly.staticHelpers.HTTPHelper;

@Component
public class OptimizationUtility {

    @Value("${service.url.optimization.start}")
    private String optimizationUrlStart;

    @Value("${service.url.optimization.stop}")
    private String optimizationUrlStop;

    public HttpResponse<String> startOptimization(String id) throws InterruptedException, ExecutionException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(optimizationUrlStart + id + "/startOptimization");
        String emptybody = " ";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("accept", "*/*")
                .header("Content-Type", "application/x-www-form-urlencoded") // Set appropriate content type
                // .POST(HttpRequest.BodyPublishers.noBody()) did not work, this is a workaround
                .POST(HttpRequest.BodyPublishers.ofString(emptybody))
                .build();

        HttpResponse<String> response = HTTPHelper.getResponseAsynch(httpClient, request);
        return response;
    }

    public HttpResponse<String> stopOptimization(String id) throws InterruptedException, ExecutionException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(optimizationUrlStop + id + "/stopOptimization");

        // .POST(HttpRequest.BodyPublishers.noBody()) did not work, this is a workaround
        String emptybody = " ";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("accept", "*/*")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(emptybody))
                .build();

        HttpResponse<String> response = HTTPHelper.getResponseAsynch(httpClient, request);
        return response;
    }
}
