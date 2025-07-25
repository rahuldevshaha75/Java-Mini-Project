package admin;
import product.*;

import java.io.*;
import java.util.*;

public class Admin {
    private final String FILE_PATH = "resources/products.txt";
    private List<Product> productList;

    public Admin() {
        productList = new ArrayList<>();
        loadProductData();
    }


    public void loadProductData() {
        productList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String productName = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    int quantity = Integer.parseInt(parts[2].trim());
                    String category = parts[3].trim();
                    String color = parts[4].trim();

                    productList.add(new Product(productName, price, quantity, category, color));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean login(String email, String password) {
        return "admin@mailcom".equals(email.trim()) && "jav@25".equals(password.trim());
    }
    

    public List<Product> getProductList() {
        return productList;
    }


    public void addProduct(Product product) {
        productList.add(product);
        saveProductData();
    }



    public void deleteProduct(String productName) {
        try {
            boolean removed = false;
            for (int i = 0; i < productList.size(); i++) {
                if (productList.get(i).getProductName().equals(productName)) {
                    productList.remove(i);
                    removed = true;
                    break;
                }
            }

            if (removed) {
                saveProductData();
            } 
        } catch (Exception e) {
            System.out.println("Something Went Wrong. Error: " + e.getMessage());
            e.printStackTrace(); 
        }
        
    }
    


    public void updateProduct(String oldProductName, Product updatedProduct) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getProductName().equals(oldProductName)) {
                productList.set(i, updatedProduct);
                break;
            }
        }
        saveProductData();
    }


    private void saveProductData() {
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (int i = 0; i < productList.size(); i++) {
                Product product = productList.get(i);
                bw.write(product.getProductName() + "," + 
                product.getPrice() + "," +
                product.getQuantity() + "," + 
                product.getCategory() + "," + 
                product.getColor());

                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
