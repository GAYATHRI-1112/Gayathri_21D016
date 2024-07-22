package InvoiceBillingSystem;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI frame = new MainGUI();
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
	 public MainGUI() {
	        Initialise();
	    }

	private void Initialise() {
	
		setTitle("RAM STORE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 692, 494);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JButton btnNewButton = new JButton(" CREATE INVOICE");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//BillingGUI.Initialise();
				BillingGUI create = new BillingGUI();
				create.setVisible(true);
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton.setBounds(205, 39, 280, 46);
		contentPane.add(btnNewButton);
		
	    JButton btnViewAllInvoices = new JButton("VIEW ALL INVOICES");
        btnViewAllInvoices.addActionListener(e -> viewAllInvoices());
        btnViewAllInvoices.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnViewAllInvoices.setBounds(205, 98, 280, 46);
        contentPane.add(btnViewAllInvoices);
		
		JButton btnNewButton_2 = new JButton("   VIEW INVOICE BY ID");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ViewInvoiceByIdGUI view = new ViewInvoiceByIdGUI();
				view.setVisible(true);
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton_2.setBounds(205, 155, 280, 54);
		contentPane.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("VIEW CUSTOMER BALANCE");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewCustomerBalance();
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton_3.setBounds(205, 220, 280, 54);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("VIEW ITEM SALES");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				viewItemSales();
			}
		});
		btnNewButton_4.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton_4.setBounds(205, 285, 280, 47);
		contentPane.add(btnNewButton_4);
		
		JButton btnNewButton_5 = new JButton("CREATE ITEM");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CreateItemGUI create=new CreateItemGUI();
				create.setVisible(true);
			}
		});
		btnNewButton_5.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton_5.setBounds(205, 343, 280, 45);
		contentPane.add(btnNewButton_5);
		
		JButton btnNewButton_6 = new JButton("EXIT");
		btnNewButton_6.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnNewButton_6.setBounds(205, 399, 280, 45);
		contentPane.add(btnNewButton_6);
	}

	

protected void viewCustomerBalance() {
    // Prompt user for mobile number
    String mobileNumber = JOptionPane.showInputDialog(this, "Enter Mobile Number:");
   
        	  do { if (!Customer.isValidMobileNumber(mobileNumber)) {
                  JOptionPane.showMessageDialog(null, "Invalid mobile number format. Please enter a valid 10-digit number.", "Error", JOptionPane.ERROR_MESSAGE);
                  mobileNumber = JOptionPane.showInputDialog(this, "Enter Mobile Number:");
                  return;
              }}while(!Customer.isValidMobileNumber(mobileNumber));
             try (Connection connection = DataBaseConnection.getConnection()) {
          // Fetch the customer by mobile number
             Customer customer = Customer.getByMobile(connection, mobileNumber);
            double  balance = customer.getBalance();
            // Show balance in a dialog
            JOptionPane.showMessageDialog(this, "Customer Balance: " + balance, "Customer Balance", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error retrieving balance", "Error", JOptionPane.ERROR_MESSAGE);
        }
}
protected void viewAllInvoices() {
    // Define the column names
    String[] columnNames = {"Invoice ID", "Customer Name", "Date", "Total", "Balance"};
    
    // Create a table model and set column names
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

    // Fetch data from database
    String query = "SELECT i.InvoiceID, c.Name AS CustomerName, i.Date, i.Total, c.Balance " +
                   "FROM Invoices i " +
                   "JOIN Customers c ON i.CustomerID = c.CustomerID";

    try (Connection connection = DataBaseConnection.getConnection();
         PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            // Retrieve data from result set
            int invoiceId = rs.getInt("InvoiceID");
            String customerName = rs.getString("CustomerName");
            String date = rs.getString("Date");
            double total = rs.getDouble("Total");
            double balance = rs.getDouble("Balance");

            // Add row to table model
            Object[] row = {invoiceId, customerName, date, total, balance};
            tableModel.addRow(row);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    // Create table and scroll pane
    JTable table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);

    // Create and configure frame
    JFrame frame = new JFrame("View All Invoices");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(800, 400);
    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    frame.setVisible(true);
    JButton exitButton = new JButton("Exit");
    exitButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
        }
    });
    frame.getContentPane().add(exitButton, BorderLayout.SOUTH);

    frame.setVisible(true);
}
protected void viewItemSales() {
    // Define the column names
    String[] columnNames = {"Item Name", "Unit", "Total Quantity", "Total Sales"};
    
    // Create a table model and set column names
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

    String query = "SELECT i.ItemName, i.Unit, SUM(d.Quantity) AS TotalQuantity, SUM(d.Price) AS TotalSales " +
                   "FROM Items i JOIN InvoiceDetails d ON i.ItemID = d.ItemID " +
                   "GROUP BY i.ItemID ORDER BY TotalSales DESC";

    try (Connection connection = DataBaseConnection.getConnection();
         PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            // Retrieve data from result set
            String itemName = rs.getString("ItemName");
            String unit = rs.getString("Unit");
            int totalQuantity = rs.getInt("TotalQuantity");
            double totalSales = rs.getDouble("TotalSales");

            // Add row to table model
            Object[] row = {itemName, unit, totalQuantity, totalSales};
            tableModel.addRow(row);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    // Create table and scroll pane
    JTable table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);

    // Create and configure frame
    JFrame frame = new JFrame("View Item Sales");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(800, 400);
    frame.getContentPane().setLayout(new BorderLayout());
    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    frame.setVisible(true);
    JButton exitButton = new JButton("Exit");
    exitButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
        }
    });
    frame.getContentPane().add(exitButton, BorderLayout.SOUTH);

    frame.setVisible(true);
}

}