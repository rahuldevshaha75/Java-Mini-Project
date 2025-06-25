package UI;

import admin.*;
import product.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AdminPanel extends JFrame implements ActionListener {
    private Admin admin;
    private JPanel loginPanel;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JLabel EmailLabel, PasswordLabel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton  loginButton, addButton, deleteButton, editButton, submitButton, cancelButton;
    
    
    private JDialog addProductDialog;
    private JDialog editProductDialog;


    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField categoryField;
    private JTextField colorField;


    private JButton editSaveButton;
    private JButton editCancelButton;


    private String oldProductName;
    private JTextField editNameField;
    private JTextField editPriceField;
    private JTextField editQuantityField;
    private JTextField editCategoryField;

    private String[] colors = {"Red", "Blue", "Violet", "Green", "Yellow", "Orange"};
    private JComboBox<String>  colorBtn = new JComboBox<>(colors);  



    public AdminPanel() {

        admin = new Admin();

        setTitle(" Login Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(new BorderLayout());


        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        EmailLabel = new JLabel("Email:");
        loginPanel.add(EmailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        loginPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        PasswordLabel = new JLabel("Password:");
        loginPanel.add(PasswordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Login");
        loginPanel.add(loginButton, gbc);

        loginButton.addActionListener(this);


        ImageIcon icon = new ImageIcon("logo1.png");
        this.setIconImage(icon.getImage());
        add(loginPanel, BorderLayout.CENTER);
        setSize(450, 250);
        setLocationRelativeTo(null); 

    }

    
    public void actionPerformed(ActionEvent ae){

        if (ae.getSource() == loginButton) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if(email.isEmpty() && password.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please Enter Email and Password!");
            }else if(email.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please Enter Email!");
            }else if (password.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please Enter Password!");
            }else{
                if (admin.login(email, password)) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    showProductManagementPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Data, Try Again!");
                }

            }



        }else if(ae.getSource() == addButton){
            showAddProductDialog();

        }else if(ae.getSource() == deleteButton){
            deleteProduct();


        }else if(ae.getSource() == editButton){
            showEditProductDialog();


        }else if(ae.getSource() == submitButton){
            try {
                String name = nameField.getText().trim();
                String category = categoryField.getText().trim();
                String color = (String) colorBtn.getSelectedItem();
                double price = Double.parseDouble(priceField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());
            
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please Enter Name.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } else if (category.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please Enter Category.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } 
                
                Product newProduct = new Product(name, price, quantity, category, color);
                admin.addProduct(newProduct);
                JOptionPane.showMessageDialog(this, "Product Added Successfully!");
                addProductDialog.dispose();
            
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Enter Valid Info.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An Unexpected Error Occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            tableModel.setRowCount(0);
            loadProductData(); 


        }else if(ae.getSource() == cancelButton){
            addProductDialog.dispose();
            JOptionPane.showMessageDialog(this, "Product Adding Cancelled!"); 


        }else if(ae.getSource() == editSaveButton){
            try {
                String newName = editNameField.getText().trim();
                String newCategory = editCategoryField.getText().trim();
                String newColor = (String) colorBtn.getSelectedItem();
                double newPrice = Double.parseDouble(editPriceField.getText().trim());
                int newQuantity = Integer.parseInt(editQuantityField.getText().trim());
            
                if (newName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please Enter Name.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } else if (newCategory.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please Enter Category.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } else {
                    Product updatedProduct = new Product(newName, newPrice, newQuantity, newCategory, newColor);
                    admin.updateProduct(oldProductName, updatedProduct);
            
                    JOptionPane.showMessageDialog(this, "Product Updated Successfully!");
                    editProductDialog.dispose(); 
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Enter Valid Info.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An Unexpected Error Occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            tableModel.setRowCount(0);  
            loadProductData();  
            
        }
        else if(ae.getSource() == editCancelButton){
            editProductDialog.dispose();
            JOptionPane.showMessageDialog(this, "Product Update Cancelled!"); 

        }


    }



    private void showProductManagementPanel() {
        getContentPane().removeAll();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String columnNames[] = {"Product Name", "Price", "Quantity", "Category", "Color"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        loadProductData();

        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);

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
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            tableModel.addRow(new Object[]{
                product.getProductName(), product.getPrice(), product.getQuantity(),
                product.getCategory(), product.getColor()
            });
        }
    }
    


    private void showAddProductDialog() {
        addProductDialog = new JDialog(this, "Add New Product", true);
        addProductDialog.setLayout(new GridLayout(6, 2, 10, 10));

        
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


        
        colorBtn.setSelectedIndex(0);
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");

        
        addProductDialog.add(nameLabel);
        addProductDialog.add(nameField);
        addProductDialog.add(priceLabel);
        addProductDialog.add(priceField);
        addProductDialog.add(quantityLabel);
        addProductDialog.add(quantityField);
        addProductDialog.add(categoryLabel);
        addProductDialog.add(categoryField);
        addProductDialog.add(colorLabel);
        addProductDialog.add(colorBtn);
        addProductDialog.add(submitButton);
        addProductDialog.add(cancelButton);



        submitButton.addActionListener(this);
        cancelButton.addActionListener(this);


    
        addProductDialog.setSize(450, 300);
        addProductDialog.setLocationRelativeTo(this);
        addProductDialog.setLocationRelativeTo(null);
        addProductDialog.setVisible(true);

    }


    private void deleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            String productName = (String) tableModel.getValueAt(selectedRow, 0);
            admin.deleteProduct(productName); 
            JOptionPane.showMessageDialog(this, "Product Deleted!"); 
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please Select a Product to Delete.");
        }
    }

    private void showEditProductDialog() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            oldProductName = (String) tableModel.getValueAt(selectedRow, 0);

            editProductDialog = new JDialog(this, "Edit Product", true);
            editProductDialog.setLayout(new GridLayout(6, 2, 10, 10));

            String name = (String) tableModel.getValueAt(selectedRow, 0);
            double price = (double) tableModel.getValueAt(selectedRow, 1);
            int quantity = (int) tableModel.getValueAt(selectedRow, 2);
            String category = (String) tableModel.getValueAt(selectedRow, 3);
            String color = (String) tableModel.getValueAt(selectedRow, 4);

            
            editNameField = new JTextField(name);
            editPriceField = new JTextField(String.valueOf(price));
            editQuantityField = new JTextField(String.valueOf(quantity));
            editCategoryField = new JTextField(category);
            colorBtn.setSelectedItem(color);
            
            editSaveButton = new JButton("Save Changes");
            editCancelButton = new JButton("Cancel");


            editProductDialog.add(new JLabel("Product Name:"));
            editProductDialog.add(editNameField);
            editProductDialog.add(new JLabel("Price:"));
            editProductDialog.add(editPriceField);
            editProductDialog.add(new JLabel("Quantity:"));
            editProductDialog.add(editQuantityField);
            editProductDialog.add(new JLabel("Category:"));
            editProductDialog.add(editCategoryField);
            editProductDialog.add(new JLabel("Color:"));
            editProductDialog.add(colorBtn);
            editProductDialog.add(editSaveButton);
            editProductDialog.add(editCancelButton);



            editSaveButton.addActionListener(this);
            editCancelButton.addActionListener(this);



            editProductDialog.setSize(450, 300);
            editProductDialog.setLocationRelativeTo(null);
            editProductDialog.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "Please Select a Product to Edit.");
        }
    }


}
