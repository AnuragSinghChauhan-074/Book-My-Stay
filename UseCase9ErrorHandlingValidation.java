import java.util.*;

/**
 * Use Case 9: Error Handling & Validation
 * Demonstrates validation of booking inputs and custom exception handling
 * to maintain system reliability.
 *
 * @author Anurag
 * @version 9.0
 */


/* ---------- Custom Exception ---------- */

class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}


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


/* ---------- Inventory Service ---------- */

class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {

        inventory = new HashMap<>();

        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public void validateRoomType(String roomType) throws InvalidBookingException {

        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    public void allocateRoom(String roomType) throws InvalidBookingException {

        int available = inventory.get(roomType);

        if (available <= 0) {
            throw new InvalidBookingException(
                    "No rooms available for type: " + roomType);
        }

        inventory.put(roomType, available - 1);

        System.out.println("Room allocated. Remaining " + roomType +
                " : " + inventory.get(roomType));
    }

    public void displayInventory() {

        System.out.println("\nCurrent Inventory:");

        for (String type : inventory.keySet()) {
            System.out.println(type + " → " + inventory.get(type));
        }
    }
}


/* ---------- Booking Validator ---------- */

class BookingValidator {

    private RoomInventory inventory;

    public BookingValidator(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation reservation)
            throws InvalidBookingException {

        // Validate room type
        inventory.validateRoomType(reservation.getRoomType());

        // Allocate room if valid
        inventory.allocateRoom(reservation.getRoomType());

        System.out.println("Reservation confirmed for "
                + reservation.getGuestName());
    }
}


/* ---------- Application Entry ---------- */

public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Welcome to Book My Stay App ");
        System.out.println(" Hotel Booking System v9.0 ");
        System.out.println("====================================");

        RoomInventory inventory = new RoomInventory();

        BookingValidator validator = new BookingValidator(inventory);

        // Valid booking
        Reservation r1 =
                new Reservation("R201", "Alice", "Single Room");

        // Invalid booking example
        Reservation r2 =
                new Reservation("R202", "Bob", "Penthouse");

        try {

            validator.processBooking(r1);

        } catch (InvalidBookingException e) {

            System.out.println("Booking Failed: " + e.getMessage());
        }

        try {

            validator.processBooking(r2);

        } catch (InvalidBookingException e) {

            System.out.println("Booking Failed: " + e.getMessage());
        }

        inventory.displayInventory();

        System.out.println("\nSystem continues running safely after errors.");
    }
}