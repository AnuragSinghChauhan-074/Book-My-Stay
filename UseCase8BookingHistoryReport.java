import java.util.*;

/**
 * Use Case 8: Booking History & Reporting
 * Demonstrates storing confirmed reservations and generating reports.
 *
 * @version 8.0
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
        System.out.println("Guest Name     : " + guestName);
        System.out.println("Room Type      : " + roomType);
        System.out.println("-----------------------------");
    }
}


/* ---------- Booking History ---------- */

class BookingHistory {

    // Stores confirmed bookings in order
    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        System.out.println("Reservation stored in booking history: "
                + reservation.getReservationId());
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }
}


/* ---------- Booking Report Service ---------- */

class BookingReportService {

    public void generateReport(List<Reservation> reservations) {

        System.out.println("\n========== BOOKING REPORT ==========");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation reservation : reservations) {
            reservation.displayReservation();
        }

        System.out.println("Total Bookings : " + reservations.size());
        System.out.println("====================================");
    }
}


/* ---------- Application Entry ---------- */

public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("  Book My Stay App - Version 8.0");
        System.out.println("  Booking History & Reporting");
        System.out.println("====================================");

        // Booking history storage
        BookingHistory history = new BookingHistory();

        // Example confirmed reservations
        Reservation r1 = new Reservation("R101", "Alice", "Single Room");
        Reservation r2 = new Reservation("R102", "Bob", "Double Room");
        Reservation r3 = new Reservation("R103", "Charlie", "Suite Room");

        // Add reservations to history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin generates report
        BookingReportService reportService = new BookingReportService();

        reportService.generateReport(history.getAllReservations());
    }
}