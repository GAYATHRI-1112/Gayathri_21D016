package InvoiceBillingSystem;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class BillingGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textItem;
    private JTextField textQuantity;
    private JTextField textDiscount;
    private JTextField textPaid;
    private JTextField textNumber;
    private JTextField textID;
    private JTextField textName;
    private double totalAmount = 0.0; // Total amount for the invoice
    Invoice invoice;
    Customer customer;
    private int invoiceId; // Invoice ID
    private int customerId;
    private double initialBalance=0;
    private String mobileNumber;
    private JTextField textInitialBalance;
    private double discount = 0.0;
    private double paidAmount = 0.0;
    private double subTotal;
    private int quantity;
    private double price;
    private double newPrice;
    private JButton  btnNewButton;
    private JButton  btnNewButton_1;

    
    JLabel lblInfo1 = new JLabel("");
    JLabel lblInfo2 = new JLabel("");
    JLabel lblInfo3 =new JLabel("");
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BillingGUI frame = new BillingGUI();
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
    public BillingGUI() {
        Initialise();
    }

    private void Initialise() {
        setTitle("RAM STORE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 727, 506);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblMobileNumber = new JLabel("Mobile Number");
        lblMobileNumber.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblMobileNumber.setBounds(75, 23, 160, 14);
        contentPane.add(lblMobileNumber);

        JLabel lblItem = new JLabel("Item");
        lblItem.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblItem.setBounds(75, 212, 160, 14);
        contentPane.add(lblItem);

        JLabel lblQuantity = new JLabel("Quantity");
        lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblQuantity.setBounds(75, 237, 160, 14);
        contentPane.add(lblQuantity);

        JLabel lblDiscount = new JLabel("Discount");
        lblDiscount.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblDiscount.setBounds(75, 146, 160, 14);
        contentPane.add(lblDiscount);

        JLabel lblPaid = new JLabel("Paid");
        lblPaid.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPaid.setBounds(75, 352, 160, 14);
        contentPane.add(lblPaid);

        textItem = new JTextField();
        textItem.setBounds(308, 211, 190, 20);
        contentPane.add(textItem);
        textItem.setColumns(10);

        textQuantity = new JTextField();
        textQuantity.setBounds(308, 236, 190, 20);
        contentPane.add(textQuantity);
        textQuantity.setColumns(10);

        textDiscount = new JTextField();
        textDiscount.setBounds(310, 145, 190, 20);
        contentPane.add(textDiscount);
        textDiscount.setColumns(10);

        textPaid = new JTextField();
        textPaid.setBounds(310, 351, 190, 20);
        contentPane.add(textPaid);
        textPaid.setColumns(10);

        textNumber = new JTextField();
        textNumber.setBounds(310, 22, 190, 20);
        contentPane.add(textNumber);
        textNumber.setColumns(10);

        textID = new JTextField();
        textID.setBounds(308, 95, 190, 20);
        contentPane.add(textID);
        textID.setColumns(10);

        textName = new JTextField();
        textName.setBounds(308, 71, 190, 20);
        contentPane.add(textName);
        textName.setColumns(10);

     
        JButton btnSearch = new JButton("Next");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleNextButtonAction();
            }
        });

     
        btnSearch.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSearch.setBounds(556, 19, 89, 23);
        contentPane.add(btnSearch);

        JButton btnAdd = new JButton("Add item");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleAddItemButtonAction();
            }
        });
        btnAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnAdd.setBounds(68, 293, 109, 23);
        contentPane.add(btnAdd);

        JButton btnCreate = new JButton("Create Invoice");
        btnCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleCreateInvoiceButtonAction();
            }
        });
        btnCreate.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCreate.setBounds(148, 399, 190, 25);
        contentPane.add(btnCreate);

        JLabel lblCustomerID = new JLabel("Customer ID");
        lblCustomerID.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblCustomerID.setBounds(75, 96, 160, 14);
        contentPane.add(lblCustomerID);

        JLabel lblCustomerName = new JLabel("Customer Name");
        lblCustomerName.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblCustomerName.setBounds(75, 77, 160, 14);
        contentPane.add(lblCustomerName);
        
        JButton btnDone = new JButton("Done");
        btnDone.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		handleDoneButtonAction();
        	}
        });
        btnDone.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnDone.setBounds(221, 170, 104, 23);
        contentPane.add(btnDone);
        
        JLabel lblNewLabel = new JLabel("Initial Balance");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel.setBounds(75, 121, 160, 14);
        contentPane.add(lblNewLabel);
        
        textInitialBalance = new JTextField();
        textInitialBalance.setBounds(308, 120, 190, 20);
        contentPane.add(textInitialBalance);
        textInitialBalance.setColumns(10);
        
        JButton btnRemove = new JButton("Remove");
        btnRemove.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		handleRemoveItemButtonAction();
        	}
        });
        btnRemove.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnRemove.setBounds(308, 293, 89, 23);
        contentPane.add(btnRemove);
        
        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		handleUpdateItemButtonAction();
        	}
        });
        btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnUpdate.setBounds(197, 293, 89, 23);
        contentPane.add(btnUpdate);
        
       // JLabel lblInfo1 = new JLabel("New label");
        lblInfo1.setBounds(75, 48, 425, 18);
        contentPane.add(lblInfo1);
        
       
        lblInfo2.setBounds(75, 268, 425, 14);
        contentPane.add(lblInfo2);
        
        JButton btnFinish = new JButton("Finish");
        btnFinish.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	            // Exit the loop and proceed to discount and payment
        		handleFinishButtonAction();
        	           
        	}
        });
        btnFinish.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnFinish.setBounds(409, 293, 89, 23);
        contentPane.add(btnFinish);
        
        btnNewButton = new JButton("View Invoice");
        btnNewButton.setVisible(false);
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		ViewInvoiceByIdGUI view = new ViewInvoiceByIdGUI();
                view.setVisible(true);
        	}
        });
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton.setBounds(348, 400, 168, 23);
        contentPane.add(btnNewButton);
        
        
        lblInfo3.setBounds(75, 435, 580, 21);
        contentPane.add(lblInfo3);
        
        btnNewButton_1 = new JButton("Exit");
        btnNewButton_1.setVisible(false);
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		  dispose();
        	}
        });
        btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton_1.setBounds(537, 399, 89, 23);
        contentPane.add(btnNewButton_1);
        
       
    }

    private void handleNextButtonAction() {
         mobileNumber = textNumber.getText(); 
       do { if (!Customer.isValidMobileNumber(mobileNumber)) {
            JOptionPane.showMessageDialog(null, "Invalid mobile number format. Please enter a valid 10-digit number.", "Error", JOptionPane.ERROR_MESSAGE);
            textNumber.setText("");
            textNumber.getText();
            return;
        }}while(!Customer.isValidMobileNumber(mobileNumber));
       try (Connection connection = DataBaseConnection.getConnection()) {
    // Fetch the customer by mobile number
       customer = Customer.getByMobile(connection, mobileNumber);
       if (customer != null) {
    	   lblInfo1.setText("The customer already exists");
    	    customerId = customer.getCustomerId();
    	    String customerName = customer.getName();
    	    textName.setText(customerName);
    	    textID.setText(String.valueOf(customerId));
    	    initialBalance = customer.getBalance();
    	    textInitialBalance.setText(String.valueOf(initialBalance));
    	    
    	    
    	}
       else {
    	   lblInfo1.setText("The customer is new. Please enter the name:");
       }
    
 }
       catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
        
    }

    private void handleAddItemButtonAction() {
        String itemName = textItem.getText();
        try {
            quantity = Integer.parseInt(textQuantity.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid quantity. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DataBaseConnection.getConnection()) {
            Item item = Item.getItemIdByItemName(connection, itemName);
            if (item == null) {
                JOptionPane.showMessageDialog(null, "Item not found. Please enter a valid item name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            price = item.getRate() * quantity;
            invoice.createInvoiceDetail(connection, item, quantity, price);
            // Clear item and quantity fields for next entry
            lblInfo2.setText("Item added successfully.");
            textItem.setText("");
            textQuantity.setText("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCreateInvoiceButtonAction() {
        try (Connection connection = DataBaseConnection.getConnection()) {
            try {
                paidAmount = Double.parseDouble(textPaid.getText());
                invoice.setPaid(paidAmount);
                invoice.updateInvoiceTotal(connection, invoiceId, discount);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid paid amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double finalBalance = initialBalance + totalAmount - paidAmount;
            customer.updateBalance(connection, finalBalance);

            // Show the "View Invoice" button
            btnNewButton.setVisible(true);
            btnNewButton_1.setVisible(true);
           
            lblInfo3.setText("Invoice created successfully.\nInvoice ID:\n "+invoiceId+" \nBalance due: " + finalBalance);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   public void handleUpdateItemButtonAction() {
	   String itemName = textItem.getText();
	   int newQuantity;
       try {
        newQuantity = Integer.parseInt(textQuantity.getText());
       } catch (NumberFormatException ex) {
           JOptionPane.showMessageDialog(null, "Invalid quantity. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
           return;
       }

       try (Connection connection = DataBaseConnection.getConnection()) {
           Item item = Item.getItemIdByItemName(connection, itemName);
           if (item == null) {
               JOptionPane.showMessageDialog(null, "Item not found. Please enter a valid item name.", "Error", JOptionPane.ERROR_MESSAGE);
               return;
           }
           int itemId = item.getItemId();
           newPrice = item.getRate() * newQuantity;
           Invoice.updateInvoiceDetail(connection, invoiceId, itemId, newQuantity, newPrice);
           lblInfo2.setText("Item updated successfully.");
           textItem.setText("");
           textQuantity.setText("");

       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
   }
   public void handleRemoveItemButtonAction() {
	   String itemName = textItem.getText();
       try (Connection connection = DataBaseConnection.getConnection()) {
           Item item = Item.getItemIdByItemName(connection, itemName);
           if (item == null) {
               JOptionPane.showMessageDialog(null, "Item not found. Please enter a valid item name.", "Error", JOptionPane.ERROR_MESSAGE);
               return;
           }
           int itemId=item.getItemId();
          
           textQuantity.setText("-");
           Invoice.deleteInvoiceDetail(connection, invoiceId, itemId);
           lblInfo2.setText("Item removed successfully.");
           textItem.setText("");
           textQuantity.setText("");

       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
   }
   public void handleDoneButtonAction() {
	   try (Connection connection = DataBaseConnection.getConnection()) {
           if(customer==null) {
               String name = textName.getText();
               if (!Customer.isValidName(name)) {
                   JOptionPane.showMessageDialog(null, "Enter valid name", "Error", JOptionPane.ERROR_MESSAGE);
                   textName.setText("");
                   return;
               }
              
               customer = new Customer(name, mobileNumber);
               customer.createCustomer(connection);
               customerId = customer.getCustomerId();
               initialBalance=0;
               textID.setText(String.valueOf(customerId));
               textInitialBalance.setText(String.valueOf(initialBalance));
           }
           // Validate discount
           String discountText = textDiscount.getText();
           if (discountText == null || discountText.isEmpty()) {
               JOptionPane.showMessageDialog(null, "Enter a discount", "Error", JOptionPane.ERROR_MESSAGE);
               textDiscount.setText("");
               return;
           }
           // Parse discount value
           try {
               discount = Double.parseDouble(discountText);
           } catch (NumberFormatException e) {
               JOptionPane.showMessageDialog(null, "Enter a valid discount value", "Error", JOptionPane.ERROR_MESSAGE);
               textDiscount.setText("");
               return;
           }
           invoice = new Invoice(customerId, 0, discount,0, 0);
           invoice.createInvoice(connection);
           invoiceId = invoice.getInvoiceId(); 
          

       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
	   
   }
  public void  handleFinishButtonAction() {
	  try (Connection connection = DataBaseConnection.getConnection()) {
          // Update invoice total with discount 
      try {
         invoice.updateInvoiceTotal(connection, invoiceId, discount);
          totalAmount= invoice.getTotal();
      	subTotal= invoice.getsubTotal();
      	 lblInfo2.setText("Amount to be paid:\n " + subTotal + "Amount to be paid after discount:"+ totalAmount);
          JOptionPane.showMessageDialog(null, "Amount to be paid: " + subTotal);
          JOptionPane.showMessageDialog(null, "Amount to be paid after discount: " + totalAmount);
      } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(null, "Invalid discount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
          return;
      }
      } catch (SQLException ex) {
          JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
  }
}