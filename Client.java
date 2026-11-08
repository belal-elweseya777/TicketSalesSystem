import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 5000;

    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private JTextField idField;
    private JLabel statsLabel;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client() {
        setTitle("Ticket Sales System");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(750, 55));

        JLabel headerTitle = new JLabel("  TICKET BOOKING SYSTEM", JLabel.LEFT);
        headerTitle.setFont(new Font("Arial", Font.BOLD, 18));
        headerTitle.setForeground(Color.WHITE);
        headerPanel.add(headerTitle, BorderLayout.WEST);

        statsLabel = new JLabel("Available: 20 | Booked: 0  ", JLabel.RIGHT);
        statsLabel.setFont(new Font("Arial", Font.BOLD, 13));
        statsLabel.setForeground(new Color(46, 204, 113));
        headerPanel.add(statsLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Event", "Price", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ticketTable = new JTable(tableModel);
        ticketTable.setRowHeight(26);
        ticketTable.setFont(new Font("Arial", Font.PLAIN, 13));
        ticketTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        ticketTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;
                if ("BOOKED".equalsIgnoreCase(status)) {
                    c.setForeground(Color.RED);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setForeground(new Color(39, 174, 96));
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(ticketTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));

        JButton viewBtn = new JButton("View / Search");
        JLabel idLabel = new JLabel("Ticket ID:");
        idField = new JTextField(6);
        JButton bookBtn = new JButton("Book");
        JButton cancelBtn = new JButton("Cancel");

        controlPanel.add(viewBtn);
        controlPanel.add(idLabel);
        controlPanel.add(idField);
        controlPanel.add(bookBtn);
        controlPanel.add(cancelBtn);

        add(controlPanel, BorderLayout.SOUTH);

        connectToServer();

        viewBtn.addActionListener(e -> sendCommand("VIEW", idField.getText().trim()));
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
            JOptionPane.showMessageDialog(this, "Server connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
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
                tableModel.setRowCount(0);
                String[] tickets = response.split(";");
                int available = 0, booked = 0;
                String foundStatus = null;

                for (String tStr : tickets) {
                    if (tStr.trim().isEmpty()) continue;
                    String[] parts = tStr.split(",");
                    String id = parts[0];
                    String event = parts[1];
                    String price = parts[2];
                    String status = parts[3];

                    if ("BOOKED".equalsIgnoreCase(status)) booked++;
                    else available++;

                    tableModel.addRow(new Object[]{id, event, "$" + price, status});

                    if (!searchId.isEmpty() && id.equals(searchId)) {
                        foundStatus = "Ticket #" + id + " is " + status;
                    }
                }

                statsLabel.setText("Available: " + available + " | Booked: " + booked + "  ");

                if (!searchId.isEmpty()) {
                    if (foundStatus != null) {
                        JOptionPane.showMessageDialog(this, foundStatus, "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Ticket ID not found!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else if (response != null) {
                JOptionPane.showMessageDialog(this, "Server Response: " + response);
                sendCommand("VIEW", "");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Connection lost!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Client().setVisible(true));
    }
}