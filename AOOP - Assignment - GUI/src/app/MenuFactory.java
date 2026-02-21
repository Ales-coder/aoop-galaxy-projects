package app;

import app.screens.GraphPlanetTypePieScreen;
import app.screens.GraphPlanetsPerStarHistogramScreen;
import app.screens.GraphPlanetsVsStarTypeScreen;
import app.screens.GraphStarDistanceBarScreen;
import app.screens.StarsScreen;
import app.ui.DialogTheme;
import domain.collections.StarCollection;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class MenuFactory {

    public static MenuBar buildMenuBar(BorderPane root) {
        MenuBar menuBar = new MenuBar();


        Menu fileMenu = new Menu("File");

        Menu newSub = new Menu("New");
        MenuItem newStar = new MenuItem("New Star");
        MenuItem newSpecies = new MenuItem("New Species"); 
        newSub.getItems().addAll(newStar, newSpecies);

        Menu loadSub = new Menu("Load");
        MenuItem loadStars = new MenuItem("Stars");
        MenuItem loadSpecies = new MenuItem("Species"); 
        loadSub.getItems().addAll(loadStars, loadSpecies);


        loadStars.setOnAction(e -> {
            try {
                StarCollection sc = StarCollection.instance();


                sc.loadFromSettings();


                boolean addExtra = askYesNo(
                        "Load Stars",
                        "Stars loaded from settings: " + sc.size() + "\n\n" +
                                "Do you want to add an extra CSV file too?"
                );

                int added = 0;

                if (addExtra) {
                    File chosen = chooseCsvFile(root);
                    if (chosen != null) {

                        added = sc.addFromCsv(chosen);
                    }
                }


                if (root.getCenter() instanceof StarsScreen screen) {
                    screen.reloadFromCollection();
                }


                if (addExtra) {
                    showInfo("Load Stars",
                            "Reloaded from settings.\n" +
                                    "Extra CSV added stars: " + added + "\n\n" +
                                    "Total stars now: " + sc.size());
                } else {
                    showInfo("Load Stars", "Total stars loaded: " + sc.size());
                }

            } catch (Exception ex) {
                showError("Load Stars failed", ex.getMessage());
            }
        });


        loadSpecies.setOnAction(e -> showNotImplemented("Load Species"));
        newSpecies.setOnAction(e -> showNotImplemented("New Species"));

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> Platform.exit());

        fileMenu.getItems().addAll(newSub, loadSub, new SeparatorMenuItem(), exit);

        Menu starMenu = new Menu("Star");
        MenuItem showStars = new MenuItem("Show Stars");
        MenuItem createStar = new MenuItem("Create Star");

        showStars.setOnAction(e2 -> openStarsScreen(root));

        newStar.setOnAction(e2 -> createStarFlow(root));
        createStar.setOnAction(e2 -> createStarFlow(root));

        starMenu.getItems().addAll(showStars, createStar);

        Menu speciesMenu = new Menu("Species");
        MenuItem showSpecies = new MenuItem("Show Species");
        MenuItem createSpecies = new MenuItem("Create Species");

        showSpecies.setOnAction(e2 -> showNotImplemented("Show Species"));
        createSpecies.setOnAction(e2 -> showNotImplemented("Create Species"));

        speciesMenu.getItems().addAll(showSpecies, createSpecies);

        Menu graphsMenu = new Menu("Graphs");
        MenuItem graph1 = new MenuItem("Planet Types (Pie)");
        MenuItem graph2 = new MenuItem("Star Distances (Bar)");
        MenuItem graph3 = new MenuItem("Planets vs Star Types (Scatter/Line)");
        MenuItem graph4 = new MenuItem("Planets per Star (Histogram)");

        graph1.setOnAction(e2 -> root.setCenter(new GraphPlanetTypePieScreen()));

        graph2.setOnAction(e2 -> {
            try {
                root.setCenter(new GraphStarDistanceBarScreen());
            } catch (Exception ex) {
                showError("Graph failed", ex.getMessage());
            }
        });

        graph3.setOnAction(e2 -> {
            try {
                root.setCenter(new GraphPlanetsVsStarTypeScreen());
            } catch (Exception ex) {
                showError("Graph failed", ex.getMessage());
            }
        });

        graph4.setOnAction(e2 -> {
            try {
                root.setCenter(new GraphPlanetsPerStarHistogramScreen());
            } catch (Exception ex) {
                showError("Graph failed", ex.getMessage());
            }
        });

        graphsMenu.getItems().addAll(graph1, graph2, graph3, graph4);


        Menu helpMenu = new Menu("Help");
        MenuItem about = new MenuItem("About");
        about.setOnAction(e2 -> showAbout());
        helpMenu.getItems().addAll(about);

        menuBar.getMenus().addAll(fileMenu, starMenu, speciesMenu, graphsMenu, helpMenu);
        return menuBar;
    }

    private static void openStarsScreen(BorderPane root) {
        try {
            root.setCenter(new StarsScreen(root));
        } catch (Exception ex) {
            showError("Show Stars failed", ex.getMessage());
        }
    }

    private static void createStarFlow(BorderPane root) {
        if (root.getCenter() instanceof StarsScreen screen) {
            screen.createStar();
            return;
        }

        try {
            StarsScreen screen = new StarsScreen(root);
            root.setCenter(screen);
            screen.createStar();
        } catch (Exception ex) {
            showError("Create Star failed", ex.getMessage());
        }
    }



    private static File chooseCsvFile(BorderPane root) {
        Window owner = (root.getScene() == null) ? null : root.getScene().getWindow();

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select a Star CSV file to add");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );


        try {
            File dataDir = Settings.instance().getDataDirectory();
            if (dataDir != null && dataDir.exists()) {
                chooser.setInitialDirectory(dataDir);
            }
        } catch (Exception ignored) {}

        return chooser.showOpenDialog(owner);
    }

    private static boolean askYesNo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yes, no);

        DialogTheme.apply(alert);
        return alert.showAndWait().orElse(no) == yes;
    }



    private static void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("AOOP Galaxy Explorer");
        alert.setContentText(
                "AOOP Galaxy Explorer - Alesia Gjeta\n\n" +
                        "Advanced OOP - 2026\n" +
                        "JavaFX application to explore Stars and generated Planets.\n\n"
        );

        DialogTheme.apply(alert);
        alert.showAndWait();
    }

    private static void showNotImplemented(String featureName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Not implemented");
        alert.setHeaderText(featureName);
        alert.setContentText(
                "This feature is not implemented.\n\n" +
                        "Note: The assignment requires Species menu options to exist, " +
                        "but it explicitly says you do NOT implement Species."
        );

        DialogTheme.apply(alert);
        alert.showAndWait();
    }

    private static void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        DialogTheme.apply(alert);
        alert.showAndWait();
    }

    private static void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg == null ? "Unknown error" : msg);

        DialogTheme.apply(alert);
        alert.showAndWait();
    }
}