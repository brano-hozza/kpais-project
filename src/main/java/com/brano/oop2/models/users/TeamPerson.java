package com.brano.oop2.models.users;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class TeamPerson extends Employee {
    private static final long serialVersionUID = 1L;
    protected String project;
    public static final ArrayList<ROLES> roles = new ArrayList<>(Arrays.asList(ROLES.TEAM_MANAGER, ROLES.DEVELOPER));

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }


    public TeamPerson(int ID, String username, String password, ROLES role, boolean available, String project) {
        super(ID, username, password, role, available);
        this.project = project;
    }
}
