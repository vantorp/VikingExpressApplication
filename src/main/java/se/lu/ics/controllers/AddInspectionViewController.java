package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import se.lu.ics.models.Shipment;
import se.lu.ics.models.Warehouse;
import se.lu.ics.models.InspectionLogEntry;
import java.time.LocalDateTime;
import javafx.scene.control.Alert;


/*
 * AddInspectionViewController class
 * Opens a new window where one can add an inspection
 */
public class AddInspectionViewController {

    /*
     * Instance variables
     */
    private Warehouse currentWarehouse;
    private Shipment currentShipment;

    /*
     * FXML objects and handlers
     */
    @FXML
    private Label labelTopHeading;

    @FXML
    private Label labelLocation;

    @FXML
    private Label labelShipment;

    @FXML
    private Label labelInspector;

    @FXML
    private TextField textFieldInspector;

    @FXML
    private CheckBox checkBoxApproved;

    @FXML
    private Label labelComment;

    @FXML
    public Button buttonCloseWindow;

    @FXML
    public Button buttonCreateInspection;

    /*
     * Handles the button to save a new inspection
     */
    @FXML
    public void handleButtonAddInspectionAction(ActionEvent event) {
        try{
            // Inspector name input
            String inspectorInput = textFieldInspector.getText().trim();
            if (inspectorInput.isBlank()){
                throw new IllegalArgumentException("Inspector field cannot be empty.");
            }
            // Everything inside "[]" is allowed in the input field. "^" and "$" signify beginning and end of line
            if(!inspectorInput.matches("^[a-zA-Zåäö ]+$")){
                throw new IllegalArgumentException("Error: Name must only contain alphabetical characters.");
            }
            // Create new InspectionLogEntry object
            InspectionLogEntry newInspection = new InspectionLogEntry(currentShipment, LocalDateTime.now(), inspectorInput, currentShipment.getId(), checkBoxApproved.isSelected());
            // Add the log object in the shipment log list
            currentShipment.addInspection(newInspection);
            // Add the log object to the warehouse log list
            currentWarehouse.addInspection(newInspection);
            // Close the window
            Stage stage = (Stage) buttonCreateInspection.getScene().getWindow();
            stage.close();
        } catch(IllegalArgumentException e){
            // See helper method
            showErrorDialog("Error:", e.getMessage());
        }
    }

    // Handler for the button that closes the window
    @FXML
    public void handleButtonCloseWindowAction() {
        Stage currentStage = (Stage) buttonCloseWindow.getScene().getWindow();
        currentStage.close();
    }

    // Helper-metod för att visa felmeddelande
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // This method is called from appController to set the labels
    public void setLabels(){
        labelLocation.setText("Warehouse: " + currentWarehouse.getName());
        labelShipment.setText("Shipment: " + currentShipment.getId());
    }

    /*
     * Getters and setters
     */
    public void setCurrentWarehouse(Warehouse warehouse) {
        this.currentWarehouse = warehouse;
    }

    public void setCurrentShipment(Shipment shipment) {
        this.currentShipment = shipment;
    }
}
