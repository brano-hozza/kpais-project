package com.brano.oop2.controllers;

import com.brano.oop2.App;
import com.brano.oop2.db.daos.UserDAO;
import com.brano.oop2.models.users.ROLES;
import com.brano.oop2.models.users.UserModel;
import com.brano.oop2.router.ROUTES;
import com.brano.oop2.router.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController {
    UserDAO userDAO;
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;

    public LoginController() {
        this.userDAO = UserDAO.getInstance();
    }

    /**
     * Function takes control of login
     */
    public void login() {
        UserModel userModel = this.userDAO.login(usernameInput.getText(), passwordInput.getText());

        if (userModel != null) {
            App.activeUserModel = userModel;
            if (userModel.getRole() == ROLES.BUYER) {
                Router.goTo(ROUTES.NEW_DEAL);
                return;
            }
            if (userModel.getRole() == ROLES.DEVELOPER || userModel.getRole() == ROLES.TEAM_MANAGER) {
                Router.goTo(ROUTES.TASKS);
                return;
            }
            Router.goTo(ROUTES.DEALS);
            return;
        }
        Alert alert;
        alert = new Alert(Alert.AlertType.ERROR, "Neplatne udaje :(");
        alert.show();
    }

    /**
     * Check for entering the ENTER key
     *
     * @param keyEvent Current pressed key
     */
    public void checkKey(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) login();
    }

    /**
     * Route to register
     */
    public void gotoRegister() {
        Router.goTo(ROUTES.REGISTER);
    }
}
