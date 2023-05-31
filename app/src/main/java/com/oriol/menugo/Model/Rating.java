package com.oriol.menugo.Model;

/**
 * Clase para guardar el objeto Valoraciones
 * @author Oriol
 */
public class Rating {
    private String userPhone, foodId, rateValue, comment;

    /**
     * Constructor vacío de Valoraciones
     */
    public Rating(){
    }

    /**
     * Constructor paramétrico de Valoraciones
     * @param userPhone número de teléfono del usuario
     * @param foodId ID de la comida
     * @param rateValue valor de la valoración
     * @param comment comentario del usuario
     */
    public Rating(String userPhone, String foodId, String rateValue, String comment) {
        this.userPhone = userPhone;
        this.foodId = foodId;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
