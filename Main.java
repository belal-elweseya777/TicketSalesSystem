import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

class Client extends JFrame {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 5000;

    private JTextArea displayArea;
    private JTextField idField;

    public Client() {
        setTitle("Ticket Sales System - App");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("TICKET BOOKING SYSTEM", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(25, 42, 86)); // Dark Blue
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setPreferredSize(new Dimension(650, 50));
        add(headerLabel, BorderLayout.NORTH);

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        displayArea.setBackground(new Color(245, 246, 250));
        displayArea.setForeground(new Color(47, 53, 66));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        controlPanel.setBackground(new Color(220, 221, 225));

        JButton viewBtn = new JButton("Refresh / View");
        viewBtn.setBackground(new Color(52, 152, 219));
        viewBtn.setForeground(Color.WHITE);

        JLabel idLabel = new JLabel("Enter Ticket ID (101-120):");
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

        // Button Actions
        viewBtn.addActionListener(e -> sendRequest("VIEW"));
        bookBtn.addActionListener(e -> {
            if (!idField.getText().trim().isEmpty()) {
                sendRequest("BOOK " + idField.getText().trim());
            }
        });
        cancelBtn.addActionListener(e -> {
            if (!idField.getText().trim().isEmpty()) {
                sendRequest("CANCEL " + idField.getText().trim());
            }
        });

        sendRequest("VIEW");
    }

    private void sendRequest(String command) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(command);
            String response = in.readLine();

            if (command.startsWith("VIEW") && response != null) {
                displayArea.setText("");
                String[] tickets = response.split(";");
                for (String t : tickets) {
                    displayArea.append(" " + t + "\n-----------------------------------------------------\n");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Server Response: " + response);
                sendRequest("VIEW");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Server is not running!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Client().setVisible(true));
    }
}