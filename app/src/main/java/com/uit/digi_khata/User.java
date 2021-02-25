package com.uit.digi_khata;

public class User
{
    private String name ;
    private String comapnyName ;
    private String phone ;
    private String email ;

    public User() {
    }

    public User(String name, String comapnyName, String phone, String email) {
        this.name = name;
        this.comapnyName = comapnyName;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getComapnyName() {
        return comapnyName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
