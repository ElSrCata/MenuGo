package com.oriol.menugo.Model;

public class User {
    private String Name;
    private String Password;

    private String Phone;

    private String Staff;

    public User() {

    }

    public User(String name, String password) {
        Name = name;
        Password = password;
        Staff = "false";
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
