package se.lu.ics.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.lu.ics.models.AppModel;
import se.lu.ics.models.Warehouse;
import se.lu.ics.models.Shipment;
import se.lu.ics.models.InspectionLogEntry;
import se.lu.ics.models.TravelLogEntry;
import javafx.scene.control.*;
import javafx.scene.image.Image;

/*
 * AppController class.
 * Handles opening new windows and tabs, and deals with dependency injection between controllers.
 */
public class AppController {
    
    /*
     * Instance variables
     */
    private final Stage primaryStage;
    private final Stage manageWarehousesStage;
    private final Stage addShipmentStage;
    private final AppModel appModel;
    // Reference to the mainTabPane where all the tabs connect to
    private TabPane mainTabPane;
    // Reference to the MainViewController, workaround for updating stats for now
    private MainViewController mainViewController;

    /*
     * Constructor
     */
    public AppController(Stage primaryStage, AppModel appModel) {
        this.primaryStage = primaryStage;
        this.manageWarehousesStage = new Stage();
        this.addShipmentStage = new Stage();
        this.appModel = appModel;
    }

    /*
     * Opens the MainView in a new window
     */
    public void showMainView() {
        try {
            // Read the fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            // Find the root node of this file
            Parent root = loader.load();
            // Make an object of the controller specified in the fxml
            MainViewController mainController = loader.getController();
            // Dependency injection using the setters in the controller class
            mainController.setAppController(this);
            mainController.setAppModel(appModel);
            // Create a scene (the content to show)
            Scene mainScene = new Scene(root);
            // Put the scene in the stage (window)
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Viking Express");
            primaryStage.getIcons().add(new Image("VikingExpressTransparent.png"));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens ManagewarehouseView in a new window
     */
    public void showManageWarehouseView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ManageWarehousesView.fxml"));
            Parent root = loader.load();
            ManageWarehousesViewController manageWarehousesController = loader.getController();
            manageWarehousesController.setAppController(this);
            manageWarehousesController.setAppModel(appModel);

            manageWarehousesStage.setScene(new Scene(root));
            manageWarehousesStage.setTitle("Viking Express");
            manageWarehousesStage.getIcons().add(new Image("VikingExpressTransparent.png"));
            manageWarehousesStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens TabWarehouseDetailsView under mainTabPane
     */
    public void showTabWarehouseDetailsView(TabPane tabPane, Warehouse warehouse) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TabWarehouseDetailsView.fxml"));
            Parent root = loader.load();
            TabWarehouseDetailsViewController controller = loader.getController();
            controller.setCurrentWarehouse(warehouse);
            controller.setAppModel(appModel);
            
            Tab newTab = new Tab("Warehouse Details");
            newTab.setContent(root);
            tabPane.getTabs().add(newTab);
            tabPane.getSelectionModel().select(newTab);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens ManageShipmentsView under mainTabPane
     */
    public void showManageShipmentsView(TabPane tabPane, Warehouse warehouse, TableView<Warehouse> tableViewWarehouseRegister) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ManageShipmentsView.fxml"));
            Parent root = loader.load();
            ManageShipmentsViewController manageShipmentsController = loader.getController();
            // Dependency injection
            manageShipmentsController.setAppController(this);
            manageShipmentsController.setCurrentWarehouse(warehouse);
            manageShipmentsController.setTableViewWarehouseRegister(tableViewWarehouseRegister);

            Tab newTab = new Tab("View Shipments");
            newTab.setContent(root);
            tabPane.getTabs().add(newTab);
            tabPane.getSelectionModel().select(newTab);

            // Show the warning popup if there are stale shipments. 
            // The method .runLater() makes sure that everything else is loaded first.
            Platform.runLater(() -> {
                manageShipmentsController.warnAboutStaleShipments();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens AddShipmentView in a new window
     */
    public void showAddShipmentView(Warehouse warehouse, TableView<Shipment> tableViewWarehouseShipments, TableView<Warehouse> tableViewWarehouseRegister) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddShipmentView.fxml"));
            Parent root = loader.load();
            AddShipmentViewController addShipmentController = loader.getController();
            addShipmentController.setCurrentWarehouse(warehouse);
            addShipmentController.setTableViewWarehouseRegister(tableViewWarehouseRegister);

            addShipmentStage.setScene(new Scene(root));
            addShipmentStage.setTitle("Add Shipment");
            addShipmentStage.getIcons().add(new Image("VikingExpressTransparent.png"));
            addShipmentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens EditShipmentView in a new window
     */
    public void showEditShipmentView(Shipment shipment, TableView<Shipment> tableViewWarehouseShipments){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditShipmentView.fxml"));
            Parent root = loader.load();
            EditShipmentViewController editShipmentViewController = loader.getController();
            editShipmentViewController.setCurrentTableView(tableViewWarehouseShipments);
            editShipmentViewController.setCurrentShipment(shipment);

            addShipmentStage.setScene(new Scene(root));
            addShipmentStage.setTitle("Edit Shipment");
            addShipmentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens EditWarehouseView in a new window
     */
    public void showEditWarehouseView(Warehouse selectedWarehouse){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditWarehouseView.fxml"));
            Parent root = loader.load();
            EditWarehouseViewController editWarehouseController = loader.getController();
            editWarehouseController.setAppModel(appModel);  
            editWarehouseController.setAppController(this);
            editWarehouseController.setCurrentWarehouse(selectedWarehouse);
            
            Stage stage = new Stage();
            stage.setTitle("Edit Warehouse");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("VikingExpressTransparent.png"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens TabWarehouseTravelLog under mainTabPane
     */
    public void showTabWarehouseTravelLog(TabPane tabPane, Warehouse warehouse){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TabWarehouseTravelLogView.fxml"));
            Parent root = loader.load();
            TabWarehouseTravelLogViewController controller = loader.getController();
            controller.setCurrentWarehouse(warehouse);
            controller.setAppController(this);

            Tab newTab = new Tab("Warehouse Travel Log");
            newTab.setContent(root);
            tabPane.getTabs().add(newTab);
            tabPane.getSelectionModel().select(newTab);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens TabWarehouseInspectionsView under mainTabPane
     */
    public void showTabWarehouseInspectionsView(TabPane tabPane, Warehouse warehouse){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TabWarehouseInspectionsView.fxml"));
            Parent root = loader.load();
            TabWarehouseInspectionsViewController controller = loader.getController();
            controller.setCurrentWarehouse(warehouse);
            controller.setAppController(this);

            Tab newTab = new Tab("Warehouse Inspections");
            newTab.setContent(root);
            tabPane.getTabs().add(newTab);
            tabPane.getSelectionModel().select(newTab);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
     * Opens AddWarehouseView in a new window
     */
    public void showAddWarehouseView(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddWarehouseView.fxml"));
            Parent root = loader.load();
            AddWarehouseViewController addWarehouseController = loader.getController();
            addWarehouseController.setAppModel(appModel);  
            addWarehouseController.setAppController(this);

            Stage stage = new Stage();
            stage.setTitle("Add Warehouse");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("VikingExpressTransparent.png"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens AddInspectionView in a new window
     */
    public void showAddInspectionView(Warehouse warehouse, Shipment shipment){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddInspectionView.fxml"));
            Parent root = loader.load();
            AddInspectionViewController addInspectionController = loader.getController();
            addInspectionController.setCurrentWarehouse(warehouse);
            addInspectionController.setCurrentShipment(shipment);
            addInspectionController.setLabels();

            Stage stage = new Stage();
            stage.setTitle("Add Inspection");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("VikingExpressTransparent.png"));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens EditWarehouseTravelLogView in a new window
     */
    public void showEditWarehouseTravelLogView(TravelLogEntry selectedEntry, TableView<TravelLogEntry> tableView, Shipment chosenShipment){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditWarehouseTravelLogView.fxml"));
            Parent root = loader.load();
            EditWarehouseTravelLogViewController editWarehouseTravelLog = loader.getController();
            editWarehouseTravelLog.setCurrentTravelEntry(selectedEntry);
            editWarehouseTravelLog.setTableView(tableView);

            Stage stage = new Stage();
            stage.setTitle("Edit Shipment Log");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("VikingExpressTransparent.png"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens EditWarehouseInspectionsView in a new window
     */
    public void showEditWarehouseInspectionsView(InspectionLogEntry chosenEntry, Shipment chosenShipment, TableView<InspectionLogEntry> tableView){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditWarehouseInspectionsView.fxml"));
            Parent root = loader.load();
            EditWarehouseInspectionsViewController editInspections = loader.getController();
            editInspections.setCurrentInspectionEntry(chosenEntry);
            editInspections.setCurrentShipment(chosenShipment);
            editInspections.setTableView(tableView);

            Stage stage = new Stage();
            stage.setTitle("Edit Inspection Log");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("VikingExpressTransparent.png"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens TabHandleShipmentActionsView in a new tab
     */
    public void showTabHandleShipmentActionsView (Warehouse warehouse, Shipment shipment) {
        System.out.println("Handle Shipment button clicked");
        try{
            Tab newTab = new Tab("Handle Shipment");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TabHandleShipmentActionsView.fxml"));
            Parent root = loader.load();
            TabHandleShipmentActionsViewController newTabController = loader.getController();
            newTabController.setCurrentWarehouse(warehouse);
            newTabController.setAppModel(appModel);
            newTabController.setCurrentShipment(shipment);

            newTab.setContent(root);
            mainTabPane.getTabs().add(newTab);
            mainTabPane.getSelectionModel().select(newTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Opens TabShipmentInspectionsView in a new tab
     */
    public void showTabShipmentInspectionsView (Shipment shipment, Warehouse warehouse){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TabShipmentInspectionsView.fxml"));
            Parent root = loader.load();
            TabShipmentInspectionsViewController newTabController = loader.getController();            
            newTabController.setCurrentShipment(shipment);
            newTabController.setCurrentWarehouse(warehouse);
            newTabController.setAppController(this);

            Tab newTab = new Tab("Inspections");
            newTab.setContent(root);
            mainTabPane.getTabs().add(newTab);
            mainTabPane.getSelectionModel().select(newTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Getters and setters
     */
    public void setMainTabPane(TabPane mainTabPane) {
        this.mainTabPane = mainTabPane;
    }

    public TabPane getMainTabPane() {
        return this.mainTabPane;
    }

    public void setMainViewController(MainViewController controller) {
        this.mainViewController = controller;
    }

    public MainViewController getMainViewController() {
        return mainViewController;
    }

}