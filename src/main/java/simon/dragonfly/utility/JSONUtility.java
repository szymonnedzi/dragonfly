package simon.dragonfly.utility;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JSONUtility {

    @Value("${json.path.base}")
    private String jsonPathBase;

    @Value("${json.path.input}")
    private String jsonPathInput;

    @Value("${json.path.output}")
    private String jsonPathOutput;

    public String buildJsonPayload() {
        String jsonPayload = "";
        try {
            Path path = Paths.get(jsonPathInput);
            jsonPayload = Files.readString(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonPayload;
    }

    public void saveJsonStringToFile(String jsonString) {
        try (FileWriter fileWriter = new FileWriter(jsonPathOutput)) {
            fileWriter.write(jsonString);
            System.out.println("JSON data saved to file: " + jsonPathOutput);
        } catch (IOException e) {
            System.err.println("Error saving JSON data to file: " + e.getMessage());
        }
    }

    public String readJSONFileAsString() throws IOException {
        // Read the file content as a string
        Path path = Path.of(jsonPathOutput);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }
}
