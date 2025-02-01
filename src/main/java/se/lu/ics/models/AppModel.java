package se.lu.ics.models;

import java.time.LocalDateTime;
//import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;

import javafx.collections.ObservableList;

/**
 * AppModel class
 * Contains references to register objects so that the whole program can reference the same data. 
 * And alsocreates test data. 
 */
public class AppModel {

    /*
     * Instance variables
     */
    private WarehouseRegister warehouseRegister;
    private ObservableList<Warehouse> warehouses;

    /*
     * Constructor
     */
    public AppModel() {
        this.warehouseRegister = new WarehouseRegister();
        this.warehouses = this.warehouseRegister.getWarehouses();
        // Adds the second version of the test data generation 
        addTestData2();
    }

    /*
     * There are two versions of test data generation, here begins addTestData()
     * Further down begins the now used addTestData2()
     */

    /*
     * Helper method to return a random inspection date
     */
    public LocalDateTime getInspectionDate(){
        Random random = new Random();
        int randomDays = random.nextInt(46); // 46 is exclusive, generates [0, 45]
        LocalDateTime randomizedDate =  LocalDateTime.now().minusDays(randomDays);
        return randomizedDate;
    }

    /*
     * Helper method to return a random inspector name
     */
    public String getInspectorName(){
        String inspectorName = "";
        Random random = new Random();
        int randomNumber = random.nextInt(10);
        switch(randomNumber){
            case(0):
                inspectorName = "Richard Stallman";
                break;
            case(1):
                inspectorName = "Aaron Schwartz";
                break;
            case(2):
                inspectorName = "Jacob Appelbaum";
                break;
            case(3):
                inspectorName = "Alison Macrina";
                break;
            case(4):
                inspectorName = "Samuel Skånberg";
                break;
            case(5):
                inspectorName = "Roger Dingledine";
                break;
            case(6):
                inspectorName = "Christopher Soghoian";
                break;
            case(7):
                inspectorName = "Moxie Marlinspike";
                break;
            case(8):
                inspectorName = "Bruce Schneider";
                break;
            case(9):
                inspectorName = "Brian Krebs";
                break;
        }
        return inspectorName;

    }

    /*
     * Helper method to return a random status for a shipment
     * NB, not currently used in the model, "status is deprecated in favor of separate warehouse arraylists.
     */
    public String getRandomStatus(){
        String status = "";
        Random random = new Random();
        int randomNumber = random.nextInt(8);
        switch(randomNumber){
            case(0):
                status =  "incoming";
                break;
            case(1):
                status = "incoming";
                break;
            case(2):
                status = "incoming";
                break;
            case(3):
                status = "idle";
                break;
            case(4):
                status = "idle";
                break;
            case(5):
                status = "idle";
                break;
            case(6):
                status = "outgoing";
                break;
            case(7):
                status = "outgoing";
                break;
            case(8):
                status = "outgoing";
                break;
        }
        return status;

    }

    /*
     * Helper method to return a random warehouse
     */
    public Warehouse getRandomWarehouse(){
        Warehouse randomWarehouse = null;
        Random random = new Random();
        int randomNumber = random.nextInt(10);
        if (warehouses == null || warehouses.isEmpty()) {
            throw new IllegalStateException("Warehouses list is not initialized or is empty");
        }
        switch(randomNumber){
            case(0):
                randomWarehouse = warehouses.get(0);
                break;
            case(1):
                randomWarehouse = warehouses.get(1);
                break;
            case(2):
                randomWarehouse = warehouses.get(2);
                break;
            case(3):
                randomWarehouse = warehouses.get(3);
                break;
            case(4):
                randomWarehouse = warehouses.get(4);
                break;
            case(5):
                randomWarehouse = warehouses.get(5);
                break;
            case(6):
                randomWarehouse = warehouses.get(6);
                break;
            case(7):
                randomWarehouse = warehouses.get(7);
                break;
            case(8):
                randomWarehouse = warehouses.get(8);
                break;
            case(9):
                randomWarehouse = warehouses.get(9);
                break;
        }
        return randomWarehouse;
    }

    /*
     * Helper method too return either approved or not approved, to use when generating inspections
     */
    public boolean getIsApproved(){
        boolean isApproved = true;
        Random random = new Random();
        int approvedOrNot = random.nextInt(9);
        switch(approvedOrNot){
            case 0, 1, 2, 3, 4, 5, 6:
                isApproved = true;
                break;
            case 7, 8:
                isApproved = false;
                break;
        }
        return isApproved;
    }

