package se.lu.ics.controllers;

import se.lu.ics.models.AppModel;
import se.lu.ics.models.TravelLogEntry;
import se.lu.ics.models.Warehouse;
import java.time.Duration;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/*
 * TabWarehouseDetailsViewController class
 * Opens a tab with warehouse details/information
 */
public class TabWarehouseDetailsViewController {
    /*
     * Instance variables
     */
    Warehouse currentWarehouse;
    AppModel appModel;

    /*
     * FXML-variables
     */

    @FXML
    private Label labelHeading; 

    @FXML
    private Label labelAddress; 

    @FXML
    private Label labelAvailableCapacityM;

    @FXML
    private Label labelThroughput; // Average for warehouse

    @FXML
    private Label labelTotalShipments;
    
    // Helper method to update labels
    private void updateLabels() {
        labelHeading.setText("Warehouse Details for " + currentWarehouse.getName());
        labelAddress.setText("Full address:  " + currentWarehouse.getAddress());

        labelAvailableCapacityM.setText("Available capacity:  " + currentWarehouse.calculateRemainingCapacity() + " m³");

        labelTotalShipments.setText("Total Number of Shipments: " + currentWarehouse.getAllShipments().size());
        
        // Calculate average throughput as all days that all shipments have spent in the warehouse, divided by number of shipments
        double totalDaysInWarehouse = 0;
        // ...divided by... 
        double totalNumberOfShipments = 0;
        // equals... 
        double averageThroughput = 0;

        for(TravelLogEntry logEntry : currentWarehouse.getHistoryShipments()){
            // Only use the ones who have both arrival and departure date
            if(logEntry.getArrivalDate() != null && logEntry.getDepartureDate() != null){
                totalNumberOfShipments += 1;
                Duration duration = Duration.between(logEntry.getArrivalDate(), logEntry.getDepartureDate());
                totalDaysInWarehouse += (double) duration.toDays();
            }
        }
        
        if(totalNumberOfShipments != 0){ // Just to make sure we're not dividing by zero
            averageThroughput = totalDaysInWarehouse / totalNumberOfShipments;
            labelThroughput.setText("Average Throughput:   " + String.format("%.1f", averageThroughput) + " days.");
        } else {
            labelThroughput.setText("Average Throughtput: -");
        }
        


    }
    
    /*
     * Getters and setters
     */
    public void setAppModel(AppModel appModel) {
        this.appModel = appModel;
    }

    public void setCurrentWarehouse(Warehouse warehouse){
        this.currentWarehouse = warehouse;
        //Update labels
        updateLabels();
    }


}
