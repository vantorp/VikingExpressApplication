package se.lu.ics.controllers;

import javafx.fxml.FXML;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import se.lu.ics.models.AppModel;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TabPane;

/*
 * First window, contains stats about regions
 */
public class MainViewController {
    /*
     *  Instance variables
     */
    private AppModel appModel;
    private AppController appController;
    // Properties to make tableview update
    private DoubleProperty progressNorth = new SimpleDoubleProperty(0.0);
    private DoubleProperty progressCentral = new SimpleDoubleProperty(0.0);
    private DoubleProperty progressSouth = new SimpleDoubleProperty(0.0);

    /*
     * FXML objects and handlers
     */

    @FXML
    private TabPane mainTabPane;

    @FXML
    private ProgressBar progressBarNorth;

    @FXML
    private ProgressBar progressBarCentral;

    @FXML
    private ProgressBar progressBarSouth;

    @FXML
    private Label labelStatsNorth;

    @FXML
    private Label labelStatsNorthSecond;

    @FXML
    private Label labelStatsCentral;

    @FXML
    private Label labelStatsCentralSecond;

    @FXML
    private Label labelStatsSouth;

    @FXML
    private Label labelStatsSouthSecond;

    @FXML
    private Label labelTotalCapacityNorth;

    @FXML
    private Label labelTotalCapacityCentral;

    @FXML
    private Label labelTotalCapacitySouth;

    @FXML
    private Label labelTotalUsedSpaceNorth;

    @FXML
    private Label labelTotalUsedSpaceCentral;

    @FXML
    private Label labelTotalUsedSpaceSouth;

    @FXML
    private Label labelBusiestWarehouse;

    @FXML
    private Label labelTotalUnusedSpaceNorth; 

    @FXML
    private Label labelTotalUnusedSpaceCentral;

    @FXML
    private Label labelTotalUnusedSpaceSouth;

    @FXML
    private void initialize() {
        // Makes sure mainTabPane is in appController. Old code, but repurposed.
        if (appController != null) {
            appController.setMainTabPane(mainTabPane);
        }
        // Make the progress bars listen to properties
        progressBarNorth.progressProperty().bind(progressNorth);
        progressBarCentral.progressProperty().bind(progressCentral);
        progressBarSouth.progressProperty().bind(progressSouth);
    }

    // Helper method to update labels
    private void updateLabelStats(){
        double northTextCapacity = appModel.getWarehouseRegister().regionalCapacity("North");
        double northStockLevel = appModel.getWarehouseRegister().getNorthStockLevel();
        double northFullness = northStockLevel / northTextCapacity;
        double northRemainingCubic = northTextCapacity - northStockLevel;
        double northRemaining = 1 - northFullness;
        progressNorth.set(northFullness);
        labelTotalCapacityNorth.setText("Total capacity: " + northTextCapacity + "m³");
        labelTotalUsedSpaceNorth.setText("Total space used: " + northStockLevel + "m³");
        labelTotalUnusedSpaceNorth.setText("Total space remaining: " + northRemainingCubic + "m³");
        labelStatsNorth.setText(String.format("%.2f", (northFullness*100)) + "% full");
        labelStatsNorthSecond.setText(String.format("%.2f", (northRemaining*100)) + "% remaining");

        double centralTextCapacity = appModel.getWarehouseRegister().regionalCapacity("Central");
        double centralStockLevel = appModel.getWarehouseRegister().getCentralStockLevel();
        double centralFullness = centralStockLevel / centralTextCapacity;
        double centralRemainingCubic = centralTextCapacity - centralStockLevel;
        double centralRemaining = 1 - centralFullness;
        progressCentral.set(centralFullness);
        labelTotalCapacityCentral.setText("Total capacity : " + centralTextCapacity + "m³");
        labelTotalUsedSpaceCentral.setText("Total space used: " + centralStockLevel + "m³");
        labelTotalUnusedSpaceCentral.setText("Total space remaining: " + centralRemainingCubic + "m³");
        labelStatsCentral.setText(String.format("%.2f", (centralFullness*100)) + "% full");
        labelStatsCentralSecond.setText(String.format("%.2f", (centralRemaining*100)) + "% remaining");

        double southTextCapacity = appModel.getWarehouseRegister().regionalCapacity("South");
        double southStockLevel = appModel.getWarehouseRegister().getSouthStockLevel();
        double southFullness = southStockLevel / southTextCapacity;
        double southRemainingCubic = southTextCapacity - southStockLevel;
        double southRemaining = 1 - southFullness;
        progressSouth.set(southFullness);
        labelTotalCapacitySouth.setText("Total capacity: " + southTextCapacity + "m³");
        labelTotalUsedSpaceSouth.setText("Used warehouse space: " + southStockLevel + "m³");
        labelTotalUnusedSpaceSouth.setText("Total space remaining: " + southRemainingCubic + "m³");
        labelStatsSouth.setText(String.format("%.2f", (southFullness*100)) + "% full");
        labelStatsSouthSecond.setText(String.format("%.2f", (southRemaining*100)) + "% remaining");

        labelBusiestWarehouse.setText(appModel.getWarehouseRegister().getBusiestWarehouseLastTwoWeeks().getName() + " has had the most shipments arriving and departing within the last two weeks.");
    }

    // Handle the button to open ManageWarehouseView
     @FXML
    private void handleButtonManageWarehousesAction(ActionEvent event) {
        appController.showManageWarehouseView();
    }

    //Helper method to update mainview/refresh mainview
    public void updateStatistics() {
        updateLabelStats(); // Refresh the statistics in the MainView
    }

    /*
     * Getters and setters
     */
    public void setAppController(AppController appController) {
        this.appController = appController;
    }
    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
        updateLabelStats();
    }

}
