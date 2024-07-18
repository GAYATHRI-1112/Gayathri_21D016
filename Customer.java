package InvoiceBillingSystem;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Customer {

    private int customerId;
    private String name;
    private String mobileNumber;
    private double balance;

    public Customer(String name, String mobileNumber) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.balance = 0; // Default balance
    }

    public Customer(int customerId, String name, String mobileNumber, double balance) {
        this.customerId = customerId;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.balance = balance;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public double getBalance() {
        return balance;
    }

    // View customer balance method
    public static void viewCustomerBalance(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter Customer's Mobile Number: ");
            String mobileNumber = scanner.next();
            Customer customer = Customer.getByMobile(connection, mobileNumber);
            if (customer != null) {
                System.out.printf("Customer Name: %s, Balance: %.2f\n", customer.getName(), customer.getBalance());
            } else {
                System.out.println("Customer not found.Enter custemer name: ");
               customer.name= scanner.nextLine();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create customer method
    public void createCustomer(Connection connection) throws SQLException {
        String query = "INSERT INTO Customers (Name, MobileNumber, Balance) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, this.name);
            stmt.setString(2, this.mobileNumber);
            stmt.setDouble(3, this.balance);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                this.customerId = rs.getInt(1);
            }
        }
    }

    // Get customer by mobile number
    public static Customer getByMobile(Connection connection, String mobile) throws SQLException {
        String query = "SELECT * FROM Customers WHERE MobileNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, mobile);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Customer(rs.getInt("CustomerID"), rs.getString("Name"),
                        rs.getString("MobileNumber"), rs.getDouble("Balance"));
            }
        }
        return null;
    }

    // Update customer balance method
    public void updateBalance(Connection connection, double newBalance) throws SQLException {
        String query = "UPDATE Customers SET Balance = ? WHERE CustomerID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, this.customerId);
            stmt.executeUpdate();
            this.balance = newBalance;
        }
    }
    public static boolean isValidMobileNumber(String mobileNumber) {
        return mobileNumber.matches("\\d{10}"); // Validates 10-digit numbers
    }
    public static boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+"); // Allows only alphabetic characters
    }

}
