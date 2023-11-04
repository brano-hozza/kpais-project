package com.brano.oop2.models.tasks;

import com.brano.oop2.models.Model;

public abstract class TaskModel implements Model {
    private static final long serialVersionUID = 1L;
    private int ID;

    private String name;

    private TASK_TYPE type;

    private TASK_STATE state;

    public TaskModel(String name, TASK_TYPE type, TASK_STATE state) {
        this.name = name;
        this.type = type;
        this.state = state;
    }

    @Override
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TASK_TYPE getType() {
        return type;
    }

    public void setType(TASK_TYPE type) {
        this.type = type;
    }

    public TASK_STATE getState() {
        return state;
    }

    public void setState(TASK_STATE state) {
        this.state = state;
    }
}
