package com.brano.oop2;

import com.brano.oop2.db.Database;
import com.brano.oop2.db.daos.DAOExceptions;
import com.brano.oop2.db.daos.DealDAO;
import com.brano.oop2.db.daos.TaskDAO;
import com.brano.oop2.db.daos.UserDAO;
import com.brano.oop2.models.deals.Eshop;
import com.brano.oop2.models.tasks.Basic;
import com.brano.oop2.models.tasks.TASK_STATE;
import com.brano.oop2.models.tasks.TASK_TYPE;
import com.brano.oop2.models.tasks.TaskModel;
import com.brano.oop2.models.users.*;
import com.brano.oop2.router.ROUTES;
import com.brano.oop2.router.Router;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Date;
import java.util.Objects;


public class App extends Application {
    public static Stage optionsStage;
    public static UserModel activeUserModel;

    Database db;

    /**
     * Function creates stage for options
     */
    public void createOptions() {
        optionsStage = new Stage();
        optionsStage.setTitle("Options");
        GridPane gp = new GridPane();
        gp.getStyleClass().add("bg");


        Label label = new Label("Nastavenia");
        gp.addRow(0, label);

        Button logout = new Button("Logout");
        logout.setOnAction(e -> {
            App.activeUserModel = null;
            App.optionsToggle();
            Router.goTo(ROUTES.LOGIN);
        });
        gp.addRow(1, logout);

        Scene optionsScene = new Scene(gp);
        optionsScene.getStylesheets().add(Objects.requireNonNull(App.class.getResource("styles/styles.css")).toExternalForm());
        optionsStage.setScene(optionsScene);
        optionsStage.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                optionsToggle();
            }

        });
    }

    /**
     * Function toggles the visibility of options menu
     */
    public static void optionsToggle() {
        if (optionsStage.isShowing()) {
            optionsStage.close();
        } else {
            optionsStage.show();
        }
    }

    /**
     * Function to fill data for testing purposes
     */
    private void fillData() throws DAOExceptions.ModelSaveException {
        UserDAO ud = UserDAO.getInstance();
        DealDAO dd = DealDAO.getInstance();
        TaskDAO td = TaskDAO.getInstance();
        if (ud.get("admin") == null) {
            Admin admin = new Admin(UserDAO.lastID, "admin", "heslo");
            admin.setAddress("Bratislava", "Mala", 5);
            ud.save(admin);
        }

        if (ud.get("dealmaker") == null) {
            Dealmaker dealmaker = new Dealmaker(UserDAO.lastID, "dealmaker", "heslo", false, "test_1");
            dealmaker.setAddress("Spisska Nova Ves", "Dolna", 255);
            ud.save(dealmaker);
        }

        if (ud.get("buyer") == null) {
            Buyer buyer = new Buyer(UserDAO.lastID, "buyer", "heslo", null);
            buyer.setAddress("Kosice", "Stare", 4);
            ud.save(buyer);
        }

        if (ud.get("manager") == null) {
            TeamManager teamManager = new TeamManager(UserDAO.lastID, "manager", "heslo", true, "");
            teamManager.setAddress("Praha", "Karlova", 8);
            ud.save(teamManager);
        }

        if (ud.get("developer1") == null) {
            Developer developer1 = new Developer(UserDAO.lastID, "developer1", "heslo", true, "", "FE");
            developer1.setAddress("Brno", "Kratka", 5);
            ud.save(developer1);
        }

        if (ud.get("developer2") == null) {
            Developer developer2 = new Developer(UserDAO.lastID, "developer2", "heslo", true, "", "BE");
            developer2.setAddress("Michalovce", "Drevena", 5);
            ud.save(developer2);
        }

        if (dd.get("test_1") == null) {
            Eshop eshop = new Eshop("test_1", (Buyer) ud.get("buyer"), (Dealmaker) ud.get("dealmaker"), 200, new Date(), 10);
            eshop.setTeamManager((TeamManager) ud.get("manager"));
            ((TeamManager) ud.get("manager")).setProject("test_1");
            dd.save(eshop);
            ((Buyer) ud.get("buyer")).setDeal(eshop);
            ((Dealmaker) ud.get("dealmaker")).setDeal("test_1");
            TaskModel task1 = new Basic("task_1", TASK_TYPE.LOGIN, TASK_STATE.PENDING, new Date(), new Date(), (TeamManager) ud.get("manager"), (Developer) ud.get("developer1"), "test_1");
            td.save(task1);
            TaskModel task2 = new Basic("task_2", TASK_TYPE.API, TASK_STATE.PENDING, new Date(), new Date(), (TeamManager) ud.get("manager"), (Developer) ud.get("developer1"), "test_1");
            td.save(task2);
            TaskModel task3 = new Basic("task_3", TASK_TYPE.SSO, TASK_STATE.PENDING, new Date(), new Date(), (TeamManager) ud.get("manager"), (Developer) ud.get("developer1"), "test_1");
            td.save(task3);
        }

    }

    /**
     * Main function
     *
     * @param args console arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starting function which prepare the application
     *
     * @param primaryStage Main stage of the app
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            db = new Database(UserDAO.class.getName(), DealDAO.class.getName(), TaskDAO.class.getName());
            db.loadAll();
            UserDAO.lastID = UserDAO.getInstance().getAll().size();
            TaskDAO.lastID = TaskDAO.getInstance().getAll().size();
            DealDAO.lastID = DealDAO.getInstance().getAll().size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.fillData();
        } catch (DAOExceptions.ModelSaveException e) {
            System.out.println("Problem s ukladanim modelov");
            e.printStackTrace();
        }
        this.createOptions();
        Router.stage = primaryStage;
        Router.goTo(ROUTES.LOGIN);
    }

    /**
     * Ending function where we save data
     *
     * @throws Exception Cannot save data
     */
    @Override
    public void stop() throws Exception {
        if(App.activeUserModel != null) {
            UserDAO.getInstance().update(App.activeUserModel);
        }
        db.saveAll();
        super.stop();

    }
}

