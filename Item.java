package InvoiceBillingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Item {
	private int itemId;
    private String itemName;
    private String unit;
    private double rate;

    public Item(String itemName, String unit, double rate) {
        this.itemName = itemName;
        this.unit = unit;
        this.rate = rate;
    }

    public Item(int itemId, String itemName, String unit, double rate) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.unit = unit;
        this.rate = rate;
    }
  


    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getUnit() {
        return unit;
    }

    public double getRate() {
        return rate;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    // Create item method
    public void createItem(Connection connection) throws SQLException {
        String query = "INSERT INTO Items (ItemName, Unit, Rate) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, this.itemName);
            stmt.setString(2, this.unit);
            stmt.setDouble(3, this.rate);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                this.itemId = rs.getInt(1);
            }
        }
    }
    public static Item getItemIdByItemName(Connection connection, String itemName) throws SQLException {
        String query = "SELECT * FROM Items WHERE ItemName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, itemName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Item(rs.getInt("ItemID"), rs.getString("ItemName"),
                        rs.getString("Unit"), rs.getDouble("Rate"));
            }
        }
        return null;
    }

    public void updateItem(Connection connection) throws SQLException {
        String query = "UPDATE Items SET Unit = ?, Rate = ? WHERE ItemID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, this.unit);
            stmt.setDouble(2, this.rate);
            stmt.setInt(3, this.itemId);
            stmt.executeUpdate();
        }
    }
    public static void deleteItem(Connection connection, int invoiceId, int itemId) throws SQLException {
        String query = "DELETE FROM InvoiceDetails WHERE InvoiceID = ? AND ItemID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, invoiceId);
            stmt.setInt(2, itemId);
            stmt.executeUpdate();
        }
    }
    // View item sales method
    public static void viewItemSales(Connection connection) throws SQLException {
        String query = "SELECT i.ItemName, i.Unit, SUM(d.Quantity) AS TotalQuantity, SUM(d.Price) AS TotalSales " +
                "FROM Items i JOIN InvoiceDetails d ON i.ItemID = d.ItemID " +
                "GROUP BY i.ItemID ORDER BY TotalSales DESC";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("Item Sales Report:");
            System.out.printf("%-20s %-10s %-15s %-15s\n", "Item Name", "Unit", "Total Quantity", "Total Sales");
            while (rs.next()) {
                System.out.printf("%-20s %-10s %-15d %-15.2f\n",
                        rs.getString("ItemName"), rs.getString("Unit"), rs.getInt("TotalQuantity"), rs.getDouble("TotalSales"));
            }
        }
    }
}
