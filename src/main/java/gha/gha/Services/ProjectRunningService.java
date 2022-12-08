package gha.gha.Services;

import gha.gha.BackEnd.GameLogic;
import gha.gha.BackEnd.Project;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ProgressBar;

/**
 * Service class to run the projects in the background.
 * It's based on the Worker interface:
 * "A Worker is an object which performs some work in one or more background threads, and who's state is observable and available to JavaFX
 * applications and is usable from the main JavaFX Application thread. This interface is primarily implemented by both Task and Service,
 * providing a common API among both classes which makes it easier for libraries and frameworks to provide workers which work well when developing user interfaces.
 * A Worker may or may not be reusable depending on the implementation. A Task, for example, is not reusable while a Service is."
 * -<a href="https://docs.oracle.com/javase/8/javafx/api/javafx/concurrent/Worker.html">...</a>
 */
public class ProjectRunningService extends Service<Void> {

    private final Project p;

    public ProjectRunningService(Project p, ProgressBar bar, GameLogic gameLogic) {

        this.p = p;
        setOnSucceeded(event -> Platform.runLater(new Runnable() {
            /**
             * When the task is done this method synchronises with the main thread to make changes in the lists.
             */
            @Override
            public void run() {
                gameLogic.CheckStatusForProjects();
            }
        }));
    }

    /**
     * Task that runs the project in the background.
     * @return Task<Void>
     * returns a Task which will be executed in another thread
     */
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
