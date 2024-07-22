package InvoiceBillingSystem;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;

public class ViewInvoiceByIdGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField invoiceIdTextField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ViewInvoiceByIdGUI frame = new ViewInvoiceByIdGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ViewInvoiceByIdGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Panel for input (Invoice ID and buttons)
        JPanel inputPanel = new JPanel();
        contentPane.add(inputPanel, BorderLayout.NORTH);
        inputPanel.setLayout(new FlowLayout());

        JLabel lblInvoiceId = new JLabel("Invoice ID:");
        inputPanel.add(lblInvoiceId);

        invoiceIdTextField = new JTextField();
        invoiceIdTextField.setColumns(10);
        inputPanel.add(invoiceIdTextField);

        JButton btnLoad = new JButton("Load Invoice");
        btnLoad.addActionListener(e -> loadInvoice());
        inputPanel.add(btnLoad);

        JButton btnExit = new JButton("Exit");
        btnExit.addActionListener(e -> dispose());
        inputPanel.add(btnExit);

        // Panel for displaying invoice details and items
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        contentPane.add(displayPanel, BorderLayout.CENTER);

        // Invoice details area
        JTextArea invoiceDetailsArea = new JTextArea();
        invoiceDetailsArea.setEditable(false);
        invoiceDetailsArea.setPreferredSize(new Dimension(780, 150));
        JScrollPane invoiceScrollPane = new JScrollPane(invoiceDetailsArea);
        displayPanel.add(invoiceScrollPane);

        // Table for items
        String[] columnNames = {"Item Name", "Quantity", "Unit", "Rate", "Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(780, 200));
        JScrollPane tableScrollPane = new JScrollPane(table);
        displayPanel.add(tableScrollPane);

        // Summary area
        JTextArea summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setPreferredSize(new Dimension(780, 100));
        JScrollPane summaryScrollPane = new JScrollPane(summaryArea);
        displayPanel.add(summaryScrollPane);
    }

    private void loadInvoice() {
        String invoiceIdStr = invoiceIdTextField.getText().trim();
        if (invoiceIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Invoice ID is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int invoiceId = Integer.parseInt(invoiceIdStr);

            try (Connection connection = DataBaseConnection.getConnection()) {
                String query = "SELECT i.InvoiceID, i.CustomerID, i.Date, i.SubTotal, i.Discount, i.Total, i.Paid, c.Name, c.Balance " +
                               "FROM Invoices i JOIN Customers c ON i.CustomerID = c.CustomerID WHERE i.InvoiceID = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, invoiceId);
                    ResultSet rs = stmt.executeQuery();

                    JTextArea invoiceDetailsArea = (JTextArea) ((JScrollPane) ((JPanel) contentPane.getComponent(1)).getComponent(0)).getViewport().getView();
                    DefaultTableModel tableModel = (DefaultTableModel) ((JTable) ((JScrollPane) ((JPanel) contentPane.getComponent(1)).getComponent(1)).getViewport().getView()).getModel();
                    JTextArea summaryArea = (JTextArea) ((JScrollPane) ((JPanel) contentPane.getComponent(1)).getComponent(2)).getViewport().getView();

                    if (rs.next()) {
                        int customerId = rs.getInt("CustomerID");
                        String customerName = rs.getString("Name");
                        LocalDate date = rs.getDate("Date").toLocalDate();
                        double subTotal = rs.getDouble("SubTotal");
                        double discount = rs.getDouble("Discount");
                        double total = rs.getDouble("Total");
                        double paid = rs.getDouble("Paid");
                        double balance = rs.getDouble("Balance");

                        StringBuilder invoiceDetails = new StringBuilder();
                        invoiceDetails.append("RAM STORE\n");
                        invoiceDetails.append("Invoice ID: ").append(invoiceId).append("\n");
                        invoiceDetails.append("Customer ID: ").append(customerId).append("\n");
                        invoiceDetails.append("Customer Name: ").append(customerName).append("\n");
                        invoiceDetails.append("Date: ").append(date).append("\n");
                       // invoiceDetails.append("Paid Amount: ").append(String.format("%.2f", paid)).append("\n");

                        invoiceDetailsArea.setText(invoiceDetails.toString());

                        // Fetch and display invoice details (items)
                        String detailQuery = "SELECT d.Quantity, i.ItemName, i.Unit, i.Rate, d.Price " +
                                             "FROM InvoiceDetails d JOIN Items i ON d.ItemID = i.ItemID WHERE d.InvoiceID = ?";
                        try (PreparedStatement detailStmt = connection.prepareStatement(detailQuery)) {
                            detailStmt.setInt(1, invoiceId);
                            ResultSet detailRs = detailStmt.executeQuery();

                            tableModel.setRowCount(0); // Clear existing rows
                            while (detailRs.next()) {
                                String itemName = detailRs.getString("ItemName");
                                int quantity = detailRs.getInt("Quantity");
                                String unit = detailRs.getString("Unit");
                                double rate = detailRs.getDouble("Rate");
                                double price = detailRs.getDouble("Price");

                                Object[] row = {itemName, quantity, unit, rate, price};
                                tableModel.addRow(row);
                            }

                            // Create the summary details
                            StringBuilder summary = new StringBuilder();
                            summary.append("SubTotal: ").append(String.format("%.2f", subTotal)).append("\n");
                            summary.append("Discount: ").append(String.format("%.0f", discount)).append("%\n");
                            summary.append("Total: ").append(String.format("%.2f", total)).append("\n");
                            summary.append("Paid: ").append(String.format("%.2f", paid)).append("\n");
                            summary.append("Customer Balance: ").append(String.format("%.2f", balance)).append("\n");
                            summary.append("Thank you for your purchase!");

                            summaryArea.setText(summary.toString());
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Invoice not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Invoice ID format.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
