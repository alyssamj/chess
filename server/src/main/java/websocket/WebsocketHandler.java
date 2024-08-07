package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.WebSocketService;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
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
            case CONNECT -> connect(userGameCommand.getGameID(), session);
        }
    }

    private void connect(Integer gameID, Session session) {
        Set<Session> sessions = sessionsMap.getOrDefault(gameID, new HashSet<>());
        sessions.add(session);
        sessionsMap.put(gameID, sessions);
        try {
            ChessGame chessGame = webSocketService.connect(gameID);
            var message = String.format("%s has entered the game", gameID.toString());
            var loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            var loadedGame = new LoadGameMessage(chessGame);
            broadcast(session, loadedGame);
        } catch (IOException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends ServerMessage> void broadcast(Session exceptThisSession, T serverMessage) throws IOException {
        Gson gson = new Gson();
        String messageJson = gson.toJson(serverMessage.toString());
        var removeList = new ArrayList<Session>();
        for (var set : sessionsMap.values()) {
            for (Session session : set) {
                if (session.isOpen()) {
                    if (session != exceptThisSession) {
                        session.getRemote().sendString(messageJson);
                    }
                } else {
                    removeList.add(session);
                }
            }
        }
        for (Session session : removeList) {
            sessionsMap.values().forEach(sessions -> sessions.remove(session));
        }
    }

}
