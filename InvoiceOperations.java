package InvoiceBillingSystem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class InvoiceOperations {
    public static void createInvoice(Connection connection, Scanner scanner) {
        try {
            connection.setAutoCommit(false);

            // Get customer's mobile number
            String mobileNumber;
            do {
                System.out.print("Enter Customer's Mobile Number: ");
                mobileNumber = scanner.next();
                if (!Customer.isValidMobileNumber(mobileNumber)) {
                    System.out.println("Invalid mobile number format. Please enter a valid 10-digit number.");
                }
            } while (!Customer.isValidMobileNumber(mobileNumber));

            Customer customer = Customer.getByMobile(connection, mobileNumber);
            int customerId;
            double initialBalance = 0;

            if (customer != null) {
                customerId = customer.getCustomerId();
                initialBalance = customer.getBalance();
            } else {
                String name;
                do {
                    System.out.print("Enter Customer Name: ");
                    name = scanner.next();
                    if (!Customer.isValidName(name)) {
                        System.out.println("Enter a valid name.");
                    }
                } while (!Customer.isValidName(name));

                customer = new Customer(name, mobileNumber);
                customer.createCustomer(connection);
                customerId = customer.getCustomerId();
            }

            //LocalDate currentDate = LocalDate.now();
            System.out.print("Enter Discount: ");
            double discount = scanner.nextDouble();
            Invoice invoice = new Invoice(customerId, 0, discount,0, 0);
            invoice.createInvoice(connection);
            int invoiceId = invoice.getInvoiceId();
            double subTotal = 0;

            while (true) {
                System.out.print("1. Add Item\n2. Update Item\n3. Delete Item\n4. Finish\nChoose an option: ");
                int option = scanner.nextInt();
                if (option == 4) break;

                System.out.print("Enter Item Name: ");
                String itemName = scanner.next();
                Item item = Item.getItemIdByItemName(connection, itemName);
                if (item == null) {
                    System.out.print("Item Name entered is not valid\n");
                } else {
                    int itemId = item.getItemId();
                    switch (option) {
                        case 1:
                            System.out.print("Enter Quantity: ");
                            int quantity = scanner.nextInt();
                            double price = item.getRate() * quantity;                          
                            invoice.createInvoiceDetail(connection, item, quantity, price);
                            break;
                        case 2:
                            System.out.print("Enter Quantity to Update: ");
                            int newQuantity = scanner.nextInt();
                            double newPrice = item.getRate() * newQuantity;
                            Invoice.updateInvoiceDetail(connection, invoiceId, itemId, newQuantity, newPrice);
                            break;
                        case 3:
                            Invoice.deleteInvoiceDetail(connection, invoiceId, itemId);
                            break;
                        default:
                            System.out.println("Invalid option. Please try again.");
                    }
                }
            }
            invoice.updateInvoiceTotal(connection, invoiceId, discount);
            double totalAmount= invoice.getTotal();
        	subTotal= invoice.getsubTotal();
            // Update the invoice total in the database
            System.out.println("Total Amount: " + totalAmount);
            System.out.print("Enter Amount Paid: ");
            double amountPaid = scanner.nextDouble();
            invoice.setPaid(amountPaid);
            invoice.updateInvoiceTotal(connection, invoiceId, discount);
            double balance = totalAmount - amountPaid + initialBalance;
            customer.updateBalance(connection, balance);
            connection.commit();

            System.out.println("Invoice created successfully.");
            System.out.println("Initial Balance: " + initialBalance);
            Invoice.viewInvoiceByID(connection, invoiceId);

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public static void createItem(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Item Name: ");
            String itemName = scanner.next();
            Item existingItem = Item.getItemIdByItemName(connection, itemName);
            if (existingItem == null) {
                System.out.print("Enter Unit: ");
                String unit = scanner.next();
                System.out.print("Enter Rate: ");
                double rate = scanner.nextDouble();
                Item item = new Item(itemName, unit, rate);
                item.createItem(connection);
                System.out.println("Item created successfully.");
            } else {
                System.out.println("Item already exists. Enter 1 to update it, 0 to cancel:");
                int val = scanner.nextInt();
                if (val == 1) {
                    System.out.print("Enter new Unit: ");
                    String unit = scanner.next();
                    System.out.print("Enter new Rate: ");
                    double rate = scanner.nextDouble();
                    existingItem.setUnit(unit);
                    existingItem.setRate(rate);
                    existingItem.updateItem(connection);
                    System.out.println("Item updated successfully.");
                } else {
                    System.out.println("Operation cancelled.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

