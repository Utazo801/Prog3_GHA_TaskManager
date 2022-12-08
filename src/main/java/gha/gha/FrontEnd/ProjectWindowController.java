package gha.gha.FrontEnd;

import gha.gha.Services.ProjectRunningService;
import gha.gha.BackEnd.Employee;
import gha.gha.BackEnd.GameLogic;
import gha.gha.BackEnd.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Display window for the selected projects. It displays the data of the selected project.
 * When at least one employee is assigned, it enables the project to run as a background task.
 * As the project runs it updates the progress bar at the bottom of the panel.
 */
public class ProjectWindowController {

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
    private ListView<gha.gha.BackEnd.Employee> EmployeeListView;

    @FXML
    private Label timeLabel;

    /**
     *
     *
     * To run a thread parallel with the main Java Fx Application thread one must use a Worker service. And that's what the
     * ProjectRunningSurface solves. It launches a Task in a new Thread and manages the synchronisation with the main thread.
     * @param p
     * Project instance
     * @param gameLogic
     * backend instance
     */
    public ProjectWindowController(Project p, GameLogic gameLogic) {
        this.p = p;
        this.gameLogic = gameLogic;
    }
    public ProjectWindowController() {}
    public void display() {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(ProjectWindowController.class.getResource("/ProjectView2_0.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }
        Scene scene = new Scene(root, 640, 640);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);

        //Injecting project data.
        ProjectNameLabel = (Label) root.lookup("#ProjectNameLabel");
        if (ProjectNameLabel != null) ProjectNameLabel.setText(p.getName());

        CostLabel = (Label) root.lookup("#CostLabel");
        if (CostLabel != null) CostLabel.setText(String.format("$%.2f", p.getCost()));

        timeLabel = (Label) root.lookup("#timeLabel");
        if (timeLabel != null) timeLabel.setText(p.getTimeCost() + " sec");

        ValueLabel = (Label) root.lookup("#ValueLabel");
        if (ValueLabel != null) ValueLabel.setText( String.format("$%.2f", p.getValue()));

        DescTextArea = (TextArea) root.lookup("#DescTextArea");
        if (DescTextArea != null) DescTextArea.setText(p.getDescription());

        ProjectProgBar = (ProgressBar) root.lookup("#ProjectProgBar");
        if (ProjectProgBar != null) {
            ProjectProgBar.setProgress(0);
        }

        EmployeeListView = (ListView<gha.gha.BackEnd.Employee>) root.lookup("#EmployeeListView");
        if (EmployeeListView != null) EmployeeListView.setItems(p.getAssignedEmployees());

        stage.setScene(scene);
        stage.show();


        //Custom employee data
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
            if (p.getAssignedEmployees() == null) {StartBtn.setDisable(true);}
            if (gameLogic.getBudget() < p.getCost()) { StartBtn.setDisable(true);}

            StartBtn.setOnAction(e -> {
                if (EmployeeListView.getItems() == null) {
                    AlertDialog alert = new AlertDialog();
                    alert.display("There is no one to work on the project,\n assign an employee!");

                } else {
                    ProjectRunningService projService = new ProjectRunningService(p, ProjectProgBar, gameLogic);
                    ProjectProgBar.progressProperty().bind(projService.progressProperty());
                    projService.start();
                    if (p.getState() == Project.ProjectSate.RUNNING){
                        StartBtn.setDisable(true);

                    }

                }

            });
        }

    }
}
