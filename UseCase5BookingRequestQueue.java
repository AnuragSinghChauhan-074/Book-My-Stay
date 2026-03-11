import java.util.LinkedList;
import java.util.Queue;

/**
 * Use Case 5: Booking Request Queue (First-Come-First-Served)
 * Demonstrates how booking requests are collected and ordered
 * using a Queue before room allocation happens.
 *
 * @author Anurag
 * @version 5.0
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

    public void displayReservation() {
        System.out.println("Guest : " + guestName);
        System.out.println("Requested Room : " + roomType);
    }
}


/* ---------- Booking Request Queue ---------- */

class BookingRequestQueue {

    private Queue<Reservation> bookingQueue;

    public BookingRequestQueue() {
        bookingQueue = new LinkedList<>();
    }

    // Add request to queue
    public void addRequest(Reservation reservation) {

        bookingQueue.offer(reservation);

        System.out.println("\nBooking request received from "
                + reservation.getGuestName());
    }

    // Display queued requests
    public void displayQueue() {

        System.out.println("\n--- Current Booking Queue (FIFO) ---");

        for (Reservation r : bookingQueue) {

            r.displayReservation();
            System.out.println("---------------------------");

        }
    }

    // Peek next request (without removing)
    public Reservation peekNextRequest() {
        return bookingQueue.peek();
    }
}


/* ---------- Application Entry ---------- */

public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Welcome to Book My Stay App ");
        System.out.println(" Hotel Booking System v5.0 ");
        System.out.println("====================================");

        // Initialize booking queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Guest booking requests
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        // Add requests to queue (FIFO order)
        requestQueue.addRequest(r1);
        requestQueue.addRequest(r2);
        requestQueue.addRequest(r3);

        // Display queue
        requestQueue.displayQueue();

        // Show next request to be processed
        System.out.println("\nNext request to process:");

        Reservation next = requestQueue.peekNextRequest();

        if (next != null) {
            next.displayReservation();
        }

        System.out.println("\nNote: No inventory updates performed at this stage.");
    }
}