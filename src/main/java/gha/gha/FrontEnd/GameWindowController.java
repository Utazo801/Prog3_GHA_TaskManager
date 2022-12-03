package gha.gha.FrontEnd;

import gha.gha.BackEnd.Employee;
import gha.gha.BackEnd.GameLogic;
import gha.gha.BackEnd.Project;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameWindowController implements Initializable {
    @FXML
    ListView<Project> projectListView;
    @FXML
    ListView<Employee> employeeListView;
    @FXML
    LineChart<String, Double> stonksChart;
    GameLogic gameLogic;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            gameLogic = new GameLogic();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        projectListView.setItems(gameLogic.getProjects());
        employeeListView.setItems(gameLogic.getEmployees());

        projectListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Project item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        employeeListView.setCellFactory(param -> new ListCell<>() {
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

        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName("Budget");
        stonksChart.getData().add(series);
        stonksChart.getYAxis().setLabel("Budget");
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss");

        // setup a scheduled executor to periodically put data into the chart
        ScheduledExecutorService scheduledExecutorService;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        // put dummy data onto graph per second
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // get a random integer between 0-10
            //Double random = ThreadLocalRandom.current().nextDouble(10);

            // Update the chart
            Platform.runLater(() -> {
                // get current time
                Date now = new Date();
                // put random number with current time
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), gameLogic.getBudget()));
                if (series.getData().size() > 10)
                    series.getData().remove(0);
            });
        }, 0, 1, TimeUnit.SECONDS);


    }


    @FXML
    public void handleProjectMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY || mouseEvent.getClickCount() == 2) {
            //Use ListView's getSelected Item
            if (projectListView.getSelectionModel().getSelectedItem() != null) {
                Project p = projectListView.getSelectionModel()
                        .getSelectedItem();
                //use this to do whatever you want to. Open Link etc.
                ProjectWindowController projController = new ProjectWindowController(p, gameLogic);
                projController.display();
            } else {
                AlertDialog alert = new AlertDialog();
                alert.display("No project selected");
            }

        }
    }

    @FXML
    public void handleEmployeeMouseClick(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY || mouseEvent.getClickCount() == 2) {
            //Use ListView's getSelected Item

            if (employeeListView.getSelectionModel().getSelectedItem() != null) {
                Employee e = employeeListView.getSelectionModel()
                        .getSelectedItem();
                new EmployeeWindowController(e, gameLogic).display();
            } else {
                AlertDialog alert = new AlertDialog();
                alert.display("No employee selected");
            }


        }
    }

    @FXML
    public void handleNewEmployeeClick(ActionEvent actionEvent) {
        new EmployeeWindowController(gameLogic).DisplayNewEmployee();

    }

    @FXML
    public void handleDeleteEmployee(ActionEvent actionEvent) {
        gameLogic.getEmployees().remove(employeeListView.getSelectionModel().getSelectedItem());
    }


}
