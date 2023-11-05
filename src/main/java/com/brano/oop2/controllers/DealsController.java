package com.brano.oop2.controllers;

import com.brano.oop2.App;
import com.brano.oop2.db.daos.DAOExceptions;
import com.brano.oop2.helpers.InputHelper;
import com.brano.oop2.models.deals.DEAL_TYPE;
import com.brano.oop2.models.deals.DealModel;
import com.brano.oop2.models.deals.Eshop;
import com.brano.oop2.models.users.Buyer;
import com.brano.oop2.models.users.Dealmaker;
import com.brano.oop2.models.users.ROLES;
import com.brano.oop2.models.users.TeamManager;
import com.brano.oop2.router.ROUTES;
import com.brano.oop2.router.Router;
import com.brano.oop2.services.DealService;
import com.brano.oop2.services.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;


public class DealsController {
    @FXML
    public Button gotoUsersButton;
    @FXML
    public AnchorPane adminPanel;
    @FXML
    public Label idLabel;
    @FXML
    public ChoiceBox<Buyer> buyerChoiceBox;
    @FXML
    public ChoiceBox<Dealmaker> dealmakerChoiceBox;
    @FXML
    public TextField priceTextField;
    @FXML
    public ChoiceBox<DEAL_TYPE> dealTypeChoiceBox;
    @FXML
    public DatePicker datePicker;
    @FXML
    public TextField productCountTextField;
    @FXML
    public Button saveButton;
    @FXML
    public TextField projectNameTextField;
    @FXML
    public Button gotoTasksButton;
    @FXML
    public ChoiceBox<TeamManager> teamManagerChoiceBox;
    @FXML
    public Label teamManagerLabel;
    @FXML
    public Button updateManagerButton;
    @FXML
    public AnchorPane dealmakerPanel;
    @FXML
    private TableView<DealModel> dealsTable;

    DealModel selectedDeal = null;

    Alert alert;

    private final UserService userService;
    private final DealService dealService;

    public DealsController() {
        this.alert = new Alert(Alert.AlertType.ERROR);

        this.userService = UserService.getInstance();
        this.dealService = DealService.getInstance();
    }

    /**
     * Function which setups my view
     */
    @FXML
    private void initialize() {
        this.setupTable();
        this.dealmakerPanel.setVisible(false);
        this.adminPanel.setVisible(false);
        this.gotoUsersButton.setVisible(false);
        this.gotoTasksButton.setVisible(false);
        if (App.activeUserModel.getRole() == ROLES.ADMIN) {
            this.adminPanel.setVisible(true);
            this.gotoUsersButton.setVisible(true);
            this.gotoTasksButton.setVisible(true);
            this.setupEdit();
        }
        if (App.activeUserModel.getRole() == ROLES.DEALMAKER) {
            this.dealmakerPanel.setVisible(true);
            this.addCompleteButton();
            this.setupDealmakerPanel();
        }

    }

    private TableCell<DealModel, Void> createButtonCell() {
        return new TableCell<>() {

            private final Button btn = new Button("HOTOVO");

            {
                btn.setOnAction((ActionEvent event) -> completeProject());
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    var dm = (DealModel) getTableRow().getItem();
                    if (!dm.getDone() || dm.isReturned()) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            }
        };
    }

    /**
     * Adds complete button to deal
     */
    private void addCompleteButton() {
        TableColumn<DealModel, Void> edit = new TableColumn<>("Ukončiť projekt");
        edit.styleProperty().set("-fx-alignment: CENTER");
        var cellFactory = new Callback<TableColumn<DealModel, Void>, TableCell<DealModel, Void>>() {
            @Override
            public TableCell<DealModel, Void> call(final TableColumn<DealModel, Void> param) {
                return createButtonCell();
            }
        };
        edit.setCellFactory(cellFactory);
        dealsTable.getColumns().add(edit);
    }

    /**
     * Action to complete the deal
     */
    private void completeProject() {
        ((Dealmaker) App.activeUserModel).returnDeal();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Zákazka");
        alert.setHeaderText("Zákazka úspešne hotová!");
        alert.show();
        this.setTableData();
    }

