package main;

import java.io.*;

import static main.FileUserRepository.saveUser;
import static main.PasswordHasher.hash;
import static main.PasswordHasher.verify;
import static main.UserRepository.findHashByUsername;

public class AuthService {

    public static CheckUserResult checkUser(String username){
        String storedUsername = findHashByUsername(username);
        if (storedUsername == null){
            return CheckUserResult.USER_NOT_FOUND;
        }
        else {
            return CheckUserResult.USER_EXISTS;
        }

    }

    public static LoginResult login(String username, String password) {
        username = username.trim();
        if (username.contains(":") || username.isEmpty()){
            return LoginResult.INVALID_INPUT;
        }
        if (password.contains(":") || password.isEmpty()){
            return LoginResult.INVALID_INPUT;
        }

        String stored = findHashByUsername(username);
        if (stored == null) {
            return LoginResult.USER_NOT_FOUND;
        }
        if (verify(password, stored)) {
            return LoginResult.SUCCESS;
        }
        return LoginResult.WRONG_PASSWORD;
    }


    public static RegisterResult register(String username, String password) {

        username = username.trim();
        // guard 1: username validation
        if (username.isEmpty()) {
            return RegisterResult.INVALID_USERNAME;
        }
        if (username.contains(":")) {
            return RegisterResult.INVALID_USERNAME;
        }

        // guard 2: password validation
        if (password.trim().isEmpty()) {
            return RegisterResult.INVALID_PASSWORD;
        }
        if (password.contains(":")) {
            return RegisterResult.INVALID_PASSWORD;
        }
        if (findHashByUsername(username) != null){
            return RegisterResult.USER_EXISTS;
        }
        saveUser(username, hash(password));
        return RegisterResult.SUCCESS;
    }
}