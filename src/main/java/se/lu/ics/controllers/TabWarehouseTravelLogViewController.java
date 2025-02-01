package se.lu.ics.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import se.lu.ics.models.TravelLogEntry;
import se.lu.ics.models.Warehouse;
import se.lu.ics.models.Shipment;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.*;


/*
 * TabWarehouseTravelLogViewController class
 * Show travel log of all previous and current shipments in a warehouse
 */
public class TabWarehouseTravelLogViewController {
    /*
     * Instance variables
     */
    private AppController appController;
    private Warehouse currentWarehouse;
    private DateTimeFormatter formatter;

    /*
     * Constructor
     */
    public TabWarehouseTravelLogViewController() {
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    /*
     * FXML objects and handlers
     */
    @FXML
    private Label labelWarehouseTravelLogTab;

    @FXML
    private TableView<TravelLogEntry> tableViewWarehouseTravelLog;

    @FXML
    private TableColumn<TravelLogEntry, String> tableColumnArrivalDate;

    @FXML
    private TableColumn<TravelLogEntry, String> tableColumnShipmentId;

    @FXML
    private TableColumn<TravelLogEntry, String> tableColumnDepartureDate;

    @FXML
    private TableColumn<TravelLogEntry, String> tableColumnThroughput;

    @FXML
    private TextField textFieldFindItem;

    @FXML
    private Button buttonFindShipment;

    // Handles button to find shipment
    @FXML
    private void handleButtonFindShipmentAction(ActionEvent event){
        //Search bar
        String inputID = textFieldFindItem.getText();

        if(!inputID.isBlank()){
            // Loop through all the shipments in the list
            for (int i = 0; i < tableViewWarehouseTravelLog.getItems().size(); i++) {
                // Variable to hold the shipment
                TravelLogEntry travelEntry = tableViewWarehouseTravelLog.getItems().get(i);
                // Check if the ID matches the text field input
                if (travelEntry.getShipmentId().equals(inputID)) {
                    // Select the row
                    tableViewWarehouseTravelLog.getSelectionModel().select(i);
                    // Makes the table view "focused" so it's like an active click by a user
                    tableViewWarehouseTravelLog.requestFocus();
                    // Stop the search once a match is found
                    break;
                }
            }
        }
    }


    public void initialize(){
        // Arrival Date
        tableColumnArrivalDate.setCellValueFactory(cellData -> {
            TravelLogEntry travelLog = cellData.getValue();
            String stringToReturn = travelLog.getArrivalDate().format(formatter);
            return new ReadOnlyObjectWrapper<>(stringToReturn);
        });

        // Arrival Location
        tableColumnShipmentId.setCellValueFactory(cellData -> {
            TravelLogEntry travelLog = cellData.getValue();
            String stringToReturn = travelLog.getShipmentId();
            return new ReadOnlyObjectWrapper<>(stringToReturn);
        });

        // Departure date
        tableColumnDepartureDate.setCellValueFactory(cellData -> {
            TravelLogEntry travelLog = cellData.getValue();
            if(travelLog.getDepartureDate() != null){
                String stringToReturn = travelLog.getDepartureDate().format(formatter);
                return new ReadOnlyObjectWrapper<>(stringToReturn);
            } else {
                return new ReadOnlyObjectWrapper<>("-");
            }
        });

        // Throughput
        tableColumnThroughput.setCellValueFactory(cellData -> {
            TravelLogEntry travelLog = cellData.getValue();
            if(travelLog.getArrivalDate() != null && travelLog.getDepartureDate() != null){
                //Integer throughput = travelLog.getThroughput();
                Integer throughput = travelLog.findThroughput();
                //System.out.println(throughput);
                String throughputString = Integer.toString(throughput);
                return new ReadOnlyObjectWrapper<>(throughputString);
            } else {
                return new ReadOnlyObjectWrapper<>("N/A");

            }
        });
    }

    // Populates the tableview from a list
    private void populateTableView(){
        tableViewWarehouseTravelLog.getItems().clear();
        tableViewWarehouseTravelLog.setItems(currentWarehouse.getHistoryShipments());
    }

    // Handles the button to edit a travel log entry
    @FXML
    private void handleButtonEditTravelLogWarehouseAction (ActionEvent event) {
        TravelLogEntry selectedEntry = tableViewWarehouseTravelLog.getSelectionModel().getSelectedItem();
        try{
            if(selectedEntry == null){
                throw new NullPointerException("Please choose a log entry first.");
            }
            Shipment chosenShipment = tableViewWarehouseTravelLog.getSelectionModel().getSelectedItem().getShipment();
            // Open the window
            appController.showEditWarehouseTravelLogView(selectedEntry, tableViewWarehouseTravelLog, chosenShipment);
        } catch(NullPointerException e) {
            showErrorDialog("Error", e.getMessage());
        } catch(Exception e) {
            showErrorDialog("Unknown error", "Please contact systems administrator.");
        }
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
    public void setAppController(AppController appController) {
        this.appController = appController;
        populateTableView();
    }

    public void setCurrentWarehouse(Warehouse currentWarehouse) {
        this.currentWarehouse = currentWarehouse;
        labelWarehouseTravelLogTab.setText("Shipment Log for " + currentWarehouse.getName());
    }
}
