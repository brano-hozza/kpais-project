package com.brano.oop2.models.users;

import com.brano.oop2.models.Model;

import java.io.Serializable;

public abstract class UserModel implements Model {
    private static final long serialVersionUID = 1L;
    private final int ID;
    private String username;
    private String password;
    private ROLES role;

    private class Address implements Serializable {
        private final String city;
        private final String street;
        private final int number;

        public Address(String city, String street, int number) {
            this.city = city;
            this.street = street;
            this.number = number;
        }

        @Override
        public String toString() {
            return this.city + "~" + this.street + "~" + this.number;
        }
    }

    private Address address;

    public void setAddress(String city, String street, int number) {
        this.address = new Address(city, street, number);
    }

    public String getAddress() {
        return address.toString();
    }


    public int getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ROLES getRole() {
        return role;
    }

    public void setRole(ROLES role) {
        this.role = role;
    }

    public UserModel(int ID, String username, String password, ROLES role) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
