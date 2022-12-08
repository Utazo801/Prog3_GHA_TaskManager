package gha.gha.FrontEnd;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Pop up window to display errors to the user, such as when they clicked any of the listviews without selecting anything, or
 * when they want to launch a project without any employee assigned to it.
 */
public class AlertDialog {


    public void display(String alertString){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label();
        label.setText(alertString);
        VBox layout = new VBox(label);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout,300,100);

        stage.setTitle("Dialog");
        stage.setScene(scene);

        stage.show();
    }
}
