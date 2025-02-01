package se.lu.ics.controllers;

import se.lu.ics.models.AppModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
//import javafx.scene.Parent;
import javafx.scene.control.*;
import se.lu.ics.models.Warehouse;
import se.lu.ics.models.TravelLogEntry;
import se.lu.ics.models.Shipment;
import java.time.LocalDateTime;
import java.util.Optional;

/*
 * TabHandleShipmentActionsViewController class
 * This tab is for manually handling/moving shipments between warehouses.
 */
public class TabHandleShipmentActionsViewController {
    /*
     *  Instance variables
     */
    private AppModel appModel;
    private Shipment currentShipment;
    private Warehouse currentWarehouse;

    /*
     * FXML objects and handlers
     */
    @FXML
    private Label shipmentIdTabLabel;

    @FXML
    private Label labelSituation;

    @FXML
    private Label moveShipmentHandleShipmentTabLabel;

    @FXML
    private ComboBox<Warehouse> comboBoxWarehouses;

    // For refreshing after performing an action
    @FXML
    private TableView<Shipment> tableViewWarehouseShipments;

    // Runs automatically, sets the ComboBox options.
    public void initialize(){

        // Decides how every alternative should be shown in the menu
        comboBoxWarehouses.setCellFactory(param -> new ListCell<Warehouse>() {
            @Override
            protected void updateItem(Warehouse warehouse, boolean empty) {
                super.updateItem(warehouse, empty);
                if (empty || warehouse == null || warehouse.getName() == null) {
                    setText(null);
                } else {
                    setText(warehouse.getName() + " - " + warehouse.getRegion());
                }
            }
        });

        // Decides how the chosen alternative shall be shown on the button after selecting
        comboBoxWarehouses.setButtonCell(new ListCell<Warehouse>() {
            @Override
            protected void updateItem(Warehouse warehouse, boolean empty) {
                super.updateItem(warehouse, empty);
                if (empty || warehouse == null || warehouse.getName() == null) {
                    setText("Select Warehouse");
                } else {
                    setText(warehouse.getName() + " - " + warehouse.getRegion());
                }
            }
        });

    }

    // Populats combobox from a list
    public void populateComboBox() {
        comboBoxWarehouses.getItems().clear();
        comboBoxWarehouses.setItems(appModel.getWarehouseRegister().getWarehouses());
    }

    /*
     * handleButtonMoveShipmentAction
     * Handles to manually move a shipment
     */
    @FXML
    private void handleButtonMoveShipmentAction(ActionEvent event) {
        System.out.println("first line of move");
        try{
            // Ensure that the shipment is in idle list
            if(currentWarehouse.getIdleShipments().contains(currentShipment)){
                System.out.println("idle contains shipment");

                // Create variable to hold selected warehouse
                Warehouse nextWarehouse = comboBoxWarehouses.getSelectionModel().getSelectedItem();
                System.out.println("nextWarehouse" + nextWarehouse.getName());

                // Make sure a receiving warehouse is chosen
                //if(nextWarehouse == null){
                //    throw new IllegalArgumentException("Please choose a receiving warehouse first.");
                //}
                System.out.println("nextWarehouse" + nextWarehouse.getName());

                // Ensure that there is available space in receiving warehouse
                if(currentShipment.getCubicMeters() > nextWarehouse.calculateRemainingCapacity()){
                    throw new IllegalArgumentException("Shipment volume exceeds current free space in the receiving warehouse. Please select a different warehouse");
                }

                // Check for transportation loop
                for(TravelLogEntry log : nextWarehouse.getHistoryShipments()) {
                    
                    if(log.getShipment() == currentShipment){

                        // Transportation loop found, user confirmation required
                        boolean userConfirmed = showConfirmationDialog("Warning", "Transportation loop detected. Shipment has been in " + nextWarehouse.getName() 
                            + " before. Are you sure you would like to proceed?");
                        if (userConfirmed) {
                            // User wants to continue
                            break; // Exit for loop
                        } else {
                            // User declines
                            return; // Abort the whole handleButtonMoveShipmentAction.
                        }
                    }
                }
                // Move the shipment to the right lists
                currentWarehouse.removeIdleShipment(currentShipment);
                currentWarehouse.addOutgoingShipment(currentShipment);
                nextWarehouse.addIncomingShipment(currentShipment);

                // Update lists
                nextWarehouse.updateAllShipments();
                currentWarehouse.updateAllShipments();
                currentShipment.updateTravelLog();
                currentWarehouse.updateTravelLog();
                
                // Update label
                updateLabelSituation();
            } else {
                throw new IllegalArgumentException("Cannot move shipment. See status line.");
            }
            System.out.println("last line of move");

        } catch(IllegalArgumentException e) {
            showErrorDialog("Error", e.getMessage());
        } catch(Exception e) {
            showErrorDialog("Unknown error", "Contact systems administrator");
        }
    }

    

