package app.screens;

import domain.galaxy.Star;
import domain.galaxy.planet.Planet;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class StarDetailsScreen extends BorderPane {

    public StarDetailsScreen(Star star) {

        setPadding(new Insets(15));


        getStyleClass().add("stars-screen");
        var css = getClass().getResource("/stars-theme.css");
        if (css != null) getStylesheets().add(css.toExternalForm());

        Label title = new Label("Star Details");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label info = new Label(
                "Designation: " + safe(star.getDesignation()) + "\n" +
                        "Name: " + safe(star.getName()) + "\n" +
                        "Type: " + star.getType() + "\n" +
                        "Temperature Sequence: " + star.getTemperatureSequence() + "\n" +
                        "Luminosity: " + star.getLuminosity() + "\n" +
                        "Absolute Magnitude: " + star.getAbsoluteMagnitude() + "\n" +
                        "Distance: " + star.getCoordinate().getDistance() + " pc\n"
        );
        info.setWrapText(true);

        ListView<String> planetList = new ListView<>();

        try {
            for (Planet p : star.getPlanets()) {
                planetList.getItems().add(
                        p.getDesignation()
                                + " | Type: " + p.getType()
                                + " | AU: " + p.getAverageDistanceToStar()
                                + " | Tilt: " + p.getTilt()
                                + " | Excentricity: " + p.getExcentricity()
                );
            }
        } catch (Exception e) {
            planetList.getItems().add("Could not generate planets.");
        }

        VBox box = new VBox(12,
                title,
                info,
                new Label("Planets in this system:"),
                planetList
        );
        box.setPadding(new Insets(10));

        setCenter(box);
    }

    private String safe(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
    }
}