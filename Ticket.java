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

    public int getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public double getPrice() {
        return price;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        this.isBooked = booked;
    }

    @Override
    public String toString() {
        return id + "," + eventName + "," + price + "," + (isBooked ? "BOOKED" : "AVAILABLE");
    }

    public static Ticket fromString(String line) {
        String[] parts = line.split(",");
        Ticket ticket = new Ticket(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]));
        if (parts[3].equalsIgnoreCase("BOOKED")) {
            ticket.setBooked(true);
        }
        return ticket;
    }
}