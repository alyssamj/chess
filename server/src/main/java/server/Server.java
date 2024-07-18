package server;

import com.google.gson.Gson;
import dataaccess.*;
import org.eclipse.jetty.client.HttpResponseException;
import service.*;
import service.Requests_Responses.*;
import spark.*;

public class Server {
    private UserService userService;
    private Clear clearService;
   private GameService gameService;

    public int run(int desiredPort) {
        final UserDAO userDAO = new UserMemoryAccess();
        final AuthDAO authDAO = new AuthMemoryAccess();
        final GameDAO gameDAO = new GameMemoryAccess();

        clearService = new Clear(userDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(userDAO, authDAO, gameDAO);
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
       // Spark.delete("/db", this::clear);
        Spark.post("/session", this::login);
        Spark.post("/user", this::register);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);

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


    private Object login(Request req, Response res) {
     //   Handler logHandler = new Handler();
        Gson gson = new Gson();
        LoginRequest loginReq = gson.fromJson(req.body(), LoginRequest.class);
        LoginResult loginResult = null;
        try {
            loginResult = userService.login(loginReq);
            if (loginResult == null) {
                res.status(401);
                return gson.toJson(loginResult);
            } else {
                res.status(200);
                return gson.toJson(loginResult);
            }
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson("message", e.getMessage().getClass());
        }
    }
    private Object register(Request req, Response res) {
        Gson gson = new Gson();
        RegisterRequest registerReq = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterResult result = null;
        try {
            result = userService.register(registerReq);
            if (result.message() != null) {
                if (result.message().contains("already taken")) {
                    res.status(403);
                    return gson.toJson(result);
                }
                res.status(400);
                return gson.toJson(result);
                // if error message contains a certain word, then throw that status
                // check for error message
            }
            res.status(200);
            return new Gson().toJson(result);
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson("message", e.getMessage().getClass());
        }
    }

    private Object logout(Request req, Response res) {
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        LogoutResult logoutResult;
        if (logoutRequest.authToken() == null) {
            res.status(401);
            logoutResult = new LogoutResult("Error: unauthorized");
            return gson.toJson(logoutResult);
        }
        try {
            logoutResult = userService.logout(logoutRequest);
            if (logoutResult.message() == null) {
                res.status(200);
                return gson.toJson(logoutResult);
            } else {
                res.status(401);
                return gson.toJson(logoutResult);
            }
        }
        catch (DataAccessException e) {
            res.status(500);
            return gson.toJson("message", e.getMessage().getClass());
        }
    }

    private Object createGame(Request req, Response res) {
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        String gameName = String.valueOf(gson.fromJson(req.body(), CreateRequest.class));
        CreateRequest createReq = new CreateRequest(authToken, gameName);
        CreateResult createResult = null;
        try {
            createResult = gameService.createGame(createReq);

            if (createResult.message() == null) {
                res.status(200);
                return gson.toJson(createResult);
            } else if (createResult.message().contains("Error: bad request")) {
                res.status(400);
                return gson.toJson(createResult);
            } else {
                res.status(401);
                return gson.toJson(createResult);
            }
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson("message", e.getMessage().getClass());
        }
    }

    private Object listGames(Request req, Response res) {
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        ListRequest listReq = new ListRequest(authToken);
        ListResult listResult = null;
        try {
            listResult = gameService.getListofGames(listReq);
            if (listResult.message() == null) {
                res.status(200);
                return gson.toJson(listResult);
            } else if (listResult.message().contains("Error: unauthorized")){
                res.status(401);
                return gson.toJson(listResult);
            }
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson("message", e.getMessage().getClass());
        }
        return null;
    }
}
