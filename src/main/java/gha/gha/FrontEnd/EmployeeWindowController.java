package gha.gha.FrontEnd;

import gha.gha.BackEnd.Employee;
import gha.gha.BackEnd.GameLogic;
import gha.gha.BackEnd.Project;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Display window for a selected employee, it shows all the loaded data from one employee. The user can select a new profile
 * picture in the top right corner. The main function of this window is to select an assigned project for the selected employee, which is done
 * using the choicebox at the bottom of the window.
 */
public class EmployeeWindowController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Employee e;
    private final Button saveBtn;
    private final AtomicInteger checkSum;


    private final Employee emp = new Employee();
    @FXML
    private TextField nameText;

    @FXML
    private TextField workRateText;
    @FXML
    private ImageView profileImage;

    @FXML
    private TextArea empDescText;
    @FXML
    private ComboBox<Project> assignProjectBox;
    @FXML
    private TextField expText;
    @FXML
    private TextField salaryText;
    @FXML
    private TextField hobbyText;
    @FXML
    private TextField ageText;
    private GameLogic gameLogic;

    @FXML
    private GridPane ContentGrid;

    /**
     *
     * @param e
     * Selected employee from the Employee list on the front page.
     * @param gameLogic
     * Instance of the Singleton class, to reach the required project.
     */
    public EmployeeWindowController(Employee e, GameLogic gameLogic) {
        this.e = e;
        this.gameLogic = gameLogic;
        checkSum = new AtomicInteger(-7);
        saveBtn = null;
    }

    public EmployeeWindowController(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        checkSum = new AtomicInteger(-7);
        saveBtn = new Button("Save employee");
    }

    public EmployeeWindowController() {
        checkSum = new AtomicInteger(-7);
        saveBtn = new Button("Save employee");
    }

    /**
     * The simple display method makes it possible to display the employee on the window. First it creates the stage and the scene
     * then checks all the required FXML element if they exist in the file. After that the correct data is displayed.
     * the ChoiceBox for the Projects is loaded with the correct data from GameLogic class and when one is selected it
     * automatically makes the correct settings in the selected project and adds the employee to its list.
     */
    public void display() {
        if (stage != null) {
            stage = null;
        }
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        try {
            root = FXMLLoader.load(Objects.requireNonNull(ProjectWindowController.class.getResource("/EmployeeView.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scene = new Scene(root, 640, 640);

        //Injection of employee data


        nameText = (TextField) root.lookup("#nameText");
        if (nameText != null) {
            nameText.setText(e.getName());
            nameText.setEditable(false);
        }

        ageText = (TextField) root.lookup("#ageText");
        if (ageText != null) {
            ageText.setText(String.valueOf(e.getAge()));
            ageText.setEditable(false);

        }

        expText = (TextField) root.lookup("#expText");
        if (expText != null) {
            expText.setText(String.format("$%.2f",e.getExperience()));
            expText.setEditable(false);
        }

        workRateText = (TextField) root.lookup("#workRateLabel");
        if (workRateText != null) {
            workRateText.setText(String.format("%.2f",e.getWorkRate()));
            workRateText.setEditable(false);

        }

        salaryText = (TextField) root.lookup("#salaryText");
        if (salaryText != null) {
            salaryText.setText(String.format("$%.2f", e.getSalary()));
            salaryText.setEditable(false);

        }

        hobbyText = (TextField) root.lookup("#hobbyText");
        if (hobbyText != null) {
            hobbyText.setText(e.getHobby());
            hobbyText.setEditable(false);

        }

        empDescText = (TextArea) root.lookup("#empDescText");
        if (empDescText != null) {
            empDescText.setText(e.getDescription());
            empDescText.setEditable(false);

        }


        profileImage = (ImageView) root.lookup("#profileImage");
        if (profileImage != null) {
            profileImage.setUserData(e);
            if (e.getPicture() != null && !e.getPicture().equals("")) {
                profileImage.setImage(new Image(e.getPicture()));
            }

        }

        //ChoiceBox
        assignProjectBox = (ComboBox<Project>) root.lookup("#assignProjectBox");
        if (assignProjectBox != null) {
            assignProjectBox.setItems(gameLogic.getProjects());
            if (e.getCurProject() == null) {
                assignProjectBox.setValue(null);
            } else {

                assignProjectBox.setValue(e.getCurProject());

            }
        }

        //Cell factory to display specific attribute of project, this case the name
        assert assignProjectBox != null;
        assignProjectBox.setCellFactory(param -> new ListCell<>() {
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

        //Listening for change in the project choice box
        assignProjectBox.setOnAction((event) -> {
            Project selectedItem = assignProjectBox.getSelectionModel().getSelectedItem();
            for (Project p: gameLogic.getProjects()){
                if (p.getAssignedEmployees() != null && p != selectedItem){
                    Platform.runLater(()-> p.getAssignedEmployees().remove(e));
                }
            }
            e.setCurProject(selectedItem);
            selectedItem.addAssignedEmployees(e);
        });

        stage.setUserData(e);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The method uses the exact same FXML template file with the added difference, that the user can edit it and add a new employee to the list with it.
     * There is a validation system to check that only valid data is being put in the fields.
     * When the system determined that enough data is put in the save button is enabled.
     */
    public void DisplayNewEmployee() {
        stage = new Stage();

        stage.initModality(Modality.WINDOW_MODAL);
        try {
            root = FXMLLoader.load(Objects.requireNonNull(ProjectWindowController.class.getResource("/EmployeeView.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scene = new Scene(root, 640, 720);

        nameText = (TextField) root.lookup("#nameText");
        if (nameText != null) {
            nameText.setText("");
            nameText.setEditable(true);
        }

        ageText = (TextField) root.lookup("#ageText");
        if (ageText != null) {
            ageText.setText("");
            ageText.setEditable(true);

        }

        expText = (TextField) root.lookup("#expText");
        if (expText != null) {
            expText.setText("");
            expText.setEditable(true);
        }

        workRateText = (TextField) root.lookup("#workRateLabel");
        if (workRateText != null) {
            workRateText.setText("");
            workRateText.setEditable(true);

        }

        salaryText = (TextField) root.lookup("#salaryText");
        if (salaryText != null) {
            salaryText.setText("");
            salaryText.setEditable(true);

        }

        hobbyText = (TextField) root.lookup("#hobbyText");
        if (hobbyText != null) {
            hobbyText.setText("");
            hobbyText.setEditable(true);

        }

        empDescText = (TextArea) root.lookup("#empDescText");
        if (empDescText != null) {
            empDescText.setText("");
            empDescText.setEditable(true);
        }

        profileImage = (ImageView) root.lookup("#profileImage");
        if (profileImage != null) {
            profileImage.setUserData(emp);
            if (emp.getPicture() != null && !emp.getPicture().equals("")) {
                profileImage.setImage(new Image(emp.getPicture()));
            }
        }

        //ChoiceBox
        assignProjectBox = (ComboBox<Project>) root.lookup("#assignProjectBox");
        if (assignProjectBox != null) {
            assignProjectBox.setItems(gameLogic.getProjects());
            if (emp.getCurProject() == null) {
                assignProjectBox.setValue(null);
            } else {
                assignProjectBox.setValue(emp.getCurProject());
            }
        }

        assert assignProjectBox != null;
        assignProjectBox.setCellFactory(param -> new ListCell<>() {
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

        assignProjectBox.setOnAction((event) -> {
            Project selectedItem = assignProjectBox.getSelectionModel().getSelectedItem();

            emp.setCurProject(selectedItem);
            selectedItem.addAssignedEmployees(emp);
        });

        //Save button to add employee to the employee list.

        assert saveBtn != null;
        saveBtn.setFont(new Font("Impact", 24));
        saveBtn.setAlignment(Pos.CENTER);
        saveBtn.setPadding(new Insets(10, 10, 10, 10));

        Label ErrorLbl = new Label("");
        ErrorLbl.setFont(new Font("Impact", 24));
        ErrorLbl.setTextFill(Color.RED);

        //Data validation for the new employee data
        //I use a checksum to validate that mostly correct data is put in.

        nameText.textProperty().addListener((arg0, oldNameValue, newNameValue) -> {
            AtomicInteger nameValidator = new AtomicInteger(-1);
            if (!newNameValue.isBlank()) {
                nameValidator.addAndGet(1);
                emp.SetName(nameText.getText());
                nameText.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                CheckValidationNum(nameValidator);
            } else {
                ErrorLbl.setText("Empty name field");
                nameText.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL)));
            }
        });
        salaryText.textProperty().addListener((arg0, oldNameValue, newNameValue) -> {
            AtomicInteger salaryValidator = new AtomicInteger(-1);
            if (!newNameValue.isBlank()) {
                double sal;

                try {
                    sal = Double.parseDouble(salaryText.getText());
                    emp.setSalary(sal);
                    salaryText.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL)));
                    salaryValidator.addAndGet(1);

                } catch (NumberFormatException e) {
                    salaryValidator.addAndGet(salaryValidator.intValue()*-1);
                    ErrorLbl.setText("Not a number in salary");

                }
                CheckValidationNum(salaryValidator);
            } else {
                ErrorLbl.setText("Empty salary field");
                salaryText.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
        });
        expText.textProperty().addListener((arg0, oldNameValue, newNameValue) -> {
            AtomicInteger expValidator = new AtomicInteger(-1);

            if (!newNameValue.isBlank()) {
                double exp;

                try {
                    exp = Double.parseDouble(expText.getText());
                    emp.setExperience(exp);
                    expText.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL)));
                    expValidator.addAndGet(1);

                } catch (NumberFormatException e) {
                    expValidator.addAndGet((int) (expValidator.intValue() * -1.0));
                    ErrorLbl.setText("Not a number in experience");

                }

                CheckValidationNum(expValidator);
            } else {
                ErrorLbl.setText("Empty experience field");
                expText.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

            }
        });
        ageText.textProperty().addListener((arg0, oldNameValue, newNameValue) -> {
            AtomicInteger ageValidator = new AtomicInteger(-1);

            if (!newNameValue.isBlank()) {
                int age;
                try {
                    age = Integer.parseInt(ageText.getText());
                    emp.setAge(age);
                    ageText.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

                    ageValidator.addAndGet(1);

                } catch (NumberFormatException e) {
                    ageValidator.addAndGet(ageValidator.intValue()*-1);
                    ErrorLbl.setText("Not a number in age");

                }

                CheckValidationNum(ageValidator);
            } else {
                ErrorLbl.setText("Empty age field");
                ageText.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
        });
        hobbyText.textProperty().addListener((arg0, oldNameValue, newNameValue) -> {
            AtomicInteger hobbyValidator = new AtomicInteger(-1);

            if (!newNameValue.isBlank()) {

                hobbyValidator.addAndGet(1);

                hobbyText.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL)));
                emp.setHobby(hobbyText.getText());
                hobbyValidator.addAndGet(1);

                CheckValidationNum(hobbyValidator);
            } else {
                ErrorLbl.setText("Empty hobby field");
                hobbyText.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
        });
        empDescText.textProperty().addListener((arg0, oldNameValue, newNameValue) -> {
            AtomicInteger descValidator = new AtomicInteger(-1);

            if (!newNameValue.isBlank()) {
                descValidator.addAndGet(1);

                empDescText.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL)));
                emp.setDescription(empDescText.getText());
                descValidator.addAndGet(1);


                CheckValidationNum(descValidator);
            } else {
                ErrorLbl.setText("Empty description field");
                empDescText.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
        });
        workRateText.textProperty().addListener((observable, oldValue, newNameValue) ->
        {
            AtomicInteger workValidator = new AtomicInteger(-1);
            if (!newNameValue.isBlank()) {
                double wr;
                try {
                    wr = Double.parseDouble(workRateText.getText());
                    emp.setWorkRate(wr);
                    workValidator.addAndGet(1);
                    workRateText.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL)));
                } catch (NumberFormatException e) {
                    workValidator.addAndGet(workValidator.intValue() *-1);
                    ErrorLbl.setText("Not a number in work rate");

                }
                CheckValidationNum(workValidator);
            } else {
                ErrorLbl.setText("Empty work rate field");
                workRateText.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
        });

        //End of validation

        //Save button activates once enough validation points are set

        saveBtn.setDisable(true);
        ContentGrid = (GridPane) root.lookup("#ContentGrid");
        if (ContentGrid != null) {
            ContentGrid.add(saveBtn, 0, 8, 1, 1);
            ContentGrid.add(ErrorLbl, 1, 8, 1, 1);
        }
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Method to check for valid user input
     * @param validatorInput
     * The validation process gives an Atomic integer which will be added to the checksum
     */
    private void CheckValidationNum(AtomicInteger validatorInput) {
        if (validatorInput.get() < 0) {
            System.out.println("This input was not correct");
            checkSum.set(checkSum.get() - 1);
        }
        if (validatorInput.get() == 0) {
            checkSum.addAndGet(1);
        }
        System.out.println("Validation input: " + validatorInput.get());
        System.out.println("CheckSum: " + checkSum.get());
        if (checkSum.get() > 0) {
            assert saveBtn != null;
            saveBtn.setDisable(false);
            saveBtn.setOnAction(event -> {
                // String name, double salary, double experience, double workRate, String picture, int age,String description, String hobby
                //on save, add to employee list
                System.out.println("Welcome: " + emp.getName() + " to the company!");
                gameLogic.addEmployee(emp);
                stage.close();

            });
        }
    }

    /**
     * When the ImageViewer Element is clicked a file chooser opens and let's the user change the profile picture of the employee
     */
    @FXML
    public void handleMouseClick(MouseEvent event){

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose a Profile Picture");
        File f = chooser.showOpenDialog(stage);
        if (f != null) {
            e = (Employee) profileImage.getUserData();
            e.setPicture(f.getPath());
            profileImage.setImage(new Image(e.getPicture()));
        }
    }
}
