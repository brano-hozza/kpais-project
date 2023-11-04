package com.brano.oop2.controllers;

import com.brano.oop2.App;
import com.brano.oop2.db.daos.DealDAO;
import com.brano.oop2.db.daos.TaskDAO;
import com.brano.oop2.db.daos.UserDAO;
import com.brano.oop2.helpers.InputHelper;
import com.brano.oop2.models.tasks.TASK_TYPE;
import com.brano.oop2.models.users.Developer;
import com.brano.oop2.models.users.ROLES;
import com.brano.oop2.models.users.TeamManager;
import com.brano.oop2.router.ROUTES;
import com.brano.oop2.router.Router;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.time.ZoneId;
import java.util.Date;

public class NewTaskController {
    public Label project_name;
    public Label manager_name;
    public ChoiceBox<Developer> developer;
    public TextField task_name;
    public ChoiceBox<TASK_TYPE> task_type;
    public DatePicker end_date;
    public AnchorPane addPanel;
    DealDAO dealDAO;
    TaskDAO taskDAO;
    UserDAO userDAO;

    @FXML
    private void initialize() {

        this.dealDAO = DealDAO.getInstance();
        this.taskDAO = TaskDAO.getInstance();
        this.userDAO = UserDAO.getInstance();
        this.setComboBox();
        project_name.setText(((TeamManager) App.activeUserModel).getProject());
        manager_name.setText(App.activeUserModel.getUsername());
    }

    /**
     * Setup combo boxes
     */
    private void setComboBox() {
        developer.setConverter(new InputHelper.UserConverter<>());
        developer.setItems(FXCollections.observableArrayList(userDAO.getAll(ROLES.DEVELOPER)));
        task_type.setItems(FXCollections.observableArrayList(TASK_TYPE.values()));
    }

    /**
     * Toggles options
     */
    public void options() {
        App.optionsToggle();
    }

    /**
     * Creates new task
     */
    public void createTask() {
        ((TeamManager) App.activeUserModel).addBasicTask(task_name.getText(), task_type.getValue(), Date.from(end_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), developer.getValue());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Super!");
        alert.setHeaderText("Úspešne vytvorená uloha!");
        alert.setOnCloseRequest(event -> gotoTasks());
        alert.show();
    }

    /**
     * Route to tasks
     */
    public void gotoTasks() {
        Router.goTo(ROUTES.TASKS);
    }

}
