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
 * TabShipmentInspectionsViewController class
 * View shipment inspections
 */
public class TabShipmentInspectionsViewController {

    /*
     *  Instance variables
     */
    private AppController appController;
    private Warehouse currentWarehouse;
    private Shipment currentShipment;
    private DateTimeFormatter formatter;

    /*
     *  Constructor
     */
    public TabShipmentInspectionsViewController() {
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    /*
     * FXML objects and handlers
     */

    @FXML
    private Label shipmentInspectionsTabLabel;

    @FXML
    private TableView<InspectionLogEntry> tableViewShipmentInspections;

    @FXML
    private TableColumn<InspectionLogEntry, String> tableColumnDateofInspection;

    @FXML
    private TableColumn<InspectionLogEntry, String> tableColumnInspectorName;

    @FXML
    private TableColumn<InspectionLogEntry, String> tableColumnInspectionResult;

    // Handles button to add inspection
    @FXML
    public void handleButtonAddInspectionAction (ActionEvent event){
            appController.showAddInspectionView(currentWarehouse, currentShipment);
    }

    // Handle button to delete inspection
    @FXML
    private void handleButtonDeleteInspectionLogAction(ActionEvent event) {
        try{
            InspectionLogEntry selectedInspection = tableViewShipmentInspections.getSelectionModel().getSelectedItem();
            if (selectedInspection == null) {
                throw new NullPointerException("Select an inspection log first.");
            } else {
                currentShipment.removeInspection(selectedInspection);
                currentWarehouse.removeInspection(selectedInspection);
            }
        } catch (NullPointerException e) {
            showErrorDialog("Error: ", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }
    }

     // Opens new window to edit inspection 
     @FXML
     private void handleButtonEditInspectionLogAction (ActionEvent event) {
        InspectionLogEntry selectedEntry = tableViewShipmentInspections.getSelectionModel().getSelectedItem();

        // Check if an inspection is selected
        if (selectedEntry == null) {
            showErrorDialog("Error", "Please select an inspection log entry.");
            return; // Exit method if nothing is selected
        }
        // Proceed with edit
        try {
            appController.showEditWarehouseInspectionsView(selectedEntry, currentShipment, tableViewShipmentInspections );
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }
    }

    // Runs automatically, sets up tableview
    public void initialize(){
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

        // Result
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

    // Sets the tableview to a list
    private void populateTableView(){
        tableViewShipmentInspections.getItems().clear();
        tableViewShipmentInspections.setItems(currentShipment.getInspectionLog());

    }

    // Method to show error popup
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
    public void setCurrentShipment(Shipment currentShipment) {
        this.currentShipment = currentShipment;
    }

    public void setCurrentWarehouse(Warehouse warehouse) {
        this.currentWarehouse = warehouse;
    }

    public void setAppController(AppController appController){
        this.appController = appController;
        populateTableView();
        shipmentInspectionsTabLabel.setText("Inspections of Shipment: " + currentShipment.getId());
    }
}




