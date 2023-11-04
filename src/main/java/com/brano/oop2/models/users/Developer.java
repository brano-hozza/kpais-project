package com.brano.oop2.models.users;

public class Developer extends TeamPerson {
    private static final long serialVersionUID = 1L;

    public enum DEVELOPER_TYPE {
        FRONTEND, BACKEND
    }

    private DEVELOPER_TYPE type;


    public String getType() {
        if (type == DEVELOPER_TYPE.FRONTEND) return "FE";
        if (type == DEVELOPER_TYPE.BACKEND) return "BE";
        return "";
    }

    public void setType(String type) {
        if (type.equals("FE"))
            this.type = DEVELOPER_TYPE.FRONTEND;
        if (type.equals("BE"))
            this.type = DEVELOPER_TYPE.BACKEND;
    }


    public Developer(int ID, String username, String password, boolean available, String project, String type) {
        super(ID, username, password, ROLES.DEVELOPER, available, project);
        this.setType(type);
    }
}
