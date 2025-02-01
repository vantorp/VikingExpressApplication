package se.lu.ics.models;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;


/**
 * Warehouse class
 * Represents a warehouse, which has a name, a region, an address, a capacity,
 * three lists that can contain shipment objects: incoming, idle, and outgoing, 
 * lists for logs and inspections and a list for all shipments that is filled in by the main three shipment lists.
 */
public class Warehouse {

    private SimpleStringProperty name;
    private SimpleStringProperty region;
    private SimpleStringProperty address;
    private SimpleDoubleProperty maxCapacity;
    private ObservableList<Shipment> allShipments;
    private ObservableList<Shipment> idleShipments;
    private ObservableList<Shipment> incomingShipments;
    private ObservableList<Shipment> outgoingShipments;
    private ObservableList<InspectionLogEntry> historyInspections;
    private ObservableList<TravelLogEntry> historyShipments;
    private DateTimeFormatter formatter;

    /*
     * Constructor
     */
    public Warehouse(String name, String region, String address, double maxCapacity){
        this.name = new SimpleStringProperty(name);
        this.region = new SimpleStringProperty(region);
        this.address = new SimpleStringProperty(address);
        this.maxCapacity = new SimpleDoubleProperty(maxCapacity);
        this.allShipments = FXCollections.observableArrayList();
        this.idleShipments = FXCollections.observableArrayList();
        this.incomingShipments = FXCollections.observableArrayList();
        this.outgoingShipments = FXCollections.observableArrayList();
        this.historyInspections = FXCollections.observableArrayList();
        this.historyShipments = FXCollections.observableArrayList();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

     /*
     * calculateRemainingCapacityPercentage
     * Returns available space in a warehouse as a percentage (0-100)
     */
     public double calculateRemainingCapacityPercentage(){

      double remainingCapacity = this.calculateRemainingCapacity();
      double percentageCapacity = (remainingCapacity/this.maxCapacity.get()) * 100;
      return percentageCapacity;
    }

    public double calculateRemainingCapacity(){
        double remainingCapacity = this.maxCapacity.get() - this.getStockLevel();
        return remainingCapacity;
    }

    /*
     * addIdleShipment
     * Adds a shipment object to the idle list
     */
    public void addIdleShipment(Shipment newShipment){ 
        //ERROR: check capacity
        if(newShipment != null){
        
            if(newShipment.getCubicMeters() > this.calculateRemainingCapacity()){
                System.out.println("Shipment not added. Warehouse has reached maximum capacity.");
                return;
            }

            //ERROR: check volume, must be positive
            if(newShipment.getCubicMeters() <= 0){
                System.out.println("Shipment not added. Capacity must be a positive number.");
                return; 
            }
        
            //Adds the shipment to the idle list
            idleShipments.add(newShipment);
            updateAllShipments();
        } else {
            System.out.println("Invalid input. Please enter a shipment.");
        }
    }

    /*
     * addIncomingShipment
     * Adds a shipment object to the incoming list
     * There must be available space.
     */
    public void addIncomingShipment(Shipment newShipment){
        //ERROR: check capacity
        if(newShipment != null){
        
            if(newShipment.getCubicMeters() > this.calculateRemainingCapacity()){
                System.out.println("Shipment not added. Warehouse has reached maximum capacity.");
                return;
            }

            //ERROR: kolla volym värde
            if(newShipment.getCubicMeters() <= 0){
                System.out.println("Shipment not added. Capacity must be a positive number.");
                return; 
            }
        
            //Lägger till detta warehouse till shipments lista av platser
            incomingShipments.add(newShipment);
        } else {
            System.out.println("Invalid input. Please enter a shipment.");

        }
        updateAllShipments();
    }

    /*
     * addOutgoingShipment
     * Adds a shipment object to outgoing list
     */
    public void addOutgoingShipment(Shipment newShipment){
        if(newShipment != null){
            //Add to outgoing
            outgoingShipments.add(newShipment);
        } else {
            System.out.println("Invalid input. Please enter a shipment.");
        }
        updateAllShipments();
    }

    /*
     * removeIdleShipment
     * Removes a shipment from the idle list
     */
    public void removeIdleShipment(Shipment removedShipment){
        if(idleShipments.contains(removedShipment)) {
            idleShipments.remove(removedShipment);
        }
        updateAllShipments();
    }

    /*
     * removeIncomingShipment
     * Removes a shipment from incoming list
     */
    public void removeIncomingShipment(Shipment removedShipment){
        if(incomingShipments.contains(removedShipment)) {
            incomingShipments.remove(removedShipment);
        }
        updateAllShipments();
    }

    /*
     * removeOutgoingShipment
     * Removes a shipment from outgoing list
     */
    public void removeOutgoingShipment(Shipment removedShipment){
        if(outgoingShipments.contains(removedShipment)){
            outgoingShipments.remove(removedShipment);
        }
        updateAllShipments();
    }

    /*
     * removeAllShipments
     * Removes a shipment from the allShipments list
     */
    public void removeAllShipments(Shipment removedShipment){
        if(allShipments.contains(removedShipment)) {
            allShipments.remove(removedShipment);
        }
        updateAllShipments();
    }

    /*
     * getStockLevel
     * Returns current stock level
     */
    public double getStockLevel(){
        double stockLevel = 0;
        for(Shipment s : idleShipments){
            stockLevel += s.getCubicMeters();
        }
        for(Shipment s : outgoingShipments){
            stockLevel += s.getCubicMeters();
        }
        return stockLevel;
    }

    /*
     * addInspection
     * Adds an inspection object to the warehouse list of inspections
     */
    public void addInspection (InspectionLogEntry entry){
        historyInspections.add(entry);
    }

    /*
     * removeInspection
     * Removes an inspection from the warehouse list of inspections
     */
    public void removeInspection (InspectionLogEntry entry){
        if(historyInspections.contains(entry)) {
            historyInspections.remove(entry);
        } else {
            System.out.println("Error: historyInspections does not contain entry");
        }
    }

    /*
     * Sort inspections based on arrival date
     * Used when we create test data
     */
    public void sortInspections(){
        Collections.sort(historyInspections, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
    }

    /*
     * Adds a travel log entry to the warehouse list of travel log entries
     */
    public void addTravelEntry(TravelLogEntry travelEntry){
        historyShipments.add(travelEntry);
    }

    // Sorts the historyShipments list from old to new based on arrival dates
    public void sortTravelLogEntries() {
        Collections.sort(historyShipments, (o1, o2) -> o1.getArrivalDate().compareTo(o2.getArrivalDate()));
    }

    /*
     * getLastInspectionDate
     * Returns the last inspection date as String
     */
    public String getLastInspectionDate (){
        if(historyInspections.size() > 0){
            int lastInspectionIndex = historyInspections.size()-1;
            InspectionLogEntry lastInspection = historyInspections.get(lastInspectionIndex);
            LocalDateTime lastInspectionDate = lastInspection.getDate();
            return lastInspectionDate.format(this.formatter);
        } else {
            return "";
        }
    }

    /*
     * getInspectorNames
     * returns a list of unique inspector names who have performed an inspection at a certain warehouse
     */
    public ArrayList<String> getInspectorNames (){
        ArrayList<String> inspectorNames = new ArrayList<>();
        for(InspectionLogEntry e : historyInspections){
            if(!inspectorNames.contains(e.getInspectorName())){
                inspectorNames.add(e.getInspectorName());
            }
        }
        return inspectorNames;
    }

    /*
     * Repopulate the allShipments list with incoming, idle, and outgoing list content
     */
    public void updateAllShipments (){
        allShipments.clear();
        allShipments.addAll(incomingShipments);
        allShipments.addAll(idleShipments);
        allShipments.addAll(outgoingShipments);
    }

    // Attempt to manipulate/change the list without changing it so the tableview updates
    public void updateTravelLog(){
        historyShipments.add(null);
        historyShipments.remove(null);
    }

    /*
     * Getters
     */
    public String getName() {
        return this.name.get();
    }

    public String getRegion(){
        return this.region.get();
    }

    public String getAddress(){
        return this.address.get();
    }

    public double getCapacity(){
        return this.maxCapacity.get();
    }

    public ObservableList<Shipment> getIncomingShipments(){
        return FXCollections.unmodifiableObservableList(this.incomingShipments);
    }

    public ObservableList<Shipment> getIdleShipments(){
        return FXCollections.unmodifiableObservableList(this.idleShipments);
    }

    public ObservableList<Shipment> getOutgoingShipments(){
        return FXCollections.unmodifiableObservableList(this.outgoingShipments);
    }

    public ObservableList<Shipment> getAllShipments(){
        return FXCollections.unmodifiableObservableList(this.allShipments);
    }

    public ObservableList<InspectionLogEntry> getHistoryInspections(){
        return FXCollections.unmodifiableObservableList(this.historyInspections);
    }

    public ObservableList<TravelLogEntry> getHistoryShipments(){
        return FXCollections.unmodifiableObservableList(this.historyShipments);
    }

    /*
     * Setters
     */
    public void setName(String name){
        this.name.set(name);
    }

    public void setRegion(String region){
        this.region.set(region);
    }

    public void setAddress(String address){
        this.address.set(address);
    }

    public void setCapacity(double capacity){
        this.maxCapacity.set(capacity);
    }

    /*
     * Properties, observable for the tableviews
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty regionProperty() {
        return region;
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public SimpleDoubleProperty maxCapacityProperty() {
        return maxCapacity;
    }

} 