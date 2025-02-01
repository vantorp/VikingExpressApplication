package se.lu.ics.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.time.LocalDateTime;
import java.time.Duration;

/*
 * TravelLogEntry class
 * Represents a travel log entry with information about a shipments arrival to and departure from a warehouse.
 */
public class TravelLogEntry {

    /*
     * Instance variables
     * Properties to make tableviews update
     */
    private SimpleStringProperty shipmentId;
    private ObjectProperty<LocalDateTime> departureDate;
    private ObjectProperty<Warehouse> departureLocation;
    private ObjectProperty<LocalDateTime> arrivalDate;
    private ObjectProperty<Warehouse> arrivalLocation;
    private SimpleIntegerProperty throughput;
    private Shipment shipment;

    /*
     * Constructor
     */
    public TravelLogEntry(Shipment shipment, String shipmentId, LocalDateTime arrivalDate, Warehouse arrivalLocation, LocalDateTime departureDate, Warehouse departureLocation) {
        this.shipmentId = new SimpleStringProperty(shipmentId);
        this.shipment = shipment;
        this.departureDate = new SimpleObjectProperty<>(departureDate);
        this.departureLocation = new SimpleObjectProperty<>(departureLocation);
        this.arrivalDate = new SimpleObjectProperty<>(arrivalDate);
        this.arrivalLocation = new SimpleObjectProperty<>(arrivalLocation);
        this.throughput = new SimpleIntegerProperty(0); // Initializing throughput
    }

    /*
     * Calculates the time spent in a warehouse
     * This method is part of the transition to properties, since it's not working fully yet
     * use the below method instead: findThroughput()
     */
    public void calculateThroughput() {
        Duration duration = Duration.between(arrivalDate.get(), departureDate.get());
        int newThroughput = (int) duration.toDays();
        this.throughput.set(newThroughput);
    }

    // Calculates and returns the time spent in a warehouse (in days)
    public Integer findThroughput(){
        Duration duration = Duration.between(arrivalDate.get(), departureDate.get());
        int newThroughput = (int) duration.toDays();
        return newThroughput;
    }

    /*
     * Getters and setters
     */
    
    // departureDate
    public LocalDateTime getDepartureDate() {
        return departureDate.get();
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate.set(departureDate);
    }

    // arrivalDate
    public LocalDateTime getArrivalDate() {
        return arrivalDate.get();
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate.set(arrivalDate);
    }

    // departureLocation
    public Warehouse getDepartureLocation() {
        return departureLocation.get();
    }

    public void setDepartureLocation(Warehouse departureLocation) {
        this.departureLocation.set(departureLocation);
    }

    // arrivalLocation
    public Warehouse getArrivalLocation() {
        return arrivalLocation.get();
    }

    public void setArrivalLocation(Warehouse arrivalLocation) {
        this.arrivalLocation.set(arrivalLocation);
    }

    // shipmentId
    public String getShipmentId() {
        return shipmentId.get();
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId.set(shipmentId);
    }

    // throughput
    public int getThroughput() {
        return throughput.get();
    }

    public void setThroughput(int throughput) {
        this.throughput.set(throughput);
    }

    // shipment
    public Shipment getShipment() {
        return shipment;
    }

    /*
     * Properties to be used in tableviews
     */

    public ObjectProperty<LocalDateTime> departureDateProperty() {
        return departureDate;
    }

    public ObjectProperty<LocalDateTime> arrivalDateProperty() {
        return arrivalDate;
    }

    public ObjectProperty<Warehouse> departureLocationProperty() {
        return departureLocation;
    }

    public ObjectProperty<Warehouse> arrivalLocationProperty() {
        return arrivalLocation;
    }

    public SimpleStringProperty shipmentIdProperty() {
        return shipmentId;
    }

    public SimpleIntegerProperty throughputProperty() {
        return throughput;
    }
}