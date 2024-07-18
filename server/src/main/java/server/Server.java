package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import org.eclipse.jetty.client.HttpResponseException;
import service.*;
import service.Requests_Responses.*;
import spark.*;

public class Server {
    private final Clear clearService;
    private final UserService userService;
    // private final GameService gameService;

    public Server(Clear clearService, UserService userService) {
        this.clearService = clearService;
        this.userService = userService;
       // this. gameService = gameService;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
       // Spark.delete("/db", this::clear);
        Spark.post("/session", this::login);
        Spark.post("/user", this::register);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }


    /**
     * Pass in multiple service objects into my thingy.
     */

//    private Object clear(Request req, Response res) {
//        Clear clearservice = new Clear();
////        clearservice.clear();
//        res.status(200);
//        return "";
//    }


    private Object login(Request req, Response res) throws HttpResponseException {
     //   Handler logHandler = new Handler();
        Gson gson = new Gson();
        LoginRequest loginReq = gson.fromJson(req.body(), LoginRequest.class);
        LoginResult loginResult = userService.login(loginReq);
        if (loginResult == null) {
            res.status(401);
        } else {
            res.status(200);
            return new Gson().toJson(loginResult);
        }
        return "";
    }
    private Object register(Request req, Response res) throws HttpResponseException {
        Gson gson = new Gson();
        RegisterRequest registerReq = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterResult result = userService.register(registerReq);
        if (result == null) {
            res.status(401);
        } else {
            res.status(200);
            return new Gson().toJson(result);
        }
        return "";

    }

}
