package simon.dragonfly.utility;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import simon.dragonfly.staticHelpers.GSONHelper;
import simon.dragonfly.staticHelpers.HTTPHelper;

@Component
public class SessionUtility {

    @Value("${service.url.session.create}")
    private String sessionUrlCreate;

    @Value("${service.url.session.poll}")
    private String sessionUrlPoll;

    @Value("${polling.interval.session}")
    private Integer pollingIntervalSession;

    @Autowired
    private JSONUtility jsonUtility;

    public HttpResponse<String> initiateSession() throws InterruptedException, ExecutionException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(sessionUrlCreate);
        String jsonPayload = jsonUtility.buildJsonPayload();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonPayload))
                .build();
        return HTTPHelper.getResponseAsynch(httpClient, request);
    }

    public Boolean pollSessionReadyStatus(String id) throws InterruptedException, ExecutionException {
        System.out.println("Session established but not ready. Polling Spider until session is ready.");
        HttpResponse<String> pollingResponse = pollSessionOnce(id);
        return GSONHelper.getIsReady(pollingResponse);
    }

    public HttpResponse<String> pollSessionOnce(String id) throws InterruptedException, ExecutionException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(sessionUrlPoll + id); // unsafe?
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> pollingResponse = HTTPHelper.getResponseAsynch(httpClient, request);
        return pollingResponse;
    }

    public Boolean awaitSessionReady(String id, Boolean isReady) throws InterruptedException, ExecutionException {
        while (!isReady) {
            isReady = pollSessionReadyStatus(id);
            Thread.sleep(pollingIntervalSession);
        }
        return isReady;
    }

    public void deleteSession(String id) throws InterruptedException, ExecutionException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(sessionUrlCreate + id);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HTTPHelper.getResponseAsynch(httpClient, request);
    }
}
