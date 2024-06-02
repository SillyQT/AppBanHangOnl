package com.example.manager.model;

public class CartModel {
    int cartid;
    String productName;
    long price;
    String productImg;
    int quality;

    public CartModel(){}
    public CartModel(int cartid, String productName, long price, String productImg, int quality) {
        this.cartid = cartid;
        this.productName = productName;
        this.price = price;
        this.productImg = productImg;
        this.quality = quality;
    }

    public int getCartid() {
        return cartid;
    }

    public void setCartid(int cartid) {
        this.cartid = cartid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}
