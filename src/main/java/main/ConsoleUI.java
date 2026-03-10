package main;

import java.util.List;
import java.util.Scanner;

import static main.Session.logOut;
import static main.TaskService.taskDelete;
import static main.TaskService.taskEdit;

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
                            System.out.println();
                            handleTaskEdit();
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
        Task task = TaskService.taskCreate(taskTitle, taskDescription);
        if (task == null) {
            System.out.println("You need to login first!");
            return;
        }
        System.out.println("Task created successfully!");
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
    public static void handleTaskDelete(){
        System.out.println();
        System.out.print("Enter task id: ");
        int taskId = scanner.nextInt();
        taskDelete(taskId);
    }
    public static void handleTaskEdit(){
        System.out.println();
        System.out.print("Enter task Id: ");
        int taskId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new task Title: ");
        String taskTitle = scanner.nextLine();
        System.out.println();

        System.out.print("Enter new task Description: ");
        String taskDescription = scanner.nextLine();
        taskEdit(taskId, taskTitle, taskDescription);
        System.out.println();

    }
}
