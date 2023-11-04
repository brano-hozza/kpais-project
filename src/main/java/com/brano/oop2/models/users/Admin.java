package com.brano.oop2.models.users;

public class Admin extends UserModel implements DealEdit {
    private static final long serialVersionUID = 1L;

    public Admin(int ID, String username, String password) {
        super(ID, username, password, ROLES.ADMIN);
    }

}
