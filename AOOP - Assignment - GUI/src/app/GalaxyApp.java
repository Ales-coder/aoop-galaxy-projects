package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GalaxyApp extends Application {

    private BorderPane root;
    @SuppressWarnings("unused")
    private MainView mainView;

    @Override
    public void start(Stage stage) {
        root = new BorderPane();
        mainView = new MainView(root);

        root.setTop(MenuFactory.buildMenuBar(root));

        Scene scene = new Scene(root, 1000, 650);

        var appCss = getClass().getResource("/app-theme.css");
        if (appCss != null) {
            scene.getStylesheets().add(appCss.toExternalForm());
        }

        stage.setTitle("AOOP Galaxy Explorer - Alesia Gjeta");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}