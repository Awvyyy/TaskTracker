package main;

import java.util.Scanner;

public class ConsoleUI {
    private static final Scanner scanner = new Scanner(System.in);

    public static int readMenuOptions(Scanner scanner) {
        int op;
        while (true) {
            while (!scanner.hasNextInt()) {
                System.err.println("Enter a number!");
                scanner.nextLine();
            }
            op = scanner.nextInt();
            if (op < 1 || op > 4) {
                System.err.println("Number must be between 1 and 4!");
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

    public static void handleCheckUser() {
        System.out.print("Enter name: ");
        String username = scanner.nextLine();

        CheckUserResult result = AuthService.checkUser(username);

        switch (result){
            case USER_EXISTS:
                System.out.println("User exists!");
                break;
            case USER_NOT_FOUND:
                System.out.println("Can't find user: " + username);
                break;
        }
    }
    public static void printMenu(){
        System.out.println("1 - Login");
        System.out.println("2 - Register");
        System.out.println("3 - Check user's existence");
        System.out.println("4 - Exit");
        System.out.print("Choose option: ");
    }

    public static void start(){
        boolean running = true;

        while (running) {
            printMenu();
            int option = readMenuOptions(scanner);
            scanner.nextLine();

            switch (option) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegister();
                    break;
                case 3:
                    handleCheckUser();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Unexpected option!");
            }
        }
    }
}
