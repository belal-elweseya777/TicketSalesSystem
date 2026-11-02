import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TicketManager {
    private List<Ticket> tickets = new ArrayList<>();
    private final String FILE_NAME = "tickets.txt";

    public TicketManager() {
        loadTicketsFromFile();
    }

    private void loadTicketsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            for (int i = 1; i <= 20; i++) {
                int id = 100 + i;
                String event = (i <= 10) ? "Cinema Ticket #" + i : "Match Ticket #" + i;
                double price = 25.0;
                tickets.add(new Ticket(id, event, price));
            }
            saveTicketsToFile();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            tickets.clear();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String event = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    boolean isBooked = Boolean.parseBoolean(parts[3]);

                    Ticket t = new Ticket(id, event, price);
                    t.setBooked(isBooked);
                    tickets.add(t);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }

    public synchronized void saveTicketsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Ticket t : tickets) {
                writer.println(t.getId() + "," + t.getEventName() + "," + t.getPrice() + "," + t.isBooked());
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public List<Ticket> getAllTickets() {
        return tickets;
    }

    public boolean bookTicket(int ticketId) {
        for (Ticket t : tickets) {
            if (t.getId() == ticketId && !t.isBooked()) {
                t.setBooked(true);
                saveTicketsToFile();
                return true;
            }
        }
        return false;
    }

    public boolean cancelBooking(int ticketId) {
        for (Ticket t : tickets) {
            if (t.getId() == ticketId && t.isBooked()) {
                t.setBooked(false);
                saveTicketsToFile();
                return true;
            }
        }
        return false;
    }
}