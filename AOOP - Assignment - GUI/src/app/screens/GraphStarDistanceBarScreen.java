package app.screens;

import app.Settings;
import domain.collections.StarCollection;
import domain.galaxy.Star;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class GraphStarDistanceBarScreen extends BorderPane {

    public GraphStarDistanceBarScreen() throws IOException {

        setPadding(new Insets(12));
        applyGraphTheme();

        Label title = new Label("Graph 2 - Star Distances Grouped (Bar Chart)");
        title.getStyleClass().add("galaxy-title");
        title.setStyle("-fx-font-size: 20px;");

        Label subtitle = new Label("Stars grouped by distance from the center (bin size fixed).");
        subtitle.getStyleClass().add("galaxy-subtitle");

        VBox topBox = new VBox(6, title, subtitle);
        topBox.setPadding(new Insets(6, 6, 10, 6));
        setTop(topBox);

        StarCollection sc = StarCollection.instance();

        int binSize = 1000; 
        int max = Settings.instance().getGalaxySize();

        Map<String, Integer> bins = new TreeMap<>();
        for (int start = 0; start <= max; start += binSize) {
            int end = start + binSize - 1;
            bins.put(start + "–" + end, 0);
        }

        for (Star s : sc) {
            if (s == null || s.getCoordinate() == null) continue;

            double d = s.getCoordinate().getDistance();
            int index = (int) (d / binSize) * binSize;
            String key = index + "–" + (index + binSize - 1);

            bins.putIfAbsent(key, 0);
            bins.put(key, bins.get(key) + 1);
        }

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Distance group (pc)");
        xAxis.setAnimated(false);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of stars");
        yAxis.setAnimated(false);

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setLegendVisible(false);
        chart.setCategoryGap(18);
        chart.setBarGap(3);
        chart.setPrefHeight(650);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        int added = 0;
        for (var entry : bins.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            added++;
            if (added > 25) break; 
        }

        chart.getData().add(series);
        setCenter(chart);
    }

    private void applyGraphTheme() {
        if (!getStyleClass().contains("galaxy-graph")) getStyleClass().add("galaxy-graph");
        var css = getClass().getResource("/graphs-theme.css");
        if (css != null) {
            String url = css.toExternalForm();
            if (!getStylesheets().contains(url)) getStylesheets().add(url);
        }
    }
}