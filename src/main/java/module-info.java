module gha.gha {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires com.google.gson;

    opens gha.gha to javafx.fxml;
    exports gha.gha;
    exports gha.gha.gameLogic;
    opens gha.gha.gameLogic to javafx.fxml,com.google.gson;
    exports gha.gha.FrontEnd;
    opens gha.gha.FrontEnd to javafx.fxml;
}