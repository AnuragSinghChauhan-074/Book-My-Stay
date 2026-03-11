import java.util.HashMap;
import java.util.Map;

/**
 * Use Case 4: Room Search & Availability Check
 * Demonstrates read-only access to room inventory and
 * filtering of available rooms for guests.
 *
 * @author Anurag
 * @version 4.0
 */


/* ---------- Room Domain Model ---------- */

abstract class Room {

    protected String roomType;
    protected int beds;
    protected int size;
    protected double price;

    public Room(String roomType, int beds, int size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + beds);
        System.out.println("Size      : " + size + " sq ft");
        System.out.println("Price     : $" + price);
    }
}


/* ---------- Room Types ---------- */

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200, 100);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350, 180);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 500, 300);
    }
}


/* ---------- Centralized Inventory ---------- */

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {

        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // example unavailable room
    }

    // Read-only method
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllInventory() {
        return inventory;
    }
}


/* ---------- Search Service ---------- */

class RoomSearchService {

    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchAvailableRooms(Room[] rooms) {

        System.out.println("\n--- Available Rooms ---\n");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            // Defensive programming check
            if (available > 0) {

                room.displayRoomDetails();
                System.out.println("Available Rooms : " + available);
                System.out.println("----------------------------");

            }
        }
    }
}


/* ---------- Application Entry ---------- */

public class UseCase4RoomSearch {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Welcome to Book My Stay App ");
        System.out.println(" Hotel Booking System v4.0 ");
        System.out.println("====================================");

        // Room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        Room[] rooms = {single, doubleRoom, suite};

        // Inventory initialization
        RoomInventory inventory = new RoomInventory();

        // Search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Guest searches available rooms
        searchService.searchAvailableRooms(rooms);

        System.out.println("\nSearch completed. Inventory state unchanged.");
    }
}