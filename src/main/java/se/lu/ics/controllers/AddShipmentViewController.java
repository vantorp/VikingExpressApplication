package se.lu.ics.controllers;

import java.time.LocalDateTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import se.lu.ics.models.Warehouse;
import se.lu.ics.models.Shipment;
import se.lu.ics.models.TravelLogEntry;

/*
 * AddShipmentViewController class
 * Opens a new window where one can add a new shipment
 */
public class AddShipmentViewController {

    /*
     * Instance variables
     */
    private Warehouse warehouse;
    private TableView<Warehouse> tableViewWarehouseRegister;

    /*
     * FXML object and handlers
     */
    @FXML
    private Label labelShipmentVolume;

    @FXML
    private Button buttonCloseWindow;;

    @FXML
    private TextField textFieldShipmentVolume;

    // Handles the button to save a shipment
    @FXML
    private void handleButtonSaveShipmentAction(ActionEvent event) {
        try{
            // Get info from input
            String volumeInput = textFieldShipmentVolume.getText();

            //Input validation
            if (volumeInput.isEmpty()) {
                throw new IllegalArgumentException("Volume cannot be empty.");
            }

            //TextField input is a String, we must convert to double
            double volumeInputParsed;
            try{
                volumeInputParsed = Double.parseDouble(volumeInput);
                if (volumeInputParsed <= 0) {
                    throw new IllegalArgumentException("Volume must be a positive number.");
                }
                if (volumeInputParsed > warehouse.calculateRemainingCapacity()) {
                    throw new IllegalArgumentException("Error: Shipment not added. This shipment exceeds the warehouse's remaining capacity.");
                }
            } catch(NumberFormatException e){
                throw new IllegalArgumentException("Volume must be a valid number.", e);
            }
            
            // Create a shipment object
            Shipment shipment = new Shipment(volumeInputParsed);

            // Add the shipment to a warehouse
            warehouse.addIdleShipment(shipment);
            tableViewWarehouseRegister.refresh();

            // Create and add TravelLogEntry to shipment and warehouse
            TravelLogEntry logentry = new TravelLogEntry(shipment, shipment.getId(), LocalDateTime.now(), warehouse, null, null);
            shipment.addTravelEntry(logentry);
            warehouse.addTravelEntry(logentry);
            
            // Update lists (possibly not needed anymore)
            warehouse.updateAllShipments();

        } catch(IllegalArgumentException e) {
            showErrorDialog("Input Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }
    }

    // Handles the button to close the window
    @FXML
    public void handleButtonCloseWindowAction() {
        Stage currentStage = (Stage) buttonCloseWindow.getScene().getWindow();
        currentStage.close();
    }

    // Helper method to show an error dialog popup
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /*
     * Setters
     */
    public void setCurrentWarehouse(Warehouse warehouse){
        this.warehouse = warehouse;
    }

    public void setTableViewWarehouseRegister(TableView<Warehouse> tableView){
        this.tableViewWarehouseRegister = tableView;
    }
}