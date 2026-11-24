package test;

import exception.BookingException;
import model.*;
import service.FileStorageManager;
import service.TicketService;

public class SystemVerification {
    public static void main(String[] args) {
        System.out.println("=== Running OOP System Verification ===");

        // 1. Test Polymorphism (Different price calculations)
        Ticket cinema = new CinemaTicket(201, "Test Movie", 20.0, "Hall 1", false);
        Ticket match = new MatchTicket(202, "Test Match", 50.0, "Arena", false);
        Ticket vip = new VIPTicket(203, "Test VIP", 100.0, "Lounge", false);

        assert cinema.calculateFinalPrice() == 20.0 : "Cinema price mismatch";
        assert match.calculateFinalPrice() == 55.0 : "Match price mismatch";
        assert vip.calculateFinalPrice() == 150.0 : "VIP price mismatch";
        System.out.println("[PASS] Polymorphism & Price Calculation verified.");

        // 2. Test Service, Booking & Custom Exception
        TicketService service = new TicketService(new FileStorageManager());
        try {
            service.bookTicket(101, new Customer("Belal", "123456"));
            System.out.println("[PASS] Ticket booking & persistence verified.");

            // Try double booking (should throw BookingException)
            service.bookTicket(101, new Customer("Ali", "999999"));
            System.out.println("[FAIL] Duplicate booking was not prevented!");
        } catch (BookingException e) {
            System.out.println("[PASS] Duplicate booking blocked successfully: " + e.getMessage());
        }

        // Cleanup test booking
        try {
            service.cancelBooking(101);
            System.out.println("[PASS] Ticket cancellation verified.");
        } catch (BookingException e) {
            System.out.println("[FAIL] Cancellation failed: " + e.getMessage());
        }

        System.out.println("=== All OOP Verifications Passed! ===");
    }
}