package gha.gha.BackEnd;

import com.google.gson.annotations.Expose;

public class Resource  {

    @Expose
    protected String name;
    public Resource(String name) {
        this.name = name;
    }

    public Resource() {

    }

    public String getName() {
        return name;
    }
    protected void SetName(String name){
        this.name = name;
    }


}
