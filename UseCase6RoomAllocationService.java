import java.util.*;

/**
 * Use Case 6: Reservation Confirmation & Room Allocation
 * Demonstrates safe booking confirmation with unique room allocation
 * while maintaining inventory consistency.
 *
 * @author Anurag
 * @version 6.0
 */


/* ---------- Reservation Model ---------- */

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}


/* ---------- Inventory Service ---------- */

class InventoryService {

    private HashMap<String, Integer> inventory;

    public InventoryService() {

        inventory = new HashMap<>();

        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {

        int current = inventory.get(roomType);

        if (current > 0) {
            inventory.put(roomType, current - 1);
        }
    }

    public void displayInventory() {

        System.out.println("\n--- Current Inventory ---");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {

            System.out.println(entry.getKey() + " Available : " + entry.getValue());
        }
    }
}


/* ---------- Booking Request Queue ---------- */

class BookingQueue {

    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {

        queue.offer(reservation);

        System.out.println("Booking request received from " + reservation.getGuestName());
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}


/* ---------- Booking Service ---------- */

class BookingService {

    private InventoryService inventory;

    // Tracks allocated room IDs
    private Set<String> allocatedRoomIds;

    // Map room type → allocated IDs
    private HashMap<String, Set<String>> roomAllocationMap;

    private int roomCounter = 1;

    public BookingService(InventoryService inventory) {

        this.inventory = inventory;

        allocatedRoomIds = new HashSet<>();

        roomAllocationMap = new HashMap<>();
    }

    public void processReservation(Reservation reservation) {

        String roomType = reservation.getRoomType();

        int available = inventory.getAvailability(roomType);

        if (available <= 0) {

            System.out.println("\nNo available rooms for " + roomType +
                    " for guest " + reservation.getGuestName());

            return;
        }

        // Generate unique room ID
        String roomId = roomType.replace(" ", "").substring(0, 2).toUpperCase() + roomCounter++;

        // Ensure uniqueness
        if (allocatedRoomIds.contains(roomId)) {
            System.out.println("Duplicate room detected. Allocation failed.");
            return;
        }

        allocatedRoomIds.add(roomId);

        // Track allocation by room type
        roomAllocationMap.putIfAbsent(roomType, new HashSet<>());
        roomAllocationMap.get(roomType).add(roomId);

        // Update inventory
        inventory.decrementRoom(roomType);

        // Confirm reservation
        System.out.println("\nReservation Confirmed!");
        System.out.println("Guest : " + reservation.getGuestName());
        System.out.println("Room Type : " + roomType);
        System.out.println("Allocated Room ID : " + roomId);
    }

    public void displayAllocations() {

        System.out.println("\n--- Allocated Rooms ---");

        for (Map.Entry<String, Set<String>> entry : roomAllocationMap.entrySet()) {

            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
    }
}


/* ---------- Application Entry ---------- */

public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Welcome to Book My Stay App ");
        System.out.println(" Hotel Booking System v6.0 ");
        System.out.println("====================================");

        InventoryService inventory = new InventoryService();

        BookingQueue queue = new BookingQueue();

        BookingService bookingService = new BookingService(inventory);

        // Simulated booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Double Room"));
        queue.addRequest(new Reservation("Charlie", "Suite Room"));
        queue.addRequest(new Reservation("David", "Single Room"));

        System.out.println("\nProcessing booking queue...\n");

        while (!queue.isEmpty()) {

            Reservation request = queue.getNextRequest();

            bookingService.processReservation(request);
        }

        bookingService.displayAllocations();

        inventory.displayInventory();

        System.out.println("\nAll requests processed.");
    }
}