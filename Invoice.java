package InvoiceBillingSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {

    private int invoiceId;
    private int customerId;
    private double subTotal;
    private double discount;
    private double total;
    private LocalDate currentDate;

    public Invoice(int customerId, double subTotal, double discount, double total) {
        this.customerId = customerId;
        this.subTotal = subTotal;
        this.discount = discount;
        this.total = total;
        this.currentDate = LocalDate.now();
    }

    // Getters
    public int getInvoiceId() {
        return invoiceId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public LocalDate getDate() {
        return currentDate;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTotal() {
        return total;
    }
    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    // Create invoice method
    public void createInvoice(Connection connection) throws SQLException {
        String query = "INSERT INTO Invoices (CustomerID, Date, SubTotal, Discount, Total) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, this.customerId);
            stmt.setDate(2, java.sql.Date.valueOf(this.currentDate));
            stmt.setDouble(3, this.subTotal);
            stmt.setDouble(4, this.discount);
            stmt.setDouble(5, this.total);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                this.invoiceId = rs.getInt(1);
            }
            //System.out.println("Invoice created successfully.");
        }
    }
    public void createInvoiceDetail(Connection connection,Item item, int quantity, double price) throws SQLException {
        String query = "INSERT INTO InvoiceDetails (invoiceid, ItemID, Quantity, Price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, this.invoiceId);
            stmt.setInt(2, item.getItemId()); // Assuming Item class has a method to get ItemID
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 1) {
               // System.out.println("Invoice detail added successfully.");
            } else {
                System.out.println("Failed to add invoice detail.");
            }
        }
    }
 // Update invoice detail method
    public static void updateInvoiceDetail(Connection connection, int invoiceId, int itemId, int quantity, double price) throws SQLException {
        String query = "UPDATE InvoiceDetails SET Quantity = ?, Price = ? WHERE InvoiceID = ? AND ItemID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setDouble(2, price);
            stmt.setInt(3, invoiceId);
            stmt.setInt(4, itemId);
            stmt.executeUpdate();
        }
    }

    // Delete invoice detail method
    public static void deleteInvoiceDetail(Connection connection, int invoiceId, int itemId) throws SQLException {
        String query = "DELETE FROM InvoiceDetails WHERE InvoiceID = ? AND ItemID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, invoiceId);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
        }
    }
 // Inside Invoice class
    public static double updateInvoiceTotal(Connection connection, int invoiceId, double discount) throws SQLException {
        String query = "UPDATE Invoices SET SubTotal=(SELECT SUM(Price) FROM InvoiceDetails WHERE InvoiceID = ?),Total = (SELECT SUM(Price) FROM InvoiceDetails WHERE InvoiceID = ?) - ? WHERE InvoiceID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
        	stmt.setInt(1, invoiceId);
            stmt.setInt(2, invoiceId);
            stmt.setDouble(3, discount);
            stmt.setInt(4, invoiceId);
            stmt.executeUpdate();

            String totalQuery = "SELECT Total FROM Invoices WHERE InvoiceID = ?";
            try (PreparedStatement totalStmt = connection.prepareStatement(totalQuery)) {
                totalStmt.setInt(1, invoiceId);
                ResultSet rs = totalStmt.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("Total");
                }
            }
        }
        return 0;
    }
    


    // View all invoices method
    public static void viewAllInvoices(Connection connection) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT * FROM Invoices";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice(
                    rs.getInt("CustomerID"),
                    rs.getDouble("SubTotal"),
                    rs.getDouble("Discount"),
                    rs.getDouble("Total")
                );
                invoice.invoiceId = rs.getInt("InvoiceID");
                invoices.add(invoice);
            }
        }
        if (invoices.isEmpty()) {
            System.out.println("No invoices found.");
        } else {
            System.out.println("List of Invoices:");
            for (Invoice invoice : invoices) {
                System.out.printf("Invoice ID: %d, Customer ID: %d, Date: %s, Total: %.2f\n",
                        invoice.getInvoiceId(), invoice.getCustomerId(),
                        invoice.getDate(), invoice.getTotal());
            }
        }
    }

    // View invoice by ID method
    public static void viewInvoiceByID(Connection connection, int invoiceId) {
        try {
            String query = "SELECT i.InvoiceID, i.CustomerID, i.Date, i.SubTotal, i.Discount, i.Total, c.Name, c.Balance " +
                           "FROM Invoices i JOIN Customers c ON i.CustomerID = c.CustomerID WHERE i.InvoiceID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, invoiceId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int customerId = rs.getInt("CustomerID");
                    String customerName = rs.getString("Name");
                    LocalDate date = rs.getDate("Date").toLocalDate();
                    double subTotal = rs.getDouble("SubTotal");
                    double discount = rs.getDouble("Discount");
                    double total = rs.getDouble("Total");
                    double balance = rs.getDouble("Balance");
                    System.out.println("-----------------------------------------------------------------------------");
                    System.out.println("\t\t RAMU STORE");

                    System.out.printf("Invoice ID: %d\nCustomer ID: %d\nCustomer Name: %s\nDate: %s\n\n",
                            invoiceId, customerId, customerName, date);


                    // Fetch and display invoice details (items)
                    String detailQuery = "SELECT d.Quantity, i.ItemName, i.Unit, i.Rate, d.Price " +
                                         "FROM InvoiceDetails d JOIN Items i ON d.ItemID = i.ItemID WHERE d.InvoiceID = ?";
                    try (PreparedStatement detailStmt = connection.prepareStatement(detailQuery)) {
                        detailStmt.setInt(1, invoiceId);
                        ResultSet detailRs = detailStmt.executeQuery();
                        System.out.printf("%-20s %-10s %-10s %-10s %-10s\n", "Item Name", "Quantity", "Unit", "Rate", "Price");
                     System.out.print("-----------------------------------------------------------------------------");
                        while (detailRs.next()) {
                            String itemName = detailRs.getString("ItemName");
                            int quantity = detailRs.getInt("Quantity");
                            String unit = detailRs.getString("Unit");
                            double rate = detailRs.getDouble("Rate");
                            double price = detailRs.getDouble("Price");
                            System.out.printf("%-20s %-10d %-10s %-10.2f %-10.2f\n",
                                    itemName, quantity, unit, rate, price);
                        }
                        // Print summary
                        System.out.printf("\nSubTotal: %.2f\nDiscount: %.2f\nTotal: %.2f\nCustomer Balance: %.2f\n",
                                subTotal, discount, total, balance);
                    }
                } else {
                    System.out.println("Invoice not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}

