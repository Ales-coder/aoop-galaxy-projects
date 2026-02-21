package app.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public final class DialogTheme {

    private static final String DIALOG_CSS = "/dialog-theme.css";

    private DialogTheme() {}

    public static void apply(Alert alert) {
        if (alert == null) return;
        apply(alert.getDialogPane());
    }

    public static void apply(DialogPane pane) {
        if (pane == null) return;

        if (!pane.getStyleClass().contains("dialog-theme")) {
            pane.getStyleClass().add("dialog-theme");
        }

        var css = DialogTheme.class.getResource(DIALOG_CSS);
        if (css != null) {
            String url = css.toExternalForm();
            if (!pane.getStylesheets().contains(url)) {
                pane.getStylesheets().add(url);
            }
        }
    }
}