    /*
     * Helper method to return a random arrival date
     */
    public LocalDateTime getArrivalDate(){
        Random random = new Random();
        int randomDays = random.nextInt(15); // 14 dag max.
        LocalDateTime randomizedArrivalDate =  LocalDateTime.now().minusDays(randomDays);
        return randomizedArrivalDate;
    }

    /*
     * Helper method to return a random warehouse for arrival
     */ 
    public Warehouse getArrivalLocation(){
        Random random = new Random();
        int randomIndex = random.nextInt(10); //Hämtar random index nummer
        Warehouse randomizedWarehouse = this.getWarehouseRegister().getWarehouses().get(randomIndex);
        return randomizedWarehouse;
    }

    /* 
     * Helper method to return a random departure date 
     */
    public LocalDateTime getDepartureDate(){
        Random random = new Random();
        int randomDays = random.nextInt(15); //14 dag max. vi ska implementera errorhantering till 14 day warning
        LocalDateTime randomizedDepartureDate =  getArrivalDate().plusDays(randomDays);
        return randomizedDepartureDate;
    }

    /*
     * Helper method to return a random warehouse for departure
     */
    public Warehouse getDepartureLocation(){
        Random random = new Random();
        int randomIndex = random.nextInt(10); //Hämtar random index nummer
        Warehouse randomizedWarehouse = this.getWarehouseRegister().getWarehouses().get(randomIndex);
        return randomizedWarehouse;
    }

    /*
     * addTestData, first version of the test data, not currently used.
     */
    public void addTestData() {

        // Create warehouses
        Warehouse warehouse1 = new Warehouse("Idunn", "North", "Ungdomsgatan 1", 20000);
        warehouseRegister.addWarehouse(warehouse1);
        
        Warehouse warehouse2 = new Warehouse("Gna", "South", "Meddelandegatan 411", 50000);
        warehouseRegister.addWarehouse(warehouse2);
        
        Warehouse warehouse3 = new Warehouse("Freja", "Central", "Kärleksvägen 68", 45000);
        warehouseRegister.addWarehouse(warehouse3);

        Warehouse warehouse4 = new Warehouse("Saga", "South", "Kunskapsallén 97", 72000);
        warehouseRegister.addWarehouse(warehouse4);

        Warehouse warehouse5 = new Warehouse("Eir", "Central", "Sjukhusgatan 112", 30000);
        warehouseRegister.addWarehouse(warehouse5);

        Warehouse warehouse6 = new Warehouse("Syn", "North", "Säkerhetsgränden 10", 25000);
        warehouseRegister.addWarehouse(warehouse6);

        Warehouse warehouse7 = new Warehouse("Lofn", "North", "Artighetsallén 43", 55000);
        warehouseRegister.addWarehouse(warehouse7);

        Warehouse warehouse8 = new Warehouse("Snotra", "Central", "Filosofisgatan 33", 60000);
        warehouseRegister.addWarehouse(warehouse8);

        Warehouse warehouse9 = new Warehouse("Hel", "South", "Underjordsallén 666", 55000);
        warehouseRegister.addWarehouse(warehouse9);

        Warehouse warehouse10 = new Warehouse("Gefjun", "South", "Bondestigen 287", 75000);
        warehouseRegister.addWarehouse(warehouse10);

        /*
         * Create shipments with inspections
         */

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timestamp = now.minusDays(14);
        //Duration daysAgo = Duration.between(timestamp, now);

        for (Warehouse w : warehouseRegister.getWarehouses()){
            while(w.getIdleShipments().size() < 10){

                // Create shipment with random volume
                Shipment shipment1 = new Shipment(((int)(Math.random()*1000 + 1)));

                
                // Add shipment to current warehouse
                if (getRandomStatus().equals("idle")){// Add the shipment to the warehouse's idle shipments
                    w.addIdleShipment(shipment1);
                    TravelLogEntry travelEntry1 = new TravelLogEntry(shipment1, shipment1.getId(), getArrivalDate(), w, null, null);

                    shipment1.addTravelEntry(travelEntry1);
                    w.addTravelEntry(travelEntry1);


                } else if (getRandomStatus().equals("incoming")){
                    
                    // Make random possible
                    Random random = new Random();

                    // Add to incoming list
                    w.addIncomingShipment(shipment1);

                    // Where it is incoming from (previous warehouse)
                    Warehouse originWarehouse = w;
                    while(originWarehouse == w){
                        originWarehouse = getRandomWarehouse();
                    }

                    // When it arrived at the previous place, where it still is.
                    LocalDateTime oldArrival = LocalDateTime.now().minusDays(10 + random.nextInt(5));

                    // Create travel entry
                    TravelLogEntry travelEntry1 = new TravelLogEntry(shipment1, shipment1.getId(), oldArrival, originWarehouse, null, w);

                    // Add log to shipment
                    shipment1.addTravelEntry(travelEntry1);

                    // Add log and shipment to previous warehouse
                    originWarehouse.addTravelEntry(travelEntry1);
                    originWarehouse.addOutgoingShipment(shipment1);
                    originWarehouse.updateAllShipments();

                } else { //getRandomStatus().equals("outgoing")
                    w.addOutgoingShipment(shipment1);
            
                    //add inside "if" method due to variabel scope
                    TravelLogEntry travelEntry1 = new TravelLogEntry(shipment1, shipment1.getId(), getArrivalDate(), w, null, null);
                    shipment1.addTravelEntry(travelEntry1);
                    w.addTravelEntry(travelEntry1);
                }

                //Add inspection to shipment and warehouse
                InspectionLogEntry inspection1 = new InspectionLogEntry(shipment1, timestamp, getInspectorName(), shipment1.getId(), getIsApproved());
                shipment1.addInspection(inspection1);
                w.addInspection(inspection1);

            }
            w.updateAllShipments();

        }
        System.out.println("Test data added");
    }

