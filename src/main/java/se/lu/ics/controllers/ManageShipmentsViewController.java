package se.lu.ics.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.*;
import se.lu.ics.models.Warehouse;
import se.lu.ics.models.Shipment;
import se.lu.ics.models.TravelLogEntry;
import javafx.util.StringConverter;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * ManageShipmentsViewController class
 * Shows shipments in a warehouse and the travel log for a shipment
 */
public class ManageShipmentsViewController {
    /*
     * Instance variables
     */
    private AppController appController;
    private Warehouse currentWarehouse;
    private DateTimeFormatter formatter;
    private TableView<Warehouse> tableViewWarehouseRegister;

    /*
     * Constructor
     */
    public ManageShipmentsViewController(){
        // Define the date pattern to use in formatted dates
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    /*
     * FXML objects and handlers
     */

    @FXML
    private Label shipmentTravelLogTabLabel;
    
    @FXML
    private Label labelManageTravelLogTableTitle;

    @FXML
    private Label labelManageShipmentsViewTitle;

    @FXML
    private TextField textFieldFindItem;

    //Table of shipments (to the left)

    @FXML
    private TableView<Shipment> tableViewWarehouseShipments;

    @FXML
    private TableColumn<Shipment, String> tableColumnShipmentId;

    @FXML
    private TableColumn<Shipment, Double> tableColumnShipmentVolume;

    @FXML
    private TableColumn<Shipment, String> tableColumnShipmentStatus;

    @FXML
    private TableColumn<Shipment, String> tableColumnShipmentLastInspectionDate;

    @FXML
    private TableColumn<Shipment, Integer> tableColumnShipmentDaysInWarehouse;

    @FXML
    private TableColumn<Shipment, Integer> tableColumnNumberWarehouses;

    // Table of travel logs (to the right)

    @FXML
    private TableView<TravelLogEntry> tableViewShipmentTravelLog;

    @FXML
    private TableColumn<TravelLogEntry, String> tableColumnArrivalDate;

    @FXML
    private TableColumn<TravelLogEntry, String> tableColumnArrivalLocation;

    @FXML
    private TableColumn<TravelLogEntry, String> tableColumnDepartureDate;

    @FXML
    private TableColumn<TravelLogEntry, String> tableColumnThroughput;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab tabManageShipments;

    // Search box for shipments. Look at all items in the table view and select the first one it finds that
    // matches what is in the text field. If the text field is empty nothing happens.
    @FXML
    private void handleButtonFindShipmentAction(ActionEvent event){
        // Get input from TextField
        String inputID = textFieldFindItem.getText();
        if(!inputID.isBlank()){
            // Loop through all the shipments in the list
            for (int i = 0; i < tableViewWarehouseShipments.getItems().size(); i++) {
                // Variable to hold the shipment
                Shipment shipment = tableViewWarehouseShipments.getItems().get(i);
                // Check if the ID matches the text field input
                if (shipment.getId().equals(inputID)) {
                    // Select the row
                    tableViewWarehouseShipments.getSelectionModel().select(i);
                    // Makes the table view "focused" so it's like an active click by a user
                    tableViewWarehouseShipments.requestFocus();
                    // Stop the search once a match is found
                    break;
                }
            }
        }
    }

    // Handles the button to open the add shipment window
    @FXML
    private void handleButtonAddShipmentAction(ActionEvent event) {
        appController.showAddShipmentView(currentWarehouse, tableViewWarehouseShipments, tableViewWarehouseRegister);
        //"Refreshes" main view to show updated main view statistics
        appController.showMainView();
    }

    // Handles the button to open the edit shipment window
    @FXML
    private void handleButtonEditShipmentAction(ActionEvent event) {
        try {
            Shipment selectedShipment = tableViewWarehouseShipments.getSelectionModel().getSelectedItem();
            if (selectedShipment == null) {
                System.err.println("A shipment must be chosen.");
                throw new NullPointerException("Select a shipment first.");
            } else {
                // Open the window
                appController.showEditShipmentView(selectedShipment, tableViewWarehouseShipments);
            }
        } catch (NullPointerException e) {
            showErrorDialog("Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }  
    }

    // Handles the button to remove a travel log entry for a shipment
    @FXML
    private void handleButtonDeleteShipmentLogAction(ActionEvent event) {
        try{
            TravelLogEntry selectedEntry = tableViewShipmentTravelLog.getSelectionModel().getSelectedItem();
            Shipment selectedShipment = tableViewWarehouseShipments.getSelectionModel().getSelectedItem();
            if (selectedEntry == null) {
                throw new NullPointerException("Select a shipment log first.");
            } else {
                selectedShipment.removeTravelEntry(selectedEntry);
                populateTableView();
            }
        } catch (NullPointerException e) {
            showErrorDialog("Error: ", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }
    }

    // Handles the button to remove a shipment
    @FXML
    private void handleButtonRemoveShipmentAction(ActionEvent event){
        try{
            Shipment selectedShipment = tableViewWarehouseShipments.getSelectionModel().getSelectedItem();
            if (selectedShipment == null) {
                throw new NullPointerException("Select a shipment first.");
            } else {
                // Find out from which list the shipment can be removed
                // Idle
                if(currentWarehouse.getIdleShipments().contains(selectedShipment)){
                    currentWarehouse.removeIdleShipment(selectedShipment);
                    tableViewWarehouseRegister.refresh();
                    currentWarehouse.removeAllShipments(selectedShipment);
                    tableViewWarehouseShipments.getSelectionModel().clearSelection();
                }
                // Incoming
                if(currentWarehouse.getIncomingShipments().contains(selectedShipment)){
                    showWarningDialog("Attention!", "You are rejecting an incoming shipment. Please notify the Operations & Logistics Department from the sending warehouse.");
                    currentWarehouse.removeIncomingShipment(selectedShipment);
                    tableViewWarehouseRegister.refresh();
                    currentWarehouse.removeAllShipments(selectedShipment);
                    tableViewWarehouseShipments.getSelectionModel().clearSelection();
                }
                // Outgoing
                if(currentWarehouse.getOutgoingShipments().contains(selectedShipment)){
                    showWarningDialog("Attention!", "You are cancelling an outgoing shipment. Please notify the Operations & Logistics Department at the receiving warehouse.");
                    currentWarehouse.removeOutgoingShipment(selectedShipment);
                    tableViewWarehouseRegister.refresh();
                    currentWarehouse.removeAllShipments(selectedShipment);
                    tableViewWarehouseShipments.getSelectionModel().clearSelection();
                }
                //Clear old travel entries associated with deleted shipment
                tableViewShipmentTravelLog.getItems().clear();
                // Update/refresh tableview and stats in main view
                populateTableView();
                appController.showMainView();
            }
        } catch (NullPointerException e) {
            showErrorDialog("Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }  
        
    }

    // Handles button to open the handle shipment tab
    @FXML
    public void handleButtonHandleShipmentAction(ActionEvent event){
        try{
            Shipment selectedShipment = tableViewWarehouseShipments.getSelectionModel().getSelectedItem();
            if (selectedShipment == null) {
                throw new NullPointerException("Error: Select a shipment first.");
            } else {
                // Open the window
                appController.showTabHandleShipmentActionsView(currentWarehouse, selectedShipment);
            }
        } catch (NullPointerException e) {
            showErrorDialog("Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        } 
    }
    
    // Handles the button to open the inspection logs for a shipment in a tab
    @FXML
    private void handleButtonViewInspectionLogAction(ActionEvent event){
        try {
            Shipment selectedShipment = tableViewWarehouseShipments.getSelectionModel().getSelectedItem();
            if (selectedShipment == null) {
                throw new NullPointerException("Error: Select a shipment first.");
            } else {
                // Open the tab
                appController.showTabShipmentInspectionsView(selectedShipment, currentWarehouse);
            }
        } catch (NullPointerException e) {
            showErrorDialog("Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }
    }

    // Opens a new window to edit a shipment travel log
    @FXML
    private void handleButtonEditTravelLogWarehouseAction (ActionEvent event) {
        TravelLogEntry selectedEntry = tableViewShipmentTravelLog.getSelectionModel().getSelectedItem();
        //Null check
        if (selectedEntry == null) {
            showErrorDialog("Error:", "Error: Please select a travel log entry to edit.");
            return;
        }
        Shipment chosenShipment = tableViewShipmentTravelLog.getSelectionModel().getSelectedItem().getShipment();
        // Open window
        appController.showEditWarehouseTravelLogView(selectedEntry, tableViewShipmentTravelLog, chosenShipment);
    }
    
    // Method to handle main tab selection, workaround to make everything update, runs every time the current tab is selected
    @FXML
    private void onMainTabSelected() {
        if(currentWarehouse != null){
            if (tabManageShipments.isSelected()) {
                populateTableView();
                tableViewShipmentTravelLog.refresh();
            }
        }
    }
    
    // Runs every time an object of this class is created
    public void initialize(){
        // TableView som visar shipments i det valda warehouset
        // Id
        tableColumnShipmentId.setCellValueFactory(cellData -> cellData.getValue().idProperty()); // nytt sätt
        tableColumnShipmentId.setCellValueFactory(new PropertyValueFactory<>("id")); // tidigare sätt
        tableColumnShipmentId.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnShipmentId.setOnEditCommit(event -> {
            Shipment shipment=event.getRowValue();
            shipment.setId(event.getNewValue());
        });

        // Volume
        tableColumnShipmentVolume.setCellValueFactory(cellData -> cellData.getValue().cubicMetersProperty().asObject()); // nytt sätt
        tableColumnShipmentVolume.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>(){
            @Override
            public String toString(Double object){
                return String.format("%.2f", object);
            }

            @Override
                public Double fromString(String string){
                    try{
                        return Double.parseDouble(string);
                    } catch (NumberFormatException e){
                String errorMessage = "Invalid input format. Please enter a number.";
                System.out.println(errorMessage);
                return 0.0;
                }
            }
        }));
        tableColumnShipmentVolume.setOnEditCommit(event -> {
            Shipment shipment=event.getRowValue();
            shipment.setCubicMeters(event.getNewValue());
        });

        // Status (Example of showing a custom value in the tableview)
        tableColumnShipmentStatus.setCellValueFactory(cellData -> {
            Shipment shipment = cellData.getValue();
            if (currentWarehouse.getIncomingShipments().contains(shipment)) {
                return new ReadOnlyObjectWrapper<>("Incoming");
            }
            if (currentWarehouse.getIdleShipments().contains(shipment)) {
                return new ReadOnlyObjectWrapper<>("Idle");
            }
            if (currentWarehouse.getOutgoingShipments().contains(shipment)) {
                return new ReadOnlyObjectWrapper<>("Outgoing");
            }
            return new ReadOnlyObjectWrapper<>("Error: Unknown status");
        });

        // Last inspection date
        tableColumnShipmentLastInspectionDate.setCellValueFactory(new PropertyValueFactory<>("lastInspectionDate"));

        // Days in warehouse
        tableColumnShipmentDaysInWarehouse.setCellValueFactory(cellData -> {
            Shipment shipment = cellData.getValue();
            if (currentWarehouse.getIncomingShipments().contains(shipment)) {
                return new ReadOnlyObjectWrapper<>(0);
            } else {
                int totalDays = shipment.getDaysInWarehouse();
                return new ReadOnlyObjectWrapper<>(totalDays);
            }
        });

        // Number of warehouses
        tableColumnNumberWarehouses.setCellValueFactory(cellData -> {
        Shipment shipment = cellData.getValue();
            int totalWarehouses = shipment.numberWarehouses();
                return new ReadOnlyObjectWrapper<>(totalWarehouses);
        });        

        // Sets the row to different background colors as warning for shipments that need attention
        tableViewWarehouseShipments.setRowFactory(tv -> {
            TableRow<Shipment> row = new TableRow<>();
            row.itemProperty().addListener((observable, oldValue, newValue) -> {
                // Check if the value returned by getSomeNumber() is greater than or equal to 14
                if (newValue != null && newValue.getDaysInWarehouse() >= 14 && !currentWarehouse.getIncomingShipments().contains(newValue)) {
                    // Change the background color to red if greater than 15
                    row.setStyle("-fx-background-color: #f67f94;");
                } else if (newValue != null && newValue.getDaysInWarehouse() >= 11 && !currentWarehouse.getIncomingShipments().contains(newValue)) {
                    // Change the background color to orange if greater than or equal to 11
                    row.setStyle("-fx-background-color:rgb(253, 172, 86);");
                } else {
                    // Reset the background color
                    row.setStyle("");
                }
            });
            return row;
        });

        /*Lägger till en "listener" som populerar travel log tableview på högra sidan
         * Det "lyssnar" på vänstra tabellen.
         */
         tableViewWarehouseShipments.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection)-> {
            if (newSelection!=null) {
                //Fylla in TravelLog tabell med valde information
                updateTravelLogTable(newSelection);
                labelManageTravelLogTableTitle.setText("Travel Log for Shipment: " + newSelection.getId());;
            }
         });

        // Arrival date
        //ny version, använder properties
        tableColumnArrivalDate.setCellValueFactory(cellData -> {
            // Get the LocalDateTime value
            LocalDateTime date = cellData.getValue().arrivalDateProperty().getValue();
            // Format the date using the formatter
            if(date != null){
                String formattedDate = date.format(formatter);
                // Return the formatted date wrapped in a ReadOnlyObjectWrapper
                return new ReadOnlyObjectWrapper<>(formattedDate);
            } else {
                return new ReadOnlyObjectWrapper<>("-");
            }
        });

        // Arrival Location
        tableColumnArrivalLocation.setCellValueFactory(cellData -> {
            Warehouse warehouse = cellData.getValue().arrivalLocationProperty().getValue();
            if(warehouse != null) {
                String stringToReturn = warehouse.getName();
                return new ReadOnlyObjectWrapper<>(stringToReturn);
            } else {
                return new ReadOnlyObjectWrapper<>("-");
            }
        });

        // Departure date
        tableColumnDepartureDate.setCellValueFactory(cellData -> {
            // Get the LocalDateTime value
            LocalDateTime date = cellData.getValue().departureDateProperty().getValue();
            // Format the date using the formatter
            if(date != null){
                String formattedDate = date.format(formatter);
                // Return the formatted date wrapped in a ReadOnlyObjectWrapper
                return new ReadOnlyObjectWrapper<>(formattedDate);
            } else {
                return new ReadOnlyObjectWrapper<>("-");
            }
        });

        // Throughput
        tableColumnThroughput.setCellValueFactory(cellData -> {
            TravelLogEntry travelLog = cellData.getValue();
            if(travelLog.getArrivalDate() != null && travelLog.getDepartureDate() != null){
                travelLog.calculateThroughput();
                Integer throughput = travelLog.getThroughput();
                String throughputString = Integer.toString(throughput);
                return new ReadOnlyObjectWrapper<>(throughputString);
            } else {
                return new ReadOnlyObjectWrapper<>("N/A");

            }
        });
    }

    // Helper method to the listener in initialize(), populates the right tableview based on selection in the left
    private void updateTravelLogTable(Shipment selectedShipment){
        ObservableList<TravelLogEntry> travelLogEntries = FXCollections.observableArrayList(selectedShipment.getTravelLog());
        //Set items of TravelLogEntry tabell
        tableViewShipmentTravelLog.setItems(travelLogEntries);
    }

    // Populates the left tableview
    public void populateTableView(){
        labelManageTravelLogTableTitle.setText("Travel Log for Shipment: ");
        tableViewWarehouseShipments.setItems(currentWarehouse.getAllShipments());
    }

    // Shows a popup window if there are "stale" shipments
    public void warnAboutStaleShipments() {
        for (Shipment shipment : tableViewWarehouseShipments.getItems()) {
            if (shipment.getDaysInWarehouse() > 12 && !currentWarehouse.getIncomingShipments().contains(shipment)) {
                    showWarningDialog("Attention needed", "One or more shipments is nearing, or has exceeded, the 14 day maximum limit for storage.");
                break; 
            }
        }
    }

    // Method to show an error dialog popup
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to show a warning dialog
    private void showWarningDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Adjust the size of the dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setPrefWidth(450); // Set preferred width
        dialogPane.setPrefHeight(100); // Set preferred height
        alert.showAndWait();
    }

    /*
     * Getters and setters
     */

    public void setAppController(AppController appController) {
        this.appController = appController;
    }
    
    public void setCurrentWarehouse(Warehouse warehouse) {
        this.currentWarehouse = warehouse;
    }

    public void setTableViewWarehouseRegister(TableView<Warehouse> tableView){
        this.tableViewWarehouseRegister = tableView;
        labelManageShipmentsViewTitle.setText("Shipments in: " + currentWarehouse.getName());
        populateTableView();
    }
}