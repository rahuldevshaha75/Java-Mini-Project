package UI;

import admin.*;
import product.*;



import javax.swing.*;
import java.awt.*;
//import java.awt.Desktop.Action;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AdminPanel extends JFrame implements ActionListener {
    private Admin admin;
    private JPanel loginPanel;
    private JTable productTable;
    private DefaultTableModel tableModel;
    JLabel EmailLabel, PasswordLabel;
    JTextField emailField;
    JPasswordField passwordField;
    JButton  loginButton, addButton, deleteButton, editButton, submitButton, cancelButton;
    
    
    JDialog addProductDialog;
    JDialog editProductDialog;


    JTextField nameField;
    JTextField priceField;
    JTextField quantityField;
    JTextField categoryField;
    JTextField colorField;


    JButton editSaveButton;
    JButton editCancelButton;


    String oldProductName;
    JTextField editNameField;
    JTextField editPriceField;
    JTextField editQuantityField;
    JTextField editCategoryField;
    JTextField editColorField;

    public AdminPanel() {
        admin = new Admin();
        setTitle("Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(new BorderLayout());

        // Login Screen
        loginPanel = new JPanel();
        
        
        EmailLabel = new JLabel("Email:");
        loginPanel.add(EmailLabel);
        emailField = new JTextField(15);
        loginPanel.add(emailField);
        

        PasswordLabel = new JLabel("Password:");
        loginPanel.add(PasswordLabel);
        passwordField = new JPasswordField(15);
        loginPanel.add(passwordField);
        
        
        loginButton= new JButton("Login");
        loginPanel.add(loginButton);


        loginButton.addActionListener(this);

        add(loginPanel, BorderLayout.CENTER);
        setSize(450, 250);
        setLocationRelativeTo(null);


        
    }

    
    public void actionPerformed(ActionEvent ae){

        if (ae.getSource() == loginButton) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (admin.login(email, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                showProductManagementPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials, try again.");
            }


        }else if(ae.getSource() == addButton){
            showAddProductDialog();

        }else if(ae.getSource() == deleteButton){
            deleteProduct();
            JOptionPane.showMessageDialog(this, "Product Deleted!"); 


        }else if(ae.getSource() == editButton){
            showEditProductDialog();


        }else if(ae.getSource() == submitButton){
             // Create new Product object from form inputs
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            String category = categoryField.getText();
            String color = colorField.getText();

            Product newProduct = new Product(name, price, quantity, category, color);
            admin.addProduct(newProduct);  // Add product to file
            JOptionPane.showMessageDialog(this, "Product Added Successfully!"); 

            // Close the dialog
            addProductDialog.dispose();

            // Refresh the product list
            tableModel.setRowCount(0);  // Clear table
            loadProductData();  // Reload table with updated product list


        }else if(ae.getSource() == cancelButton){
            // Close the dialog without adding a product
            addProductDialog.dispose();
            JOptionPane.showMessageDialog(this, "Product Adding Cancelled!"); 


        }else if(ae.getSource() == editSaveButton){
            // Update product information
            String newName = editNameField.getText();
            double newPrice = Double.parseDouble(editPriceField.getText());
            int newQuantity = Integer.parseInt(editQuantityField.getText());
            String newCategory = editCategoryField.getText();
            String newColor = editColorField.getText();

            Product updatedProduct = new Product(newName, newPrice, newQuantity, newCategory, newColor);
            admin.updateProduct(oldProductName, updatedProduct);

            JOptionPane.showMessageDialog(this, "Product Updated Successfully!"); 
            // Close the dialog
            editProductDialog.dispose();

            // Refresh the product list
            tableModel.setRowCount(0);  // Clear table
            loadProductData();  // Reload table with updated product list
            
        }
        else if(ae.getSource() == editCancelButton){
            // Close the dialog without saving changes
            editProductDialog.dispose();
            JOptionPane.showMessageDialog(this, "Product Update Cancelled!"); 

        }



    }



    private void showProductManagementPanel() {
        getContentPane().removeAll();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Product Table
        String[] columnNames = {"Product Name", "Price", "Quantity", "Category", "Color"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        loadProductData();

        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Product");
        deleteButton = new JButton("Delete Product");
        editButton = new JButton("Edit Product");


        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        editButton.addActionListener(this);


        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void loadProductData() {
        List<Product> products = admin.getProductList();
        for (Product product : products) {
            tableModel.addRow(new Object[]{
                product.getProductName(), product.getPrice(), product.getQuantity(),
                product.getCategory(), product.getColor()
            });
        }
    }



    // Method to show dialog for adding a new product
    private void showAddProductDialog() {
        addProductDialog = new JDialog(this, "Add New Product", true);
        addProductDialog.setLayout(new GridLayout(6, 2, 10, 10));

        // Product fields
        JLabel nameLabel = new JLabel("Product Name:");
        nameField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        priceField = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField();
        JLabel categoryLabel = new JLabel("Category:");
        categoryField = new JTextField();
        JLabel colorLabel = new JLabel("Color:");
        colorField = new JTextField();

        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");

        // Add components to the dialog
        addProductDialog.add(nameLabel);
        addProductDialog.add(nameField);
        addProductDialog.add(priceLabel);
        addProductDialog.add(priceField);
        addProductDialog.add(quantityLabel);
        addProductDialog.add(quantityField);
        addProductDialog.add(categoryLabel);
        addProductDialog.add(categoryField);
        addProductDialog.add(colorLabel);
        addProductDialog.add(colorField);
        addProductDialog.add(submitButton);
        addProductDialog.add(cancelButton);

        submitButton.addActionListener(this);
        cancelButton.addActionListener(this);


        // Set dialog properties
        addProductDialog.setSize(500, 300);
        addProductDialog.setLocationRelativeTo(this);

        addProductDialog.setVisible(true);

    }

    // Method to delete a product
    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            String productName = (String) tableModel.getValueAt(selectedRow, 0);
            admin.deleteProduct(productName);  // Delete product from file

            // Remove product from table
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
        }
    }

    // Method to show edit product dialog
    private void showEditProductDialog() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            oldProductName = (String) tableModel.getValueAt(selectedRow, 0);

            // Create dialog for editing product
            editProductDialog = new JDialog(this, "Edit Product", true);
            editProductDialog.setLayout(new GridLayout(6, 2, 10, 10));

            // Fetch the current values from the table
            String name = (String) tableModel.getValueAt(selectedRow, 0);
            double price = (double) tableModel.getValueAt(selectedRow, 1);
            int quantity = (int) tableModel.getValueAt(selectedRow, 2);
            String category = (String) tableModel.getValueAt(selectedRow, 3);
            String color = (String) tableModel.getValueAt(selectedRow, 4);

            editNameField = new JTextField(name);
            editPriceField = new JTextField(String.valueOf(price));
            editQuantityField = new JTextField(String.valueOf(quantity));
            editCategoryField = new JTextField(category);
            editColorField = new JTextField(color);


            editSaveButton = new JButton("Save Changes");
            editCancelButton = new JButton("Cancel");

            // Add components to the dialog
            editProductDialog.add(new JLabel("Product Name:"));
            editProductDialog.add(editNameField);
            editProductDialog.add(new JLabel("Price:"));
            editProductDialog.add(editPriceField);
            editProductDialog.add(new JLabel("Quantity:"));
            editProductDialog.add(editQuantityField);
            editProductDialog.add(new JLabel("Category:"));
            editProductDialog.add(editCategoryField);
            editProductDialog.add(new JLabel("Color:"));
            editProductDialog.add(editColorField);
            editProductDialog.add(editSaveButton);
            editProductDialog.add(editCancelButton);

            editSaveButton.addActionListener(this);
            editCancelButton.addActionListener(this);


            // saveButton.addActionListener(e -> {
            //     // Update product information
            //     String newName = nameField.getText();
            //     double newPrice = Double.parseDouble(priceField.getText());
            //     int newQuantity = Integer.parseInt(quantityField.getText());
            //     String newCategory = categoryField.getText();
            //     String newColor = colorField.getText();

            //     Product updatedProduct = new Product(newName, newPrice, newQuantity, newCategory, newColor);
            //     admin.updateProduct(oldProductName, updatedProduct);

            //     // Close the dialog
            //     editProductDialog.dispose();

            //     // Refresh the product list
            //     tableModel.setRowCount(0);  // Clear table
            //     loadProductData();  // Reload table with updated product list
            // });



            // Set dialog properties
            editProductDialog.setSize(400, 300);
            editProductDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to edit.");
        }
    }


}
