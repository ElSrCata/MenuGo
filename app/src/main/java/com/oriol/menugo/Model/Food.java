package com.oriol.menugo.Model;

/**
 * Clase para guardar el objeto Comida
 * @author Oriol
 */
public class Food {
    //Definición variables
    private String Name, Image, Description, Price, Discount, MenuId;

    /**
     * Constructor vacío de Food
     */
    public Food() {
    }

    /**
     * Constructor para objeto Food
     * @param name nombre comida
     * @param image imagen comida
     * @param description descripción comida
     * @param price precio comida
     * @param discount descuento comida
     * @param menuId ID del menú al que pertenece la comida
     */
    public Food(String name, String image, String description, String price, String discount, String menuId) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        Discount = discount;
        MenuId = menuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
