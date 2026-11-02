public class TicketTest {
    public static void main(String[] args) {
        System.out.println("=== Running Unit Tests for Presentation 5 ===");

        TicketManager manager = new TicketManager();

        // 1. Test Ticket Booking
        boolean bookSuccess = manager.bookTicket(101);
        if (bookSuccess) {
            System.out.println("[TEST PASSED] Ticket 101 booked successfully.");
        } else {
            System.out.println("[TEST FAILED] Could not book ticket 101.");
        }

        // 2. Test Double Booking (Should fail)
        boolean doubleBookSuccess = manager.bookTicket(101);
        if (!doubleBookSuccess) {
            System.out.println("[TEST PASSED] Prevented double booking for ticket 101.");
        } else {
            System.out.println("[TEST FAILED] Allowed double booking!");
        }

        // 3. Test Booking Cancellation
        boolean cancelSuccess = manager.cancelBooking(101);
        if (cancelSuccess) {
            System.out.println("[TEST PASSED] Ticket 101 cancelled successfully.");
        } else {
            System.out.println("[TEST FAILED] Could not cancel ticket 101.");
        }

        System.out.println("=== All Unit Tests Executed ===");
    }
}