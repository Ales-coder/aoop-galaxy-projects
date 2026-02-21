package app.screens;

import domain.collections.StarCollection;
import domain.galaxy.Star;
import domain.galaxy.planet.Planet;
import domain.galaxy.planet.PlanetType;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.EnumMap;
import java.util.Map;
import java.util.SortedSet;

public class GraphPlanetTypePieScreen extends BorderPane {

    public GraphPlanetTypePieScreen() {

        setPadding(new Insets(12));
        applyGraphTheme();

        Label title = new Label("Graph 1 - Planet Types Distribution (Pie Chart)");
        title.getStyleClass().add("galaxy-title");
        title.setStyle("-fx-font-size: 20px;");

        Label info = new Label("Calculating planet type distribution from all loaded stars...");
        info.getStyleClass().add("galaxy-subtitle");
        info.setWrapText(true);

        VBox topBox = new VBox(6, title, info);
        topBox.setPadding(new Insets(6, 6, 10, 6));
        setTop(topBox);

        ProgressIndicator progress = new ProgressIndicator();
        progress.setMaxSize(60, 60);

        VBox loading = new VBox(15, progress);
        loading.setPadding(new Insets(30));
        setCenter(loading);

        Task<PieChart> task = new Task<>() {
            @Override
            protected PieChart call() throws Exception {

                StarCollection sc = StarCollection.instance();

                Map<PlanetType, Integer> counts = new EnumMap<>(PlanetType.class);
                for (PlanetType pt : PlanetType.values()) counts.put(pt, 0);

                int starCounter = 0;

                for (Star star : sc) {
                    starCounter++;
                    if (starCounter % 40 == 0) {
                        updateMessage("Processed stars: " + starCounter + " / " + sc.size());
                    }

                    SortedSet<Planet> planets = star.getPlanets();
                    if (planets == null) continue;

                    for (Planet p : planets) {
                        if (p.getType() != null) {
                            counts.put(p.getType(), counts.get(p.getType()) + 1);
                        }
                    }
                }

                var chartData = FXCollections.<PieChart.Data>observableArrayList();
                int totalPlanets = 0;

                for (var e : counts.entrySet()) {
                    int v = e.getValue();
                    if (v > 0) {
                        chartData.add(new PieChart.Data(e.getKey().toString(), v));
                        totalPlanets += v;
                    }
                }

                PieChart chart = new PieChart(chartData);
                chart.setTitle("Planet Types (Total planets: " + totalPlanets + ")");
                chart.setLegendVisible(true);
                chart.setLabelsVisible(true);

                return chart;
            }
        };

        info.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(e -> {
            PieChart chart = task.getValue();
            setCenter(chart);
            BorderPane.setMargin(chart, new Insets(10));

            
            for (PieChart.Data d : chart.getData()) {
                Tooltip tp = new Tooltip(d.getName() + ": " + (int) d.getPieValue());
                tp.getStyleClass().add("galaxy-graph-tooltip");
                Tooltip.install(d.getNode(), tp);
            }
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            Label err = new Label("Failed to build chart: " + (ex == null ? "Unknown error" : ex.getMessage()));
            err.setStyle("-fx-text-fill: #ff8fa3;");
            setCenter(err);
            BorderPane.setMargin(err, new Insets(20));
        });

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
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