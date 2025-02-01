package se.lu.ics.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID; 
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * Shipment class
 * Represents a shipment
 */
public class Shipment {

    /*
     * Class variables
     */
    // A list of all used IDs
    private static ArrayList<String> usedIDs = new ArrayList<>();
    // A getter for the list of all used IDs
    public static ArrayList<String> getUsedIDs() {
        return usedIDs;
    }

    /*
     * Instance variables
     * Properties to try to make the tableviews update automatically
     */
    private SimpleStringProperty id;
    private SimpleDoubleProperty cubicMeters;
    private ObservableList<InspectionLogEntry> inspectionLog;
    private ObservableList<TravelLogEntry> travelLog;
    private SimpleStringProperty status;  // "Incoming", "Arrived", "Outgoing", "Dispatched", "Idle"
    private DateTimeFormatter formatter;

    /*
     * Constructor
     */
    public Shipment(double cubicMeters){
        // Use truncated UUID, BUT make absolutely sure there are no collisions
        String truncatedID = UUID.randomUUID().toString().substring(0, 8);
        while(usedIDs.contains(truncatedID)){
            truncatedID = UUID.randomUUID().toString().substring(0, 8);
        }
        usedIDs.add(truncatedID);

        this.id = new SimpleStringProperty(truncatedID);
        this.cubicMeters = new SimpleDoubleProperty(cubicMeters);
        this.status = new SimpleStringProperty("Received");
        this.inspectionLog = FXCollections.observableArrayList();
        this.travelLog = FXCollections.observableArrayList();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    /*
     * Instance methods
     */
    public void addTravelEntry(TravelLogEntry entry){
        travelLog.add(entry);
    }

    public void removeTravelEntry(TravelLogEntry entry){
        if(travelLog.contains(entry)){
            travelLog.remove(entry);
        }
    }

    // Sorts the historyShipments list from old to new based on arrival dates
    public void sortTravelLogEntries() {
        Collections.sort(travelLog, (o1, o2) -> o1.getArrivalDate().compareTo(o2.getArrivalDate()));
    }

    // Add an inspection entry and sort the list
    public void addInspection(InspectionLogEntry inspection){
        inspectionLog.add(inspection);
    }

    // Sort the list of inspections
    public void sortInspections(){
        Collections.sort(inspectionLog, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
    }

    // Removes an inspection entry from inspection log
    public void removeInspection(InspectionLogEntry inspection){
        if(inspectionLog.contains(inspection)){
            inspectionLog.remove(inspection);
        }
    }

    // Return last inspection date as a string
    public String getLastInspectionDate (){
        if(inspectionLog.size() != 0){
            int lastInspectionIndex = inspectionLog.size()-1;
            InspectionLogEntry lastInspection = inspectionLog.get(lastInspectionIndex);
            LocalDateTime lastInspectionDate = lastInspection.getDate();
            return lastInspectionDate.format(this.formatter);
        }else{
            return "-";
        } 
    }

    // Return number of days in a warehouse as an int
    public int getDaysInWarehouse (){
        int totalDays = 0;
        if(getTravelLog().size() > 0){
            TravelLogEntry lastLogEntry = this.getTravelLog().get(this.getTravelLog().size() - 1);
            Duration duration = Duration.between(lastLogEntry.getArrivalDate(), LocalDateTime.now());
            totalDays = (int) duration.toDays();
        }
        return totalDays;
    }

    // returns the number of warehouses a shipment has been in (number of travel log entries)
    public int numberWarehouses(){
        return travelLog.size();
    }

    // Workaround to "poke" the list to see if tableview updates
    public void updateTravelLog(){
        travelLog.add(null);
        travelLog.remove(null);
    }

    // returns true if the shipment has been inspected
    public boolean hasBeenInspected(){
        return inspectionLog.size() > 0; // > evaluerar till antingen true eller false
    }

    /*
     * Getters för Properties
     */
    public double getCubicMeters(){
        //return this.cubicMeters;
        return cubicMeters.get(); 
    }

    public String getId(){
        return id.get();
    }

    public ObservableList<TravelLogEntry> getTravelLog(){
        return FXCollections.unmodifiableObservableList(this.travelLog);
    }

    // Returns the whole inspection log list as observable list
    public ObservableList<InspectionLogEntry> getInspectionLog(){
        return FXCollections.unmodifiableObservableList(this.inspectionLog);
    }

    /*
     * Setters för Properties
     */
    public void setCubicMeters(double cubicMeters){
        this.cubicMeters.set(cubicMeters);  // Before: this.cubicMeters = cubicMeters;
    }

    public void setStatus(String status){
        this.status.set(status);
    }

    public void setId(String id){
        this.id.set(id);
    }

    /*
     * Properties to use in tableviews
     */
    public SimpleStringProperty idProperty() {
        return id;
    }

    public SimpleDoubleProperty cubicMetersProperty() {
        return cubicMeters;
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }
}
