import java.util.ArrayList;
import java.util.List;

public class TicketManager {
    private List<Ticket> tickets = new ArrayList<>();

    public TicketManager() {
       
        tickets.add(new Ticket(101, "Cinema: Avengers", 15.0));
        tickets.add(new Ticket(102, "Cinema: Avengers", 15.0));
        tickets.add(new Ticket(201, "Concert: Rock Band", 50.0));
    }

    public List<Ticket> getAllTickets() {
        return tickets;
    }

    public boolean bookTicket(int ticketId) {
        for (Ticket t : tickets) {
            if (t.getId() == ticketId && !t.isBooked()) {
                t.setBooked(true);
                return true;
            }
        }
        return false;
    }

    public boolean cancelBooking(int ticketId) {
        for (Ticket t : tickets) {
            if (t.getId() == ticketId && t.isBooked()) {
                t.setBooked(false);
                return true;
            }
        }
        return false;
    }
}