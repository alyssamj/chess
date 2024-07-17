package service;

import dataaccess.UserDAO;
import service.Requests_Responses.*;
import model.*;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public LoginResult login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

     //   UserData compareUser =




        return null;
    }


}
