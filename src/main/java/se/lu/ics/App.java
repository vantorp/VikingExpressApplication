package se.lu.ics;

import java.util.Locale;

import javafx.application.Application;
import javafx.stage.Stage;
import se.lu.ics.controllers.AppController;
import se.lu.ics.models.AppModel;

public class App extends Application {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US); // To consistently use dots instead of commas for displayed decimals
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        // Create appModel and appController objects that are used everywhere in the program
        AppModel appModel = new AppModel();
        //AppController tar appModel som parameter
        AppController appController = new AppController(primaryStage, appModel);
        appController.showMainView();
    }
}