module viking.express {
    exports se.lu.ics.models;
    exports se.lu.ics.controllers;
    exports se.lu.ics;

    requires javafx.base;
    requires javafx.fxml;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;




    /*
    * Open the 'se.lu.ics.controllers' package, allowing other modules
    * to access the public classes and interfaces within this package.
    * This is necessary because the JavaFX runtime will need to
    * create instances of the classes in this package.
    */
    opens se.lu.ics.controllers to javafx.fxml;

}
