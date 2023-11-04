package com.brano.oop2.models.users;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Employee extends UserModel {
    private static final long serialVersionUID = 1L;
    public static final ArrayList<ROLES> roles = new ArrayList<>(Arrays.asList(ROLES.TEAM_MANAGER, ROLES.DEVELOPER, ROLES.DEALMAKER));
    protected boolean available;

    public boolean isAvailable() {
        return available;
    }

    public Employee(int ID, String username, String password, ROLES role, boolean available) {
        super(ID, username, password, role);
        this.available = available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }


}
