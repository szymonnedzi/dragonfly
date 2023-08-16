package simon.dragonfly.controllers;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "CSV Controller")
public class CSVController {

    @Autowired
    private CSVUtility csvUtility;

    @PostMapping(value = "/csv", consumes = { "multipart/form-data" })
    @Operation(summary = "Upload and process a CSV file with delivery coordinates", description = "This endpoint allows you to upload a CSV file containing delivery coordinates. The coordinates will be processed and attached to the base file.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders attached to the base file"),
            @ApiResponse(responseCode = "500", description = "Error attaching orders to the base file")
    })
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        return processCoordinatesFile(file);
    }

    private ResponseEntity<String> processCoordinatesFile(MultipartFile file) {
        try {
            prepareOrders(file);
            return new ResponseEntity<>("Orders attached to the base file", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error attaching orders to the base file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void prepareOrders(MultipartFile file) throws Exception {
        // Process the CSV file and attach coordinates to the input
        List<DeliveryOrder> orders = csvUtility.processCoordinates(file);
        csvUtility.attachCoordinatesToInput(orders);
    }
}