package com.brano.oop2.router;

import com.brano.oop2.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Router {

    /**
     * Main stage of the app
     */
    public static Stage stage;

    /**
     * Function is used to replace context of current screen
     *
     * @param fxml Path to new fxml
     * @throws Exception Exception if we cannot read the path
     */
    private static void replaceSceneContent(String fxml) throws Exception {
        Parent page = FXMLLoader.load(App.class.getResource(fxml));
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page);
            scene.getStylesheets().add(Objects.requireNonNull(App.class.getResource("styles/styles.css")).toExternalForm());
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(page);
        }
        stage.sizeToScene();
        if (!stage.isShowing())
            stage.show();
    }

    /**
     * Routing to wanted route
     *
     * @param route Where you want to go
     */
    public static void goTo(ROUTES route) {
        try {
            replaceSceneContent("views/" + route.toString().toLowerCase() + ".fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
