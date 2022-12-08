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

import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The main window of the programme, it displays 2 ListViews, one for the projects, one for the employees
 * On the bottom of the panel is a LineChart which updates every second and displays the current budget available.
 * When the user click on any list element it pops up a new window with the selected element, where more details can be viewed.
 * When a project successfully runs, the budget will update.
 */
public class GameWindowController implements Initializable {
    @FXML
    ListView<Project> projectListView;
    @FXML
    ListView<Employee> employeeListView;
    @FXML
    LineChart<String, Double> stonksChart;
    GameLogic gameLogic;


    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameLogic = GameLogic.getInstance();
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

        /*
          Code snippet to update the line chart. It sets and executor service, which then runs in the background to update
          the chart every second. And it even limits the amount of nodes on the chart, to not bloat it.
         */
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


    /**
     * Handles the click event of the List view of the projects, it allows to open a new window.
     * @param mouseEvent
     * Enables to handle mouse clicks
     */
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

    /**
     * Also handles the list click event for the employee list view.
     * @param mouseEvent
     * Enables to handle mouse clicks
     */
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                employeeListView.setItems(gameLogic.getEmployees());
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
            }
        });

    }

    @FXML
    public void handleDeleteEmployee(ActionEvent actionEvent) {
        gameLogic.getEmployees().remove(employeeListView.getSelectionModel().getSelectedItem());
    }


}
