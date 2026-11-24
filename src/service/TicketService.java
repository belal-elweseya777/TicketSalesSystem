package service;

import exception.BookingException;
import model.Booking;
import model.Customer;
import model.Ticket;
import java.util.List;

public class TicketService {
    private List<Ticket> tickets;
    private StorageService storage;

    public TicketService(StorageService storage) {
        this.storage = storage;
        this.tickets = storage.loadTickets();
    }

    public synchronized List<Ticket> getAllTickets() {
        return tickets;
    }

    public synchronized Ticket getTicketById(int id) throws BookingException {
        for (Ticket t : tickets) {
            if (t.getId() == id) return t;
        }
        throw new BookingException("Ticket #" + id + " does not exist!");
    }

    public synchronized Ticket bookTicket(int id, Customer customer) throws BookingException {
        Ticket t = getTicketById(id);
        if (t.isBooked()) {
            throw new BookingException("Ticket #" + id + " is already booked by " + t.getCustomerName() + "!");
        }
        t.setBooked(true);
        t.setCustomerInfo(customer.getName(), customer.getPhone());
        storage.saveTickets(tickets);
        storage.logBooking(new Booking(t, customer));
        return t;
    }

    public synchronized void cancelBooking(int id) throws BookingException {
        Ticket t = getTicketById(id);
        if (!t.isBooked()) {
            throw new BookingException("Ticket #" + id + " is not currently booked!");
        }
        t.setBooked(false);
        t.setCustomerInfo("None", "None");
        storage.saveTickets(tickets);
    }
}