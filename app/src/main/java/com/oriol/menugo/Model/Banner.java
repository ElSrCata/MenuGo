package com.oriol.menugo.Model;

/**
 * Clase para la Slider
 * @author oriol
 */
public class Banner {
    private String id, name, image;

    /**
     * Constructor vacío
     */
    public Banner(){

    }

    /**
     * Constructor para la Slider
     * @param id id de la imagen del slide
     * @param name nombre de la imagen
     * @param image ruta de la imagen
     */
    public Banner(String id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
