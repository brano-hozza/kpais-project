package com.brano.oop2.db.daos;

import com.brano.oop2.db.daos.DAOExceptions.ModelNotFoundException;
import com.brano.oop2.models.tasks.Basic;
import com.brano.oop2.models.tasks.TASK_STATE;
import com.brano.oop2.models.tasks.TaskModel;
import com.brano.oop2.models.users.UserModel;

import java.util.ArrayList;

public class TaskDAO implements DAO<TaskModel> {
    public static int lastID = 0;
    static TaskDAO instance;
    ArrayList<TaskModel> taskModels = new ArrayList<>();

    private TaskDAO() {

    }

    public static TaskDAO getInstance() {
        if (instance == null) {
            instance = new TaskDAO();
        }
        return instance;
    }

    @Override
    public void update(TaskModel item) throws ModelNotFoundException {
        int i = 0;
        for (TaskModel task : taskModels) {
            if (task.getID() == (item).getID()) break;
            i++;
        }
        taskModels.set(i, item);
    }

    @Override
    public void save(TaskModel item) {
        item.setID(TaskDAO.lastID);
        taskModels.add(item);
        TaskDAO.lastID++;
    }

    @Override
    public TaskModel get(int ID) throws ModelNotFoundException {
        for (TaskModel task : taskModels) {
            if (task.getID() == ID) {
                return task;
            }
        }
        throw new ModelNotFoundException();
    }

    @Override
    public ArrayList<TaskModel> getAll() {
        return taskModels;
    }

    @Override
    public void setAll(ArrayList<TaskModel> list) {
        taskModels.addAll(list);
        lastID = list.size();
    }

    /**
     * Get model by task name
     *
     * @param taskName Name of task
     * @return Task model
     */
    public TaskModel get(String taskName) {
        for (TaskModel taskModel : taskModels) {
            if (taskModel.getName().equals(taskName)) {
                return taskModel;
            }
        }
        return null;
    }

    /**
     * Get tasks that are for current user
     *
     * @param userModel Should be developer/team manager
     * @return ArrayList of TaskModels
     */
    public ArrayList<TaskModel> getDoableTasksFor(UserModel userModel) {
        ArrayList<TaskModel> doableTasks = new ArrayList<>();
        for (TaskModel task : taskModels) {
            if (task instanceof Basic) {
                Basic bs = (Basic) task;
                if (!bs.isProject_done() && (bs.getDev().getID() == userModel.getID() || bs.getTm().getID() == userModel.getID()))
                    doableTasks.add(bs);
            }
        }
        return doableTasks;
    }

    /**
     * Checks if project is done
     *
     * @param projectName Name of project
     * @return TRUE - completed / FALSE - in progress
     */
    public boolean isProjectComplete(String projectName) {
        boolean atLeastOne = false;
        for (TaskModel task : taskModels) {
            if (task instanceof Basic) {
                Basic bs = (Basic) task;
                if (bs.getProject_name().equals(projectName) && !(bs.getState() == TASK_STATE.DONE || bs.getState() == TASK_STATE.CANCELED))
                    return false;
                if (bs.getProject_name().equals(projectName))
                    atLeastOne = true;
            }
        }
        return atLeastOne;
    }

    /**
     * Set all tasks in project as done
     *
     * @param projectName Name of the project
     */
    public void completeProject(String projectName) {
        for (TaskModel task : taskModels) {
            if (task instanceof Basic) {
                Basic bs = (Basic) task;
                if (bs.getProject_name().equals(projectName))
                    bs.complete();
            }
        }
    }
}
