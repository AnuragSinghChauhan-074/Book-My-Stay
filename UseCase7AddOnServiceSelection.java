import java.util.*;

/**
 * Use Case 7: Add-On Service Selection
 * Demonstrates attaching optional services to reservations
 * without modifying core booking or inventory logic.
 *
 * @author Anurag
 * @version 7.0
 */


/* ---------- Reservation Model ---------- */

class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Reservation ID : " + reservationId);
        System.out.println("Guest : " + guestName);
        System.out.println("Room Type : " + roomType);
    }
}


/* ---------- Add-On Service ---------- */

class AddOnService {

    private String serviceName;
    private double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void displayService() {
        System.out.println(serviceName + " ($" + price + ")");
    }
}


/* ---------- Add-On Service Manager ---------- */

class AddOnServiceManager {

    // reservationId → list of services
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {

        reservationServices.putIfAbsent(reservationId, new ArrayList<>());

        reservationServices.get(reservationId).add(service);

        System.out.println("Service added to reservation " + reservationId +
                ": " + service.getServiceName());
    }

    public void displayServices(String reservationId) {

        List<AddOnService> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        System.out.println("\nSelected Services:");

        for (AddOnService service : services) {
            service.displayService();
        }
    }

    public double calculateTotalServiceCost(String reservationId) {

        List<AddOnService> services = reservationServices.get(reservationId);

        double total = 0;

        if (services != null) {

            for (AddOnService service : services) {
                total += service.getPrice();
            }
        }

        return total;
    }
}


/* ---------- Application Entry ---------- */

public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Welcome to Book My Stay App ");
        System.out.println(" Hotel Booking System v7.0 ");
        System.out.println("====================================");

        // Existing reservation (from previous use case)
        Reservation reservation =
                new Reservation("R101", "Alice", "Single Room");

        reservation.displayReservation();

        // Service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Available services
        AddOnService breakfast = new AddOnService("Breakfast", 20);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 40);
        AddOnService spaAccess = new AddOnService("Spa Access", 60);

        // Guest selects services
        serviceManager.addService(reservation.getReservationId(), breakfast);
        serviceManager.addService(reservation.getReservationId(), airportPickup);
        serviceManager.addService(reservation.getReservationId(), spaAccess);

        // Display selected services
        serviceManager.displayServices(reservation.getReservationId());

        // Calculate additional cost
        double total = serviceManager.calculateTotalServiceCost(
                reservation.getReservationId());

        System.out.println("\nTotal Add-On Service Cost : $" + total);

        System.out.println("\nCore booking and inventory remain unchanged.");
    }
}