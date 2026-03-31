package com.hotel.model;

import java.io.Serializable;

public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String phone;

    public Customer(String name, String phone) {
        this.name  = name;
        this.phone = phone;
    }

    public String getName()  { return name; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return name + " (" + phone + ")";
    }
}
