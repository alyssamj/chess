package serverfacade;

import com.google.gson.Gson;
import requestsandresponses.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {
    private final int port;


    public ServerFacade(int port) {
        this.port = port;
    }

    public RegisterResult register(RegisterRequest user)  {
        var path = "/user";
        return this.makeRequest("POST", path, user, RegisterResult.class);
    }

    public LoginResult login(LoginRequest loginRequest) {
        var path = "/session";
        return this.makeRequest("POST", path, loginRequest, LoginResult.class);
    }

    public LogoutResult logout(LogoutRequest authToken) {
        var path = "/session";
        return this.makeRequest("DELETE", path, authToken, LogoutResult.class);
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
            URL url = new URL("http", "localhost", port, path);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeHeader(request, http);

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
       //     System.out.println("Unable to process request");
            return null;
        //    throw new RuntimeException("Make request is failing: " + e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            Gson gson = new Gson();
            if (request instanceof JoinRequest) {
                JoinRequest joinRequest = (JoinRequest) request;
                JoinRequestBody joinRequestBody = new JoinRequestBody(joinRequest.playerColor(), joinRequest.gameID());
                String reqData = gson.toJson(joinRequestBody);
                try (OutputStream reqBody = http.getOutputStream()) {
                    reqBody.write(reqData.getBytes(StandardCharsets.UTF_8));
                }
            } else {
                String reqData = gson.toJson(request);
                try (OutputStream reqBody = http.getOutputStream()) {
                    reqBody.write(reqData.getBytes(StandardCharsets.UTF_8));
                }
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

    private static void writeHeader(Object request, HttpURLConnection http) throws IOException {
        if (request instanceof CreateRequest) {
            CreateRequest createRequest = (CreateRequest) request;
            handleRequest(createRequest.authToken(), http);
            writeBody(createRequest, http);
        } else if (request instanceof JoinRequest) {
            JoinRequest joinRequest = (JoinRequest) request;
            handleRequest(joinRequest.authToken(), http);
            writeBody(joinRequest, http);
        } else if (request instanceof LogoutRequest) {
            LogoutRequest logoutRequest = (LogoutRequest) request;
            handleRequest(logoutRequest.authToken(), http);
        } else if (request instanceof ListRequest) {
            ListRequest listRequest = (ListRequest) request;
            handleRequest(listRequest.authToken(), http);
        } else {
            writeBody(request, http);
        }
    }



    private static void handleRequest(String header, HttpURLConnection http) {
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", header);
    }

    public record JoinRequestBody(String playerColor, int gameID) {
    }

}
