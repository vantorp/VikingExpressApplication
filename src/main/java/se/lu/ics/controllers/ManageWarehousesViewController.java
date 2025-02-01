package se.lu.ics.controllers;

import javafx.fxml.FXML;
import javafx.beans.property.ReadOnlyObjectWrapper;
import java.util.Optional;
import javafx.event.ActionEvent;
import se.lu.ics.models.AppModel;
import se.lu.ics.models.Shipment;
import javafx.scene.Parent;
import javafx.scene.control.*;
import se.lu.ics.models.Warehouse;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

/*
 * ManageWarehousesViewController
 * Shows a list of warehouses
 */
public class ManageWarehousesViewController {

    /*
     *  Instance variables
     */
    private AppModel appModel;
    private AppController appController;
    
    /*
     *  FXML objects and handlers
     */

    @FXML
    private Label testLabel;

    @FXML
    private Label remainingCapacityLabel;
    
    @FXML
    private Button buttonAddWarehouse;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private TextField textFieldFindShipment;

    @FXML
    private TableView<Warehouse> tableViewWarehouseRegister;

    @FXML
    private TableColumn<Warehouse, String> tableColumnWarehouseName;

    @FXML
    private TableColumn<Warehouse, String> tableColumnWarehouseRegion;

    @FXML
    private TableColumn<Warehouse, String> tableColumnWarehouseAddress;

    @FXML
    private TableColumn<Warehouse, Double> tableColumnWarehouseCapacity;

    @FXML 
    private TableColumn<Warehouse, Double> tableColumnWarehouseStockLevel;

    @FXML
    private TableColumn<Warehouse, String> tableColumnWarehouseAvailableCapacity;
    
    @FXML
    private TableColumn<Warehouse, String> tableColumnWarehouseLastInspectionDate;

    //Runs automatically, defines how the tableview gets its data
    public void initialize(){

        // Name
        tableColumnWarehouseName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());;
        tableColumnWarehouseName.setCellFactory(TextFieldTableCell.forTableColumn());
        // Allows editing
        tableColumnWarehouseName.setOnEditCommit(event -> {
            Warehouse warehouse=event.getRowValue();
            warehouse.setName(event.getNewValue());
        });

        // Region
        tableColumnWarehouseRegion.setCellValueFactory(cellData -> cellData.getValue().regionProperty());
        tableColumnWarehouseRegion.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnWarehouseRegion.setOnEditCommit(event -> {
            Warehouse warehouse = event.getRowValue();
            String newRegion = event.getNewValue();

            if (newRegion!= null){
                warehouse.setRegion(newRegion);
            }
        });

        // Address
        tableColumnWarehouseAddress.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        tableColumnWarehouseAddress.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumnWarehouseAddress.setOnEditCommit(event -> {
            Warehouse warehouse = event.getRowValue();
            String newAddress = event.getNewValue();

            if (newAddress!= null){
                warehouse.setAddress(newAddress);
            }
        });

        // maxCapacity
        tableColumnWarehouseCapacity.setCellValueFactory(cellData -> cellData.getValue().maxCapacityProperty().asObject());
        tableColumnWarehouseCapacity.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>(){
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

        tableColumnWarehouseCapacity.setOnEditCommit(event -> {
            Warehouse warehouse = event.getRowValue();
            Double newCapacity = event.getNewValue();

            if (newCapacity!= null){
                warehouse.setCapacity(newCapacity);
            }
        });

        tableColumnWarehouseStockLevel.setCellValueFactory(new PropertyValueFactory<>("stockLevel"));

        tableColumnWarehouseAvailableCapacity.setCellValueFactory(cellData -> {
            Double remainingCapacity = cellData.getValue().calculateRemainingCapacityPercentage();
            String stringRemainingCapacity = String.format("%.2f", remainingCapacity);
            return new ReadOnlyObjectWrapper<>(stringRemainingCapacity);
        });

        tableColumnWarehouseLastInspectionDate.setCellValueFactory(new PropertyValueFactory<>("lastInspectionDate"));
    }

    // Populates the tableview from a list
    private void populateTableView(){
        tableViewWarehouseRegister.getItems().clear();
        tableViewWarehouseRegister.setItems(appModel.getWarehouseRegister().getWarehouses());
    }

