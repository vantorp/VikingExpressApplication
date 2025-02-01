package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import se.lu.ics.models.AppModel;
import javafx.scene.control.TextField;
import se.lu.ics.models.Warehouse;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;

/*
 * AddWarehouseViewController class
 * Opens a new window where one can add a warehouse
 */
public class AddWarehouseViewController {

    /*
     * Instance variables
     */
    private AppController appController;
    private AppModel appModel;
    private String selectedRegion;

    /*
     * FXML objects and handlers
     */
    @FXML
    private Label labelWarehouseName;

    @FXML
    private Label labelWarehouseRegion;

    @FXML
    private Label labelWarehouseAddress;

    @FXML
    private Label labelWarehouseCapacity;

    @FXML
    private Button buttonCancel;

    @FXML
    private Button buttonSaveWarehouse;

    @FXML
    private ComboBox<String> comboBoxRegion;

    @FXML
    private TextField textFieldWarehouseName;

    @FXML
    private TextField textFieldWarehouseAddress;

    @FXML
    private TextField textFieldWarehouseCapacity;

    // Runs automatically first thing when an object of this class is created
    @FXML
    public void initialize() {
        // Populate the region ComboBox with values
        comboBoxRegion.setItems(FXCollections.observableArrayList("North", "Central", "South"));
        // Text when unselected
        comboBoxRegion.setPromptText("Select Region");
    }

    // Updates selectedRegion when something is selected in the ComboBox
    @FXML
    public void handleComboBoxSelection(ActionEvent event) {
        // Get the selected value from the ComboBox
        selectedRegion = comboBoxRegion.getValue();
    }

    // Handles the button to save a warehouse
    @FXML
    private void handleButtonSaveWarehouse(ActionEvent event) {
        try {            
            // Get info from input
            String nameInput = textFieldWarehouseName.getText().trim();
            // Make sure the TextBox is not empty or contains anything but letters or spaces
            if (nameInput.isEmpty()) {
                throw new IllegalArgumentException("Warehouse name cannot be empty.");
            }
            if(!nameInput.matches("^[a-zA-Zåäö ]+$")) {
                throw new IllegalArgumentException("Error: Warehouse name may only contain alphabetical characters.");
            }
            // Check duplicate name
            for(Warehouse w : appModel.getWarehouseRegister().getWarehouses()){
                if(w.getName().equalsIgnoreCase(nameInput)){
                    throw new IllegalArgumentException("Name already exists. Choose another name.");
                }
            }
            // Get region from ComboBox
            selectedRegion = comboBoxRegion.getValue();
            if (selectedRegion == null) {
                throw new IllegalArgumentException("Error: A region must be selected.");
            }
            // Get address from TextField
            String addressInput = textFieldWarehouseAddress.getText().trim();
            if (addressInput.isBlank()) {
                throw new IllegalArgumentException("Error: Address cannot be empty.");
            }
            if (!addressInput.matches("^[a-zA-Z0-9#åäö ]+$")){
                throw new IllegalArgumentException("Error: Address must contain alphanumerical characters.");
            }
            // Get capacity from TextField
            String capacityInput = textFieldWarehouseCapacity.getText().trim();
            if (capacityInput.isBlank()) {
                throw new IllegalArgumentException("Error: Capacity cannot be empty.");
            }
            // Make sure capacity input can be parsed as a double, and that it's bigger than 0
            double capacityInputParsed;
            try {
                capacityInputParsed = Double.parseDouble(capacityInput);
                if (capacityInputParsed <= 0) {
                    throw new IllegalArgumentException("Error: Capacity must be a positive number.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Error: Capacity must be a valid number.", e);
            }
            // Adds a warehouse to the warehouse registry 
            appModel.getWarehouseRegister().addWarehouse(new Warehouse(nameInput, selectedRegion, addressInput, capacityInputParsed));
            // Reload the main view to update stats
            appController.showMainView();
            // Close the window
            Stage stage = (Stage) buttonSaveWarehouse.getScene().getWindow();
            stage.close();
        } catch (IllegalArgumentException e) {
            showErrorDialog("Input Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please contact systems administrator.");
        }
    }

    // Handles the button for closing the window
    @FXML
    private void handleButtonCancel(ActionEvent event) {
        // Stänger fönstret
        Stage stage = (Stage) buttonCancel.getScene().getWindow();
        stage.close();
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
    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
    }

    public void setAppController(AppController appController){
        this.appController = appController;
    }
}