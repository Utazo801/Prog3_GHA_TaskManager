package gha.gha.Test;


import gha.gha.BackEnd.Employee;
import gha.gha.BackEnd.GameLogic;
import gha.gha.BackEnd.Project;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GameLogicTest {
    GameLogic gameLogic;
    int sizeOfEmployees;
    int sizeOfProjects;
    Employee e1;
    Employee e2;
    Project p1;
    Project p2;
    @Before
    public void SetUp(){
        gameLogic = GameLogic.getInstance();
        p1 = new Project("Test1", 100,150,1,"testing class1");
        p2 = new Project("Test2", 200,350,1,"testing class2");
        sizeOfEmployees = gameLogic.getEmployees().size();
        sizeOfProjects = gameLogic.getProjects().size();
        e1 = new Employee("Erik1",50,0.1,1,"",19,"Test person", "Pain");
        e2 = new Employee("Erik2",100,0.1,1,"",19,"Test person", "Pain");
    }

    @Test
    public void GetBudget(){
        Assert.assertEquals(gameLogic.getBudget(), 10000,0.5);
    }

    @Test
    public void AddProject(){
        gameLogic.addProject(p1);
        gameLogic.addProject(p2);
        Assert.assertEquals(gameLogic.getProjects().size(),sizeOfProjects+2);
    }
    @Test
    public void AddEmployee(){
        gameLogic.addEmployee(e1);
        gameLogic.addEmployee(e2);
        Assert.assertEquals(gameLogic.getEmployees().size(),sizeOfEmployees+2);
    }

    @Test
    public void CheckStatusForProjects(){
        p1.SetState(Project.ProjectSate.COMPLETED);
        gameLogic.addProject(p1);
        gameLogic.CheckStatusForProjects();
        Assert.assertEquals(gameLogic.getBudget(), 10000+150-100, 0.5);

    }

    @Test
    public void ReadEmployeeData(){
        ArrayList<Employee> employees;

        try {
            employees = gameLogic.ReadEmployeeData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Assume.assumeTrue(employees.size() != 0);
    }

    @Test
    public void ReadProjectData(){
        ArrayList<Project> projects;

        try {
            projects = gameLogic.ReadProjectData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Assume.assumeTrue(projects.size() != 0);
    }
}
