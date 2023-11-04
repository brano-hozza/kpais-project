module com.brano.oop2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.brano.oop2 to javafx.fxml;
    opens com.brano.oop2.controllers to javafx.fxml;
    opens com.brano.oop2.models.deals to javafx.fxml;
    opens com.brano.oop2.models.users to javafx.fxml;
    opens com.brano.oop2.models.tasks to javafx.fxml;
    opens com.brano.oop2.models to javafx.fxml;
    opens com.brano.oop2.db.daos to javafx.fxml;

    exports com.brano.oop2;
    exports com.brano.oop2.controllers;
    exports com.brano.oop2.models.deals;
    exports com.brano.oop2.models.users;
    exports com.brano.oop2.models.tasks;
    exports com.brano.oop2.models;
    exports com.brano.oop2.db.daos;
}