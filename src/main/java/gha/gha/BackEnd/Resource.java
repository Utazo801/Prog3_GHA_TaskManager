package gha.gha.BackEnd;

public class Resource  {
    protected String name;
    public Resource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    protected void SetName(String name){
        this.name = name;
    }


}