    /**
     * Getter för warehouseRegister
     */
    public WarehouseRegister getWarehouseRegister() {
        return this.warehouseRegister;
    }


    /*
     * Alternative version of adding test data, currently used.
     */
    private void addTestData2(){

        // Add warehouses
        Warehouse warehouse1 = new Warehouse("Idunn", "North", "Ungdomsgatan 1  12345 Luleå", 20000);
        warehouseRegister.addWarehouse(warehouse1);
        
        Warehouse warehouse2 = new Warehouse("Gna", "South", "Meddelandegatan 411  23456 Staffanstorp", 50000);
        warehouseRegister.addWarehouse(warehouse2);
        
        Warehouse warehouse3 = new Warehouse("Freja", "Central", "Kärleksvägen 68  34567 Uppsala", 45000);
        warehouseRegister.addWarehouse(warehouse3);

        Warehouse warehouse4 = new Warehouse("Saga", "South", "Kunskapsallén 97  45678 Ystad", 72000);
        warehouseRegister.addWarehouse(warehouse4);

        Warehouse warehouse5 = new Warehouse("Eir", "Central", "Sjukhusgatan 112  56789 Stockholm", 30000);
        warehouseRegister.addWarehouse(warehouse5);

        Warehouse warehouse6 = new Warehouse("Syn", "North", "Säkerhetsgränden 10  67890 Kiruna", 25000);
        warehouseRegister.addWarehouse(warehouse6);

        Warehouse warehouse7 = new Warehouse("Lofn", "North", "Artighetsallén 43  78901 Piteå", 55000);
        warehouseRegister.addWarehouse(warehouse7);

        Warehouse warehouse8 = new Warehouse("Snotra", "Central", "Filosofisgatan 33  89012 Göteborg", 60000);
        warehouseRegister.addWarehouse(warehouse8);

        Warehouse warehouse9 = new Warehouse("Hel", "South", "Underjordsallén 666  90123 Malmö", 55000);
        warehouseRegister.addWarehouse(warehouse9);

        Warehouse warehouse10 = new Warehouse("Gefjun", "South", "Bondestigen 287  01234 Jönköping", 75000);
        warehouseRegister.addWarehouse(warehouse10);

        // Variables to use below
        Random random = new Random();
        // For travel log entries
        ArrayList<Warehouse> warehouseOptions;
        Shipment shipment;
        LocalDateTime timestamp;
        LocalDateTime arrivalDate;
        LocalDateTime departureDate;
        TravelLogEntry travelLogEntry;

        // Looping over all warehousese, add shipments of different types in each warehouse
        for (Warehouse w : warehouseRegister.getWarehouses()){

            /*
             * Scenarios
             * 
             *  - idle shipment                        arrivaldate
             *  - outgoing shipment not dispatched     arrivaldate
             *  - outgoing shipment dispatched         arrivaldate + departure date 
             *  (- incoming shipments are created as a side effect of creating outgoing shipments)
             */

            /* 
             * Idle shipments 
             */
            // decide how many of this type there should be:
            int numIdle = 12 + random.nextInt(3);
            for(int i=0; i < numIdle; i++){

                // reset list of possible other locations than w
                // warehouseOptions is like a bag of warehouses that we draw warehouses from
                warehouseOptions = new ArrayList<>(warehouseRegister.getWarehouses());
                warehouseOptions.remove(w);

                // reset timestamp to now()
                timestamp = LocalDateTime.now();

                // Skapa nytt shipment med volym mellan 10 och 1009 m3.
                shipment = new Shipment(10 + random.nextInt(1000));

                // put shipment in idle list
                w.addIdleShipment(shipment);
                w.updateAllShipments();

                // turn back time a couple of days
                timestamp = timestamp.minusDays(5 + random.nextInt(3));
                // set arrivalDate
                arrivalDate = timestamp;
                
                // make a travel log entry
                travelLogEntry = new TravelLogEntry(shipment, shipment.getId(), arrivalDate, w, null, null);
                // put travel log entry in both shipment and warehouse
                shipment.addTravelEntry(travelLogEntry);
                shipment.sortTravelLogEntries();

                w.addTravelEntry(travelLogEntry);
                w.sortTravelLogEntries();

                // make an inspection log
                InspectionLogEntry inspection1 = new InspectionLogEntry(shipment, timestamp.plusDays(1), getInspectorName(), shipment.getId(), getIsApproved());
                shipment.addInspection(inspection1);
                shipment.sortInspections();
                w.addInspection(inspection1);
                w.sortInspections();

                /* 
                 * Previous warehouse (one step back)
                 */
                //nextInt() is the upper limit/max number of a number range. random fetches any one of those numbers
                //randomly choosing a warehouse from the list of warehouses MINUS the warehouse its already been to
                Warehouse v = warehouseOptions.get(random.nextInt(warehouseOptions.size()));
                //chosen warehouse is removed from list so a shipment cant be at that same warehouse when we create history
                warehouseOptions.remove(v);
                
                // turn back time more and set departure date, must be before arrival date (2-6 days before current warehouse)
                timestamp = timestamp.minusDays(2 + random.nextInt(4));
                departureDate = timestamp;

                // turn back time more and set arrival date
                timestamp = timestamp.minusDays(1 + random.nextInt(10));
                arrivalDate = timestamp;

                // create travel log entry and add it in the list, before sorting happens
                travelLogEntry = new TravelLogEntry(shipment, shipment.getId(), arrivalDate, v, departureDate, w);
                // add and sort
                shipment.addTravelEntry(travelLogEntry);
                shipment.sortTravelLogEntries();

                v.addTravelEntry(travelLogEntry);
                v.sortTravelLogEntries();
            }

            /* 
             * Incoming shipments (dispatched) 
             */
            // decide how many of this type there should be:
            int numIncomingDispatched = 3 + random.nextInt(2);
            for(int i=0; i < numIncomingDispatched; i++){

                // reset list of possible other locations than w
                warehouseOptions = new ArrayList<>(warehouseRegister.getWarehouses());
                warehouseOptions.remove(w);

                // reset timestamp to now()
                timestamp = LocalDateTime.now();

                // Create shipment with random volume
                shipment = new Shipment(10 + random.nextInt(1000));

                // put shipment in incoming list
                w.addIncomingShipment(shipment);
                w.updateAllShipments();

                /* 
                 * Previous warehouse (one step back)
                 */

                Warehouse v = warehouseOptions.get(random.nextInt(warehouseOptions.size()));
                warehouseOptions.remove(v);

                // turn back time a couple of days
                timestamp = timestamp.minusDays(2 + random.nextInt(3));

                // set departureDate
                departureDate = timestamp;

                // turn back time a couple of days
                timestamp = timestamp.minusDays(2 + random.nextInt(5));

                // set arrivalDate
                arrivalDate = timestamp;
                
                // make a travel log entry
                travelLogEntry = new TravelLogEntry(shipment, shipment.getId(), arrivalDate, v, departureDate, w);
                // put travel log entry in both shipment and warehouse
                shipment.addTravelEntry(travelLogEntry);
                shipment.sortTravelLogEntries();

                v.addTravelEntry(travelLogEntry);
                v.sortTravelLogEntries();

                // make an inspection log
                InspectionLogEntry inspection1 = new InspectionLogEntry(shipment, timestamp.plusDays(1), getInspectorName(), shipment.getId(), getIsApproved());
                shipment.addInspection(inspection1);
                shipment.sortInspections();
                v.addInspection(inspection1);
                v.sortInspections();

                
                /* 
                 * Previous warehouse (two steps back)
                 */
                 Warehouse u = warehouseOptions.get(random.nextInt(warehouseOptions.size()));
                 warehouseOptions.remove(u);
                 
                 // turn back time more and set departure date
                 timestamp = timestamp.minusDays(3 + random.nextInt(3));
                 departureDate = timestamp;
 
                 // turn back time more and set arrival date
                 timestamp = timestamp.minusDays(1 + random.nextInt(10));
                 arrivalDate = timestamp;
 
                 // create travel log entry and add it as the first index in the list
                 travelLogEntry = new TravelLogEntry(shipment, shipment.getId(), arrivalDate, u, departureDate, v);
                 // add and sort
                 shipment.addTravelEntry(travelLogEntry);
                 shipment.sortTravelLogEntries();
 
                 u.addTravelEntry(travelLogEntry);
                 u.sortTravelLogEntries();

            }

            /* 
             * Incoming shipments (not dispatched, so they are also outgoing from the previous warehouse) 
             */
            // decide how many of this type there should be:
            int numIncomingNotDispatched = 3 + random.nextInt(2);
            for(int i=0; i < numIncomingNotDispatched; i++){

                // reset list of possible other locations than w
                warehouseOptions = new ArrayList<>(warehouseRegister.getWarehouses());
                warehouseOptions.remove(w);

                // reset timestamp to now()
                timestamp = LocalDateTime.now();

                // Skapa nytt shipment med volym mellan 10 och 1009 m3.
                shipment = new Shipment(10 + random.nextInt(1000));

                // put shipment in incoming list
                w.addIncomingShipment(shipment);
                w.updateAllShipments();

                /* 
                 * Previous warehouse (one step back)
                 */

                Warehouse v = warehouseOptions.get(random.nextInt(warehouseOptions.size()));
                warehouseOptions.remove(v);

                //put shipment in outgoing list in v
                v.addOutgoingShipment(shipment);
                v.updateAllShipments();

                // turn back time a couple of days
                //timestamp = timestamp.minusDays(2 + random.nextInt(3));

                // DON'T set departureDate
                //departureDate = timestamp;

                // turn back time a couple of days
                timestamp = timestamp.minusDays(9 + random.nextInt(9));

                // set arrivalDate
                arrivalDate = timestamp;
                
                // make a travel log entry
                travelLogEntry = new TravelLogEntry(shipment, shipment.getId(), arrivalDate, v, null, w);
                // put travel log entry in both shipment and warehouse
                shipment.addTravelEntry(travelLogEntry);
                shipment.sortTravelLogEntries();

                v.addTravelEntry(travelLogEntry);
                v.sortTravelLogEntries();

                // make an inspection log
                InspectionLogEntry inspection1 = new InspectionLogEntry(shipment, timestamp.plusDays(1), getInspectorName(), shipment.getId(), getIsApproved());
                shipment.addInspection(inspection1);
                shipment.sortInspections();
                v.addInspection(inspection1);
                v.sortInspections();
                
                /* 
                 * Previous warehouse (two step back)
                 */

                 Warehouse u = warehouseOptions.get(random.nextInt(warehouseOptions.size()));
                 warehouseOptions.remove(u);
                 
                 // turn back time more and set departure date
                 timestamp = timestamp.minusDays(3 + random.nextInt(3));
                 departureDate = timestamp;
 
                 // turn back time more and set arrival date
                 timestamp = timestamp.minusDays(1 + random.nextInt(10));
                 arrivalDate = timestamp;
 
                 // create travel log entry and add it as the first index in the list
                 travelLogEntry = new TravelLogEntry(shipment, shipment.getId(), arrivalDate, u, departureDate, v);
                 // add and sort
                 shipment.addTravelEntry(travelLogEntry);
                 shipment.sortTravelLogEntries();
 
                 u.addTravelEntry(travelLogEntry);
                 u.sortTravelLogEntries();
            }
        }
    }
}
