import java.util.*;

/**
 * Use Case 10: Booking Cancellation & Inventory Rollback
 * Demonstrates safe cancellation of bookings and restoration
 * of inventory using Stack-based rollback.
 *
 * @author Anurag
 * @version 10.0
 */


/* ---------- Reservation Model ---------- */

class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void displayReservation() {
        System.out.println("Reservation ID : " + reservationId);
        System.out.println("Guest : " + guestName);
        System.out.println("Room Type : " + roomType);
        System.out.println("Room ID : " + roomId);
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

    public void allocateRoom(String roomType) {

        int available = inventory.get(roomType);

        if (available > 0) {

            inventory.put(roomType, available - 1);

            System.out.println("Room allocated. Remaining " + roomType +
                    " : " + inventory.get(roomType));
        }
    }

    public void restoreRoom(String roomType) {

        int available = inventory.get(roomType);

        inventory.put(roomType, available + 1);

        System.out.println("Inventory restored for " + roomType +
                ". Available: " + inventory.get(roomType));
    }

    public void displayInventory() {

        System.out.println("\nCurrent Inventory:");

        for (String type : inventory.keySet()) {
            System.out.println(type + " → " + inventory.get(type));
        }
    }
}


/* ---------- Cancellation Service ---------- */

class CancellationService {

    private Map<String, Reservation> confirmedBookings;
    private Stack<String> rollbackStack;
    private RoomInventory inventory;

    public CancellationService(RoomInventory inventory) {

        this.inventory = inventory;
        confirmedBookings = new HashMap<>();
        rollbackStack = new Stack<>();
    }

    public void confirmBooking(Reservation reservation) {

        confirmedBookings.put(reservation.getReservationId(), reservation);

        inventory.allocateRoom(reservation.getRoomType());

        System.out.println("Booking confirmed: " + reservation.getReservationId());
    }

    public void cancelBooking(String reservationId) {

        if (!confirmedBookings.containsKey(reservationId)) {

            System.out.println("Cancellation failed: Reservation not found.");
            return;
        }

        Reservation reservation = confirmedBookings.get(reservationId);

        // Push room ID for rollback tracking
        rollbackStack.push(reservation.getRoomId());

        // Restore inventory
        inventory.restoreRoom(reservation.getRoomType());

        confirmedBookings.remove(reservationId);

        System.out.println("Reservation cancelled successfully: " + reservationId);
        System.out.println("Released Room ID: " + rollbackStack.peek());
    }
}


/* ---------- Application Entry ---------- */

public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Welcome to Book My Stay App ");
        System.out.println(" Hotel Booking System v10.0 ");
        System.out.println("====================================");

        RoomInventory inventory = new RoomInventory();

        CancellationService cancellationService =
                new CancellationService(inventory);

        // Confirm booking
        Reservation reservation =
                new Reservation("R301", "Alice", "Single Room", "S1");

        cancellationService.confirmBooking(reservation);

        // Cancel booking
        cancellationService.cancelBooking("R301");

        // Attempt invalid cancellation
        cancellationService.cancelBooking("R999");

        inventory.displayInventory();
    }
}