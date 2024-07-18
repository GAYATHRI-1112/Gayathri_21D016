**Invoice Billing System**

**Project Overview:**
This project is an invoice billing system developed in Java, focused on efficiently managing customers, items, and invoices using MySQL for data storage and management.

**Features**
Create and Manage Customers:  Allows creation, updating, and viewing of customer records.

Create and Manage Items: Enables creation, updating, and viewing of item records.

Generate and Manage Invoices: Facilitates the creation of invoices, adding items to invoices, updating quantities, and calculating totals.

View Sales Reports: Provides functionality to view sales reports such as item sales and customer balances.

Database Integration using JDBC: Uses JDBC for connecting to and interacting with a MySQL database for data storage.

**Technologies Used**
Java
MySQL
JDBC (Java Database Connectivity)

**Table Descriptions for Invoice Billing System**
**Customers Table**
Description:
Stores customer information such as name, mobile number, and balance.
Columns:
customer_id: Unique identifier for each customer.
name: Customer's name.
mobile_number: Customer's contact number.
balance: Amount owed by the customer.

**Items Table**
Description:
Stores details about products or services available for invoicing.
Columns:
item_id: Unique identifier for each item.
name: Name or description of the item.
unit: Unit of measurement or sale for the item.
rate: Price per unit of the item.

**Invoices Table**
Description:
Records details about each invoice issued to customers.
Columns:
invoice_id: Unique identifier for each invoice.
customer_id: Reference to the customer associated with the invoice.
date: Date when the invoice was created.
discount: Discount applied to the invoice total.
total: Total amount payable after applying discounts.

**Invoice Details Table**
Description:
Stores line-item details for each invoice, specifying included items and quantities.
Columns:
invoice_detail_id: Unique identifier for each line item in an invoice.
invoice_id: Reference to the invoice the line item belongs to.
item_id: Reference to the item included in the line item.
quantity: Quantity of the item in the invoice.
price: Total price for the line item based on quantity and item rate.

**Relationships**
Customers to Invoices: Each customer can have multiple invoices.
Items to Invoice Details: Each invoice can list multiple items with quantities in the invoice details table.
