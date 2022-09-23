module appetite.ui {
    requires appetite.core;
    requires javafx.controls;
    requires javafx.fxml;
    requires appetite.json;

    opens ui to javafx.graphics, javafx.fxml;
}
