package gha.gha.BackEnd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Singleton class containing the list of employees and projects
 * It manages the IO operations, such as reading and writing to JSON files trough GSON class.
 * It checks whether any of the projects are completed, then it calculates the new budget, pays the employees,
 * increases their experience and the value of the project they worked on.
 *
 */
public class GameLogic {
    private static ObservableList<Employee> employees;
    private static ObservableList<Project> projects;
    private double budget;
    private static GameLogic game_instance = null;

    private GameLogic() throws FileNotFoundException {
        budget = 10000.0;
        employees = FXCollections.observableList(ReadEmployeeData());
        projects= FXCollections.observableList(ReadProjectData());
    }

    /**
     * Returns the single instance of GameLogic
     * @return GameLogic
     */
    public static GameLogic getInstance(){
        if (game_instance == null){
            try {
                game_instance = new GameLogic();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return game_instance;
    }

    /**
     * Subtracts the salary of the employee from the budget
     * @param e
     * Employee instance
     */
    public void PayEmployee(Employee e){
        budget -= e.getSalary();
        e.setExperience((e.getExperience()*1.01));
    }

    /**
     * Substracts the cost of the project
     * @param p
     * Project instance
     */
    public void PayForProject(Project p){
        budget -= p.getCost();
        if (p.getValue() < Double.MAX_VALUE - p.getValue()){
            p.setValue(p.getValue() *1.01);

        }
        if (p.getCost() <Double.MAX_VALUE - p.getCost()){
            p.setCost(p.getCost() *1.02);
        }

    }

    /**
     * Adds an employee instance to the list
     * @param e
     * Employee instance
     */
    public void addEmployee(Employee e){
        employees.add(e);
    }

    /**
     * Adds a project instance to the project list
     * @param p
     * Project instance
     */
    public void addProject(Project p) {
        projects.add(p);
    }

    /**
     * Checks the status of projects and sets the above-mentioned events in motion.
     */
    public void CheckStatusForProjects(){
        for (Project proj: projects) {
            if (proj.getState() == Project.ProjectSate.COMPLETED){
                budget+= proj.getValue();
                PayForProject(proj);
                for (Iterator<Employee> it = proj.getAssignedEmployees().iterator(); it.hasNext(); ){
                    Employee e = it.next();
                    PayEmployee(e);
                    e.setCurProject(null);
                    it.remove();
                    //cheesy way to no include list in json
                    proj.SetListNUll();
                }
                proj.FinishProject();
            }
        }
    }


    /**
     * Returns the list of employees
     * @return ObservableList<Employees>
     */
    public ObservableList<Employee> getEmployees() {
        return FXCollections.observableList(employees);
    }

    /**
     * Returns the list of projects
     * @return ObservableList<Project>
     */
    public ObservableList<Project> getProjects() {
        return FXCollections.observableList(projects);
    }

    /**
     * Reads the employee data from a JSON file the trough GSON it puts it in an Arraylist
     * @return ArrayList<Employee>
     * @throws FileNotFoundException
     * Throws an exception when the source file is missing
     */
    public ArrayList<Employee> ReadEmployeeData() throws FileNotFoundException {
        File employeeData = new File("src/main/resources/employeeData.json");
        if (employeeData.exists()){
            Gson gson = new Gson();
            ArrayList<Employee> temp;
            Type listType = new TypeToken<ArrayList<Employee>>() {}.getType();
            FileReader fr = new FileReader(employeeData);
            temp = gson.fromJson(fr,listType);
            return temp;
        }
        else {
            System.err.println("File not found");
            return null;
        }
    }

    /**
     * Reads the project data from a JSON file the trough GSON it puts it in an Arraylist
     * @return ArrayList<Project>
     * @throws FileNotFoundException
     * Throws an exception when the source file is missing
     */
    public ArrayList<Project> ReadProjectData() throws FileNotFoundException {
        File projectData = new File("src/main/resources/projectData.json");
        if (projectData.exists()) {
            Gson gson = new Gson();
            ArrayList<Project> temp;
            Type listType = new TypeToken<ArrayList<Project>>() {
            }.getType();
            FileReader fr = new FileReader(projectData);
            temp = gson.fromJson(fr, listType);
            return temp;
        } else {
            System.err.println("File not found");
            return null;
        }
    }

    /**
     * Before the application closes the programme saves the state of the employee and project objects in their respective files.
     */
    public void CloseApplication(){

        //Saving private employeeeeeee
        File fEmp = new File("src/main/resources/employeeData.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        if (!fEmp.exists()) {
            try {
                fEmp.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (FileWriter fw = new FileWriter(fEmp)){
            gson.toJson(employees,fw);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Saved employees");
        //saving the modified projects
        File fProj = new File("src/main/resources/projectData.json");
        Gson gsonProj = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        if (!fProj.exists()) {
            try {
                fProj.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (FileWriter fw = new FileWriter(fProj)){
            gsonProj.toJson(projects,fw);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Saved projects");
    }

    /**
     * Returns the budget
     * @return double
     */
    public double getBudget() {
        return budget;
    }
}
