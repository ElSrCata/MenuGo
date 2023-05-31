package com.oriol.menugo.Model;


/**
 * Clase para guardar el objeto Category
 * @author oriol
 */
public class Category {
    private String Name;
    private String Image;

    /**
     * Constructor vacío
     */
    public Category (){

    }

    /**
     * Constructor de Categoría
     * @param name nombre categoría
     * @param image imagen categoría
     */
    public Category(String name, String image) {
        Name = name;
        Image = image;
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
}
