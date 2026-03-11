import java.io.*;
import java.util.*;

/*
 * Use Case 12: Data Persistence & System Recovery
 * Demonstrates saving and restoring booking + inventory state.
 */

/* ---------- Reservation Model ---------- */

class Reservation implements Serializable {

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

    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}


/* ---------- System State (Inventory + Bookings) ---------- */

class SystemState implements Serializable {

    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState() {

        inventory = new HashMap<>();
        bookingHistory = new ArrayList<>();

        inventory.put("Single Room", 3);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }
}


/* ---------- Persistence Service ---------- */

class PersistenceService {

    private static final String FILE_NAME = "hotel_state.ser";

    public static void saveState(SystemState state) {

        try {

            FileOutputStream fileOut = new FileOutputStream(FILE_NAME);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(state);

            out.close();
            fileOut.close();

            System.out.println("System state saved successfully.");

        } catch (IOException e) {

            System.out.println("Error saving system state.");
        }
    }

    public static SystemState loadState() {

        try {

            FileInputStream fileIn = new FileInputStream(FILE_NAME);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            SystemState state = (SystemState) in.readObject();

            in.close();
            fileIn.close();

            System.out.println("System state restored successfully.");

            return state;

        } catch (Exception e) {

            System.out.println("No previous state found. Starting fresh.");

            return new SystemState();
        }
    }
}


/* ---------- Booking Service ---------- */

class BookingService {

    private SystemState state;

    public BookingService(SystemState state) {
        this.state = state;
    }

    public void bookRoom(String reservationId, String guestName, String roomType) {

        if (!state.inventory.containsKey(roomType)) {

            System.out.println("Invalid room type.");
            return;
        }

        int available = state.inventory.get(roomType);

        if (available <= 0) {

            System.out.println("No rooms available for " + roomType);
            return;
        }

        Reservation reservation =
                new Reservation(reservationId, guestName, roomType);

        state.bookingHistory.add(reservation);

        state.inventory.put(roomType, available - 1);

        System.out.println("Booking confirmed: " + reservation);
    }

    public void showBookings() {

        System.out.println("\nBooking History:");

        for (Reservation r : state.bookingHistory) {
            System.out.println(r);
        }
    }

    public void showInventory() {

        System.out.println("\nCurrent Inventory:");

        for (String type : state.inventory.keySet()) {

            System.out.println(type + " -> " + state.inventory.get(type));
        }
    }
}


/* ---------- Application Entry ---------- */

public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        System.out.println("==================================");
        System.out.println("Book My Stay App - UC12");
        System.out.println("Data Persistence & Recovery");
        System.out.println("==================================");

        // Restore previous system state
        SystemState state = PersistenceService.loadState();

        BookingService service = new BookingService(state);

        // Simulate new bookings
        service.bookRoom("R501", "Alice", "Single Room");
        service.bookRoom("R502", "Bob", "Double Room");

        service.showBookings();
        service.showInventory();

        // Save state before shutdown
        PersistenceService.saveState(state);

        System.out.println("\nSystem shutdown completed safely.");
    }
}