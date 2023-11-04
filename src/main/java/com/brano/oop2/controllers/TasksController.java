package com.brano.oop2.controllers;

import com.brano.oop2.App;
import com.brano.oop2.db.daos.DAOExceptions;
import com.brano.oop2.db.daos.DealDAO;
import com.brano.oop2.db.daos.TaskDAO;
import com.brano.oop2.db.daos.UserDAO;
import com.brano.oop2.helpers.InputHelper;
import com.brano.oop2.models.tasks.Basic;
import com.brano.oop2.models.tasks.TASK_STATE;
import com.brano.oop2.models.tasks.TASK_TYPE;
import com.brano.oop2.models.tasks.TaskModel;
import com.brano.oop2.models.users.Developer;
import com.brano.oop2.models.users.ROLES;
import com.brano.oop2.models.users.TeamManager;
import com.brano.oop2.router.ROUTES;
import com.brano.oop2.router.Router;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.sql.Date;
import java.time.ZoneId;

public class TasksController {
    public TableView<TaskModel> tasksTable;
    public AnchorPane TaskPanel;
    public Label id;
    public TextField task_name;
    public ChoiceBox<TASK_TYPE> type;
    public ChoiceBox<TASK_STATE> task_state;
    public DatePicker date_start;
    public DatePicker date_end;
    public ChoiceBox<TeamManager> managers;
    public ChoiceBox<Developer> developers;
    public Label project_name;
    public Button gotoDealsButton;
    public Button gotoUsersButton;
    public Button updateButton;
    public Button createTaskButton;
    public ChoiceBox<TASK_STATE> task_state1;
    public Button updateButton1;
    public Label state_label;
    public AnchorPane CompletePane;
    public Label projectNameLabel;

    TaskDAO taskDAO;
    DealDAO dealDAO;
    UserDAO userDAO;

    TaskModel selectedTask;

    /**
     * Prepare view based on logged in user
     */
    @FXML
    private void initialize() {

        this.userDAO = UserDAO.getInstance();
        this.taskDAO = TaskDAO.getInstance();
        this.dealDAO = DealDAO.getInstance();
        this.gotoUsersButton.setVisible(false);
        this.gotoDealsButton.setVisible(false);
        this.createTaskButton.setVisible(false);
        this.TaskPanel.setVisible(false);
        this.CompletePane.setVisible(false);
        this.setupTable();
        this.project_name.setText("");
        if (App.activeUserModel.getRole() == ROLES.TEAM_MANAGER) {
            TeamManager tm = (TeamManager) App.activeUserModel;
            if (!tm.getProject().equals("") && taskDAO.isProjectComplete(tm.getProject())) {
                this.completeTaskPanel();
            }
            createTaskButton.setVisible(!tm.getProject().equals(""));
        }
        if (App.activeUserModel.getRole() == ROLES.ADMIN) {
            this.gotoDealsButton.setVisible(true);
            this.gotoUsersButton.setVisible(true);
            this.TaskPanel.setVisible(true);
            this.setupEdit();
        }
        if (App.activeUserModel.getRole() == ROLES.DEVELOPER) {
            this.addChangeState();
            task_state1.setVisible(true);
            state_label.setVisible(true);
            updateButton1.setVisible(true);
        }
    }

    /**
     * Shows complete task panel
     */
    private void completeTaskPanel() {
        this.CompletePane.setVisible(true);
        projectNameLabel.setText(((TeamManager) App.activeUserModel).getProject());
    }

