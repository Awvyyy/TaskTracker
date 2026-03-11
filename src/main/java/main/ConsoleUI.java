package main;

import java.util.List;
import java.util.Scanner;

import static main.Session.logOut;
import static main.TaskService.*;

public class ConsoleUI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void logInMenu(){
        System.out.println("1 - Log in");
        System.out.println("2 - Register");
        System.out.println("3 - Exit");
        System.out.print("Choose option: ");
    }

    public static void start(){
        while (true) {
            while (!Session.isCurrentSession()) {
                logInMenu();

                int option = readMenuOptions(scanner, 3);
                if (option == -1) {
                    System.out.println("Exiting...");
                    return;
                }
                scanner.nextLine();

                switch (option) {
                    case 1:
                        handleLogin();
                        break;
                    case 2:
                        handleRegister();
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Unexpected option!");
                }
            }

            printMenu();
            int option1 = readMenuOptions(scanner, 4);
            if (option1 == -1) {
                System.out.println("Exiting...");
                return;
            }
            scanner.nextLine();

            switch (option1) {
                case 1:
                    printTaskMenu();
                    int option2 = readMenuOptions(scanner, 4);
                    if (option2 == -1) {
                        System.out.println("Exiting...");
                        return;
                    }
                    scanner.nextLine();
                    switch (option2) {
                        case 1:
                            System.out.println();
                            handleTaskCreate();
                            break;
                        case 2:
                            System.out.println();
                            handleTaskDelete();
                            break;
                        case 3:
                            printTaskEditMenu();
                            int option3 = readMenuOptions(scanner, 3);
                            if (option3 == -1) {
                                System.out.println("Exiting...");
                                return;
                            }
                            scanner.nextLine();
                            switch(option3) {
                                case 1:
                                    System.out.println();
                                    handleTaskEdit();
                                    break;
                                case 2:
                                    System.out.println();
                                    handleTaskMarker();
                                    break;
                                case 3:
                                    System.out.println("Exiting...");
                                    break;
                            }
                            break;
                        case 4:
                            System.out.println();
                            break;
                        default:
                            System.out.println("Unexpected option!");
                    }
                    break;
                case 2:
                    System.out.println();
                    handleShowTasks();
                    break;
                case 3:
                    logOut();
                    System.out.println("Logging out...");
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Unexpected option!");
            }
        }
    }

    public static void printMenu(){
        System.out.println();
        System.out.println("1 - Tasks");
        System.out.println("2 - Show tasks");
        System.out.println("3 - Log out");
        System.out.println("4 - Exit");
        System.out.print("Choose option: ");
    }

    public static void printTaskMenu(){
        System.out.println();
        System.out.println("1 - Create task");
        System.out.println("2 - Delete task");
        System.out.println("3 - edit task");
        System.out.println("4 - exit");
        System.out.print("Choose option: ");
    }

    public static void printTaskEditMenu(){
        System.out.println();
        System.out.println("1 - Edit task");
        System.out.println("2 - Mark task as done");
        System.out.println("3 - Exit");
        System.out.println("Choose option: ");
    }

    public static int readMenuOptions(Scanner scanner, int maxOption) {
        int op;
        while (true) {
            while (!scanner.hasNextInt()) {
                if (!scanner.hasNextLine()) {
                    return -1;
                }
                System.err.println("Enter a number!");
                scanner.nextLine();
            }
            op = scanner.nextInt();
            if (op < 1 || op > maxOption) {
                System.err.println("Number must be between 1 and " + maxOption + "!");
            } else {
                break;
            }
        }
        return op;
    }

    public static void handleLogin() {

        System.out.println();
        System.out.print("Enter name: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.println();

        LoginResult result = AuthService.login(username, password);
        switch (result) {
            case INVALID_INPUT: {
                System.out.println("Input can't contain ':' and be blank!");
                break;
            }
            case SUCCESS: {
                System.out.println("Logging in...");
                break;
            }
            case WRONG_PASSWORD: {
                System.out.println("Password is incorrect!");
                break;
            }
            case USER_NOT_FOUND: {
                System.out.println("Can't find user: " + username + ".");
                break;
            }
            default:
                System.out.println("Unexpected error!");
                break;
        }
    }

    public static void handleRegister() {
        System.out.print("Enter name: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        RegisterResult result = AuthService.register(username, password);
        switch (result) {
            case INVALID_USERNAME: {
                System.out.println("Username can't contain ':' and be blank!");
                break;
            }
            case INVALID_PASSWORD: {
                System.out.println("Password can't contain ':' and be blank!");
                break;
            }
            case USER_EXISTS: {
                System.out.println("username " + username + " is already taken!");
                break;
            }
            case SUCCESS: {
                System.out.println("Registered successfully!");
                break;
            }
            default:
                System.out.println("Unexpected error!");
                break;
        }
    }

    public static void handleTaskCreate(){
        System.out.print("Enter task title: ");
        String taskTitle = scanner.nextLine();
        System.out.print("Enter task description: ");
        String taskDescription = scanner.nextLine();
        System.out.println();

        TaskActionResult result = TaskService.taskCreate(taskTitle, taskDescription);
        printTaskActionResult(result, "Task created successfully!");
    }

    public static void handleShowTasks() {
        if (!Session.isCurrentSession()) {
            System.out.println("User not logged in!");
            return;
        }

        List<Task> tasks = TaskService.userTasks();
        if (tasks.isEmpty()) {
            System.out.println("No current tasks");
            return;
        }

        for (Task task : tasks) {
            System.out.println("Id: " + task.getTaskId());
            System.out.println("Title: " + task.getTaskTitle());
            System.out.println("Description: " + task.getTaskDescription());
            System.out.println("Status: " + task.getTaskStatus());
            System.out.println();
        }
    }

    public static void handleTaskDelete() {
        Integer taskId = readTaskId("Enter task id: ");
        if (taskId == null) {
            System.out.println("Exiting...");
            return;
        }

        TaskActionResult result = taskDelete(taskId);
        printTaskActionResult(result, "Task deleted successfully!");
    }

    public static void handleTaskEdit() {
        System.out.println();
        Integer taskId = readTaskId("Enter task Id: ");
        if (taskId == null) {
            System.out.println("Exiting...");
            return;
        }

        System.out.print("Enter new task Title: ");
        String taskTitle = scanner.nextLine();

        System.out.print("Enter new task Description: ");
        String taskDescription = scanner.nextLine();

        TaskActionResult result = taskEdit(taskId, taskTitle, taskDescription);
        printTaskActionResult(result, "Task updated successfully!");
    }

    public static void handleTaskMarker() {
        System.out.println();
        Integer taskId = readTaskId("Enter task ID: ");
        if (taskId == null) {
            System.out.println("Exiting...");
            return;
        }

        TaskActionResult result = taskMarker(taskId);
        printTaskActionResult(result, "Task marked as done!");
    }

    private static Integer readTaskId(String prompt) {
        while (true) {
            System.out.print(prompt);
            while (!scanner.hasNextInt()) {
                if (!scanner.hasNextLine()) {
                    return null;
                }
                System.err.println("Enter a valid task id!");
                scanner.nextLine();
                System.out.print(prompt);
            }

            int taskId = scanner.nextInt();
            scanner.nextLine();
            if (taskId < 1) {
                System.err.println("Task id must be positive!");
                continue;
            }
            return taskId;
        }
    }

    private static void printTaskActionResult(TaskActionResult result, String successMessage) {
        switch (result) {
            case SUCCESS:
                System.out.println(successMessage);
                break;
            case NOT_LOGGED_IN:
                System.out.println("You need to login first!");
                break;
            case TASK_NOT_FOUND:
                System.out.println("Task not found!");
                break;
            case INVALID_TITLE:
                System.out.println("Task title can't be blank!");
                break;
            case INVALID_DESCRIPTION:
                System.out.println("Task description can't be blank!");
                break;
            case ALREADY_DONE:
                System.out.println("Task is already marked as done!");
                break;
            default:
                System.out.println("Unexpected error!");
                break;
        }
    }
}
