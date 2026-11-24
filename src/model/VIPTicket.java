package model;

public class VIPTicket extends Ticket {
    private String perk;

    public VIPTicket(int id, String title, double basePrice, String perk, boolean booked) {
        super(id, title, basePrice, booked);
        this.perk = perk;
    }

    @Override
    public double calculateFinalPrice() {
        return basePrice * 1.5; // زيادة 50% للـ VIP
    }

    @Override
    public String getTicketType() { return "VIP"; }

    @Override
    public String getExtraInfo() { return perk; }
}