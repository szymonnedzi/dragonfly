package simon.dragonfly.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import simon.dragonfly.model.DeliveryOrder;
import simon.dragonfly.utility.CSVUtility;

@RestController
public class CSVController {

    @Autowired
    private CSVUtility csvUtility;

    // Endpoint to handle accepting a CSV file
    @PostMapping(value = "/csv", consumes = { "multipart/form-data" })
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        return processCoordinatesFile(file);
    }

    public ResponseEntity<String> processCoordinatesFile(MultipartFile file) throws IOException {
        prepareOrders(file);
        try {
            return new ResponseEntity<>("Orders attached to the base file", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error attaching orders to the base file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void prepareOrders(MultipartFile file) throws IOException, FileNotFoundException {
        List<DeliveryOrder> orders = csvUtility.processCoordinates(file);
        csvUtility.attachCoordinatesToInput(orders);
    }
}
