import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 5000;

    private JTextArea displayArea;
    private JTextField idField;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client() {
        setTitle("Ticket Sales System - App");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel headerLabel = new JLabel("TICKET BOOKING SYSTEM", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(25, 42, 86));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setPreferredSize(new Dimension(650, 50));
        add(headerLabel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        displayArea.setBackground(new Color(245, 246, 250));
        displayArea.setForeground(new Color(47, 53, 66));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        controlPanel.setBackground(new Color(220, 221, 225));

        JButton viewBtn = new JButton("Search / Refresh");
        viewBtn.setBackground(new Color(52, 152, 219));
        viewBtn.setForeground(Color.WHITE);

        JLabel idLabel = new JLabel("Enter Ticket ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 12));

        idField = new JTextField(6);
        idField.setFont(new Font("Arial", Font.BOLD, 14));

        JButton bookBtn = new JButton("Book Ticket");
        bookBtn.setBackground(new Color(46, 204, 113));
        bookBtn.setForeground(Color.WHITE);

        JButton cancelBtn = new JButton("Cancel Ticket");
        cancelBtn.setBackground(new Color(231, 76, 60));
        cancelBtn.setForeground(Color.WHITE);

        controlPanel.add(viewBtn);
        controlPanel.add(idLabel);
        controlPanel.add(idField);
        controlPanel.add(bookBtn);
        controlPanel.add(cancelBtn);

        add(controlPanel, BorderLayout.SOUTH);

        connectToServer();

        viewBtn.addActionListener(e -> {
            String inputId = idField.getText().trim();
            sendCommand("VIEW", inputId);
        });

        bookBtn.addActionListener(e -> {
            if (!idField.getText().trim().isEmpty()) {
                sendCommand("BOOK " + idField.getText().trim(), "");
            }
        });

        cancelBtn.addActionListener(e -> {
            if (!idField.getText().trim().isEmpty()) {
                sendCommand("CANCEL " + idField.getText().trim(), "");
            }
        });

        sendCommand("VIEW", "");
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Server is not running!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendCommand(String command, String searchId) {
        try {
            if (socket == null || socket.isClosed()) {
                connectToServer();
            }

            out.println(command);
            String response = in.readLine();

            if (command.startsWith("VIEW") && response != null) {
                displayArea.setText("");
                String[] tickets = response.split(";");
                String foundStatus = null;

                for (String t : tickets) {
                    displayArea.append(" " + t + "\n-----------------------------------------------------\n");
                    
                    if (!searchId.isEmpty() && t.contains("Ticket ID: " + searchId + " ")) {
                        if (t.contains("Status: BOOKED")) {
                            foundStatus = "Ticket #" + searchId + " is BOOKED";
                        } else if (t.contains("Status: AVAILABLE")) {
                            foundStatus = "Ticket #" + searchId + " is AVAILABLE";
                        }
                    }
                }

                if (!searchId.isEmpty()) {
                    if (foundStatus != null) {
                        JOptionPane.showMessageDialog(this, foundStatus, "Ticket Status", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Ticket ID #" + searchId + " Not Found!", "Search Result", JOptionPane.WARNING_MESSAGE);
                    }
                }

            } else if (response != null) {
                JOptionPane.showMessageDialog(this, "Server Response: " + response);
                sendCommand("VIEW", "");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Connection lost to server!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Client().setVisible(true));
    }
}