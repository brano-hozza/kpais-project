package com.brano.oop2.models.users;

import com.brano.oop2.db.daos.DealDAO;
import com.brano.oop2.db.daos.TaskDAO;
import com.brano.oop2.models.tasks.Basic;
import com.brano.oop2.models.tasks.TASK_STATE;
import com.brano.oop2.models.tasks.TASK_TYPE;
import com.brano.oop2.models.tasks.TaskModel;

import java.util.Date;

public class TeamManager extends TeamPerson {
    private static final long serialVersionUID = 1L;

    public TeamManager(int ID, String username, String password, boolean available, String project) {
        super(ID, username, password, ROLES.TEAM_MANAGER, available, project);
    }

    /**
     * Sets project as complete
     */
    public void completeProject() {
        TaskDAO.getInstance().completeProject(this.getProject());
        DealDAO.getInstance().completeProject(this.getProject());
        this.project = "";
        this.available = true;
    }

    /**
     * Adds basic task to project and sets developer
     *
     * @param name      Name of the task
     * @param type      Task type
     * @param dateEnd   End date for task
     * @param developer Developer to complete task
     */
    public void addBasicTask(String name, TASK_TYPE type, Date dateEnd, Developer developer) {
        TaskModel task = new Basic(name, type, TASK_STATE.PENDING, new Date(System.currentTimeMillis()),
                dateEnd, this, developer, this.project);
        TaskDAO.getInstance().save(task);

    }
}
