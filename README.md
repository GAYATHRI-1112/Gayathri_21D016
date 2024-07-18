**Invoice Billing System**

**Project Overview:**
This project is an invoice billing system developed in Java, focused on efficiently managing customers, items, and invoices using MySQL for data storage and management. The system allows for creating, updating, and managing records while ensuring accurate billing, reporting, and ease of use.

**Features**

Create and Manage Customers: Allows creation, updating, and viewing of customer records. Customer information is encapsulated, ensuring data integrity and security.

Create and Manage Items: Enables creation, updating, and viewing of item records. Methods are provided for updating item details and deleting items if necessary.

Generate and Manage Invoices: Facilitates the creation of invoices, adding items to invoices, updating quantities, and calculating totals. Invoices can be corrected by updating or deleting items. The system ensures that customer balances are carried over and updated with each purchase.

View Sales Reports: Provides functionality to view sales reports such as item sales and customer balances.

Database Integration using JDBC: Uses JDBC for connecting to and interacting with a MySQL database for data storage..

**Technologies Used**
Java
MySQL
JDBC (Java Database Connectivity)

**Table Descriptions for Invoice Billing System**
**Customers Table**
**Description:**
Stores customer information such as name, mobile number, and balance.
**Columns:**
customer_id: Unique identifier for each customer.
name: Customer's name.
mobile_number: Customer's contact number.
balance: Amount owed by the customer.

**Items Table**
**Description:**
Stores details about products or services available for invoicing.
**Columns:**
item_id: Unique identifier for each item.
name: Name or description of the item.
unit: Unit of measurement or sale for the item.
rate: Price per unit of the item.

**Invoices Table**
**Description:**
Records details about each invoice issued to customers.
**Columns:**
invoice_id: Unique identifier for each invoice.
customer_id: Reference to the customer associated with the invoice.
date: Date when the invoice was created.
discount: Discount applied to the invoice total.
total: Total amount payable after applying discounts.

**Invoice Details Table**
**Description:**
Stores line-item details for each invoice, specifying included items and quantities.
**Columns:**
invoice_detail_id: Unique identifier for each line item in an invoice.
invoice_id: Reference to the invoice the line item belongs to.
item_id: Reference to the item included in the line item.
quantity: Quantity of the item in the invoice.
price: Total price for the line item based on quantity and item rate.

**Relationships**
Customers to Invoices: Each customer can have multiple invoices.
Items to Invoice Details: Each invoice can list multiple items with quantities in the invoice details table.


**Create Invoice Method:** The method facilitates creating invoices, adding items, updating quantities, and calculating totals. After creating an invoice, the details are displayed, showing all the necessary information.

**Customer Management:** The system allows for the creation of customers with unique customer IDs. If a customer has a balance from a previous purchase, it is added to their current transaction. The system provides methods to update customer details in the database.

**Error Correction**: If items are entered incorrectly during invoice creation, methods are provided to update or delete items from the invoice.

**Detailed Views and Reports:** The system includes methods to view invoices by ID, view all invoices, and check customer balances.
