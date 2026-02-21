package app.screens;

import domain.collections.StarCollection;
import domain.galaxy.Star;
import domain.galaxy.planet.Planet;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.*;

public class GraphPlanetsVsStarTypeScreen extends BorderPane {

    private static final int CAP = 50;
    private static final int MAX_POINTS_PER_TYPE = 250;
    private static final double JITTER = 0.33;

    public GraphPlanetsVsStarTypeScreen() throws IOException {

        setPadding(new Insets(12));
        applyGraphTheme();

        Label title = new Label("Graph 3 - Planets vs Star Types (Scatter + Avg Line)");
        title.getStyleClass().add("galaxy-title");
        title.setStyle("-fx-font-size: 20px;");

        Label subtitle = new Label("Dots = stars (jittered, capped at 50). Line = average planets per star type.");
        subtitle.getStyleClass().add("galaxy-subtitle");

        VBox topBox = new VBox(6, title, subtitle);
        topBox.setPadding(new Insets(6, 6, 10, 6));
        setTop(topBox);

        List<String> orderedTypes = List.of("O", "B", "A", "F", "G", "K", "M", "D", "H", "S", "C");
        Map<String, Integer> typeIndex = new HashMap<>();
        for (int i = 0; i < orderedTypes.size(); i++) typeIndex.put(orderedTypes.get(i), i);

        StarCollection sc = StarCollection.instance();

        Map<String, Integer> countStars = new HashMap<>();
        Map<String, Integer> sumPlanets = new HashMap<>();
        Map<String, List<Integer>> perTypeCounts = new HashMap<>();

        Map<String, List<XYChart.Data<Number, Number>>> pointsPerType = new HashMap<>();

        for (Star s : sc) {
            if (s == null || s.getType() == null) continue;

            String typeLabel = s.getType().toString();
            if (!typeIndex.containsKey(typeLabel)) continue;

            int planetsCount = 0;
            try {
                SortedSet<Planet> planets = s.getPlanets();
                planetsCount = (planets == null) ? 0 : planets.size();
            } catch (Exception ignored) {
            }

            countStars.put(typeLabel, countStars.getOrDefault(typeLabel, 0) + 1);
            sumPlanets.put(typeLabel, sumPlanets.getOrDefault(typeLabel, 0) + planetsCount);
            perTypeCounts.computeIfAbsent(typeLabel, k -> new ArrayList<>()).add(planetsCount);

            int idx = typeIndex.get(typeLabel);
            double x = idx + deterministicJitter(s.getDesignation(), idx);

            int shown = Math.min(planetsCount, CAP);

            String tooltipText =
                    "Type: " + typeLabel + "\n" +
                            "Planets: " + planetsCount + (planetsCount > CAP ? " (shown as " + CAP + "+)" : "") + "\n" +
                            "Star: " + safe(s.getDesignation());

            XYChart.Data<Number, Number> pt = new XYChart.Data<>(x, shown, tooltipText);
            pointsPerType.computeIfAbsent(typeLabel, k -> new ArrayList<>()).add(pt);
        }

        XYChart.Series<Number, Number> starsSeries = new XYChart.Series<>();
        starsSeries.setName("Stars");

        for (String t : orderedTypes) {
            List<XYChart.Data<Number, Number>> pts = pointsPerType.get(t);
            if (pts == null || pts.isEmpty()) continue;

            if (pts.size() <= MAX_POINTS_PER_TYPE) {
                starsSeries.getData().addAll(pts);
            } else {
                pts.sort(Comparator.comparingInt(p -> String.valueOf(p.getExtraValue()).hashCode()));
                starsSeries.getData().addAll(pts.subList(0, MAX_POINTS_PER_TYPE));
            }
        }

        XYChart.Series<Number, Number> avgSeries = new XYChart.Series<>();
        avgSeries.setName("Average");

        XYChart.Series<Number, Number> medianSeries = new XYChart.Series<>();
        medianSeries.setName("Median");

        for (String type : orderedTypes) {
            Integer c = countStars.get(type);
            if (c == null || c == 0) continue;

            int idx = typeIndex.get(type);
            int sum = sumPlanets.getOrDefault(type, 0);
            double avg = sum * 1.0 / c;

            double median = median(perTypeCounts.get(type));

            avgSeries.getData().add(new XYChart.Data<>(idx, Math.min(avg, CAP)));
            medianSeries.getData().add(new XYChart.Data<>(idx, Math.min(median, CAP)));
        }

        NumberAxis xAxis = new NumberAxis(-0.5, orderedTypes.size() - 0.5, 1);
        xAxis.setLabel("Star type");
        xAxis.setMinorTickVisible(false);
        xAxis.setAnimated(false);

        xAxis.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number object) {
                int i = (int) Math.round(object.doubleValue());
                if (i < 0 || i >= orderedTypes.size()) return "";
                return orderedTypes.get(i);
            }

