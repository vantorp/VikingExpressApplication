package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import se.lu.ics.models.AppModel;
import javafx.scene.control.TextField;
import se.lu.ics.models.Warehouse;
import javafx.collections.FXCollections;

/*
 * EditWarehouseViewController class
 * A window where one can edit a warehouse
 */
public class EditWarehouseViewController {
    /*
     * Instance variables
     */
    private AppModel appModel;
    private AppController appController;
    private String selectedRegion;
    private Warehouse selectedWarehouse;

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
    private Button buttonUpdateWarehouse;

    @FXML
    private ComboBox<String> comboBoxRegion;

    @FXML
    private TextField textFieldWarehouseName;

    @FXML
    private TextField textFieldWarehouseAddress;

    @FXML
    private TextField textFieldWarehouseCapacity;

    // Runs automatically when an object of this class is created
    @FXML
    public void initialize() {
        // Populate the ComboBox with region Strings
        comboBoxRegion.setItems(FXCollections.observableArrayList("North", "Central", "South"));
    }

    // Updates the selectedRegion String when something is selected in the ComboBox
    @FXML
    public void handleComboBoxSelection(ActionEvent event) {
        // Get the selected value from the ComboBox
        selectedRegion = comboBoxRegion.getValue();
    }

    // Handles the button to save/update a warehouse
    @FXML
    private void handleButtonUpdateWarehouse(ActionEvent event) {
        try {            
            // Get warehouse name from input TextField, must be non-empty and alphanumeric
            String nameInput = textFieldWarehouseName.getText().trim();
            if (nameInput.isEmpty()) {
                throw new IllegalArgumentException("Warehouse name cannot be empty.");
            }
            if(!nameInput.matches("^[a-zA-Zåäö ]+$")) {
                throw new IllegalArgumentException("Error: Warehouse name may only contain alphabetical characters.");
            }

            // Check duplicate name
            for(Warehouse w : appModel.getWarehouseRegister().getWarehouses()){
                if(!selectedWarehouse.getName().equals(nameInput) && w.getName().equals(nameInput)){
                    throw new IllegalArgumentException("Name already exists. Choose another name.");
                }
            }

            // Get selected region from ComboBox
            selectedRegion = comboBoxRegion.getValue();

            //Get address from input TextField
            String addressInput = textFieldWarehouseAddress.getText().trim();
            if (addressInput.isBlank()) {
                throw new IllegalArgumentException("Address cannot be empty.");
            }

            // Get capacity from TextField
            String capacityInput = textFieldWarehouseCapacity.getText().trim();
            if (capacityInput.isBlank()) {
                throw new IllegalArgumentException("Capacity cannot be empty.");
            }

            // Parse capacity into double
            double capacityInputParsed;
            try {
                capacityInputParsed = Double.parseDouble(capacityInput);
                if (capacityInputParsed <= 0) {
                    throw new IllegalArgumentException("Capacity must be a positive number.");
                }
                if (capacityInputParsed < selectedWarehouse.getStockLevel()){
                    throw new IllegalArgumentException("Warehouse capacity must not be less than the current stock level.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Capacity must be a valid number.", e);
            }

            // Uppdate the warehouse info
            selectedWarehouse.setName(nameInput);
            selectedWarehouse.setRegion(selectedRegion);
            selectedWarehouse.setCapacity(capacityInputParsed);
            selectedWarehouse.setAddress(addressInput);

            // Reload main view and manage warehouse view to reflect changes
            appController.showMainView();
            appController.showManageWarehouseView();
            // Close the window
            Stage stage = (Stage) buttonUpdateWarehouse.getScene().getWindow();
            stage.close();

        } catch (IllegalArgumentException e) {
            showErrorDialog("Input Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }
    }

    // Handles the button to close the window
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

    // Helper method to fill in current warehouse info
    public void fillInEditable() {
        // TextField for Warehouse name
        textFieldWarehouseName.setText(selectedWarehouse.getName());
        // TextField for address
        textFieldWarehouseAddress.setText(selectedWarehouse.getAddress());
        // TextField for capacity
        textFieldWarehouseCapacity.setText(Double.toString(selectedWarehouse.getCapacity()));
        // ComboBox for region
        comboBoxRegion.setValue(selectedWarehouse.getRegion());
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

    public void setCurrentWarehouse(Warehouse warehouse) {
        this.selectedWarehouse = warehouse;
        fillInEditable();
    }
}
