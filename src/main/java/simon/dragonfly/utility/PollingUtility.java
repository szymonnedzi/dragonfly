package simon.dragonfly.utility;

import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import simon.dragonfly.staticHelpers.GSONHelper;

@Component
public class PollingUtility {

    @Value("${service.url.session.poll}")
    private String sessionUrlPoll;

    @Value("${polling.interval.progress}")
    private Integer pollingIntervalProgress;

    @Autowired
    private SessionUtility sessionUtility;

    public Double pollUntilBestSolutionValueIsReached(String id) throws InterruptedException, ExecutionException {
        // Init
        Boolean isMinimumReached = false;
        HttpResponse<String> response = sessionUtility.pollSessionOnce(id);
        Double currentBSV = GSONHelper.getBestSolutionValue(response);
        System.out.println("Polling for best value, current candidate: %s , polling interval: %s ms"
                .formatted(String.valueOf(currentBSV), String.valueOf(pollingIntervalProgress)));

        // Poll
        while (!isMinimumReached) {
            Thread.sleep(pollingIntervalProgress);
            HttpResponse<String> newResponse = sessionUtility.pollSessionOnce(id);
            Double newestBSV = GSONHelper.getBestSolutionValue(newResponse);
            if (newestBSV < currentBSV) {
                System.out.println("Polling for best value, current candidate: %s , polling interval: %s ms"
                        .formatted(String.valueOf(newestBSV), String.valueOf(pollingIntervalProgress)));
                currentBSV = newestBSV;
            } else {
                System.out.println("Found best value: %s".formatted(String.valueOf(newestBSV)));
                currentBSV = newestBSV;
                isMinimumReached = true;
            }
        }
        return currentBSV;
    }
}
