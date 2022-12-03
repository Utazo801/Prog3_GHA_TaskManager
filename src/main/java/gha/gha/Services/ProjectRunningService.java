package gha.gha.Services;

import gha.gha.BackEnd.GameLogic;
import gha.gha.BackEnd.Project;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;

public class ProjectRunningService extends Service<Void> {

    private final Project p;
    private final ProgressBar bar;
    private final GameLogic gameLogic;

    public ProjectRunningService(Project p, ProgressBar bar, GameLogic gameLogic) {

        this.p = p;
        this.bar = bar;
        this.gameLogic = gameLogic;
        setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                //result = new String("Success running project "+ p.getName());
            }
        });
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                int i = 0;
                while (p.getState() != Project.ProjectSate.COMPLETED) {
                    if (p.getAssignedEmployees().size() > 0) {
                        p.RunProject();
                        i++;
                        System.out.println(i);
                        System.out.println("Project " + p.getName() + " state: " + p.getCompletion());
                        updateProgress(p.getCompletion(), p.getTimeCost());
                    }
                }
                gameLogic.CheckStatusForProjects();
                return null;
            }
        };

    }
}