    /*
     * handleButtonConfirmReceivedShipmentAction
     * Handle the button to confirm that a shipment is received at current warehouse
     */
    @FXML 
    private void handleButtonConfirmReceivedShipmentAction(ActionEvent event) {
        TravelLogEntry lastLog = currentShipment.getTravelLog().get(currentShipment.getTravelLog().size() - 1);
        try{
            // We can only receive if the previous warehouse has dispathed, i.e. it has a departure date there
            if(currentWarehouse.getIncomingShipments().contains(currentShipment) && lastLog.getDepartureDate() != null){
                // Move shipment to the right list
                currentWarehouse.removeIncomingShipment(currentShipment);
                currentWarehouse.addIdleShipment(currentShipment);
                // Create new travel log entry
                TravelLogEntry newEntry = new TravelLogEntry(currentShipment, currentShipment.getId(), LocalDateTime.now(), currentWarehouse, null, null);
                currentShipment.addTravelEntry(newEntry);
                currentWarehouse.addTravelEntry(newEntry);
                // Update lists
                currentWarehouse.updateAllShipments();
                currentShipment.updateTravelLog();
                currentWarehouse.updateTravelLog();
                updateLabelSituation();
            } else {
                throw new IllegalArgumentException("Cannot receive shipment. See status line.");
            }
        } catch(IllegalArgumentException e) {
            showErrorDialog("Error", e.getMessage());
        } catch(Exception e){
            showErrorDialog("Unknown error", "Contact systems administrator");
        }

    }

    /*
     * handleButtonConfirmDispatchedShipmentAction
     * Handles the button to confirm dispatched shipment
     */
    @FXML
    private void handleButtonConfirmDispatchedShipmentAction(ActionEvent event){
        try{
            if(currentWarehouse.getOutgoingShipments().contains(currentShipment)){

                // Remove from outgoing list
                System.out.println(currentWarehouse);
                if(currentWarehouse.getOutgoingShipments().contains(currentShipment)){
                    currentWarehouse.removeOutgoingShipment(currentShipment);
                } else {
                    throw new IllegalArgumentException("Cannot remove outgoing shipment.");
                }

                // Add log
                TravelLogEntry lastLogEntry = currentShipment.getTravelLog().get(currentShipment.getTravelLog().size() - 1);
                lastLogEntry.setDepartureDate(LocalDateTime.now());

                // Update lists
                currentWarehouse.updateAllShipments();
                currentShipment.updateTravelLog();
                currentWarehouse.updateTravelLog();

                updateLabelSituation();

                /*
                 * Work in progres to update the tableViewWarehouseTravelLog
                 */
                // Uppdatera warehouse travel log table view
                // Use any fxml-object in this tab that we can see, and get its "root".        
                //Parent root = labelSituation.getScene().getRoot();
                // Climb higher up the graph

                //System.out.println("after parent root" + root);
                // Lookup the TableView by its fx:id (from TabWarehouseTravelLogViewController)
                // tableViewWarehouseTravelLog IS a TableView<TravelLogEntry>, no need to warn
                //@SuppressWarnings("unchecked")
                //TableView<TravelLogEntry> tableView = (TableView<TravelLogEntry>) root.lookup("#tableViewWarehouseTravelLog");
                //tableView.refresh();

                //System.out.println("after unchecked +" + tableView);

                // Refresh the TableView if found
                //if (tableView != null) {
                //    System.out.println("found tableview");
                //    tableView.refresh();
                //}
                System.out.println("last line of dispatch");
            } else {
                throw new IllegalArgumentException("Cannot dispatch shipment. See status line.");
            }
        } catch(IllegalArgumentException e) {
            showErrorDialog("Error", e.getMessage());
        } catch(Exception e){
            showErrorDialog("Unknown error", "Contact the systems administrator");
        }
    }

