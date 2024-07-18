package InvoiceBillingSystem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Connection connection = DataBaseConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("Invoice Billing System");
                System.out.println("1. Create Invoice");
                System.out.println("2. View All Invoices");
                System.out.println("3. View Invoice by ID");
                System.out.println("4. View Customer Balance");
                System.out.println("5. View Item Sales");
                System.out.println("6. Create Item");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();

                switch (option) {
                    case 1:
                        InvoiceOperations.createInvoice(connection, scanner);
                        break;
                    case 2:
                        Invoice.viewAllInvoices(connection);
                        break;
                    case 3:
                        System.out.print("Enter Invoice ID: ");
                        int invoiceId = scanner.nextInt();
                        Invoice.viewInvoiceByID(connection, invoiceId);
                        break;
                    case 4:
                        Customer.viewCustomerBalance(connection);
                        break;
                    case 5:
                        Item.viewItemSales(connection);
                        break;
                    case 6:
                    	 InvoiceOperations.createItem(connection, scanner);
                        break;
                    case 7:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
