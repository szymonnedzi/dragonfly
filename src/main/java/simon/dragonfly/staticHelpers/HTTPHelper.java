package simon.dragonfly.staticHelpers;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HTTPHelper {

    public static HttpResponse<String> getResponseAsynch(HttpClient httpClient, HttpRequest request)
            throws InterruptedException, ExecutionException {
        // Send the request asynchronously
        CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(request, BodyHandlers.ofString());
        return responseFuture.get();
    }
}
