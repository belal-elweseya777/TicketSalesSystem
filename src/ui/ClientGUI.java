package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientGUI extends JFrame {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField;
    private JLabel statsLabel;

    public ClientGUI() {
        setTitle("Ticket Sales System - OOP Edition");
        setSize(860, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(241, 245, 249));

        // Top Bar (Dark Slate + Warm Gold accent)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(15, 23, 42));
        topPanel.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JLabel titleLabel = new JLabel("Ticket Sales & Reservation System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        statsLabel = new JLabel("Available: 0 | Booked: 0");
        statsLabel.setForeground(new Color(251, 191, 36));
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(statsLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Category", "Event Title", "Final Price ($)", "Details", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(224, 231, 255));
        table.setSelectionForeground(new Color(15, 23, 42));
        table.setGridColor(new Color(226, 232, 240));

        table.getTableHeader().setBackground(new Color(51, 65, 85));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFoc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSel, hasFoc, row, col);
                String status = val != null ? val.toString() : "";
                c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                if ("BOOKED".equals(status)) {
                    c.setForeground(new Color(185, 28, 28));
                } else {
                    c.setForeground(new Color(4, 120, 87));
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Controls
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        bottomPanel.setBackground(new Color(241, 245, 249));

        JLabel idLbl = new JLabel("Ticket ID:");
        idLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLbl.setForeground(new Color(30, 41, 59));
        bottomPanel.add(idLbl);

        idField = new JTextField(8);
        idField.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bottomPanel.add(idField);

        JButton viewBtn = new JButton("View Details / Refresh");
        JButton bookBtn = new JButton("Book Ticket");
        JButton cancelBtn = new JButton("Cancel Booking");

        styleButton(viewBtn, new Color(13, 148, 136));
        styleButton(bookBtn, new Color(79, 70, 229));
        styleButton(cancelBtn, new Color(220, 38, 38));

        bottomPanel.add(viewBtn);
        bottomPanel.add(bookBtn);
        bottomPanel.add(cancelBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                idField.setText(tableModel.getValueAt(row, 0).toString());
            }
        });

        viewBtn.addActionListener(e -> handleViewOrRefresh());
        bookBtn.addActionListener(e -> handleBook());
        cancelBtn.addActionListener(e -> handleCancel());

        connectToServer();
        loadTickets();
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot connect to Server! Start Server.java first.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTickets() {
        if (out == null) return;
        try {
            out.println("VIEW");
            String response = in.readLine();
            tableModel.setRowCount(0);
            int avail = 0, booked = 0;

            if (response != null && !response.isEmpty()) {
                for (String item : response.split(";")) {
                    String[] p = item.split(",");
                    if (p.length == 6) {
                        tableModel.addRow(new Object[]{p[0], p[1], p[2], "$" + p[3], p[4], p[5]});
                        if ("BOOKED".equals(p[5])) booked++; else avail++;
                    }
                }
            }
            statsLabel.setText("Available: " + avail + " | Booked: " + booked);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading from server.");
        }
    }

    private void handleViewOrRefresh() {
        loadTickets();
        String id = idField.getText().trim();
        if (!id.isEmpty()) {
            sendCommand("INFO " + id);
        } else {
            JOptionPane.showMessageDialog(this, "Ticket list refreshed! Select a Ticket ID to view booking details.", "Refreshed", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleBook() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select or enter a Ticket ID.");
            return;
        }
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        Object[] form = {"Customer Name:", nameField, "Phone Number:", phoneField};

        int opt = JOptionPane.showConfirmDialog(this, form, "Customer Details for Ticket #" + id, JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim().isEmpty() ? "Customer" : nameField.getText().trim();
            String phone = phoneField.getText().trim().isEmpty() ? "0000" : phoneField.getText().trim();
            sendCommand("BOOK " + id + "|" + name + "|" + phone);
        }
    }

    private void handleCancel() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select or enter a Ticket ID.");
            return;
        }
        sendCommand("CANCEL " + id);
    }

    private void sendCommand(String cmd) {
        try {
            out.println(cmd);
            String res = in.readLine();
            if (res == null) return;

            if (res.startsWith("SUCCESS|")) {
                JOptionPane.showMessageDialog(this, res.substring(8), "Success", JOptionPane.INFORMATION_MESSAGE);
                loadTickets();
            } else if (res.startsWith("INFO_OK|")) {
                String formatted = res.substring(8).replace("::", "\n");
                JOptionPane.showMessageDialog(this, formatted, "Ticket Booking Details", JOptionPane.INFORMATION_MESSAGE);
            } else if (res.startsWith("ERROR|")) {
                JOptionPane.showMessageDialog(this, res.substring(6), "Notice", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Communication error with server.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientGUI().setVisible(true));
    }
}