package simon.dragonfly.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import simon.dragonfly.model.Delivery;
import simon.dragonfly.model.DeliveryOrder;

@Component
public class CSVUtility {

    @Value("${json.path.base}")
    private String jsonPathBase;

    @Value("${json.path.input}")
    private String jsonPathInput;

    public List<DeliveryOrder> processCoordinates(MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        List<String> lines = reader.lines().collect(Collectors.toList());
        List<DeliveryOrder> orders = new ArrayList<>();

        // ASSUMPTION: First line is always "latitude,longitude" and is ommitted
        // This is a fairly naive take on a builder
        for (int i = 1; i < lines.size(); i++) {
            String[] co = lines.get(i).split(",");
            String address = "lat=%s;lon=%s".formatted(co[0], co[1]);
            String id = String.valueOf(i);
            String type = "delivery";
            Integer[] size = { 1 };
            Delivery delivery = new Delivery(address);
            orders.add(new DeliveryOrder(id, type, size, delivery));
        }
        return orders;
    }

    public void attachCoordinatesToInput(List<DeliveryOrder> orders) throws FileNotFoundException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        JsonElement ordersJson = gson.toJsonTree(orders);

        JsonObject jsonObject = JsonParser
                .parseReader(new FileReader(jsonPathBase))
                .getAsJsonObject();
        jsonObject.getAsJsonObject("vrp")
                .add("orders", ordersJson);

        try (FileWriter fileWriter = new FileWriter(jsonPathInput)) {
            gson.toJson(jsonObject, fileWriter);
            System.out.println("Field 'orders' added to the JSON object and saved to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
