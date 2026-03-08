package main;

import static main.AuthService.checkUser;
import static main.FileUserRepository.readUser;
import static main.FileUserRepository.saveUser;
import static main.PasswordHasher.hash;

public class UserRepository {


    public static String findHashByUsername(String username) {
        return readUser(username);
    }

}

