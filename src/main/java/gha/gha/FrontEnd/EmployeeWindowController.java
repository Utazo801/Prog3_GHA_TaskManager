package gha.gha.FrontEnd;

import gha.gha.BackEnd.Employee;
import gha.gha.BackEnd.GameLogic;
import gha.gha.BackEnd.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class EmployeeWindowController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Employee e;
    private final Button saveBtn;
    private final AtomicInteger checkSum;


    private Employee emp = new Employee();
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

    public EmployeeWindowController(Employee e, GameLogic gameLogic) {
        this.e = e;
        this.gameLogic = gameLogic;
        checkSum = new AtomicInteger(-7);
        saveBtn = new Button("Save employee");
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

    public void display() {
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        try {
            root = FXMLLoader.load(Objects.requireNonNull(ProjectWindowController.class.getResource("/EmployeeView.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scene = new Scene(root, 640, 640);

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
            expText.setText(String.valueOf(e.getExperience()));
            expText.setEditable(false);
        }

        workRateText = (TextField) root.lookup("#workRateLabel");
        if (workRateText != null) {
            workRateText.setText(String.valueOf(e.getWorkRate()));
            workRateText.setEditable(false);

        }

        salaryText = (TextField) root.lookup("#salaryText");
        if (salaryText != null) {
            salaryText.setText("$" + e.getSalary());
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
        if (profileImage != null) profileImage.setImage(new Image(e.getPicture()));

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
            int selectedIndex = assignProjectBox.getSelectionModel().getSelectedIndex();
            Project selectedItem = assignProjectBox.getSelectionModel().getSelectedItem();

            e.setCurProject(selectedItem);
            selectedItem.addAssignedEmployees(e);
        });


        stage.setScene(scene);
        stage.show();
    }

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

        //TODO FileChooser for profile pictures
        profileImage = (ImageView) root.lookup("#profileImage");
        if (profileImage != null) profileImage.setImage(null);

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
            int selectedIndex = assignProjectBox.getSelectionModel().getSelectedIndex();
            Project selectedItem = assignProjectBox.getSelectionModel().getSelectedItem();

            emp.setCurProject(selectedItem);
            selectedItem.addAssignedEmployees(emp);
        });


        saveBtn.setFont(new Font("Impact", 24));
        saveBtn.setAlignment(Pos.CENTER);
        saveBtn.setPadding(new Insets(10, 10, 10, 10));

        Label ErrorLbl = new Label("");
        ErrorLbl.setFont(new Font("Impact", 24));
        ErrorLbl.setTextFill(Color.RED);
        ErrorLbl.setText("itt vagyok");

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
                Integer age;
                try {
                    age = Integer.parseInt(ageText.getText());
                    emp.setAge(age);
                    ageText.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

                    ageValidator.addAndGet(1);

                } catch (NumberFormatException e) {
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
                    ErrorLbl.setText("Not a number in work rate");

                }
                CheckValidationNum(workValidator);
            } else {
                ErrorLbl.setText("Empty work rate field");
                workRateText.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
        });


        saveBtn.setDisable(true);
        ContentGrid = (GridPane) root.lookup("#ContentGrid");
        if (ContentGrid != null) {
            ContentGrid.add(saveBtn, 0, 8, 1, 1);
            ContentGrid.add(ErrorLbl, 1, 8, 1, 1);
        }
        stage.setScene(scene);
        stage.show();

    }

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
}
