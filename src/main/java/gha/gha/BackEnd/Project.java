package gha.gha.BackEnd;

import com.google.gson.annotations.Expose;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Project class, loaded from JSON
 */
public class Project extends Resource {
    @Expose
    private double cost;
    @Expose
    private double value;
    private BigDecimal completion;
    @Expose

    private int timeCost;
    @Expose

    private  String description;
    private ObservableList<Employee> assignedEmployees;

    public enum ProjectSate {RUNNING, COMPLETED, IDLE, PAUSED}

    private ProjectSate state;

    //constructor
    public Project(String name, double cost, double value, int timeCost, String description) {
        super(name);
        this.cost = cost;
        this.value = value;
        this.timeCost = timeCost;
        this.description = description;
        this.assignedEmployees = FXCollections.observableArrayList();
        this.state = ProjectSate.IDLE;
        completion = new BigDecimal("0.0");
    }
    public Project(){
        super();
    }

    //Getters and Setters
    public ProjectSate getState() {
        return state;
    }
    public void SetState(ProjectSate projectSate) {
        state = projectSate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getValue() {
        return value;
    }
    public void setValue(double val) {value += val;}

    public double getCompletion() {
        return completion.doubleValue();
    }


    public ObservableList<Employee> getAssignedEmployees() {
        return assignedEmployees;
    }

    public void SetListNUll() {assignedEmployees = null;}

    /**
     * Adds a new employee to the assigned employees, if the list is null, it creates an instance of it
     * @param e
     * Employee instance
     */
    public void addAssignedEmployees(Employee e) {
        if (assignedEmployees == null) {
            assignedEmployees = FXCollections.observableArrayList(new ArrayList<>());
            //UpdateTimeCost();
        }
        assignedEmployees.add(e);
        System.out.println("Added Employee: " + e.getName() + " to Project " + getName());
    }

    public int getTimeCost() {
        return timeCost;
    }


    public String getDescription() {
        return description;
    }

    //Methods:

    /**
     * Sets the state of the project to completed, and resets the completion
     */
    public void FinishProject() {
        completion = null;
        state = ProjectSate.IDLE;
    }

    /**
     * Runs the project once, calculates completion
     */
    public void RunProject() {

        if (completion == null) completion = new BigDecimal("0.0");
        if (assignedEmployees.size() > 0) {
            state = ProjectSate.RUNNING;
            for (Employee employee : assignedEmployees) {

                completion = BigDecimal.valueOf(completion.doubleValue() + (employee.getExperience()*employee.getWorkRate())/(double)timeCost).setScale(2,RoundingMode.HALF_UP);
                System.out.println(completion);
            }
            if (completion.doubleValue() >= 1) {
                state = ProjectSate.COMPLETED;
                System.out.println("Project: " + getName() + " finished");
            }
        } else {
            System.out.println("No employee assigned to project");
        }

    }
    @Override
    public String toString() {
        return getName();
    }

}
