package main;

import java.util.ArrayList;
import java.util.List;

public class TaskService {
    static int taskId = 0;
    static List<Task> tasks = new ArrayList<>();


    public static Task taskCreate(String taskTitle, String taskDescription){
        if (Session.isCurrentSession()){
            String taskOwner = Session.getCurrentUser();
            TaskStatus taskStatus = TaskStatus.ACTIVE;
            taskId++;

            Task task = new Task(taskId, taskTitle, taskDescription, taskStatus, taskOwner);
            tasks.add(task);
            return task;

        }
        return null;
    }


    public static List<Task> userTasks(){
        List<Task> currentUserTasks = new ArrayList<>();
        if (!Session.isCurrentSession()) {
            return currentUserTasks;
        }

        String currentUser = Session.getCurrentUser();
        for (Task task : tasks){
            if(task.getTaskOwner().equals(currentUser)){
                currentUserTasks.add(task);
            }
        }
        return currentUserTasks;
    }
    public static List<Task>  taskDelete(int taskId){
        List<Task> currentUserTasks = new ArrayList<>();
        tasks.removeIf(task -> taskId == task.getTaskId());
        return tasks;
    }
    public static List<Task> taskEdit (int taskId, String taskTitle, String taskDescription){
        List<Task> currentUserTasks = new ArrayList<>();
        String currentUser = Session.getCurrentUser();
        for (Task task : tasks){
            if(task.getTaskOwner().equals(currentUser)){
                if(task.getTaskId() == taskId){
                    task.setTaskTitle(taskTitle);
                    task.setTaskDescription(taskDescription);
                    return tasks;
                }
            }
        }
        return tasks;
    }

    public static void taskMarker (int taskId){
        List<Task> currentUserTasks = new ArrayList<>();
        String currentUser = Session.getCurrentUser();
        for (Task task : tasks){
            if(task.getTaskOwner().equals(currentUser)){
                if(taskId == task.getTaskId()){
                    task.setTaskStatus(TaskStatus.TASK_DONE);
                }
            }
        }
    }
}
