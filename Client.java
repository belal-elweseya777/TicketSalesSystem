import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Ticket Sales Client (Presentation 3) ===");

        while (true) {
            System.out.println("\n1. View All Tickets");
            System.out.println("2. Book a Ticket");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            if (choice == 4) break;

            String command = "";
            if (choice == 1) {
                command = "VIEW";
            } else if (choice == 2) {
                System.out.print("Enter Ticket ID: ");
                command = "BOOK " + scanner.nextInt();
            } else if (choice == 3) {
                System.out.print("Enter Ticket ID: ");
                command = "CANCEL " + scanner.nextInt();
            }

            try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println(command);
                String response = in.readLine();

                if (choice == 1 && response != null) {
                    String[] tickets = response.split(";");
                    for (String t : tickets) {
                        System.out.println(t);
                    }
                } else {
                    System.out.println("Server Response: " + response);
                }

            } catch (IOException e) {
                System.out.println("Error connecting to server: " + e.getMessage());
            }
        }
        System.out.println("Client closed.");
        scanner.close();
    }
}