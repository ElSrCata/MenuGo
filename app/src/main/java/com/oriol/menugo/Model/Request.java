package com.oriol.menugo.Model;

import java.util.List;

/**
 * Clase para guardar el objeto Pedido
 * @author Oriol
 */
public class Request {
    private String phone;
    private String name;
    private String address;
    private String total;
    private String status;
    private String comment;
    private String paymentState;
    private String paymentMethod;
    private List<Order> foods; //List of food order

    /**
     * Constructor vació del Pedido
     */
    public Request() {
    }

    /**
     * Constructor paramétrico del Pedido
     * @param phone teléfono del usuario
     * @param name nombre del usuario
     * @param address dirección del usuario
     * @param total precio total del pedido
     * @param status estado del pedido
     * @param comment comentario del pedido
     * @param paymentState estado de pago del pedido
     * @param paymentMethod método de pago del pedido
     * @param foods comidas
     */
    public Request(String phone, String name, String address, String total, String status, String comment, String paymentState, String paymentMethod, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = status;
        this.comment = comment;
        this.paymentState = paymentState;
        this.paymentMethod = paymentMethod;
        this.foods = foods;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
