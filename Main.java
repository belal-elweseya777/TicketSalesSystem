import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TicketManager manager = new TicketManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Ticket Sales System (Presentation 2) ===");
        
        while (true) {
            System.out.println("\n1. View All Tickets");
            System.out.println("2. Book a Ticket");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            if (choice == 4) break;

            switch (choice) {
                case 1:
                    for (Ticket t : manager.getAllTickets()) {
                        System.out.println(t);
                    }
                    break;
                case 2:
                    System.out.print("Enter Ticket ID to book: ");
                    int bookId = scanner.nextInt();
                    if (manager.bookTicket(bookId)) {
                        System.out.println("Ticket booked successfully!");
                    } else {
                        System.out.println("Booking failed! (Invalid ID or already booked)");
                    }
                    break;
                case 3:
                    System.out.print("Enter Ticket ID to cancel: ");
                    int cancelId = scanner.nextInt();
                    if (manager.cancelBooking(cancelId)) {
                        System.out.println("Booking cancelled successfully!");
                    } else {
                        System.out.println("Cancellation failed! (Invalid ID or not booked)");
                    }
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
        System.out.println("System closed.");
        scanner.close();
    }
}