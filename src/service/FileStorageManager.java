package service;

import model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorageManager implements StorageService {
    private static final String TICKETS_FILE = "tickets.txt";
    private static final String BOOKINGS_LOG = "bookings_history.log";

    @Override
    public List<Ticket> loadTickets() {
        List<Ticket> list = new ArrayList<>();
        File file = new File(TICKETS_FILE);

        if (!file.exists()) {
            // 9 Cinema Tickets (101 - 109)
            list.add(new CinemaTicket(101, "Inception IMAX", 20.0, "Hall A", false));
            list.add(new CinemaTicket(102, "Interstellar 3D", 22.0, "Hall B", false));
            list.add(new CinemaTicket(103, "Oppenheimer 70mm", 25.0, "Hall A", false));
            list.add(new CinemaTicket(104, "The Dark Knight", 18.0, "Hall C", false));
            list.add(new CinemaTicket(105, "Dune: Part Two", 24.0, "IMAX 2", false));
            list.add(new CinemaTicket(106, "Avatar: Way of Water", 26.0, "IMAX 1", false));
            list.add(new CinemaTicket(107, "The Matrix", 17.0, "Hall D", false));
            list.add(new CinemaTicket(108, "Parasite", 19.0, "Hall B", false));
            list.add(new CinemaTicket(109, "Mad Max: Fury Road", 21.0, "Hall C", false));

            // 8 Match Tickets (110 - 117)
            list.add(new MatchTicket(110, "Champions League Final", 50.0, "National Stadium", false));
            list.add(new MatchTicket(111, "Derby Cup Match", 40.0, "City Arena", false));
            list.add(new MatchTicket(112, "Premier League Clash", 45.0, "North Stadium", false));
            list.add(new MatchTicket(113, "World Cup Qualifier", 55.0, "Olympic Arena", false));
            list.add(new MatchTicket(114, "Super Cup Final", 60.0, "Grand Stadium", false));
            list.add(new MatchTicket(115, "National Cup Semi-Final", 38.0, "East Stadium", false));
            list.add(new MatchTicket(116, "International Friendly", 35.0, "West Arena", false));
            list.add(new MatchTicket(117, "Continental Cup", 48.0, "Central Stadium", false));

            // 8 VIP Tickets (118 - 125)
            list.add(new VIPTicket(118, "VIP Gala Concert", 100.0, "Backstage + Lounge", false));
            list.add(new VIPTicket(119, "VIP Box Match Pass", 120.0, "Royal Box", false));
            list.add(new VIPTicket(120, "VIP Cinema Premiere", 80.0, "Red Carpet + Dinner", false));
            list.add(new VIPTicket(121, "VIP Symphony Night", 95.0, "Front Row + Lounge", false));
            list.add(new VIPTicket(122, "VIP Championship Suite", 150.0, "Skyline Suite", false));
            list.add(new VIPTicket(123, "VIP Jazz Festival", 85.0, "VIP Terrace", false));
            list.add(new VIPTicket(124, "VIP Comedy Gala", 75.0, "Platinum Table", false));
            list.add(new VIPTicket(125, "VIP Grand Finale Pass", 180.0, "All-Access Suite", false));

            saveTickets(list);
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String type = parts[0];
                    int id = Integer.parseInt(parts[1]);
                    String title = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    String extra = parts[4];
                    boolean booked = parts[5].equals("BOOKED");
                    String cName = parts.length > 6 ? parts[6] : "None";
                    String cPhone = parts.length > 7 ? parts[7] : "None";

                    Ticket t = null;
                    switch (type) {
                        case "CINEMA": t = new CinemaTicket(id, title, price, extra, booked); break;
                        case "MATCH":  t = new MatchTicket(id, title, price, extra, booked); break;
                        case "VIP":    t = new VIPTicket(id, title, price, extra, booked); break;
                    }
                    if (t != null) {
                        t.setCustomerInfo(cName, cPhone);
                        list.add(t);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tickets: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void saveTickets(List<Ticket> tickets) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(TICKETS_FILE))) {
            for (Ticket t : tickets) {
                pw.println(t.toFileFormat());
            }
        } catch (IOException e) {
            System.err.println("Error saving tickets: " + e.getMessage());
        }
    }

    @Override
    public void logBooking(Booking booking) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BOOKINGS_LOG, true))) {
            pw.println(booking.toLogLine());
        } catch (IOException e) {
            System.err.println("Error logging booking: " + e.getMessage());
        }
    }
}