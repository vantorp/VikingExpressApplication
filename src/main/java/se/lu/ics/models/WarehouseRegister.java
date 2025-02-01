package se.lu.ics.models;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;


/**
 * WarehouseRegister class
 * Contains a list over warehouses and methods to add and remove warehouses to the list,
 * and methods to calculate stats
 */
public class WarehouseRegister {

    /*
     * Instance variables
     */
    private ObservableList<Warehouse> warehouses;
    private double totalCapacity;

    /*
     * Constructor
    */
    public WarehouseRegister() {
        this.warehouses = FXCollections.observableArrayList();
        this.totalCapacity = 0.0;
    }

    /*
     * Adds a warehouse to the register
     */
    public void addWarehouse(Warehouse warehouse){
        warehouses.add(warehouse);
    }

    /*
     * Removes a warehouse from the register
     */
    public void deleteWarehouse(Warehouse warehouse){
        if(warehouses.contains(warehouse)) {
            warehouses.remove(warehouse);
        } else {
            System.out.println("warehouses does not contain warehouse");
        }
    }
    
    /*
     * Calculate and return total capacity for all warehouses together
     */
    public double totalCapacity(){
        double totalCapacity = 0.0;
        for(Warehouse w : warehouses){
            totalCapacity += w.getCapacity();
        }
        return totalCapacity;
    }
    
    /*
     * Calculate and return capacity based on region
     */
    public double regionalCapacity(String region){
        double regionalCapacity = 0.0;
        switch (region) {
            case ("North"):
                for(Warehouse w : warehouses){
                    if (w.getRegion().equals("North")){
                        regionalCapacity += w.getCapacity();
                    }
                }
                return regionalCapacity;

            case ("South"):
                for (Warehouse w : warehouses){
                    if (w.getRegion().equals("South")){
                        regionalCapacity += w.getCapacity();
                    }
                }
                return regionalCapacity;

            case ("Central"):
                for (Warehouse w : warehouses){
                    if (w.getRegion().equals("South")){
                        regionalCapacity += w.getCapacity();
                    }
                }
                return regionalCapacity;
            default:
                return 0.0;
        }
    }

    /*
     * Calculate and return stock level for north region
     */
    public double getNorthStockLevel() {
        double stockLevel = 0.0;
        for(Warehouse w : warehouses){
            if(w.getRegion().equals("North")){
                stockLevel += w.getStockLevel();
            }
        }
        return stockLevel;
    }

    /*
     * Calculate and return stock level for central region
     */
    public double getCentralStockLevel() {
        double stockLevel = 0.0;
        for(Warehouse w : warehouses){
            if(w.getRegion().equals("Central")){
                stockLevel += w.getStockLevel();
            }
        }
        return stockLevel;
    }

    /*
     * Calculate stock level for south region
     */
    public double getSouthStockLevel() {
        double stockLevel = 0.0;
        for(Warehouse w : warehouses){
            if(w.getRegion().equals("South")){
                stockLevel += w.getStockLevel();
            }
        }
        return stockLevel;
    }

    /*
     * Returns the busiest warehouse in the last two weeks
     */
    public Warehouse getBusiestWarehouseLastTwoWeeks() {
        Warehouse busiestWarehouse = null;
        int maxActions = 0;

        for (Warehouse warehouse : warehouses) {
            int actionCount = 0;

            for (TravelLogEntry entry : warehouse.getHistoryShipments()) {
                // Check arrival date
                if (entry.getArrivalDate() != null && 
                    ChronoUnit.DAYS.between(entry.getArrivalDate(), LocalDateTime.now()) <= 14) {
                    actionCount++;
                }

                // Check departure date
                if (entry.getDepartureDate() != null && 
                    ChronoUnit.DAYS.between(entry.getDepartureDate(), LocalDateTime.now()) <= 14) {
                    actionCount++;
                }
            }

            // Update the busiest warehouse if this one has more actions
            if (actionCount > maxActions) {
                maxActions = actionCount;
                busiestWarehouse = warehouse;
            }
        }

        return busiestWarehouse;
    }

    /*
     * Getters
     */
    public ObservableList<Warehouse> getWarehouses() {
        return FXCollections.unmodifiableObservableList(this.warehouses);
    }

    public double getTotalCapacity() {
        return totalCapacity;
    }

}
