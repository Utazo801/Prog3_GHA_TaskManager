package gha.gha.Services;

import gha.gha.BackEnd.GameLogic;
import gha.gha.BackEnd.Project;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;

public class ProjectRunningService extends Service<Void> {

    private final Project p;

    public ProjectRunningService(Project p, ProgressBar bar, GameLogic gameLogic) {

        this.p = p;
        setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gameLogic.CheckStatusForProjects();
                    }
                });

            }
        });
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws InterruptedException {
                while (p.getState() != Project.ProjectSate.COMPLETED) {
                    if (p.getAssignedEmployees().size() > 0) {
                        p.RunProject();
                        Thread.sleep(1000);
                        System.out.println("Project " + p.getName() + " state: " + p.getCompletion()+ " %");
                        updateProgress(p.getCompletion(), 1);

                    }
                    if(p.getState() == Project.ProjectSate.PAUSED){
                        wait();
                    }
                }
                return null;
            }
        };

    }
}
