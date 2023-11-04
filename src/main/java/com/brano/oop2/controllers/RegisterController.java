package com.brano.oop2.controllers;

import com.brano.oop2.db.daos.UserDAO;
import com.brano.oop2.helpers.InputHelper;
import com.brano.oop2.models.users.UserModel;
import com.brano.oop2.router.ROUTES;
import com.brano.oop2.router.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    public TextField username;
    @FXML
    public TextField password;
    @FXML
    public TextField passwordAgain;
    @FXML
    public TextField city;
    @FXML
    public TextField street;
    @FXML
    public TextField number;

    UserDAO userDAO;

    public RegisterController() {
        this.userDAO = UserDAO.getInstance();
    }

    @FXML
    public void initialize() {
        InputHelper.fixCountInput(number);
    }

    /**
     * Registers new user
     */
    public void register() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba!");
        if (username.getText().length() < 5) {
            alert.setHeaderText("Meno musí mať aspoň 5 znakov!");
            alert.show();
            return;
        }
        if (userDAO.get(username.getText()) != null) {
            alert.setHeaderText("Tento pouzivatel uz existuje!");
            alert.show();
            return;
        }
        if (password.getText().length() < 6) {
            alert.setHeaderText("Heslo musí mať aspoň 6 znakov!");
            alert.show();
            return;
        }
        if (!password.getText().equals(passwordAgain.getText())) {
            alert.setHeaderText("Heslá sa nezhodujú!");
            alert.show();
            return;
        }
        if (city.getText().length() < 3) {
            alert.setHeaderText("Nazov mesta musi mat aspon 3 znaky!");
            alert.show();
            return;
        }
        if (street.getText().length() < 3) {
            alert.setHeaderText("Nazov ulice musi mat aspon 3 znaky!");
            alert.show();
            return;
        }
        if (Integer.parseInt(number.getText()) < 1) {
            alert.setHeaderText("Cislo ulice musi byt vacsie ako 0!");
            alert.show();
            return;
        }
        UserModel ud = userDAO.register(username.getText(), password.getText());
        ud.setAddress(city.getText(), street.getText(), Integer.parseInt(number.getText()));
        alert.setTitle("Super!");
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Úspešne registrovaný!");
        alert.setOnCloseRequest(event -> gotoLogin());
        alert.show();

    }

    /**
     * Routes to login
     */
    public void gotoLogin() {
        Router.goTo(ROUTES.LOGIN);
    }
}
