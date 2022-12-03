package gha.gha.BackEnd;

public class Employee extends Resource{
    private double salary;
    private double experience;
    private double workRate;
    private String description;
    private String picture;
    private String hobby;
    private int age;
    private Project curProject;

    //name, salary, experience,workRate, picture, age, description, traits[], hobby
    public Employee(String name, double salary, double experience, double workRate, String picture, int age,String description, String hobby) {
        super(name);
        this.salary = salary;
        this.experience = experience;
        this.workRate = workRate;
        this.picture = picture;
        this.description = description;
        this.age = age;
        curProject = null;
        this.hobby = hobby;
    }
    public Employee(){
        super("");
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project getCurProject() {
        return curProject;
    }

    public void setCurProject(Project curProject) {
        this.curProject = curProject;
    }
    public double getWorkRate() {
        return workRate;
    }

    public void setWorkRate(double workRate) {
        this.workRate = workRate;
    }

    public int getAge() {
        return age;
    }

    public String getHobby() {
        return hobby;
    }
    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getPicture() {
        return picture;
    }
    public void setPicture(String pic) {
        this.picture = pic;
    }

    @Override
    public void SetName(String name){
        super.SetName(name);
    }


    public void setAge(int parseInt) {
        this.age = parseInt;
    }
}
