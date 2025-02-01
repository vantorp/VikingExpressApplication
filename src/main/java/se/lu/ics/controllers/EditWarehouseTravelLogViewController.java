package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import se.lu.ics.models.TravelLogEntry;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.scene.control.*;

/*
 * EditWarehouseTravelLogViewController class
 * A window where one can edit a travel log entry in a warehouse
 */
public class EditWarehouseTravelLogViewController {

    /*
     * Instance variables
     */
    private TravelLogEntry selectedEntry;
    private TableView<TravelLogEntry> tableView;

    /*
     * FXML objects and handlers  
     */ 

    @FXML
    private Label labelTopHeadingEditWarehouseTravelLog;

    @FXML
    private Label labelShipmentIdWarehouseTravelLog;

    @FXML
    private Label labelArrivalDateWarehouseTravelLog;

    @FXML
    private Label labelDepartureDateWarehouseTravelLog;

    @FXML
    private TextField textFieldShipmentIdEditWarehouseTravelLog;

    @FXML
    private DatePicker arrivalDatePicker;

    @FXML
    private DatePicker departureDatePicker;

    @FXML
    private Spinner<Integer> arrivalHourSpinner;

    @FXML
    private Spinner<Integer> arrivalMinuteSpinner;

    @FXML
    private Spinner<Integer> departureHourSpinner;

    @FXML
    private Spinner<Integer> departureMinuteSpinner; 
  
    @FXML
    public Button buttonCloseWindow;

    @FXML
    public Button buttonUpdateWarehouseTravelLog;

    // Runs automatically when an object of this class is created
    public void initialize(){
        // Define possible values for the input UI elements
        arrivalHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        arrivalMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));
        departureHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        departureMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));
    }

    // Handles the button to edit a travel log entry for a warehouse
    @FXML   
    public void handleButtonUpdateWarehouseTravelLogAction(ActionEvent event) {
        try{
            // Read in arrival date picker and time spinners
            LocalDate arrivalDateInput = arrivalDatePicker.getValue();
            int arrivalHourInput = arrivalHourSpinner.getValue();
            int arrivalMinuteInput = arrivalMinuteSpinner.getValue();

            // Read in departure date picker and time spinners
            LocalDate departureDateInput = departureDatePicker.getValue();
            int departureHourInput = departureHourSpinner.getValue();
            int departureMinuteInput = departureMinuteSpinner.getValue();

            // Create date variables for the inputs
            LocalDateTime chosenArrival;

            // Arrival date *should* not be possible to be null, but better safe than sorry.
            if(arrivalDateInput != null){
                // Convert LocalDate to LocalDateTime object by combining the date + hour + minute input
                chosenArrival = LocalDateTime.of(arrivalDateInput, LocalTime.of(arrivalHourInput, arrivalMinuteInput));
            } else {
                throw new Exception();
            }

            // Departure date could be empty, and this could be desired behaviour, so not an error in itself
            // But, if it is set, we set the departure date in here. Can't be before arrival date.
            if(departureDateInput != null){
                //  Convert LocalDate to LocalDateTime object
                LocalDateTime chosenDeparture = LocalDateTime.of(departureDateInput, LocalTime.of(departureHourInput, departureMinuteInput));
                // Check to see that arrival comes before departure
                if(chosenDeparture.isAfter(chosenArrival)){
                    // Set departure date for the Travel Log entry
                    selectedEntry.setDepartureDate(chosenDeparture);
                } else {
                    throw new IllegalArgumentException("Error: Arrival date must be before departure date.");
                }
            }
            
            // Set arrival date for the Travel Log Entry
            selectedEntry.setArrivalDate(chosenArrival);
            // Close window
            Stage stage = (Stage) buttonUpdateWarehouseTravelLog.getScene().getWindow();
            stage.close();
        } catch(IllegalArgumentException e){
            //e.getMessage retrieves the illegalArgumentException message above
            //This error handling is scalable
            showErrorDialog("Error:", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Unknown error", e.getStackTrace().toString());
        }
        // Workaround until properties updates all tableviews automatically
        tableView.refresh();
    }

    // Handles the button to close the window
    @FXML
    public void handleButtonCloseWindowAction() {
        Stage currentStage = (Stage) buttonCloseWindow.getScene().getWindow();
        currentStage.close();
    }

    // Helper method to show an error dialog popup
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to fill in current info from the travel log
    public void fillInEditable() {
        // Set heading label
        labelTopHeadingEditWarehouseTravelLog.setText("Edit Shipment: " + selectedEntry.getShipmentId());
        // DatePicker for arrival, our LocalDateTime object is converted to LocalDate through the method .toLocalDate()
        arrivalDatePicker.setValue(selectedEntry.getArrivalDate().toLocalDate());
        // HourSpinner for arrival
        arrivalHourSpinner.getValueFactory().setValue(selectedEntry.getArrivalDate().getHour());
        // MinuteSpinner for arrival
        arrivalMinuteSpinner.getValueFactory().setValue(selectedEntry.getArrivalDate().getMinute());
        // DatePicker for departure, our LocalDateTime object is converted to LocalDate through the method .toLocalDate()
        if (selectedEntry.getDepartureDate() != null) {
            departureDatePicker.setValue(selectedEntry.getDepartureDate().toLocalDate());
        } else {
                departureDatePicker.setValue(null); // Clear the DatePicker
                departureMinuteSpinner.getValueFactory().setValue(0); // Default minute
        }
        // HourSpinner for departure
        if (selectedEntry.getDepartureDate() != null) {
            departureHourSpinner.getValueFactory().setValue(selectedEntry.getDepartureDate().getHour());
        } else {
            // Default hour
            departureHourSpinner.getValueFactory().setValue(0); 
        }
        if (selectedEntry.getDepartureDate() != null){
        // MinuteSpinner for departure
        departureMinuteSpinner.getValueFactory().setValue(selectedEntry.getDepartureDate().getMinute());
        } else {
            // Default minute
            departureMinuteSpinner.getValueFactory().setValue(0); 
        }
    }

    /*
     * Getters and setters
     */
    public void setCurrentTravelEntry(TravelLogEntry selectedEntry) {
        this.selectedEntry = selectedEntry;
    }

    public void setTableView(TableView<TravelLogEntry> tableView) {
        this.tableView = tableView;
        fillInEditable();
    }

}

