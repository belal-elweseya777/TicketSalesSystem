import java.io.*;
import java.util.*;

public class TicketManager {
    private List<Ticket> tickets = new ArrayList<>();
    private final String filePath = "tickets.txt";

    public TicketManager() {
        loadTickets();
    }

    private void loadTickets() {
        File file = new File(filePath);
        if (!file.exists()) {
            generateDefaultTickets();
            saveTickets();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    tickets.add(Ticket.fromString(line));
                }
            }
        } catch (IOException e) {
            generateDefaultTickets();
        }
    }

    private void generateDefaultTickets() {
        tickets.clear();
        for (int i = 101; i <= 120; i++) {
            tickets.add(new Ticket(i, "Match Ticket #" + (i - 100), 25.0));
        }
    }

    public synchronized List<Ticket> getAllTickets() {
        return tickets;
    }

    public synchronized boolean bookTicket(int id) {
        for (Ticket ticket : tickets) {
            if (ticket.getId() == id && !ticket.isBooked()) {
                ticket.setBooked(true);
                saveTickets();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean cancelBooking(int id) {
        for (Ticket ticket : tickets) {
            if (ticket.getId() == id && ticket.isBooked()) {
                ticket.setBooked(false);
                saveTickets();
                return true;
            }
        }
        return false;
    }

    private void saveTickets() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Ticket ticket : tickets) {
                writer.println(ticket.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}