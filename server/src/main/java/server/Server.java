package server;

import requestsandresponses.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.*;
import service.*;
import spark.*;

public class Server {
    private UserService userService;
    private ClearService clearService;
    private GameService gameService;

    public int run(int desiredPort) {
        final UserDAO userDAO;
        try {
            userDAO = new MySQLUserAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        final AuthDAO authDAO;
        try {
            authDAO = new MySQLAuthAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        final GameDAO gameDAO;
        try {
            gameDAO =new MySQLGameAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        clearService = new ClearService(userDAO, authDAO, gameDAO);
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
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);

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

    private Object clear(Request req, Response res) {
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Gson gson = new Gson();
        try {
            ClearResult  clearResult = clearService.clear();
            if (clearResult.message() == null) {
                res.status(200);
                return gson.toJson(clearResult);
            }
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson("message", e.getMessage().getClass());
        }
        return null;
    }


    private Object login(Request req, Response res) {
        Gson gson = new Gson();
        LoginRequest loginReq = gson.fromJson(req.body(), LoginRequest.class);
        LoginResult loginResult = null;
        try {
            loginResult = userService.login(loginReq);
            if (loginResult.message() != null && loginResult.message().contains("unauthorized")) {
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
                } else {
                    res.status(400);
                    return gson.toJson(result);
                }
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

    private Object joinGame(Request req, Response res) {
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        JsonObject body = gson.fromJson(req.body(), JsonObject.class);
        if (body.get("playerColor") == null || body.get("gameID") == null) {
            JoinResult joinResult = new JoinResult("Error: bad request");
            res.status(400);
            return gson.toJson(joinResult);
        }
        String playerColor = body.get("playerColor").getAsString();
        Integer gameID = body.get("gameID").getAsInt();
        JoinRequest joinReq = new JoinRequest(authToken, playerColor, gameID);
        JoinResult joinResult = null;
        try {
            joinResult = gameService.joinGame(joinReq);
            if (joinResult.message() == null) {
                res.status(200);
                return gson.toJson(joinResult);
            } else if (joinResult.message().contains("Error: bad request")) {
                res.status(400);
                return gson.toJson(joinResult);
            } else if (joinResult.message().contains("Error: unauthorized")){
                res.status(401);
                return gson.toJson(joinResult);
            } else {
                res.status(403);
                return gson.toJson(joinResult);
            }
        } catch (DataAccessException e) {
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
