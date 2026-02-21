package app.screens;

import app.ui.DialogTheme;
import domain.galaxy.Coordinate;
import domain.galaxy.LuminosityClass;
import domain.galaxy.Star;
import domain.galaxy.StarType;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class NewStarDialog {

    public static Optional<Star> showAndCreateStar() {
        Dialog<Star> dialog = new Dialog<>();
        dialog.setTitle("New Star");
        dialog.setHeaderText("Create a new Star (added only in memory)");

        ButtonType createBtnType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createBtnType, ButtonType.CANCEL);


        DialogTheme.apply(dialog.getDialogPane());


        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        TextField designationField = new TextField();
        designationField.setPromptText("Optional (auto if empty)");

        TextField nameField = new TextField();
        nameField.setPromptText("Optional");

        ComboBox<StarType> typeBox =
                new ComboBox<>(FXCollections.observableArrayList(StarType.values()));
        typeBox.getSelectionModel().select(StarType.M);

        Spinner<Integer> tempSeqSpinner = new Spinner<>(0, 9, 0);
        tempSeqSpinner.setEditable(true);

        ComboBox<LuminosityClass> lumBox =
                new ComboBox<>(FXCollections.observableArrayList(LuminosityClass.values()));
        lumBox.getSelectionModel().select(LuminosityClass.V);

        TextField absMagField = new TextField("0.0");
        TextField distanceField = new TextField("10000");
        TextField longitudeField = new TextField("0.0");
        TextField latitudeField = new TextField("0.0");

        int r = 0;
        grid.add(new Label("Designation:"), 0, r); grid.add(designationField, 1, r++);
        grid.add(new Label("Name:"), 0, r);        grid.add(nameField, 1, r++);
        grid.add(new Label("Star Type:"), 0, r);   grid.add(typeBox, 1, r++);
        grid.add(new Label("Temp Sequence:"), 0, r); grid.add(tempSeqSpinner, 1, r++);
        grid.add(new Label("Luminosity:"), 0, r);  grid.add(lumBox, 1, r++);
        grid.add(new Label("Abs Magnitude:"), 0, r); grid.add(absMagField, 1, r++);
        grid.add(new Label("Distance (pc):"), 0, r); grid.add(distanceField, 1, r++);
        grid.add(new Label("Longitude:"), 0, r);   grid.add(longitudeField, 1, r++);
        grid.add(new Label("Latitude:"), 0, r);    grid.add(latitudeField, 1, r++);

        dialog.getDialogPane().setContent(grid);


        Button createBtn = (Button) dialog.getDialogPane().lookupButton(createBtnType);

        Runnable validate = () -> {
            boolean okType = typeBox.getValue() != null && lumBox.getValue() != null;

            boolean okAbsMag = isFloat(absMagField.getText());
            boolean okDist   = isDouble(distanceField.getText());
            boolean okLon    = isDouble(longitudeField.getText());
            boolean okLat    = isDouble(latitudeField.getText());

            createBtn.setDisable(!(okType && okAbsMag && okDist && okLon && okLat));
        };

        validate.run();
        absMagField.textProperty().addListener((a,b,c) -> validate.run());
        distanceField.textProperty().addListener((a,b,c) -> validate.run());
        longitudeField.textProperty().addListener((a,b,c) -> validate.run());
        latitudeField.textProperty().addListener((a,b,c) -> validate.run());
        typeBox.valueProperty().addListener((a,b,c) -> validate.run());
        lumBox.valueProperty().addListener((a,b,c) -> validate.run());

        dialog.setResultConverter(btn -> {
            if (btn != createBtnType) return null;

            try {
                StarType type = typeBox.getValue();
                int tempSeq = tempSeqSpinner.getValue();
                LuminosityClass lum = lumBox.getValue();

                float absMag = parseFloat(absMagField.getText());

                double distance = parseDouble(distanceField.getText());
                double lon = parseDouble(longitudeField.getText());
                double lat = parseDouble(latitudeField.getText());

                Coordinate coord = new Coordinate(distance, lon, lat);

                String designation = designationField.getText().trim();
                if (designation.isEmpty()) {
                    designation = Star.randomDesignation(type, coord);
                }

                String name = nameField.getText().trim();
                if (name.isEmpty()) name = null;

                return new Star(designation, name, type, tempSeq, lum, absMag, coord);

            } catch (Exception ex) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Invalid input");
                a.setHeaderText("Please check your values");
                a.setContentText(ex.getMessage());
                DialogTheme.apply(a);
                a.showAndWait();
                return null;
            }
        });

        return dialog.showAndWait();
    }


    private static boolean isDouble(String s) {
        try { parseDouble(s); return true; } catch (Exception e) { return false; }
    }

    private static boolean isFloat(String s) {
        try { parseFloat(s); return true; } catch (Exception e) { return false; }
    }

    private static double parseDouble(String s) {
        return Double.parseDouble(s.trim().replace(',', '.'));
    }

    private static float parseFloat(String s) {
        return Float.parseFloat(s.trim().replace(',', '.'));
    }
}