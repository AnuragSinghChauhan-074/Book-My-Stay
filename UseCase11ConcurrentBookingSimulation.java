import java.util.*;

/**
 * Use Case 11: Concurrent Booking Simulation
 * Demonstrates thread-safe booking under concurrent access.
 *
 * @author Anurag
 * @version 11.0
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
}


/* ---------- Shared Inventory ---------- */

class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {

        inventory = new HashMap<>();

        inventory.put("Single Room", 2);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    // synchronized critical section
    public synchronized void allocateRoom(Reservation reservation) {

        String roomType = reservation.getRoomType();
        int available = inventory.get(roomType);

        if (available > 0) {

            inventory.put(roomType, available - 1);

            System.out.println(Thread.currentThread().getName()
                    + " booked " + roomType
                    + " for " + reservation.getGuestName()
                    + " | Remaining: " + inventory.get(roomType));

        } else {

            System.out.println(Thread.currentThread().getName()
                    + " FAILED booking for "
                    + reservation.getGuestName()
                    + " (No rooms available)");
        }
    }

    public void displayInventory() {

        System.out.println("\nFinal Inventory State:");

        for (String type : inventory.keySet()) {
            System.out.println(type + " → " + inventory.get(type));
        }
    }
}


/* ---------- Booking Processor (Thread) ---------- */

class BookingProcessor extends Thread {

    private Queue<Reservation> bookingQueue;
    private RoomInventory inventory;

    public BookingProcessor(String name,
                            Queue<Reservation> bookingQueue,
                            RoomInventory inventory) {

        super(name);
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            Reservation reservation;

            synchronized (bookingQueue) {

                if (bookingQueue.isEmpty()) {
                    break;
                }

                reservation = bookingQueue.poll();
            }

            inventory.allocateRoom(reservation);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


/* ---------- Application Entry ---------- */

public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Welcome to Book My Stay App ");
        System.out.println(" Hotel Booking System v11.0 ");
        System.out.println(" Concurrent Booking Simulation ");
        System.out.println("====================================");

        RoomInventory inventory = new RoomInventory();

        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Simulated guest booking requests
        bookingQueue.add(new Reservation("R401", "Alice", "Single Room"));
        bookingQueue.add(new Reservation("R402", "Bob", "Single Room"));
        bookingQueue.add(new Reservation("R403", "Charlie", "Single Room"));
        bookingQueue.add(new Reservation("R404", "David", "Double Room"));
        bookingQueue.add(new Reservation("R405", "Emma", "Suite Room"));

        // Multiple booking threads
        BookingProcessor t1 =
                new BookingProcessor("Thread-1", bookingQueue, inventory);

        BookingProcessor t2 =
                new BookingProcessor("Thread-2", bookingQueue, inventory);

        BookingProcessor t3 =
                new BookingProcessor("Thread-3", bookingQueue, inventory);

        t1.start();
        t2.start();
        t3.start();

        try {

            t1.join();
            t2.join();
            t3.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.displayInventory();

        System.out.println("\nAll concurrent bookings processed safely.");
    }
}