            @Override
            public Number fromString(String string) {
                return 0;
            }
        });

        NumberAxis yAxis = new NumberAxis(0, CAP + 5, 5);
        yAxis.setLabel("Number of planets");
        yAxis.setAnimated(false);

        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setCreateSymbols(true);
        chart.setLegendVisible(true);
        chart.setPrefHeight(650);

        chart.getData().addAll(starsSeries, avgSeries, medianSeries);
        setCenter(chart);

        Platform.runLater(() -> {
            hideSeriesLine(starsSeries);      
            hideSeriesLine(medianSeries);     
            thickenSeriesLine(avgSeries, 3);  

            for (XYChart.Data<Number, Number> d : starsSeries.getData()) {
                if (d.getNode() != null) {
                    styleStarDot(d);
                } else {
                    d.nodeProperty().addListener((obs, oldN, newN) -> {
                        if (newN != null) styleStarDot(d);
                    });
                }
            }

            for (XYChart.Data<Number, Number> d : medianSeries.getData()) {
                if (d.getNode() != null) {
                    d.getNode().setStyle("-fx-background-radius: 9px; -fx-padding: 7px;");
                } else {
                    d.nodeProperty().addListener((obs, o, n) -> {
                        if (n != null) n.setStyle("-fx-background-radius: 9px; -fx-padding: 7px;");
                    });
                }
            }
        });
    }

    private static void hideSeriesLine(XYChart.Series<Number, Number> s) {
        Node seriesNode = s.getNode();
        if (seriesNode == null) return;
        Node line = seriesNode.lookup(".chart-series-line");
        if (line != null) line.setStyle("-fx-stroke: transparent;");
    }

    private static void thickenSeriesLine(XYChart.Series<Number, Number> s, int px) {
        Node seriesNode = s.getNode();
        if (seriesNode == null) return;
        Node line = seriesNode.lookup(".chart-series-line");
        if (line != null) line.setStyle("-fx-stroke-width: " + px + "px;");
    }

    private void styleStarDot(XYChart.Data<Number, Number> d) {
        Node n = d.getNode();
        if (n == null) return;

        n.setStyle("-fx-background-radius: 7px; -fx-padding: 5px;");

        Object extra = d.getExtraValue();
        if (extra != null) {
            Tooltip tp = new Tooltip(extra.toString());
            tp.getStyleClass().add("galaxy-graph-tooltip");
            Tooltip.install(n, tp);
        }
    }

    private static double deterministicJitter(String designation, int idx) {
        int h = Objects.hash(designation, idx);
        double r = (Math.floorMod(h, 1000) / 1000.0);
        return (r - 0.5) * (2 * JITTER);
    }

    private static double median(List<Integer> values) {
        if (values == null || values.isEmpty()) return 0;
        List<Integer> copy = new ArrayList<>(values);
        copy.sort(Integer::compareTo);
        int n = copy.size();
        if (n % 2 == 1) return copy.get(n / 2);
        return (copy.get(n / 2 - 1) + copy.get(n / 2)) / 2.0;
    }

    private static String safe(String s) {
        return (s == null || s.isBlank()) ? "-" : s;
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