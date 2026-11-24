package model;

public class CinemaTicket extends Ticket {
    private String hallName;

    public CinemaTicket(int id, String title, double basePrice, String hallName, boolean booked) {
        super(id, title, basePrice, booked);
        this.hallName = hallName;
    }

    @Override
    public double calculateFinalPrice() {
        return basePrice;
    }

    @Override
    public String getTicketType() { return "CINEMA"; }

    @Override
    public String getExtraInfo() { return hallName; }
}