    // Handles the button to open a tab with inspections for a warehouse
    @FXML
    private void handleButtonGetWarehouseInspectionLogAction(ActionEvent event) {
        try{
            Warehouse selectedWarehouse = tableViewWarehouseRegister.getSelectionModel().getSelectedItem();
            if(selectedWarehouse == null){
                throw new NullPointerException("Select a warehouse first.");
            } else {
                // Opens the tab
                appController.showTabWarehouseInspectionsView(mainTabPane, selectedWarehouse);
            }
        } catch(NullPointerException e) {
            showErrorDialog("Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }
    }

    // Handles the button to open a tab with all shipments that's been at the warehouse
    @FXML
    public void handleButtonGetWarehouseTravelLogAction(ActionEvent event){
        try{
            Warehouse selectedWarehouse = tableViewWarehouseRegister.getSelectionModel().getSelectedItem();
            if(selectedWarehouse == null){
                throw new NullPointerException("Select a warehouse first.");
            } else {
                // Opens the tab
                appController.showTabWarehouseTravelLog(mainTabPane, selectedWarehouse);
            }
        } catch(NullPointerException e) {
            showErrorDialog("Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }
    }

    // Handles the button to add a warehouse
    @FXML
    private void handleButtonAddWarehouseAction(ActionEvent event) {
        appController.showAddWarehouseView();
    }

    // Handles the button to open a window to edit a warehouse
    @FXML
    private void handleButtonEditWarehouseAction(ActionEvent event) {
        try{
            Warehouse selectedWarehouse = tableViewWarehouseRegister.getSelectionModel().getSelectedItem();
            if (selectedWarehouse == null) {
                throw new NullPointerException("Select a warehouse first.");
            } else {
                // Open the window
                appController.showEditWarehouseView(selectedWarehouse);
            }
        } catch(NullPointerException e) {
            showErrorDialog("Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }
    }
    
    // Try to find a shipment and show it in the right shipments view
    @FXML
    private void handleButtonFindShipmentAction(ActionEvent event) {
        // Look at all items in the table view and select the first one it finds that
        // matches what is in the text field. If the text field is empty nothing happens.
        // (Which should be the only one, since the IDs are unique.)

        String inputID = textFieldFindShipment.getText();

        if(!inputID.isBlank()){
            // Loop through all the shipments in all warehouses
            for(Warehouse warehouse : appModel.getWarehouseRegister().getWarehouses()){
                for (Shipment shipment : warehouse.getAllShipments()) {
                    // Check if the ID matches the text field input
                    if (shipment.getId().equals(inputID)) {
                        appController.showManageShipmentsView(mainTabPane, warehouse, tableViewWarehouseRegister);                
                        // Use any fxml-object in this tab that we can see, and get its "root".        
                        Parent root = mainTabPane.getScene().getRoot();
                        // Lookup the TableView by its fx:id (from TabWarehouseTravelLogViewController)
                        //tableViewWarehouseShipments IS a TableView<Shipment>, no need to warn
                        @SuppressWarnings("unchecked")
                        TableView<Shipment> tableView = (TableView<Shipment>) root.lookup("#tableViewWarehouseShipments");
                        tableView.getSelectionModel().select(shipment);
                        // Makes the table view "focused" so it's like an active click by a user
                        tableView.requestFocus();
                        // Select the row
                        return;
                    }
                }
            }
        }
    }

    // Handles the button to view shipments in a warehouse
    @FXML
    private void handleButtonViewWarehouseAction(ActionEvent event) {
        try{
            Warehouse selectedWarehouse = tableViewWarehouseRegister.getSelectionModel().getSelectedItem();
            if (selectedWarehouse == null) {
                throw new NullPointerException("Select a warehouse first.");
            } else {
                // Open tab
                appController.showManageShipmentsView(mainTabPane, selectedWarehouse, tableViewWarehouseRegister);
            }
        } catch(NullPointerException e) {
            showErrorDialog("Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }
    }

    // Handles the button to remove a warehouse
    @FXML
    private void handleButtonRemoveWarehouseAction(ActionEvent event) {
        try{
            Warehouse selectedWarehouse = tableViewWarehouseRegister.getSelectionModel().getSelectedItem();
            if (selectedWarehouse == null) {
                throw new NullPointerException("Select a warehouse first.");
            } else {
                boolean userConfirmed = showConfirmationDialog(
                "Warning",
                "You have chosen to delete a warehouse. Doing so will lead to the loss of all the warehouse's shipments. \n \n Are you sure you would like to proceed?"
                );
                // Proceed only if the user clicked 'Yes'
                if (userConfirmed) {
                    // Delete the warehouse
                    appModel.getWarehouseRegister().deleteWarehouse(selectedWarehouse);
                    appController.showMainView();
                } else {
                    System.out.println("Warehouse deletion cancelled.");
                }
            }
        } catch (NullPointerException e) {
            showErrorDialog("Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }
    }

     // Opens a tab with Warehouse Details such as full address and useful information
    @FXML
    private void handleButtonWarehouseDetailsAction(ActionEvent event) {
        try{
            Warehouse selectedWarehouse = tableViewWarehouseRegister.getSelectionModel().getSelectedItem();
            if (selectedWarehouse == null) {
                throw new NullPointerException("Select a warehouse first.");
            } else {
                // Open tab
                appController.showTabWarehouseDetailsView(mainTabPane, selectedWarehouse);
            }
        } catch(NullPointerException e) {
            showErrorDialog("Error", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred. Please try again.");
        }
    }

    // Helper method to show an error dialog popup
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to show a confirmation window
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
        // Väntar på användarens svar innan koden kör vidare
        Optional<ButtonType> result = alert.showAndWait();
        // Kollar om användaren har gjort ett val(.isPresent är boolean typ) och om användaren har valt OK
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /*
     * Setters
     */
    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
        appController.setMainTabPane(mainTabPane);
        populateTableView();
    }
    public void setAppController(AppController appController) {
        this.appController = appController;
    } 
}

