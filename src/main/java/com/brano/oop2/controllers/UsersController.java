package com.brano.oop2.controllers;

import com.brano.oop2.App;
import com.brano.oop2.db.daos.DAOExceptions;
import com.brano.oop2.db.daos.UserDAO;
import com.brano.oop2.models.deals.DealModel;
import com.brano.oop2.models.users.*;
import com.brano.oop2.router.ROUTES;
import com.brano.oop2.router.Router;
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
    public Label id;
    @FXML
    public TextField username;
    @FXML
    public CheckBox available;
    @FXML
    public ChoiceBox<ROLES> role;
    @FXML
    public Button saveBtn;
    UserModel selectedUserModel;
    UserDAO userDAO;
    @FXML
    private TableView<UserModel> usersTable;


    public UsersController() {
        this.userDAO = UserDAO.getInstance();
    }

    @FXML
    private void initialize() {
        role.setItems(FXCollections.observableArrayList(ROLES.values()));
        TableColumn<UserModel, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("ID"));
        id.getStyleClass().add("font");
        TableColumn<UserModel, String> username = new TableColumn<>("Username");
        username.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        TableColumn<UserModel, ROLES> role = new TableColumn<>("Rola");
        role.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getRole()));
        TableColumn<UserModel, Boolean> available = new TableColumn<>("Dostupný");
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
        TableColumn<UserModel, String> project = new TableColumn<>("Názov projektu");
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
        TableColumn<UserModel, String> address = new TableColumn<>("Adresa");
        address.setCellValueFactory(new PropertyValueFactory<>("address"));

        //noinspection unchecked
        usersTable.getColumns().setAll(id, username, role, available, project, address);
        this.addUpdateButton();
        usersTable.setItems(FXCollections.observableArrayList(userDAO.getUsers()));
    }

    /**
     * Add update button to users
     */
    private void addUpdateButton() {
        TableColumn<UserModel, Void> edit = new TableColumn<>("Upraviť");
        edit.styleProperty().set("-fx-alignment: CENTER");
        Callback<TableColumn<UserModel, Void>, TableCell<UserModel, Void>> cellFactory = new Callback<TableColumn<UserModel, Void>, TableCell<UserModel, Void>>() {
            @Override
            public TableCell<UserModel, Void> call(final TableColumn<UserModel, Void> param) {
                return new TableCell<UserModel, Void>() {

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
        id.setText(String.valueOf(userModel.getID()));
        username.setText(userModel.getUsername());
        ROLES mRole = userModel.getRole();
        if (Employee.roles.contains(mRole)) {
            available.setSelected(((Employee) userModel).isAvailable());
            available.setDisable(false);
        } else
            available.setDisable(true);
        role.setValue(userModel.getRole());
        updatePanel.setDisable(false);

    }

    /**
     * Updates the current user
     */
    public void update() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba!");
        if (username.getText().length() < 5) {
            alert.setHeaderText("Meno užívateľa musí mať aspoň 5 znakov!");
            alert.show();
            return;
        }
        if (!selectedUserModel.getUsername().equals(username.getText()) && userDAO.get(username.getText()) != null) {
            alert.setHeaderText("Meno užívateľa musí byť jedinečné!");
            alert.show();
            return;
        }
        String[] address = selectedUserModel.getAddress().split("~");
        //BUYER
        try {
            if (role.getValue() == ROLES.BUYER) {
                DealModel deal = null;
                if (selectedUserModel instanceof Buyer)
                    deal = ((Buyer) selectedUserModel).getDeal();
                Buyer buyer = new Buyer(selectedUserModel.getID(), username.getText(), selectedUserModel.getPassword(), deal);
                buyer.setAddress(address[0], address[1], Integer.parseInt(address[2]));
                userDAO.update(buyer);
            }
            //DEALMAKER
            if (role.getValue() == ROLES.DEALMAKER) {
                String deal = "";
                boolean userAvailable = true;
                if (selectedUserModel instanceof Dealmaker)
                    deal = ((Dealmaker) selectedUserModel).getDeal();
                if (selectedUserModel instanceof Employee)
                    userAvailable = ((Employee) selectedUserModel).isAvailable();
                Dealmaker dm = new Dealmaker(selectedUserModel.getID(), username.getText(), selectedUserModel.getPassword(), userAvailable, deal);
                dm.setAvailable(available.isSelected());
                dm.setAddress(address[0], address[1], Integer.parseInt(address[2]));
                userDAO.update(dm);
            }
            //ADMIN
            if (role.getValue() == ROLES.ADMIN) {
                Admin admin = new Admin(selectedUserModel.getID(), username.getText(), selectedUserModel.getPassword());
                admin.setAddress(address[0], address[1], Integer.parseInt(address[2]));
                userDAO.update(admin);
            }
            //DEVELOPER
            if (role.getValue() == ROLES.DEVELOPER) {
                String project = "";
                if (selectedUserModel instanceof TeamPerson)
                    project = ((TeamPerson) selectedUserModel).getProject();
                String type = "FE";
                if (selectedUserModel instanceof Developer)
                    type = ((Developer) selectedUserModel).getType();
                Developer developer = new Developer(selectedUserModel.getID(), username.getText(), selectedUserModel.getPassword(), available.isSelected(), project, type);
                developer.setAddress(address[0], address[1], Integer.parseInt(address[2]));
                userDAO.update(developer);
            }
            //TEAM MANAGER
            if (role.getValue() == ROLES.TEAM_MANAGER) {
                String project = "";
                if (selectedUserModel instanceof TeamPerson)
                    project = ((TeamPerson) selectedUserModel).getProject();
                TeamManager tm = new TeamManager(selectedUserModel.getID(), username.getText(), selectedUserModel.getPassword(), available.isSelected(), project);
                tm.setAddress(address[0], address[1], Integer.parseInt(address[2]));
                userDAO.update(tm);
            }
        } catch (DAOExceptions.ModelSaveException e) {
            alert.setHeaderText("Nepodarilo sa upraviť užívateľa!");
            alert.show();
            System.out.println(e);
            return;
        }
        usersTable.setItems(FXCollections.observableArrayList(userDAO.getAll()));
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
        this.available.setDisable(this.role.getValue() != ROLES.DEALMAKER);
    }
}
