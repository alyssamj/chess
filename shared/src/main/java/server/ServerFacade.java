package server;

import com.google.gson.Gson;
import requestsandresponses.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;

public class ServerFacade {

    /*
    Spark.post("/session", this::login);
        Spark.post("/user", this::register);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);
     */

    private final String serverUrl;

    public ServerFacade(String url) { serverUrl = url;}

    public RegisterResult register(RegisterRequest registerRequest)  {
        var path = "/user";
        return this.makeRequest("POST", path, registerRequest, RegisterResult.class);
    }

    public LoginResult login(LoginRequest loginRequest) {
        var path = "/session";
        return this.makeRequest("POST", path, loginRequest, LoginResult.class);
    }

    public LogoutResult logout(LogoutRequest logoutRequest) {
        var path = "/session";
        return this.makeRequest("DELETE", path, logoutRequest, LogoutResult.class);
    }

    public CreateResult createGame(CreateRequest createRequest) {
        var path = "/game";
        return this.makeRequest("POST", path, createRequest, CreateResult.class);
    }

    public JoinResult joinGame(JoinRequest joinRequest) {
        var path = "/game";
        return this.makeRequest("PUT", path, joinRequest, JoinResult.class);
    }

    public ListResult listGames(ListRequest listRequest) {
        var path = "/game";
        return this.makeRequest("GET", path, listRequest, ListResult.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeHeader(request, http);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new RuntimeException("Make request is failing");
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }


    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new IOException();
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    private static void writeHeader(Object request, HttpURLConnection http) {
        if (request instanceof CreateRequest) {
            CreateRequest createRequest = (CreateRequest) request;
            handleRequest(createRequest.authToken(), http);
        } else if (request instanceof JoinRequest) {
            JoinRequest joinRequest = (JoinRequest) request;
            handleRequest(joinRequest.authToken(), http);
        } else if (request instanceof LogoutRequest) {
            LogoutRequest logoutRequest = (LogoutRequest) request;
            handleRequest(logoutRequest.authToken(), http);
        } else if (request instanceof ListRequest) {
            ListRequest listRequest = (ListRequest) request;
            handleRequest(listRequest.authToken(), http);
        }
    }

    private static void handleRequest(String header, HttpURLConnection http) {
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", header);
    }

}
