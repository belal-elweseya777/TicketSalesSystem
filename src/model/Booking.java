package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Booking {
    private Ticket ticket;
    private Customer customer;
    private String bookingTime;

    public Booking(Ticket ticket, Customer customer) {
        this.ticket = ticket;
        this.customer = customer;
        this.bookingTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String toLogLine() {
        return bookingTime + " | Ticket #" + ticket.getId() + " (" + ticket.getTicketType() + ") | Price: $"
                + ticket.calculateFinalPrice() + " | Customer: " + customer.getName() + " (" + customer.getPhone() + ")";
    }
}