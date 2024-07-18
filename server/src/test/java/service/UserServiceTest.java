package service;

import dataaccess.AuthMemoryAccess;
import dataaccess.DataAccessException;
import dataaccess.GameMemoryAccess;
import dataaccess.UserMemoryAccess;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Requests_Responses.LoginRequest;
import service.Requests_Responses.LoginResult;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService us;
    UserMemoryAccess uma = new UserMemoryAccess();
    AuthMemoryAccess auma = new AuthMemoryAccess();
    GameMemoryAccess gamema = new GameMemoryAccess();

    @BeforeEach
    void setup() {
        us = new UserService(uma, auma);
    }

    @Test
    void loginPreviousUser() {
        UserData newUser = new UserData("username", "password", "email");
        try {
            uma.addUser(newUser);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertEquals("[200] { \"username\":\"\", \"authToken\":\"\" }", us.login(new LoginRequest("username", "password")));

    }

}