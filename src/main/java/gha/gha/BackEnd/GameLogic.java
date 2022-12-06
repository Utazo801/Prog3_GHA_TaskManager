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

    //TODO
    // loading of data [done so]
    // load data to FontEnd [ongoing]
    // enabling the player to launch a project
    // displaying status of project
    // enabling the player to select employees to work on said project
    // make checks to constraint the employees
    // line chart for budget
    // for each project: when one finishes make a sequence of events,
    // such as increasing the cost and value, adding xp to employees and resetting the state of the project, so it can be relaunched]
    // change the path for the source files, and make sure they are dynamiccally reachable


    public void PayEmployee(Employee e){
        budget -= e.getSalary();
        e.setExperience(e.getExperience()*1.01);
    }
    public void PayForProject(Project p){
        budget -= p.getCost();
        p.setValue(p.getValue() *1.1);

        p.setCost(p.getCost() *1.02);
    }

    public void addEmployee(Employee e){
        employees.add(e);
    }
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

    public ObservableList<Employee> getEmployees() {
        return FXCollections.observableList(employees);
    }

    public ObservableList<Project> getProjects() {
        return FXCollections.observableList(projects);
    }

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

    public double getBudget() {
        return budget;
    }
}
