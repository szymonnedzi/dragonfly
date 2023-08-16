package simon.dragonfly.staticHelpers;

import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GSONHelper {
    
    public static Boolean getIsReady(HttpResponse<String> pollingResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(pollingResponse.body(), JsonObject.class);
        Boolean isReady = jsonObject.get("isReady").getAsBoolean();
        return isReady;
    }

    public static String getID(HttpResponse<String> pollingResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(pollingResponse.body(), JsonObject.class);
        String id = jsonObject.get("id").getAsString();
        return id;
    }

    public static Double getBestSolutionValue(HttpResponse<String> pollingResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(pollingResponse.body(), JsonObject.class);
        Double bestSolutionValue = jsonObject.get("bestSolutionValue").getAsDouble();
        return bestSolutionValue;
    }
}
