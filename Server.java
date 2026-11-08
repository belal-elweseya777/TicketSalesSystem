import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 5000;
    private static TicketManager manager = new TicketManager();

    public static void main(String[] args) {
        System.out.println("Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String request;
                while ((request = in.readLine()) != null) {
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
                        out.println(success ? "SUCCESS" : "FAILED");
                    } else if (command.equalsIgnoreCase("CANCEL")) {
                        int id = Integer.parseInt(parts[1]);
                        boolean success = manager.cancelBooking(id);
                        out.println(success ? "SUCCESS" : "FAILED");
                    }
                }
            } catch (IOException e) {
            }
        }
    }
}