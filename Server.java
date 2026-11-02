import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        TicketManager manager = new TicketManager();
        System.out.println("=== Ticket Sales Server Running on Port " + PORT + " ===");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String request = in.readLine();
                if (request != null) {
                    String[] parts = request.split(" ");
                    String command = parts[0];

                    if (command.equalsIgnoreCase("VIEW")) {
                        StringBuilder sb = new StringBuilder();
                        for (Ticket t : manager.getAllTickets()) {
                            sb.append(t.toString()).append(";");
                        }
                        out.println(sb.toString());
                    } else if (command.equalsIgnoreCase("BOOK")) {
                        int id = Integer.parseInt(parts[1]);
                        boolean success = manager.bookTicket(id);
                        out.println(success ? "SUCCESS: Ticket booked" : "FAILED: Invalid ID or already booked");
                    } else if (command.equalsIgnoreCase("CANCEL")) {
                        int id = Integer.parseInt(parts[1]);
                        boolean success = manager.cancelBooking(id);
                        out.println(success ? "SUCCESS: Booking cancelled" : "FAILED: Invalid ID or not booked");
                    } else {
                        out.println("UNKNOWN COMMAND");
                    }
                }
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}