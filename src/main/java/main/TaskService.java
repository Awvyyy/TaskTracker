package main;

import java.util.ArrayList;
import java.util.List;

public class TaskService {
    private static int nextTaskId = 0;
    private static final List<Task> tasks = new ArrayList<>();


    public static TaskActionResult taskCreate(String taskTitle, String taskDescription) {
        String currentUser = getCurrentUser();
        if (currentUser == null) {
            return TaskActionResult.NOT_LOGGED_IN;
        }

        String normalizedTitle = taskTitle.trim();
        if (normalizedTitle.isBlank()) {
            return TaskActionResult.INVALID_TITLE;
        }

        String normalizedDescription = taskDescription.trim();
        if (normalizedDescription.isBlank()) {
            return TaskActionResult.INVALID_DESCRIPTION;
        }

        Task task = new Task(++nextTaskId, normalizedTitle, normalizedDescription, TaskStatus.ACTIVE, currentUser);
        tasks.add(task);
        return TaskActionResult.SUCCESS;
    }


    public static List<Task> userTasks() {
        List<Task> currentUserTasks = new ArrayList<>();
        String currentUser = getCurrentUser();
        if (currentUser == null) {
            return currentUserTasks;
        }

        for (Task task : tasks) {
            if (task.getTaskOwner().equals(currentUser)) {
                currentUserTasks.add(task);
            }
        }
        return currentUserTasks;
    }

    public static TaskActionResult taskDelete(int taskId) {
        Task task = findCurrentUserTaskById(taskId);
        if (task == null) {
            return getCurrentUser() == null ? TaskActionResult.NOT_LOGGED_IN : TaskActionResult.TASK_NOT_FOUND;
        }

        tasks.remove(task);
        return TaskActionResult.SUCCESS;
    }

    public static TaskActionResult taskEdit(int taskId, String taskTitle, String taskDescription) {
        String normalizedTitle = taskTitle.trim();
        if (normalizedTitle.isBlank()) {
            return TaskActionResult.INVALID_TITLE;
        }

        String normalizedDescription = taskDescription.trim();
        if (normalizedDescription.isBlank()) {
            return TaskActionResult.INVALID_DESCRIPTION;
        }

        Task task = findCurrentUserTaskById(taskId);
        if (task == null) {
            return getCurrentUser() == null ? TaskActionResult.NOT_LOGGED_IN : TaskActionResult.TASK_NOT_FOUND;
        }

        task.setTaskTitle(normalizedTitle);
        task.setTaskDescription(normalizedDescription);
        return TaskActionResult.SUCCESS;
    }

    public static TaskActionResult taskMarker(int taskId) {
        Task task = findCurrentUserTaskById(taskId);
        if (task == null) {
            return getCurrentUser() == null ? TaskActionResult.NOT_LOGGED_IN : TaskActionResult.TASK_NOT_FOUND;
        }
        if (task.getTaskStatus() == TaskStatus.TASK_DONE) {
            return TaskActionResult.ALREADY_DONE;
        }

        task.setTaskStatus(TaskStatus.TASK_DONE);
        return TaskActionResult.SUCCESS;
    }

    private static Task findCurrentUserTaskById(int taskId) {
        String currentUser = getCurrentUser();
        if (currentUser == null) {
            return null;
        }

        for (Task task : tasks) {
            if (task.getTaskOwner().equals(currentUser) && task.getTaskId() == taskId) {
                return task;
            }
        }
        return null;
    }

    private static String getCurrentUser() {
        if (!Session.isCurrentSession()) {
            return null;
        }

        String currentUser = Session.getCurrentUser();
        if (currentUser == null || currentUser.isBlank()) {
            return null;
        }
        return currentUser;
    }
}
