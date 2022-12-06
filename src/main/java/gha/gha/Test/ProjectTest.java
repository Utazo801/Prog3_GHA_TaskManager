package gha.gha.Test;


import gha.gha.BackEnd.Employee;
import gha.gha.BackEnd.Project;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

public class ProjectTest {
    Project p;
    Employee e;

    @Before
    public void SetUp() {
        p = new Project("Test", 100,150,1,"testing class");
        e = new Employee("Erik",50,0.1,1,"",19,"Test person", "Pain");

    }

    @Test
    public void setValue(){
        Project p = new Project();
        p.setValue(1.25);

        Assert.assertEquals(p.getValue(),1.25, 0.5);
    }
    @Test
    public void FinishProject(){
        p.FinishProject();
        Assert.assertEquals(p.getState(), Project.ProjectSate.IDLE);
    }

    @Test
    public void addEmployee(){
        p.addAssignedEmployees(e);
        Assert.assertEquals(p.getAssignedEmployees().size(),1);
    }

    @Test
    public void RunProject(){
        p.RunProject();
        Assert.assertEquals(p.getState(), Project.ProjectSate.IDLE);
    }

    @Test
    public void RunProjectWithEmployee(){
        p.addAssignedEmployees(e);
        p.RunProject();
        Assert.assertEquals(p.getState(), Project.ProjectSate.RUNNING);
    }



}
