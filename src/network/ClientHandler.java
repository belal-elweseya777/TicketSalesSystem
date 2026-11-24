package network;

import exception.BookingException;
import model.Customer;
import model.Ticket;
import service.TicketService;
import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private TicketService ticketService;

    public ClientHandler(Socket socket, TicketService ticketService) {
        this.socket = socket;
        this.ticketService = ticketService;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String request;
            while ((request = in.readLine()) != null) {
                if (request.equals("VIEW")) {
                    StringBuilder sb = new StringBuilder();
                    for (Ticket t : ticketService.getAllTickets()) {
                        sb.append(t.getId()).append(",")
                          .append(t.getTicketType()).append(",")
                          .append(t.getTitle()).append(",")
                          .append(t.calculateFinalPrice()).append(",")
                          .append(t.getExtraInfo()).append(",")
                          .append(t.isBooked() ? "BOOKED" : "AVAILABLE").append(";");
                    }
                    out.println(sb.toString());
                } else if (request.startsWith("INFO ")) {
                    try {
                        int id = Integer.parseInt(request.substring(5).trim());
                        Ticket t = ticketService.getTicketById(id);
                        String info = "Ticket #" + t.getId() + " - " + t.getTitle() + " (" + t.getTicketType() + ")"
                                + "::Price: $" + t.calculateFinalPrice() + " | Details: " + t.getExtraInfo()
                                + "::Status: " + (t.isBooked() ? "BOOKED" : "AVAILABLE");
                        if (t.isBooked()) {
                            info += "::Booked By: " + t.getCustomerName() + "::Phone: " + t.getCustomerPhone();
                        }
                        out.println("INFO_OK|" + info);
                    } catch (BookingException e) {
                        out.println("ERROR|" + e.getMessage());
                    } catch (Exception e) {
                        out.println("ERROR|Invalid Ticket ID!");
                    }
                } else if (request.startsWith("BOOK ")) {
                    try {
                        String[] data = request.substring(5).split("\\|");
                        int id = Integer.parseInt(data[0].trim());
                        String name = data.length > 1 ? data[1].trim() : "Guest";
                        String phone = data.length > 2 ? data[2].trim() : "N/A";

                        Ticket booked = ticketService.bookTicket(id, new Customer(name, phone));
                        out.println("SUCCESS|Booked " + booked.getTitle() + " for " + name + " ($" + booked.calculateFinalPrice() + ")");
                    } catch (BookingException e) {
                        out.println("ERROR|" + e.getMessage());
                    } catch (Exception e) {
                        out.println("ERROR|Invalid booking format!");
                    }
                } else if (request.startsWith("CANCEL ")) {
                    try {
                        int id = Integer.parseInt(request.substring(7).trim());
                        ticketService.cancelBooking(id);
                        out.println("SUCCESS|Booking cancelled for Ticket #" + id);
                    } catch (BookingException e) {
                        out.println("ERROR|" + e.getMessage());
                    } catch (Exception e) {
                        out.println("ERROR|Invalid Ticket ID!");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        }
    }
}