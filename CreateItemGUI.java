package InvoiceBillingSystem;

import java.awt.EventQueue;
import java.sql.Connection;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateItemGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldItemName;
	private JTextField textFieldRate;
	private JTextField textFieldUnit;
	private JLabel lblMessage;
	private Item item;
	private Connection connection;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateItemGUI frame = new CreateItemGUI();
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
	public CreateItemGUI() {
		setTitle("Create/Update Item");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 564, 345);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblItemName = new JLabel("ITEM NAME");
		lblItemName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblItemName.setBounds(50, 50, 100, 25);
		contentPane.add(lblItemName);

		textFieldItemName = new JTextField();
		textFieldItemName.setBounds(160, 50, 200, 25);
		contentPane.add(textFieldItemName);
		textFieldItemName.setColumns(10);

		JLabel lblRate = new JLabel("RATE");
		lblRate.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblRate.setBounds(50, 100, 100, 25);
		contentPane.add(lblRate);

		textFieldRate = new JTextField();
		textFieldRate.setBounds(160, 100, 200, 25);
		contentPane.add(textFieldRate);
		textFieldRate.setColumns(10);

		JLabel lblUnit = new JLabel("UNIT");
		lblUnit.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUnit.setBounds(50, 150, 100, 25);
		contentPane.add(lblUnit);

		textFieldUnit = new JTextField();
		textFieldUnit.setBounds(160, 150, 200, 25);
		contentPane.add(textFieldUnit);
		textFieldUnit.setColumns(10);

		JButton btnAddItem = new JButton("ADD ITEM");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addItem();
			}
		});
		btnAddItem.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnAddItem.setBounds(50, 200, 150, 35);
		contentPane.add(btnAddItem);

		JButton btnUpdateItem = new JButton("UPDATE ITEM");
		btnUpdateItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateItem();
			}
		});
		btnUpdateItem.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnUpdateItem.setBounds(210, 200, 150, 35);
		contentPane.add(btnUpdateItem);
		 lblMessage = new JLabel("");
	        lblMessage.setFont(new Font("Tahoma", Font.PLAIN, 14));
	        lblMessage.setBounds(50, 250, 350, 25);
	        contentPane.add(lblMessage);
	        
	        JButton btnNewButton = new JButton("EXIT");
	        btnNewButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		 exitToMain();
	        	}
	        });
	        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
	        btnNewButton.setBounds(378, 202, 122, 33);
	        contentPane.add(btnNewButton);

		// Initialize the database connection
		initializeDatabaseConnection();
	}

	protected void initializeDatabaseConnection() {
		try {
			connection = DataBaseConnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to connect to the database");
		}
	}

	protected void addItem() {
	    try {
	        String itemName = textFieldItemName.getText();
	        
	        // Check if the item already exists
	        item = Item.getItemIdByItemName(connection, itemName);
	        
	        if (item != null) {
	            // Item already exists
	            lblMessage.setText("Item already exists: " + itemName);
	            textFieldItemName.setText("");
	            textFieldRate.setText("");
	            textFieldUnit.setText("");
	        } else {
	            // Item does not exist, proceed with adding
	            Double rate = Double.parseDouble(textFieldRate.getText());
	            String unit = textFieldUnit.getText();
	            item = new Item(itemName, unit, rate);
	            item.createItem(connection);
	            
	            // Clear text fields
	            textFieldItemName.setText("");
	            textFieldRate.setText("");
	            textFieldUnit.setText("");
	            
	            lblMessage.setText("Item added: " + itemName + ", Rate: " + rate + ", Unit: " + unit);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        lblMessage.setText("Failed to add item");
	    }
	}


	protected void updateItem() {
		try {
			String itemName = textFieldItemName.getText();
			item = Item.getItemIdByItemName(connection, itemName);
			if (item != null) {
				Double rate = Double.parseDouble(textFieldRate.getText());
				String unit = textFieldUnit.getText();
				item.setUnit(unit);
				item.setRate(rate);
				item.updateItem(connection);
				textFieldItemName.setText("");
				textFieldRate.setText("");
				textFieldUnit.setText("");
				lblMessage.setText("Item updated: " + itemName + ", Rate: " + rate + ", Unit: " + unit);
			} else {
				JOptionPane.showMessageDialog(null, "Item not found.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to update item.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	 protected void exitToMain() {
	        dispose();
	    }
}
