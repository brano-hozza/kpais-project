package com.brano.oop2.controllers;

import com.brano.oop2.App;
import com.brano.oop2.db.daos.DealDAO;
import com.brano.oop2.db.daos.UserDAO;
import com.brano.oop2.helpers.InputHelper;
import com.brano.oop2.models.deals.DEAL_TYPE;
import com.brano.oop2.models.users.Buyer;
import com.brano.oop2.models.users.Dealmaker;
import com.brano.oop2.router.ROUTES;
import com.brano.oop2.router.Router;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class NewDealController {
    @FXML
    public TextField project_name;
    @FXML
    public TextField price;
    @FXML
    public ChoiceBox<DEAL_TYPE> deal_type;
    @FXML
    public TextField product_count;
    @FXML
    public Label dealmaker;
    @FXML
    public AnchorPane addPanel;
    public Button createButton;
    DealDAO dealDAO;
    UserDAO userDAO;
    private Dealmaker dm = null;

    public NewDealController() {
        this.dealDAO = DealDAO.getInstance();
        this.userDAO = UserDAO.getInstance();
    }

    @FXML
    private void initialize() {
        InputHelper.fixPriceInput(price);
        InputHelper.fixCountInput(product_count);
        deal_type.setItems(FXCollections.observableArrayList(DEAL_TYPE.values()));
        Buyer by = (Buyer) App.activeUserModel;
        DEAL_STATUS status = by.checkExistingDeal();
        if (status == DEAL_STATUS.NONE) {
            getValDms();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (status == DEAL_STATUS.PENDING) {
            addPanel.setDisable(true);
            alert.setTitle("Zákazka");
            alert.setHeaderText("Existujúca zákazka!");
            alert.setContentText("Je nam to ľúto ale vaša zákazka zatiaľ nebol spracovaná");
            alert.setOnCloseRequest(e -> Router.goTo(ROUTES.LOGIN));
            alert.show();
            return;
        }
        alert.setTitle("Zákazka");
        alert.setHeaderText("Zákazka hotová!");
        alert.setContentText("S radosťou vám oznamujeme že vaša zákazka je hotová! Môžete pokračovať ďalšou objednávkou");
        alert.show();
        getValDms();

    }

    /**
     * Change deal type
     */
    public void changeDeal() {
        if (deal_type.getValue() == DEAL_TYPE.PORTFOLIO) {
            product_count.setDisable(true);
        }
        if (deal_type.getValue() == DEAL_TYPE.ESHOP) {
            product_count.setDisable(false);
        }
    }

    /**
     * Creates new deal
     */
    public void createDeal() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba!");

        String projectName = this.project_name.getText();
        double price = 0;
        try {
            price = Double.parseDouble(this.price.getText());
        } catch (NumberFormatException e) {
            alert.setHeaderText("Zadajte cenu!");
            alert.show();
            return;
        }
        DEAL_TYPE dealType = this.deal_type.getValue();
        int productCount = Integer.parseInt(this.product_count.getText());
        try {
            ((Buyer) App.activeUserModel).createNewDeal(projectName, dealType, dm, price, productCount);
        } catch (Buyer.ShortNameException e) {
            alert.setHeaderText("Názov projektu musí mať aspoň 5 znakov!");
            alert.show();
        } catch (Buyer.LowPriceException e) {
            alert.setHeaderText("Zadajte cenu vacsiu ako 1!");
            alert.show();
        } catch (Buyer.NoDealTypeExeption e) {
            alert.setHeaderText("Zvoľte druh zákazky!");
            alert.show();
        } catch (Buyer.NeedAmountException e) {
            alert.setHeaderText("Zadajte počet produktov!");
            alert.show();
        } catch (Buyer.IncorrectDealExceptions e) {
            e.printStackTrace();
            alert.setHeaderText("Nespavne vstupne pole!");
            alert.show();
        } finally {
            alert.setTitle("Super!");
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Úspešne vytvorená zákazka!");
            alert.show();
            List<Dealmaker> dms = userDAO.getAvailableDealmakers();
            if (dms.size() > 0) {
                dm = dms.get(0);
                dealmaker.setText(dm.getUsername());
            } else {
                addPanel.setDisable(true);
                dealmaker.setText("Žiaden voľný dealmaker :(");
            }
        }

    }

    /**
     * Gets and sets the valid dealmaker for current deal
     */
    private void getValDms() {
        List<Dealmaker> dms = userDAO.getAvailableDealmakers();
        if (dms.size() > 0) {
            dm = dms.get(0);
            dealmaker.setText(dm.getUsername());
        } else {
            addPanel.setDisable(true);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chyba!");
            alert.setHeaderText("Nedostatok dealmakerov :(");
            alert.setContentText("Je nám to ľúto ale nemáme žiadneho voľného dealmakera na spracovanie zmluvy :/");
            alert.show();
        }
    }

    /**
     * Toggles the options windows
     */
    public void options() {
        App.optionsToggle();
    }
}
