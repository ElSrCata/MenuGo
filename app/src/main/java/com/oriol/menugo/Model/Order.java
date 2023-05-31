package com.oriol.menugo.Model;

/**
 * Clase para guardar el objeto Orden
 * @author Oriol
 */
public class Order {
    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Discount;
    private String Image;

    /**
     * Constructor vacío de Oden
     */
    public Order(){

    }

    /**
     * Constructor paramétrico de Oden
     * @param productId ID del producto
     * @param productName nombre del producto
     * @param quantity cantidad del producto
     * @param price precio del orden
     * @param discount descuento del orden
     * @param image imagen del orden
     */
    public Order(String productId, String productName, String quantity, String price, String discount, String image) {
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
