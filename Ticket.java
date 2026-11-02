public class Ticket {
    private int id;
    private String eventName;
    private double price;
    private boolean isBooked;

    public Ticket(int id, String eventName, double price) {
        this.id = id;
        this.eventName = eventName;
        this.price = price;
        this.isBooked = false;
    }

    public int getId() { return id; }
    public String getEventName() { return eventName; }
    public double getPrice() { return price; }
    public boolean isBooked() { return isBooked; }

    public void setBooked(boolean booked) { isBooked = booked; }

    @Override
    public String toString() {
        return "Ticket ID: " + id + " | Event: " + eventName + " | Price: $" + price + " | Status: " + (isBooked ? "BOOKED" : "AVAILABLE");
    }
}