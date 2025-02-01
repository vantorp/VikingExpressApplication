package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import se.lu.ics.models.Shipment;

/*
 * EditShipmentViewController class
 * Opens a window where one can edit a shipment
 */
public class EditShipmentViewController {

    /*
     * Instance variables
     */
    private Shipment currentShipment;
    private TableView<Shipment> tableViewWarehouseShipments;

    /*
     * FXML objects and handlers
     */
    @FXML
    private Button buttonCloseWindow;

    @FXML
    private TextField textFieldShipmentID;

    @FXML
    private TextField textFieldShipmentVolume;

    @FXML
    private Button buttonSaveShipment;

    // Handles the button for saving a shipment
    @FXML
    private void handleButtonSaveShipmentAction(ActionEvent event) {
        try{
            // Get shipment id from TextField
            String shipmentID = textFieldShipmentID.getText();
            if (shipmentID.isBlank()) {
                throw new IllegalArgumentException("Error: Shipment ID cannot be empty.");
            }
            if(!shipmentID.matches("^[a-zA-Z0-9 ]+$")) {
                throw new IllegalArgumentException("Error: Shipment ID may only contain alphanumerical characters.");
            }
            // Check to see if the id has been used before. There's a static method in the Shipmentclass called
            // getUsedIDs() that can run without having a specific shipment object (because it's declared static)
            if (!shipmentID.equals(currentShipment.getId()) && Shipment.getUsedIDs().contains(shipmentID)){
                throw new IllegalArgumentException("Error: Shipment ID already exists in the system. Choose a new ID.");
            }
            
            // Volume of the shipment
            String volumeInput = textFieldShipmentVolume.getText();
            if (volumeInput.isEmpty()) {
                throw new IllegalArgumentException("Error: Volume cannot be empty.");
            }
            // Parsing input String to double, must be positive number
            double volumeInputParsed;
            try{
                volumeInputParsed = Double.parseDouble(volumeInput);
                if (volumeInputParsed <= 0) {
                    throw new IllegalArgumentException("Error: Volume must be a positive number.");
                }
            } catch(NumberFormatException e){
                throw new IllegalArgumentException("Error: Volume must be a valid number.", e);
            }
            
            // Set the details of a shipment based on input above
            currentShipment.setId(shipmentID);
            Shipment.getUsedIDs().add(shipmentID);
            currentShipment.setCubicMeters(volumeInputParsed);
            tableViewWarehouseShipments.refresh();
            // Close the window
            Stage stage = (Stage) buttonSaveShipment.getScene().getWindow();
            stage.close();
        } catch(IllegalArgumentException e) {
            showErrorDialog("Input Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }
    }

    // Handles the button to close a window 
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

    // Helper method to preset the input fields from the current values
    private void fillInEditable() {
        textFieldShipmentID.setText(currentShipment.getId());
        textFieldShipmentVolume.setText(Double.toString(currentShipment.getCubicMeters()));
    }

    /*
     * Setters
     */
    public void setCurrentShipment(Shipment shipment){
        this.currentShipment = shipment;
        // Run the method that fills in input fields when setCurrentShipment is called from the AppController
        fillInEditable(); 
    }

    public void setCurrentTableView(TableView<Shipment> tableViewWarehouseShipments){
        this.tableViewWarehouseShipments = tableViewWarehouseShipments;
    }
}