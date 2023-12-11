package com.cs360.mattsmithsinventoryappcs360;

public class Product {
    private String productId;
    private String productDesc;
    private int quantity;

    public Product(String productId, String productDesc, int quantity) {
        this.productId = productId;
        this.productDesc = productDesc;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}