    // Helper method to show confirmation window
    private boolean showConfirmationDialog(String title, String message) {
        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Get the dialog pane to adjust the size
        DialogPane dialogPane = alert.getDialogPane();
        // Set preferred width and height to fit all the content
        dialogPane.setPrefSize(450, 150);  // Adjust the values as needed
        

        // Wait for user action
        Optional<ButtonType> result = alert.showAndWait();
        
        // Make sure the user made a choice
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /*
     * Helper-metod to update status label
     */
    private void updateLabelSituation(){

        String situation;
        TravelLogEntry lastLog = currentShipment.getTravelLog().get(currentShipment.getTravelLog().size() - 1);

        // If the shipment is in the current warehouses idle list, current warehouse is its physical location
        if(currentWarehouse.getIdleShipments().contains(currentShipment)){

            situation = "The shipment is in " + currentWarehouse.getName() +", pending move order.";

        } else if(currentWarehouse.getOutgoingShipments().contains(currentShipment)) {// if it is in outgoing, it is still in current warehouse

            situation = "The shipment is in " + currentWarehouse.getName() + ", awaiting dispatch confirmation.";

        } else if (currentWarehouse.getIncomingShipments().contains(currentShipment)) {// it must be in the incoming list...
            
            // if the travel log entry has no departure date, it is still in the previous warehouse...
            if(lastLog.getDepartureDate() == null){

                //...which means that it's physical location is that of the travel log entry
                situation = "The shipment is incoming from " + lastLog.getArrivalLocation().getName() + " but has not been dispatched from there yet.";

            } else { // it has a dispatch date, so it is on the road

                situation = "The shipment is in transit between " + lastLog.getArrivalLocation().getName() + " and " + currentWarehouse.getName()
                    + ". Awaiting arrival confirmation at this warehouse.";}
            } else if (lastLog.getDepartureDate() != null && !currentWarehouse.getIncomingShipments().contains(currentShipment)) {
                // Shipment has been dispatched and is in transit
                situation = "The shipment has been dispatched from " + lastLog.getArrivalLocation().getName() +
                            " and is currently in transit.";
            } else {
                situation = "The shipment's status is unknown.";
        }

        // Finally set the situation label
        labelSituation.setText(situation);
    }

    /* 
     * Helper method to show an error dialog popup
     */
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
        populateComboBox();
    }

    public void setCurrentWarehouse(Warehouse currentWarehouse) {
        this.currentWarehouse = currentWarehouse;
        System.out.println("inside setcurrent warehouse" + this.currentWarehouse);
    }
    public void setTableView(TableView<Shipment> tableView){
        this.tableViewWarehouseShipments = tableView;
    }
    // In this setter we take the opportunity to update labels
    public void setCurrentShipment(Shipment currentShipment) {
        this.currentShipment = currentShipment;
        
        // Update header
        shipmentIdTabLabel.setText("Shipment: " + currentShipment.getId());
        
        // Update status/situation
        updateLabelSituation();
    }    
}
