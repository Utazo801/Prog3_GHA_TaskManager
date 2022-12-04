package gha.gha.BackEnd;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Project extends Resource {
    private double cost;
    private final double value;
    private BigDecimal completion;
    private int timeCost;
    private final String description;
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

    //Getters and Setters
    public ProjectSate getState() {
        return state;
    }

    public void setState(ProjectSate state) {
        this.state = state;
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

    public double getCompletion() {
        return completion.doubleValue();
    }


    public ObservableList<Employee> getAssignedEmployees() {
        return assignedEmployees;
    }

    public void addAssignedEmployees(Employee e) {
        if (assignedEmployees == null) {
            assignedEmployees = FXCollections.observableArrayList(new ArrayList<Employee>());
            //UpdateTimeCost();
        }
        assignedEmployees.add(e);
        System.out.println("Added Employee: " + e.getName() + " to Project " + getName());
    }

    private void UpdateTimeCost() {
        double initialTimeCost = timeCost;
        for (Employee e: assignedEmployees){
            timeCost += initialTimeCost - (e.getExperience()/e.getWorkRate());
        }
    }

    public int getTimeCost() {
        return timeCost;
    }


    public String getDescription() {
        return description;
    }

    //Methods:


    public void StartProject() {
        if (state == ProjectSate.PAUSED) {
            state = ProjectSate.RUNNING;
            notify();
        } else {
            state = ProjectSate.RUNNING;
            completion = new BigDecimal("0.0");
        }
    }

    public void Pause() throws InterruptedException {
        if (state == ProjectSate.RUNNING) {
            setState(ProjectSate.PAUSED);
            wait();
        }
    }

    public void FinishProject() {

        for(Employee e: assignedEmployees){
            assignedEmployees.remove(e);
        }
        completion = BigDecimal.valueOf(Double.parseDouble("0.0"));
        state = ProjectSate.IDLE;
    }


    public void RunProject() {
        if (state == null){
            StartProject();
        }
        if (assignedEmployees.size() > 0) {
            for (Employee employee : assignedEmployees) {
                //TODO Ezt át kéne dolgozni, hogy egyes dolgozók egyszerre tudjanak hatni a dologra0 és legyen köze az időtartamnak az alkalmazottak képességeihez, Multithreading?
                completion = BigDecimal.valueOf(completion.doubleValue() + (employee.getExperience()+employee.getWorkRate())/(double)timeCost).setScale(2,RoundingMode.HALF_UP);
                //TODO scale to time cost and calculate change it dynamically, for every assigned employee
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
