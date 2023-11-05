package com.brano.oop2.controllers;

import com.brano.oop2.App;
import com.brano.oop2.db.daos.DAOExceptions;
import com.brano.oop2.models.users.Employee;
import com.brano.oop2.models.users.ROLES;
import com.brano.oop2.models.users.TeamPerson;
import com.brano.oop2.models.users.UserModel;
import com.brano.oop2.router.ROUTES;
import com.brano.oop2.router.Router;
import com.brano.oop2.services.UserService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class UsersController {
    @FXML
    public AnchorPane updatePanel;

    @FXML
    public Label idLabel;
    @FXML
    public TextField usernameField;
    @FXML
    public CheckBox availableField;
    @FXML
    public ChoiceBox<ROLES> roleField;
    @FXML
    public Button saveButton;
    UserModel selectedUserModel;

    private final UserService userService;
    @FXML
    private TableView<UserModel> usersTable;

    Alert alert;


    public UsersController() {
        this.alert = new Alert(Alert.AlertType.ERROR);
        this.alert.setTitle("Chyba!");
        this.userService = UserService.getInstance();
    }

    @FXML
    private void initialize() {
        roleField.setItems(FXCollections.observableArrayList(ROLES.values()));
        var id = new TableColumn<UserModel, Integer>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("ID"));
        id.getStyleClass().add("font");

        var username = new TableColumn<UserModel, String>("Username");
        username.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));

        var role = new TableColumn<UserModel, ROLES>("Rola");
        role.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRole()));

        var available = new TableColumn<UserModel, Boolean>("Dostupný");
        available.setCellValueFactory(data -> {
            SimpleBooleanProperty ssb;
            ROLES mRole = data.getValue().getRole();
            if (Employee.roles.contains(mRole)) {
                ssb = new SimpleBooleanProperty(((Employee) data.getValue()).isAvailable());
            } else {
                ssb = new SimpleBooleanProperty(false);
            }
            return ssb;
        });
        available.setCellFactory(data -> new CheckBoxTableCell<>());

        var project = new TableColumn<UserModel, String>("Názov projektu");
        project.setCellValueFactory(data -> {
            SimpleStringProperty ssp;
            ROLES mRole = data.getValue().getRole();
            if (TeamPerson.roles.contains(mRole)) {
                ssp = new SimpleStringProperty(((TeamPerson) data.getValue()).getProject());
            } else {
                ssp = new SimpleStringProperty("");
            }
            return ssp;
        });

        var address = new TableColumn<UserModel, String>("Adresa");
        address.setCellValueFactory(new PropertyValueFactory<>("address"));

        usersTable.getColumns().setAll(id, username, role, available, project, address);
        this.addUpdateButton();
        usersTable.setItems(FXCollections.observableArrayList(userService.getUsers()));
    }

    /**
     * Add update button to users
     */
    private void addUpdateButton() {
        TableColumn<UserModel, Void> edit = new TableColumn<>("Upraviť");
        edit.styleProperty().set("-fx-alignment: CENTER");
        var cellFactory = new Callback<TableColumn<UserModel, Void>, TableCell<UserModel, Void>>() {
            @Override
            public TableCell<UserModel, Void> call(final TableColumn<UserModel, Void> param) {
                return new TableCell<>() {

                    private final Button btn = new Button("Uprav");

                    {
                        btn.setOnAction((ActionEvent event) -> setUser(getTableView().getItems().get(getIndex())));
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
        usersTable.getColumns().add(edit);
    }

    /**
     * Set the current user to update panel
     *
     * @param userModel User model
     */
    private void setUser(UserModel userModel) {
        selectedUserModel = userModel;
        idLabel.setText(String.valueOf(userModel.getID()));
        usernameField.setText(userModel.getUsername());
        ROLES mRole = userModel.getRole();
        if (Employee.roles.contains(mRole)) {
            availableField.setSelected(((Employee) userModel).isAvailable());
            availableField.setDisable(false);
        } else
            availableField.setDisable(true);
        roleField.setValue(userModel.getRole());
        updatePanel.setDisable(false);

    }



    /**
     * Updates the current user
     */
    public void update() {
        var result = this.userService.validateUser(usernameField.getText(), selectedUserModel);
        if(result != null){
            alert.setHeaderText(result);
            alert.show();
            return;
        }
        String[] address = selectedUserModel.getAddress().split("~");
        //BUYER
        try {
            switch (roleField.getValue()){
                case BUYER:
                    this.userService.updateBuyer(selectedUserModel, usernameField.getText(), address);
                    break;
                case DEALMAKER:
                    this.userService.updateDealmaker(selectedUserModel, usernameField.getText(), address, availableField.isSelected() );
                    break;
                case ADMIN:
                    this.userService.updateAdmin(selectedUserModel, usernameField.getText(), address);
                    break;
                case DEVELOPER:
                    this.userService.updateDeveloper(selectedUserModel, usernameField.getText(), address, availableField.isSelected());
                    break;
                case TEAM_MANAGER:
                    this.userService.updateTeamManager(selectedUserModel, usernameField.getText(), address, availableField.isSelected());
                    break;
            }
        } catch (DAOExceptions.ModelSaveException e) {
            alert.setHeaderText("Nepodarilo sa upraviť užívateľa!");
            alert.show();
            System.out.println(e);
            return;
        }
        usersTable.setItems(FXCollections.observableArrayList(userService.getUsers()));
        usersTable.refresh();
        alert.setTitle("Úspech!");
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Užívateľ úspešne upravený!");
        alert.show();
    }

    /**
     * Toggles options
     */
    public void options() {
        App.optionsToggle();
    }

    /**
     * Route to deals
     */
    public void goDeals() {
        Router.goTo(ROUTES.DEALS);
    }

    /**
     * Route to tasks
     */
    public void goTasks() {
        Router.goTo(ROUTES.TASKS);
    }

    /**
     * This is called when role is changed in choice box
     */
    public void changeRole() {
        this.availableField.setDisable(this.roleField.getValue() != ROLES.DEALMAKER);
    }
}
