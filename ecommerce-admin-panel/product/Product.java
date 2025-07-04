package product;



public class Product {
    private String productName;
    private double price;
    private int quantity;
    private String category;
    private String color;

    
    public Product(String productName, double price, int quantity, String category, String color) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.color = color;
    }

    
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    
    
}
