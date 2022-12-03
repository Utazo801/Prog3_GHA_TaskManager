package gha.gha.FrontEnd;

import gha.gha.Services.ProjectRunningService;
import gha.gha.gameLogic.Employee;
import gha.gha.gameLogic.GameLogic;
import gha.gha.gameLogic.Project;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ProjectWindowController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Project p;
    private GameLogic gameLogic;
    @FXML
    private Label ProjectNameLabel;

    @FXML
    private Label CostLabel;

    @FXML
    private Label ValueLabel;

    @FXML
    private TextArea DescTextArea;

    @FXML
    private ProgressBar ProjectProgBar;

    @FXML
    private Button StartBtn;

    @FXML
    private Button PauseBtn;

    @FXML
    private ListView<gha.gha.gameLogic.Employee> EmployeeListView;

    @FXML
    private Label timeLabel;

    public ProjectWindowController(Project p, GameLogic gameLogic) {
        this.p = p;
        this.gameLogic = gameLogic;
    }

    public ProjectWindowController() {
    }


    public void display() {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(ProjectWindowController.class.getResource("/ProjectView2_0.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }
        scene = new Scene(root, 640, 640);
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        ProjectNameLabel = (Label) root.lookup("#ProjectNameLabel");
        if (ProjectNameLabel != null) ProjectNameLabel.setText(p.getName());

        CostLabel = (Label) root.lookup("#CostLabel");
        if (CostLabel != null) CostLabel.setText("$" + p.getCost());

        timeLabel = (Label) root.lookup("#timeLabel");
        if (timeLabel != null) timeLabel.setText(p.getTimeCost() + " sec");

        ValueLabel = (Label) root.lookup("#ValueLabel");
        if (ValueLabel != null) ValueLabel.setText("$" + p.getValue());

        DescTextArea = (TextArea) root.lookup("#DescTextArea");
        if (DescTextArea != null) DescTextArea.setText(p.getDescription());

        ProjectProgBar = (ProgressBar) root.lookup("#ProjectProgBar");
        if (ProjectProgBar != null) {
            ProjectProgBar.setProgress(0);
        }

        EmployeeListView = (ListView<gha.gha.gameLogic.Employee>) root.lookup("#EmployeeListView");
        if (EmployeeListView != null) EmployeeListView.setItems(p.getAssignedEmployees());

        stage.setScene(scene);
        stage.show();


        EmployeeListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        StartBtn = (Button) root.lookup("#StartBtn");
        if (StartBtn != null) {
            if (p.getAssignedEmployees() == null) {
                StartBtn.setDisable(true);
            }
            StartBtn.setOnAction(e -> {
                if (EmployeeListView.getItems() == null) {
                    AlertDialog alert = new AlertDialog();
                    alert.display("There is no one to work on the project,\n assign an employee!");

                } else {
                    //Will have to look into slowing down the progress of this shit

                    ProjectRunningService projService = new ProjectRunningService(p, ProjectProgBar, gameLogic);
                    ProjectProgBar.progressProperty().bind(projService.progressProperty());
                    projService.start();
                }
            });
        }
        PauseBtn = (Button) root.lookup("#PauseBtn");
        if (PauseBtn != null) {
            PauseBtn.setOnAction(e -> {
                try {
                    p.Pause();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

    }
}