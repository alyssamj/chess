package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.commands.MakeMove;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
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

            this.session.addMessageHandler(new javax.websocket.MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    System.out.println("Received message: " + message);
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch(serverMessage.getServerMessageType()) {
                        case LOAD_GAME:
                            LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                            messageHandler.loadGame(loadGameMessage);
                            break;
                        case NOTIFICATION:
                            Notification notification = new Gson().fromJson(message, Notification.class);
                            messageHandler.notify(notification);
                            break;
                        case ERROR:
                            ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                            messageHandler.error(errorMessage);
                            break;
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

//    @OnWebSocketConnect
//    public void onConnect(Session session) {
//        this.session = session;
//        System.out.println("CONNCTD TO WBSOCKT");
//    }

//    @OnWebSocketMessage
//    public void onMessage(Session session, String message) {
//        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
//        switch(serverMessage.getServerMessageType()) {
//            case LOAD_GAME:
//                LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
//                messageHandler.loadGame(loadGameMessage);
//                break;
//            case NOTIFICATION:
//                Notification notification = new Gson().fromJson(message, Notification.class);
//                messageHandler.notify(notification);
//                break;
//            case ERROR:
//                ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
//                messageHandler.error(errorMessage);
//                break;
//        }
//    }

    public void connectToGame(Integer gameID, String authToken) {
        if (this.session == null || !this.session.isOpen()) {
            System.out.println("session isn't open. Please try again.");;
        }
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

    public void makeMove(String authToken, Integer gameID, ChessMove move, ChessGame.TeamColor teamColor) {
        try {
            var makeMove = new MakeMove(move, authToken, gameID, teamColor);
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
            System.out.println("Unable to resign");
        }
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("CLOSED");
    }
}
