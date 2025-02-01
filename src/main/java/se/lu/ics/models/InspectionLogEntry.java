package se.lu.ics.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDateTime;

/*
 * InspectionLogEntry class
 * Represents an inspection of a shipment at a warehouse.
 */
public class InspectionLogEntry {

    /*
     * Instance variables
     * Using properties to make the tableview update automatically  
     */
    private ObjectProperty<LocalDateTime> date;
    private SimpleStringProperty inspectorName;
    private SimpleStringProperty shipmentId;
    private SimpleBooleanProperty isApproved;
    private Shipment shipment;

    /*
     * Constructor
     */
    public InspectionLogEntry(Shipment shipment, LocalDateTime date, String inspectorName, String shipmentId, boolean isApproved){
        this.shipment = shipment;
        this.date = new SimpleObjectProperty<>(date);
        this.inspectorName = new SimpleStringProperty(inspectorName);
        this.shipmentId = new SimpleStringProperty(shipmentId);
        this.isApproved = new SimpleBooleanProperty(isApproved);
    }

    /*
     * Getters för properties
     */

    public Shipment getShipment(){
        return shipment;
    }
    
    public LocalDateTime getDate(){
        return date.get();
    }

    public String getInspectorName(){
        return inspectorName.get();
    }

    public String getShipmentId(){
        return shipmentId.get();
    }

    public boolean getIsApproved(){
        return isApproved.get();
    }

    /*
     * Setters för properties
     */
    public void setDate(LocalDateTime date){
        this.date.set(date);
    }

    public void setInspectorName(String inspectorName){
        this.inspectorName.set(inspectorName);
    }

    public void setShipmentId(String shipmentId){
        this.shipmentId.set(shipmentId);
    }

    public void setIsApproved(boolean isApproved){
        this.isApproved.set(isApproved);
    }

    /*
     * Properties to use in initialize() for TableView columns
     */
    public ObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }

    public SimpleStringProperty inspectorNameProperty() {
        return inspectorName;
    }

    public SimpleStringProperty shipmentIdProperty() {
        return shipmentId;
    }

    public SimpleBooleanProperty isApprovedProperty() {
        return isApproved;
    }
}