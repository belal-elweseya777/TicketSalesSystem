package model;

public abstract class Ticket {
    protected int id;
    protected String title;
    protected double basePrice;
    protected boolean booked;
    protected String customerName = "None";
    protected String customerPhone = "None";

    public Ticket(int id, String title, double basePrice, boolean booked) {
        this.id = id;
        this.title = title;
        this.basePrice = basePrice;
        this.booked = booked;
    }

    public abstract double calculateFinalPrice();
    public abstract String getTicketType();
    public abstract String getExtraInfo();

    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getBasePrice() { return basePrice; }
    public boolean isBooked() { return booked; }
    public void setBooked(boolean booked) { this.booked = booked; }

    public String getCustomerName() { return customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerInfo(String name, String phone) {
        this.customerName = (name == null || name.isEmpty()) ? "None" : name;
        this.customerPhone = (phone == null || phone.isEmpty()) ? "None" : phone;
    }

    public String toFileFormat() {
        return getTicketType() + "," + id + "," + title + "," + basePrice + "," + getExtraInfo() + ","
                + (booked ? "BOOKED" : "AVAILABLE") + "," + customerName + "," + customerPhone;
    }
}