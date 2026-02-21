package app.screens;

import domain.collections.StarCollection;
import domain.galaxy.Star;
import domain.galaxy.planet.Planet;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.SortedSet;

public class GraphPlanetsPerStarHistogramScreen extends BorderPane {


    private static final String[] BINS = {"0–9", "10–19", "20–29", "30–39", "40–49", "50+"};

    public GraphPlanetsPerStarHistogramScreen() throws IOException {

        setPadding(new Insets(12));


        applyGraphTheme();


        Label title = new Label("Graph 4 - Planets per Star (Histogram)");
        title.getStyleClass().add("galaxy-title");
        title.setStyle("-fx-font-size: 20px;");

        Label subtitle = new Label("Stars are grouped by how many planets they have (values above 50 go to 50+).");
        subtitle.getStyleClass().add("galaxy-subtitle");

        VBox topBox = new VBox(6, title, subtitle);
        topBox.setPadding(new Insets(6, 6, 10, 6));
        setTop(topBox);

 
        StarCollection sc = StarCollection.instance();

        int totalStars = 0;
        long totalPlanetsAllStars = 0;

        int[] counts = new int[BINS.length];

        for (Star s : sc) {
            if (s == null) continue;

            int planetCount = 0;
            try {
                SortedSet<Planet> planets = s.getPlanets();
                planetCount = (planets == null) ? 0 : planets.size();
            } catch (Exception ignored) { }

            totalStars++;
            totalPlanetsAllStars += planetCount;

            counts[binIndex(planetCount)]++;
        }

        final int totalStarsFinal = Math.max(totalStars, 0);
        double avg = (totalStarsFinal == 0) ? 0 : (totalPlanetsAllStars * 1.0 / totalStarsFinal);


        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Planets per star (grouped)");
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
        for (int i = 0; i < BINS.length; i++) {
            series.getData().add(new XYChart.Data<>(BINS[i], counts[i]));
        }
        chart.getData().add(series);

        setCenter(chart);


        Label footer = new Label(String.format("Total stars: %d   |   Average planets per star: %.2f",
                totalStarsFinal, avg));
        footer.getStyleClass().add("galaxy-subtitle");
        footer.setPadding(new Insets(8, 6, 0, 6));
        setBottom(footer);

    
        Platform.runLater(() -> addPercentLabels(series, totalStarsFinal));
    }

    private void applyGraphTheme() {
        if (!getStyleClass().contains("galaxy-graph")) getStyleClass().add("galaxy-graph");
        var css = getClass().getResource("/graphs-theme.css");
        if (css != null) {
            String url = css.toExternalForm();
            if (!getStylesheets().contains(url)) getStylesheets().add(url);
        }
    }

    private int binIndex(int planets) {
        if (planets <= 9) return 0;
        if (planets <= 19) return 1;
        if (planets <= 29) return 2;
        if (planets <= 39) return 3;
        if (planets <= 49) return 4;
        return 5; 
    }

    private void addPercentLabels(XYChart.Series<String, Number> series, int totalStars) {

        final int denom = (totalStars <= 0) ? 1 : totalStars;

        for (XYChart.Data<String, Number> d : series.getData()) {
            Node node = d.getNode();
            if (!(node instanceof StackPane bar)) continue;

            int count = (d.getYValue() == null) ? 0 : d.getYValue().intValue();
            double pct = (count * 100.0) / denom;

            Tooltip tp = new Tooltip(String.format("%s: %d stars (%.1f%%)", d.getXValue(), count, pct));
            tp.getStyleClass().add("galaxy-graph-tooltip");
            Tooltip.install(bar, tp);

            Label lbl = new Label(String.format("%.1f%%", pct));
            lbl.setStyle(
                    "-fx-font-size: 11px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #0b1026;" +
                    "-fx-background-color: rgba(233,239,255,0.85);" +
                    "-fx-padding: 2 6 2 6;" +
                    "-fx-background-radius: 10;"
            );
            lbl.setMouseTransparent(true);
            lbl.setTranslateY(-14);

            bar.getChildren().add(lbl);
        }
    }
}