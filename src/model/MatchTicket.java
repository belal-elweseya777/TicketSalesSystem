package model;

public class MatchTicket extends Ticket {
    private String stadium;

    public MatchTicket(int id, String title, double basePrice, String stadium, boolean booked) {
        super(id, title, basePrice, booked);
        this.stadium = stadium;
    }

    @Override
    public double calculateFinalPrice() {
        return basePrice + 5.0; // رسوم تنظيم ملعب
    }

    @Override
    public String getTicketType() { return "MATCH"; }

    @Override
    public String getExtraInfo() { return stadium; }
}