    /**
     * Function prepares panel for dealmaker
     */
    private void setupDealmakerPanel() {
        this.addSetProjectManagerColumn();
        this.teamManagerChoiceBox.setConverter(new InputHelper.UserConverter<>());
        this.teamManagerChoiceBox.setItems(FXCollections.observableArrayList(this.userService.getUsers(ROLES.TEAM_MANAGER)));
    }

    /**
     * Function adds column with setting project to some manager
     */
    private void addSetProjectManagerColumn() {
        TableColumn<DealModel, Void> edit = new TableColumn<>("Upravit");
        edit.styleProperty().set("-fx-alignment: CENTER");
        var cellFactory = new Callback<TableColumn<DealModel, Void>, TableCell<DealModel, Void>>() {
            @Override
            public TableCell<DealModel, Void> call(final TableColumn<DealModel, Void> param) {
                return new TableCell<>() {

                    private final Button btn = new Button("Nastav manazera");

                    {
                        btn.setOnAction((ActionEvent event) -> setDeal(getTableView().getItems().get(getIndex())));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                            setGraphic(null);
                        } else {
                            var dm = (DealModel) getTableRow().getItem();
                            if (dm.getDone()) {
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
        dealsTable.getColumns().add(edit);
    }

    /**
     * Function for setting data to combo boxes
     */
    private void setComboBox() {
        dealmakerChoiceBox.setConverter(new InputHelper.UserConverter<>());
        buyerChoiceBox.setConverter(new InputHelper.UserConverter<>());
        dealmakerChoiceBox.setItems(FXCollections.observableArrayList(this.userService.getAvailableDealmakers()));
        buyerChoiceBox.setItems(FXCollections.observableArrayList(this.userService.getUsers(ROLES.BUYER)));
        if (selectedDeal != null) {
            dealmakerChoiceBox.setValue(selectedDeal.getDealmaker());
            buyerChoiceBox.setValue(selectedDeal.getBuyer());
        }
    }

    /**
     * Function that setups table rows
     */
    private void setupTable() {
        var id = new TableColumn<DealModel, Integer>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("ID"));

        var name = new TableColumn<DealModel, String>("Názov");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        var buyer = new TableColumn<DealModel, String>("Zákazník");
        buyer.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBuyer().getUsername()));

        var dealmaker = new TableColumn<DealModel, String>("Dealmaker");
        dealmaker.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDealmaker().getUsername()));

        var price = new TableColumn<DealModel, Double>("Cena");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        var date = new TableColumn<DealModel, String>("Dátum objednávky");
        date.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate().toString()));

        var product_count = new TableColumn<DealModel, Integer>("Počet produktov");
        product_count.setCellValueFactory(new PropertyValueFactory<>("productCount"));

        var project_done = new TableColumn<DealModel, Boolean>("Kompletný");
        project_done.setCellValueFactory(new PropertyValueFactory<>("done"));

        dealsTable.getColumns().setAll(id, name, buyer, dealmaker, price, date, product_count, project_done);
        this.setTableData();
    }

    /**
     * Function sets data in table by corresponding user whose is logged in
     */
    private void setTableData() {
        if (App.activeUserModel.getRole() == ROLES.DEALMAKER)
            dealsTable.setItems(FXCollections.observableArrayList(dealService.getAllDealsFor((Dealmaker) App.activeUserModel)));
        else
            dealsTable.setItems(FXCollections.observableArrayList(dealService.getAllDeals()));
        dealsTable.refresh();
    }

    private EventHandler<ActionEvent> getUpdateEvent() {
        return event -> {
            System.out.println("Okay");
            if (selectedDeal == null) {
                return;
            }
            if (dealTypeChoiceBox.getValue() != DEAL_TYPE.ESHOP) {
                productCountTextField.setDisable(true);
                return;
            }
            productCountTextField.setDisable(false);

            if (selectedDeal instanceof Eshop) {
                productCountTextField.setText(String.valueOf(((Eshop) selectedDeal).getProductCount()));
            } else {
                productCountTextField.setText("0");
            }

        };
    }

    /**
     * Function to prepare inputs for editing rows
     */
    private void setupEdit() {
        this.setComboBox();
        this.addUpdateButton();
        dealTypeChoiceBox.setItems(FXCollections.observableArrayList(DEAL_TYPE.values()));
        dealTypeChoiceBox.setOnAction(this.getUpdateEvent());
        InputHelper.fixPriceInput(priceTextField);
        InputHelper.fixCountInput(productCountTextField);
        adminPanel.setVisible(true);
    }

    /**
     * Function adds Update button column to table
     */
    private void addUpdateButton() {
        TableColumn<DealModel, Void> edit = new TableColumn<>("Upraviť");
        edit.styleProperty().set("-fx-alignment: CENTER");
        var cellFactory = new Callback<TableColumn<DealModel, Void>, TableCell<DealModel, Void>>() {
            @Override
            public TableCell<DealModel, Void> call(final TableColumn<DealModel, Void> param) {
                return new TableCell<>() {

                    private final Button btn = new Button("Uprav");

                    {
                        btn.setOnAction((ActionEvent event) -> setDeal(getTableView().getItems().get(getIndex())));
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
        dealsTable.getColumns().add(edit);
    }

    /**
     * Function fills the edit form with data
     *
     * @param dealModel Deal to be set
     */
    private void setDeal(DealModel dealModel) {
        selectedDeal = dealModel;

        teamManagerChoiceBox.setValue(dealModel.getTeamManager());

        idLabel.setText(String.valueOf(dealModel.getID()));
        projectNameTextField.setText(dealModel.getName());
        List<Dealmaker> dms = dealmakerChoiceBox.getItems();
        if (!dms.contains(selectedDeal.getDealmaker())) {
            dms.add(selectedDeal.getDealmaker());
        }
        dealmakerChoiceBox.setItems(FXCollections.observableArrayList(dms));
        dealmakerChoiceBox.setValue(dealModel.getDealmaker());
        buyerChoiceBox.setValue(dealModel.getBuyer());
        priceTextField.setText(String.valueOf(dealModel.getPrice()));
        dealTypeChoiceBox.setValue(dealModel.getDealType());
        datePicker.setValue(dealModel.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        if (dealTypeChoiceBox.getValue() != DEAL_TYPE.ESHOP) {
            productCountTextField.disableProperty();
        } else {
            productCountTextField.setDisable(false);
            productCountTextField.setText(String.valueOf(((Eshop) dealModel).getProductCount()));
        }
        adminPanel.setDisable(false);
        dealmakerPanel.setDisable(false);

    }

    /**
     * Updates the model corresponding to data in table
     *
     * @throws DAOExceptions.ModelNotFoundException Cant find current model
     */
    public void update() throws DAOExceptions.ModelNotFoundException, DAOExceptions.ModelSaveException {
        //split business logic to model
        var currentDealmaker = (Dealmaker) App.activeUserModel;
        String productCount = productCountTextField.getText();
        int parsedProductCount = productCount.isEmpty() ? 0 : Integer.parseInt(productCount);

        DealModel newDeal = this.dealService.updateDeal(
                currentDealmaker,
                dealTypeChoiceBox.getValue(),
                Integer.parseInt(idLabel.getText()),
                projectNameTextField.getText(),
                buyerChoiceBox.getValue(),
                dealmakerChoiceBox.getValue(),
                Double.parseDouble(priceTextField.getText()),
                Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                parsedProductCount,
                selectedDeal
        );

        //update view
        this.setComboBox();
        this.setDeal(newDeal);
        this.setTableData();
        //Alert user about success
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Úspech!");
        alert.setHeaderText("Záznam úspešne upravený!");
        alert.show();
    }

    /**
     * Toggles options
     */
    public void optionsToggle() {
        App.optionsToggle();
    }

    /**
     * Change window to users
     */
    public void goUsers() {
        Router.goTo(ROUTES.USERS);
    }

    /**
     * Change window to tasks
     */
    public void goTasks() {
        Router.goTo(ROUTES.TASKS);
    }

    /**
     * Function updates the manager to selected deal
     */
    public void updateManager() throws DAOExceptions.ModelNotFoundException, DAOExceptions.ModelSaveException {
        TeamManager tm = teamManagerChoiceBox.getValue();
        tm.setProject(selectedDeal.getName());
        //userDAO.update(tm);
        selectedDeal.setTeamManager(tm);
        this.update();
    }

}
