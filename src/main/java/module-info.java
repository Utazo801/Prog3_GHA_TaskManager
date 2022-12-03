module gha.gha {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires com.google.gson;

    exports gha.gha.BackEnd;
    opens gha.gha.BackEnd to javafx.fxml,com.google.gson;
    exports gha.gha.FrontEnd;
    opens gha.gha.FrontEnd to javafx.fxml;
}