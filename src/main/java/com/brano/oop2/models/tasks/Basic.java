package com.brano.oop2.models.tasks;

import com.brano.oop2.models.users.Developer;
import com.brano.oop2.models.users.TeamManager;

import java.util.Date;

public class Basic extends TaskModel {
    private static final long serialVersionUID = 1L;
    private Date start_date;
    private Date end_date;
    private TeamManager tm;
    private Developer dev;
    private String project_name;
    private boolean project_done = false;

    public Basic(String name, TASK_TYPE type, TASK_STATE state, Date start_date, Date end_date, TeamManager teamManager, Developer dev, String project_name) {
        super(name, type, state);
        this.start_date = start_date;
        this.end_date = end_date;
        this.tm = teamManager;
        this.dev = dev;
        this.project_name = project_name;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public TeamManager getTm() {
        return tm;
    }

    public void setTm(TeamManager teamManager) {
        this.tm = teamManager;
    }

    public Developer getDev() {
        return dev;
    }

    public void setDev(Developer dev) {
        this.dev = dev;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public boolean isProject_done() {
        return project_done;
    }

    /**
     * set the tasks project as done
     */
    public void complete() {
        this.project_done = true;
    }
}
