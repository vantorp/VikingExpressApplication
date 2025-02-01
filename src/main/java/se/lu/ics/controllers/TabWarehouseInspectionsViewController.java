package se.lu.ics.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import se.lu.ics.models.Warehouse;
import se.lu.ics.models.Shipment;
import se.lu.ics.models.InspectionLogEntry;
import javafx.scene.control.cell.TextFieldTableCell;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * TabWarehouseInspectionsViewController
 * Shows inspections at a warehouse
 */

public class TabWarehouseInspectionsViewController {
    /*
     *  Instance variables
     */
    private AppController appController;
    private Warehouse currentWarehouse;
    private DateTimeFormatter formatter;

    /*
     *  Constructor
     */
    public TabWarehouseInspectionsViewController() {
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    /*
     * FXML objects and handlers
     */

    @FXML
    private Label warehouseInspectionsTabLabel;

    @FXML
    private TableView<InspectionLogEntry> tableViewWarehouseInspections;

    @FXML
    private TableColumn<InspectionLogEntry, String> tableColumnShipmentId;

    @FXML
    private TableColumn<InspectionLogEntry, String> tableColumnDateofInspection;

    @FXML
    private TableColumn<InspectionLogEntry, String> tableColumnInspectorName;

    @FXML
    private TableColumn<InspectionLogEntry, String> tableColumnInspectionResult;
    
    @FXML
    private TableColumn<Warehouse, String> tableColumnWarehouseLastInspectionDate;

    @FXML
    private TextField textFieldFindItem;

    @FXML
    private Button buttonFindShipmentAction;

    // Handles the button to find a shipment
    @FXML
    private void handleButtonFindShipmentAction(ActionEvent event){
        //Search bar
        String inputID = textFieldFindItem.getText();

        if(!inputID.isBlank()){
            // Loop through all the shipments in the list
            for (int i = 0; i < tableViewWarehouseInspections.getItems().size(); i++) {
                // Variable to hold the shipment
                InspectionLogEntry inspection = tableViewWarehouseInspections.getItems().get(i);
                // Check if the ID matches the text field input
                if (inspection.getShipmentId().equals(inputID)) {
                    // Select the row
                    tableViewWarehouseInspections.getSelectionModel().select(i);
                    // Makes the table view "focused" so it's like an active click by a user
                    tableViewWarehouseInspections.requestFocus();
                    // Stop the search once a match is found
                    break;
                }
            }
        }
    }

    // Handles the button to remove log
    @FXML
    private void handleButtonDeleteWarehouseInspectionLogAction(ActionEvent event) {
        System.out.println("Remove Inspection Log clicked");
        try{
            InspectionLogEntry selectedInspection = tableViewWarehouseInspections.getSelectionModel().getSelectedItem();
            if (selectedInspection == null) {
                throw new NullPointerException("Select a warehouse first.");
            } 
            currentWarehouse.removeInspection(selectedInspection);
            Shipment foundShipment = selectedInspection.getShipment();
            foundShipment.removeInspection(selectedInspection);            
        } catch (NullPointerException e) {
            showErrorDialog("Error: ", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please contact systems administrator.");
        }
        // Update, workaround until better way
        tableViewWarehouseInspections.refresh();
    }

    // Opens a window to edit a warehouse shipment log
    @FXML
    private void handleButtonUpdateWarehouseInspectionLogAction (ActionEvent event) {
        try{
            InspectionLogEntry selectedEntry = tableViewWarehouseInspections.getSelectionModel().getSelectedItem();
            if (selectedEntry == null){
                throw new NullPointerException("Please select an inspection first.");
            }
            Shipment chosenShipment = tableViewWarehouseInspections.getSelectionModel().getSelectedItem().getShipment();
            appController.showEditWarehouseInspectionsView(selectedEntry, chosenShipment, tableViewWarehouseInspections);
        } catch(NullPointerException e){
            showErrorDialog("Error: ", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please contact systems administrator.");
        }
    }

    // Runs automatically
    public void initialize(){
        // Shipment ID
        tableColumnShipmentId.setCellValueFactory(cellData -> cellData.getValue().shipmentIdProperty());

        // Date (formatted to String)
        tableColumnDateofInspection.setCellValueFactory(cellData -> {
            // Get the LocalDateTime value
            LocalDateTime date = cellData.getValue().dateProperty().getValue();
            // Format the date using the formatter
            String formattedDate = date.format(formatter);
            // Return the formatted date wrapped in a ReadOnlyObjectWrapper
            return new ReadOnlyObjectWrapper<>(formattedDate);
        });

        // Inspector name
        tableColumnInspectorName.setCellValueFactory(cellData -> cellData.getValue().inspectorNameProperty()); // Ny version
        tableColumnInspectorName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnInspectorName.setOnEditCommit(event -> {
            InspectionLogEntry inspectionLogEntry=event.getRowValue();
            inspectionLogEntry.setInspectorName(event.getNewValue());
        });

        tableColumnInspectionResult.setCellValueFactory(cellData ->{
            InspectionLogEntry inspectionLogEntry=cellData.getValue();
            //boolean already returns the true or false condition needed to proceed
            if(inspectionLogEntry.getIsApproved()){
                return new ReadOnlyObjectWrapper<>("Approved");
            } else {
                return new ReadOnlyObjectWrapper<>("Not Approved");
            }
        });
    }

    // Helper method to populate tableview from list
    private void populateTableView(){
        tableViewWarehouseInspections.getItems().clear();
        tableViewWarehouseInspections.setItems(currentWarehouse.getHistoryInspections());
    }

    // Helper method for error popup
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /*
     *  Getters och setters
     */
    public void setCurrentWarehouse(Warehouse currentWarehouse) {
        this.currentWarehouse = currentWarehouse;
        populateTableView();
        warehouseInspectionsTabLabel.setText("History of Inspections at " + currentWarehouse.getName() + " Warehouse:");
    }
    public void setAppController(AppController appController){
        this.appController = appController;
    }
}
