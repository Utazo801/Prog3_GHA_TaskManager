package gha.gha.gameLogic;

import javafx.concurrent.Task;

import java.security.PublicKey;
import java.util.Random;

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
