package app.screens;

import app.ui.DialogTheme;
import domain.collections.StarCollection;
import domain.galaxy.Star;
import domain.galaxy.planet.Planet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.SortedSet;

public class StarsScreen extends BorderPane {

    private final BorderPane root;
    private final StarCollection starCollection;
    private final ObservableList<Star> stars;

    private final ListView<Star> starListView;
    private final Label detailLabel;

    public StarsScreen(BorderPane root) throws IOException {
        this.root = root;

        setPadding(new Insets(10));

   
        if (!getStyleClass().contains("stars-screen")) {
            getStyleClass().add("stars-screen");
        }

        var starsCss = getClass().getResource("/stars-theme.css");
        if (starsCss != null) {
            String cssUrl = starsCss.toExternalForm();
            if (!getStylesheets().contains(cssUrl)) {
                getStylesheets().add(cssUrl);
            }
        }

      
        starCollection = StarCollection.instance();
        stars = FXCollections.observableArrayList(starCollection);


        starListView = new ListView<>(stars);
        starListView.setPrefWidth(430);

       
        starListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Star s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) {
                    setText(null);
                } else {
                    setText(
                            s.getDesignation() + "   |   " + s.getType() + "   |   " +
                                    String.format("%.2f pc", s.getCoordinate().getDistance())
                    );
                }
            }
        });

        
        Label header = new Label("Star Details");
        header.getStyleClass().add("stars-title");

        detailLabel = new Label("Select a star to see details...");
        detailLabel.setWrapText(true);
        detailLabel.getStyleClass().add("stars-detail");

        VBox detailBox = new VBox(10, header, detailLabel);
        detailBox.setPadding(new Insets(15));
        detailBox.setPrefWidth(520);
        detailBox.getStyleClass().add("stars-panel");

        
        Button newBtn = new Button("New");
        Button deleteBtn = new Button("Delete");
        Button detailsBtn = new Button("Details");

        ToolBar toolBar = new ToolBar(newBtn, deleteBtn, detailsBtn);
        toolBar.getStyleClass().add("stars-toolbar");

        setLeft(starListView);
        setCenter(detailBox);
        setBottom(toolBar);

        
        starListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldStar, newStar) -> updateDetails(newStar)
        );

        
        deleteBtn.setOnAction(e -> deleteSelected());

      
        newBtn.setOnAction(e -> createStar());

     
        detailsBtn.setOnAction(e -> openDetails());
    }

    
    public void reloadFromCollection() {
        stars.setAll(starCollection);
        starListView.getSelectionModel().clearSelection();
        detailLabel.setText("Select a star to see details...");
    }

    public void createStar() {
        NewStarDialog.showAndCreateStar().ifPresent(star -> {
            try {
                starCollection.addAndPersist(star);

                stars.add(star);
                starListView.getSelectionModel().select(star);
                starListView.scrollTo(star);

            } catch (Exception ex) {
                showError("Save failed",
                        "Could not save star to custom_stars.csv\n\n" + ex.getMessage());
            }
        });
    }

    private void deleteSelected() {
        Star selected = starListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Delete", "Select a star first.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Star");
        confirm.setHeaderText("Delete " + selected.getDesignation() + "?");
        confirm.setContentText("This removes it from the list (not saved to disk).");
        DialogTheme.apply(confirm);

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        stars.remove(selected);
        starCollection.remove(selected);
        detailLabel.setText("Star deleted.");
    }

    private void openDetails() {
        Star selected = starListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Details", "Select a star first.");
            return;
        }
        root.setCenter(new StarDetailsScreen(selected));
    }

    private void updateDetails(Star star) {
        if (star == null) {
            detailLabel.setText("Select a star to see details...");
            return;
        }

        int planetCount;
        try {
            SortedSet<Planet> planets = star.getPlanets(); 
            planetCount = (planets == null) ? 0 : planets.size();
        } catch (Exception ignored) {
            planetCount = 0;
        }

        detailLabel.setText(
                "Designation: " + safe(star.getDesignation()) + "\n" +
                        "Name: " + safe(star.getName()) + "\n" +
                        "Type: " + star.getType() + "\n" +
                        "Temperature Sequence: " + star.getTemperatureSequence() + "\n" +
                        "Luminosity: " + star.getLuminosity() + "\n" +
                        "Absolute Magnitude: " + star.getAbsoluteMagnitude() + "\n" +
                        "Distance: " + star.getCoordinate().getDistance() + " pc\n" +
                        "Planets: " + planetCount
        );
    }

    private String safe(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }

    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        DialogTheme.apply(alert);
        alert.showAndWait();
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        DialogTheme.apply(alert);
        alert.showAndWait();
    }
}