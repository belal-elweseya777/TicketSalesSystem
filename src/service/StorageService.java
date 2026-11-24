package service;

import model.Booking;
import model.Ticket;
import java.util.List;

public interface StorageService {
    List<Ticket> loadTickets();
    void saveTickets(List<Ticket> tickets);
    void logBooking(Booking booking);
}