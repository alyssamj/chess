package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.commands.MakeMove;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    MessageHandler messageHandler;
    private ChessGame chessGame;

    public WebSocketFacade(String url, MessageHandler messageHandler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.messageHandler = messageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new javax.websocket.MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    messageHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch(serverMessage.getServerMessageType()) {
            case LOAD_GAME -> chessGame = new Gson().fromJson(message, LoadGameMessage.class).getGame();
        }
    }

    public void connectToGame(Integer gameID, String authToken) {
        try {
            var userCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userCommand));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public ChessGame getChessGame() {
        return chessGame;
    }

    public void makeMove(String authToken, Integer gameID, ChessMove move) {
        try {
            var makeMove = new MakeMove(move, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(makeMove));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void leaveGame(String authToken, Integer gameID) {
        try {
            var userCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userCommand));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void resign(String authToken, Integer gameID) {
        try {
            var userCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(userCommand));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