    /**
     * Add possibility for developer to change task state
     */
    private void addChangeState() {
        task_state1.setItems(FXCollections.observableArrayList(TASK_STATE.values()));
        TableColumn<TaskModel, Void> edit = new TableColumn<>("Zmenit stav");
        edit.styleProperty().set("-fx-alignment: CENTER");
        Callback<TableColumn<TaskModel, Void>, TableCell<TaskModel, Void>> cellFactory = new Callback<TableColumn<TaskModel, Void>, TableCell<TaskModel, Void>>() {
            @Override
            public TableCell<TaskModel, Void> call(final TableColumn<TaskModel, Void> param) {
                return new TableCell<TaskModel, Void>() {

                    private final Button btn = new Button("Zmen");

                    {
                        btn.setOnAction((ActionEvent event) -> setTask(getTableView().getItems().get(getIndex())));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        edit.setCellFactory(cellFactory);
        tasksTable.getColumns().add(edit);
    }

    /**
     * Setup task edit for admin
     */
    private void setupEdit() {

        this.setComboBox();
        this.addUpdateButton();
        type.setItems(FXCollections.observableArrayList(TASK_TYPE.values()));
        task_state.setItems(FXCollections.observableArrayList(TASK_STATE.values()));
        TaskPanel.setVisible(true);
    }

    /**
     * Setup combo boxes data
     */
    private void setComboBox() {
        managers.setConverter(new InputHelper.UserConverter<>());
        developers.setConverter(new InputHelper.UserConverter<>());
        managers.setItems(FXCollections.observableArrayList(userDAO.getAll(ROLES.TEAM_MANAGER)));
        developers.setItems(FXCollections.observableArrayList(userDAO.getAll(ROLES.DEVELOPER)));
        if (selectedTask != null) {
            managers.setValue(((Basic) selectedTask).getTm());
            developers.setValue(((Basic) selectedTask).getDev());
        }
    }

    /**
     * Add update button to table for editing
     */
    private void addUpdateButton() {
        TableColumn<TaskModel, Void> edit = new TableColumn<>("Upraviť");
        edit.styleProperty().set("-fx-alignment: CENTER");
        Callback<TableColumn<TaskModel, Void>, TableCell<TaskModel, Void>> cellFactory = new Callback<TableColumn<TaskModel, Void>, TableCell<TaskModel, Void>>() {
            @Override
            public TableCell<TaskModel, Void> call(final TableColumn<TaskModel, Void> param) {
                return new TableCell<TaskModel, Void>() {

                    private final Button btn = new Button("Uprav");

                    {
                        btn.setOnAction((ActionEvent event) -> setTask(getTableView().getItems().get(getIndex())));
                    }
                    

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                            setGraphic(null);
                        } else {
                            TaskModel task = (TaskModel) getTableRow().getItem();
                            if (((Basic) task).isProject_done()) {
                                setGraphic(null);
                            } else {
                                setGraphic(btn);
                            }
                        }
                    }
                };
            }
        };
        edit.setCellFactory(cellFactory);
        tasksTable.getColumns().add(edit);
    }

    /**
     * Prepare edit panel for task editing
     *
     * @param taskModel Task which we want to edit
     */
    private void setTask(TaskModel taskModel) {
        this.selectedTask = taskModel;
        this.id.setText(String.valueOf(taskModel.getID()));
        this.task_name.setText(taskModel.getName());
        this.type.setValue(taskModel.getType());
        this.task_state.setValue(taskModel.getState());
        this.date_start.setValue(((Basic) taskModel).getStart_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        this.date_end.setValue(((Basic) taskModel).getEnd_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        this.managers.setValue(((Basic) taskModel).getTm());
        this.developers.setValue(((Basic) taskModel).getDev());
        this.project_name.setText(((Basic) taskModel).getProject_name());
        this.TaskPanel.setDisable(false);
        this.updateButton1.setDisable(false);
        this.task_state1.setDisable(false);
        this.task_state1.setValue(taskModel.getState());
    }

    /**
     * Setups table of tasks
     */
    private void setupTable() {
        TableColumn<TaskModel, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<TaskModel, String> task_name = new TableColumn<>("Názov úlohy");
        task_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<TaskModel, String> task_type = new TableColumn<>("Typ");
        task_type.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType().toString()));
        TableColumn<TaskModel, String> task_state = new TableColumn<>("Stav");
        task_state.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getState().toString()));
        TableColumn<TaskModel, String> start_date = new TableColumn<>("Zaciatok");
        start_date.setCellValueFactory(data -> new SimpleStringProperty(((Basic) data.getValue()).getStart_date().toString()));
        TableColumn<TaskModel, String> end_date = new TableColumn<>("Koniec");
        end_date.setCellValueFactory(data -> new SimpleStringProperty(((Basic) data.getValue()).getEnd_date().toString()));
        TableColumn<TaskModel, String> manager = new TableColumn<>("Manazer");
        manager.setCellValueFactory(data -> new SimpleStringProperty(((Basic) data.getValue()).getTm().getUsername()));
        TableColumn<TaskModel, String> dev = new TableColumn<>("Developer");
        dev.setCellValueFactory(data -> new SimpleStringProperty(((Basic) data.getValue()).getDev().getUsername()));
        TableColumn<TaskModel, String> project = new TableColumn<>("Projekt");
        project.setCellValueFactory(data -> new SimpleStringProperty(((Basic) data.getValue()).getProject_name()));
        //noinspection unchecked
        tasksTable.getColumns().setAll(id, task_name, task_type, task_state, start_date, end_date, manager, dev, project);
        tasksTable.setItems(FXCollections.observableArrayList(taskDAO.getDoableTasksFor(App.activeUserModel)));
    }

    /**
     * Toggles options
     */
    public void options() {
        App.optionsToggle();
    }

    /**
     * Updates the row and save new data
     */
    public void update() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        selectedTask.setName(task_name.getText());
        if (App.activeUserModel.getRole() == ROLES.DEVELOPER)
            selectedTask.setState(task_state1.getValue());
        else
            selectedTask.setState(task_state.getValue());
        selectedTask.setType(type.getValue());
        ((Basic) selectedTask).setDev(developers.getValue());
        ((Basic) selectedTask).setTm(managers.getValue());
        ((Basic) selectedTask).setStart_date(
                Date.from(date_start.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        ((Basic) selectedTask).setEnd_date(
                Date.from(date_end.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        ((Basic) selectedTask).setProject_name(project_name.getText());
        try {
            taskDAO.update(selectedTask);
        } catch (DAOExceptions.ModelNotFoundException e) {
            e.printStackTrace();
        }
        this.setComboBox();
        this.setTask(selectedTask);
        if (App.activeUserModel.getRole() == ROLES.DEVELOPER)
            tasksTable.setItems(FXCollections.observableArrayList(taskDAO.getDoableTasksFor(App.activeUserModel)));
        else
            tasksTable.setItems(FXCollections.observableArrayList(taskDAO.getAll()));
        tasksTable.refresh();


        alert.setTitle("Super!");
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Úspešne upravena uloha!");
        alert.show();
    }

    /**
     * Route to deals
     */
    public void goDeals() {
        Router.goTo(ROUTES.DEALS);
    }

    /**
     * Routes to users
     */
    public void goUsers() {
        Router.goTo(ROUTES.USERS);
    }

    /**
     * Routes to new task creation
     */
    public void goCreateTask() {
        Router.goTo(ROUTES.NEW_TASK);
    }

    /**
     * Sets the project as completed
     */
    public void completeProject() {
        ((TeamManager) App.activeUserModel).completeProject();
        tasksTable.setItems(FXCollections.observableArrayList(taskDAO.getDoableTasksFor(App.activeUserModel)));
        tasksTable.refresh();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Projekt");
        alert.setHeaderText("Projekt hotový!");
        alert.show();
        this.CompletePane.setVisible(false);
    }
}
