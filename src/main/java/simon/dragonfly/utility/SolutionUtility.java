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
public class SolutionUtility {

    @Value("${service.url.solution.fetch}")
    private String fetchBestSolution;

    public HttpResponse<String> getSolution(String id) throws InterruptedException, ExecutionException {
        // Create an HTTP request with POST method
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(fetchBestSolution + id + "/bestSolution");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("accept", "*/*")
                .GET()
                .build();

        HttpResponse<String> response = HTTPHelper.getResponseAsynch(httpClient, request);
        return response;
    }


}
