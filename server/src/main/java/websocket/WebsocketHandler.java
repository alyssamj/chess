package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.WebSocketService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.util.*;

public class WebsocketHandler {

    public final HashMap<Integer, Set<Session>> sessionsMap;
    private final WebSocketService webSocketService;

    public WebsocketHandler(WebSocketService webSocketService) {
        sessionsMap = new HashMap<>();
        this.webSocketService = webSocketService;
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case CONNECT -> connect(userGameCommand.getGameID(), session, sessionsMap);
        }
    }

    private void connect(Integer gameID, Session session, HashMap<Integer, Set<Session>> sessionsMap) {
        Set<Session> sessions = sessionsMap.getOrDefault(gameID, new HashSet<>());
        sessions.add(session);
        sessionsMap.put(gameID, sessions);
        try {
            ChessGame chessGame = webSocketService.connect(gameID);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void broadcast(String excludeVisitorName, ServerMessage serverMessage) {
        var removeList = new ArrayList<Session>();

        for (var set : sessionsMap.values()) {
            for (Session session : set) {
            }
        }
    }

}
