package se.lu.ics.models;
import java.time.LocalDateTime;

public class ModelIntegrationTest {
    public static void main(String[] args){

        System.out.println("Creating WarehouseRegister");
        WarehouseRegister warehouseRegister1 = new WarehouseRegister();
        System.out.println(warehouseRegister1);
        System.out.println(); // Blank line
        
        System.out.println("Creating Warehouse");
        Warehouse warehouse1 = new Warehouse("Doggyhaus", "North", "123 street", 20000);
        System.out.println(warehouse1);
        System.out.println(); // Blank line

        System.out.println("Testing warehouse getters");
        System.out.println(warehouse1.getName());
        System.out.println(warehouse1.getRegion());
        System.out.println(warehouse1.getAddress());
        System.out.println(warehouse1.getCapacity());
        System.out.println(); // Blank line

        System.out.println("Adding warehouse to warehouseRegister");
        warehouseRegister1.addWarehouse(warehouse1);
        System.out.println(warehouseRegister1.getWarehouses().get(0).getName());
        System.out.println(); // Blank line

        System.out.println("Create new shipment");
        Shipment shipment1 = new Shipment(20000);
        System.out.println(shipment1.getId());
        System.out.println(); // Blank line

        System.out.println("Testing shipment getters");
        System.out.println(shipment1.getId());
        System.out.println(shipment1.getCubicMeters() + "m³");
        System.out.println(); // Blank line

        System.out.println("Create TravelLogEntry");
        TravelLogEntry travelLogEntry1 = new TravelLogEntry(shipment1, "123", LocalDateTime.now(), warehouse1,
            LocalDateTime.now().plusDays(5), warehouse1);
        System.out.println(travelLogEntry1);
        System.out.println(); // Blank line


        System.out.println("Testing travelLogEntry getters");
        System.out.println(travelLogEntry1.getShipmentId()); 
        System.out.println(travelLogEntry1.getDepartureDate().toString());
        System.out.println(travelLogEntry1.getArrivalDate().toString());
        System.out.println(); // Blank line


        

    }
    
}
