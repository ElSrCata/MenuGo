package com.oriol.menugo.Model;

/**
 * Clase para guardar el objeto Usuario
 * @author Oriol
 */
public class User {
    private String Name;
    private String Password;

    private String Phone;

    private String Staff;

    private String secureCode;

    /**
     * Constructor vacío de Usuario
     */
    public User() {

    }

    /**
     * Constructor paramétrico de Usuario
     * @param name nombre usuario
     * @param password contraseña usuario
     * @param secureCode código seguridad usuario
     */
    public User(String name, String password, String secureCode) {
        Name = name;
        Password = password;
        Staff = "false";
        this.secureCode = secureCode;
    }

    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getStaff() {
        return Staff;
    }

    public void setStaff(String staff) {
        Staff = staff;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
