package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import se.lu.ics.models.Shipment;
import se.lu.ics.models.InspectionLogEntry;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.scene.control.*;

/*
 * EditWarehouseInspectionsViewController class
 * Opens a new window to edit inspections in a warehouse
 */
public class EditWarehouseInspectionsViewController {
    /*
     * Instance variables
     */
    private Shipment currentShipment;
    private InspectionLogEntry chosenEntry;
    private TableView<InspectionLogEntry> tableView;

    /*
     * FXML objects and handlers
     */
    @FXML
    private Label labelTopHeadingEditInspectionsWarehouseLog;

    @FXML
    private Label labelShipmentIdInspectionsWarehouseLog;

    @FXML
    private Label labelInspectionDateWarehouseInspectionsLog;

    @FXML
    private Label labelInspectorNameWarehouseInspectionsLog;

    @FXML
    private TextField textFieldInspectorNameEditWarehouseInspectionsLog;

    @FXML
    private DatePicker inspectionDatePicker;

    @FXML
    private Spinner<Integer> inspectionHourSpinner;

    @FXML
    private Spinner<Integer> inspectionMinuteSpinner;

    @FXML
    private CheckBox checkBoxApprovedEditWarehouseInspectionsLog;
  
    @FXML
    public Button buttonCloseWindow;

    @FXML
    public Button buttonUpdateWarehouseInspectionLog;

    // Runs automatically when a new object of this class is created
    public void initialize(){
        // Define possible values of the hour and minute input spinners
        inspectionHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        inspectionMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));
    }

    // Handles the button to save/update an inspection log in a warehouse
    @FXML   
    public void handleButtonUpdateWarehouseInspectionLogAction(ActionEvent event) {
        try{
            // Get inspector name from text field
            String shipmentInspectorNameInput = textFieldInspectorNameEditWarehouseInspectionsLog.getText().trim();
            // Make sure inspector name is not empty
            if (shipmentInspectorNameInput.isBlank()){
                throw new IllegalArgumentException("Inspector field cannot be empty.");
            }
            // Everthing inside "[]" are valid characters, "^" and "$" means beginning and end of line
            if(!shipmentInspectorNameInput.matches("^[a-zA-Zåäö ]+$")){
                throw new IllegalArgumentException("Error: Name must only contain alphabetical characters.");
            }
            // Read Date, hour, and minute from date picker, hour spinner, and minute spinner
            LocalDate inspectionDateInput = inspectionDatePicker.getValue();
            int inspectionHourInput = inspectionHourSpinner.getValue();
            int inspectionMinuteInput = inspectionMinuteSpinner.getValue();
            // The Date Picker should already be initialized, but better check that it's not null here too.
            if (inspectionDateInput != null) {
                // Combine the date and time into a LocalDateTime object
                LocalDateTime chosenInspectionDate = LocalDateTime.of(inspectionDateInput, LocalTime.of(inspectionHourInput, inspectionMinuteInput));
                // Set inspection date based on input
                chosenEntry.setDate(chosenInspectionDate);
            } else {
                throw new Exception();
            }
            // Set approved status
            if(checkBoxApprovedEditWarehouseInspectionsLog.isSelected()) {
                chosenEntry.setIsApproved(true);
            }else{
                chosenEntry.setIsApproved(false);
            }
            // Set inspector name
            chosenEntry.setInspectorName(shipmentInspectorNameInput);
            // Close the window
            Stage stage = (Stage) buttonUpdateWarehouseInspectionLog.getScene().getWindow();
            stage.close();
        } catch(IllegalArgumentException e){
            showErrorDialog("Error:", e.getMessage());
        } catch (Exception e) {
            showErrorDialog("Unknown error", "Contact systems administrator");
        }
        // Refresh the tableview, workaround until properties work to update automatically
        tableView.refresh();
    }

    // Handles the button that closes the window
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

    // Helper method to fill in the fields with the current info
    public void fillInEditable() {
        // Fill in label heading
        labelTopHeadingEditInspectionsWarehouseLog.setText("Edit Inspection for shipment: " + currentShipment.getId());
        // DatePicker for the inspection, must be converted to LocalDate through the method .toLocalDate()
        inspectionDatePicker.setValue(chosenEntry.getDate().toLocalDate());
        // HourSpinner
        inspectionHourSpinner.getValueFactory().setValue(chosenEntry.getDate().getHour());
        // MinuteSpinner
        inspectionMinuteSpinner.getValueFactory().setValue(chosenEntry.getDate().getMinute());
        // TextField for inspector
        textFieldInspectorNameEditWarehouseInspectionsLog.setText(chosenEntry.getInspectorName());
        // Approved
        checkBoxApprovedEditWarehouseInspectionsLog.setSelected(chosenEntry.getIsApproved());
    }

    /*
     * Getters and setters
     */
    public void setCurrentShipment(Shipment shipment) {
        this.currentShipment = shipment;
        // Run the helper method when setCurrentShipment is run from the AppController
        fillInEditable(); 
    }

    public void setCurrentInspectionEntry(InspectionLogEntry chosenEntry) {
        this.chosenEntry = chosenEntry;
    }

    public void setTableView(TableView<InspectionLogEntry> tableView) {
        this.tableView = tableView;
    }

}


