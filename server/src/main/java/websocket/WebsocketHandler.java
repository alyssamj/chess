package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.WebSocketService;
import websocket.commands.MakeMove;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebsocketHandler {

    public final ConcurrentHashMap<Integer, Set<Session>> sessionsMap;
    private final WebSocketService webSocketService;

    public WebsocketHandler(WebSocketService webSocketService) {
        sessionsMap = new ConcurrentHashMap<>();
        this.webSocketService = webSocketService;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {

    }



    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        if (userGameCommand.getGameID() == null || userGameCommand.getAuthToken() == null) {
            ErrorMessage errorMessage = new ErrorMessage("Unvalid information");
            notify(session, errorMessage);
        } else {
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> {
                    connect(userGameCommand.getAuthToken(), userGameCommand.getGameID(), session);
                }
                case MAKE_MOVE -> {
                    MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
                    makeMove(makeMove.getMove(), userGameCommand.getGameID(), userGameCommand.getAuthToken(), session, makeMove.getTeamColor());
                }
                case LEAVE -> {
                    leaveGame(userGameCommand.getAuthToken(), userGameCommand.getGameID(), session);
                }
                case RESIGN -> {
                    resign(userGameCommand.getAuthToken(), userGameCommand.getGameID(), session);
                }
            }
        }
    }

    private void connect(String authToken, Integer gameID, Session session) throws IOException {
        if (!checkForNullValues(authToken, gameID, session)) {
            return;
        }
        Set<Session> sessions = sessionsMap.getOrDefault(gameID, new HashSet<>());
        sessions.add(session);
        sessionsMap.put(gameID, sessions);
        try {
            ChessGame chessGame = webSocketService.connect(gameID);
            var message = String.format("%s has entered the game", webSocketService.getUsername(authToken));
            Notification notification = new Notification(message);
            LoadGameMessage loadedGame = new LoadGameMessage(chessGame);
            if (session.isOpen()) {
                notify(session, loadedGame);
            }
            broadcast(session, notification);
        } catch (DataAccessException e) {
            var error = new ErrorMessage("Unable to connect to game");
            notify(session, error);
        }
    }

    private void makeMove(ChessMove chessMove, Integer gameID, String authToken, Session session, ChessGame.TeamColor teamColor) throws DataAccessException, IOException {
        if (!checkForNullValues(authToken, gameID, session)) {
            return;
        }
        System.out.println("ARE WE GETTING HERe");
        String username = webSocketService.getUsername(authToken);
        try {
            ChessGame game = webSocketService.connect(gameID);
            ChessPiece piece = game.getBoard().getPiece(chessMove.getStartPosition());
            game = webSocketService.connect(gameID);
            ChessGame.TeamColor turnColor = game.getTeamTurn();
            System.out.println("OR HRE");
            System.out.println(game.getState().toString());
            if (game.getState() == ChessGame.GameState.GAME_OVER) {
                String error = "Game is already over";
                ErrorMessage errorMessage = new ErrorMessage(error);
                notify(session, errorMessage);
                return;
            } else if (game.isInCheckmate(teamColor)) {
                LoadGameMessage loadGameMessage = new LoadGameMessage(game);
                notify(session, loadGameMessage);
                return;
            } else if (!game.validMoves(chessMove.getStartPosition()).contains(chessMove)) {
                String error = "Chess move is not valid";
                ErrorMessage errorMessage = new ErrorMessage(error);
                notify(session, errorMessage);
                return;
           } //else if (teamColor != turnColor) {
//                String error = "Not your turn";
//                ErrorMessage errorMessage = new ErrorMessage(error);
//                notify(session, errorMessage);
//                return;
//            }
            String result = webSocketService.makeMove(chessMove, gameID);
            game = webSocketService.connect(gameID);
            LoadGameMessage loadGame = new LoadGameMessage(game);
            Notification notification = new Notification(result);
            String message = String.format("%s has made the move: %s", username, chessMove);
            Notification broadcast = new Notification(message);
            notify(session, loadGame);
            //     notify(session, notification);
            broadcast(session, broadcast);
            broadcast(session, loadGame);
        } catch (IOException e) {
            var error = new ErrorMessage("unable to make move");
            notify(session, error);
        }
    }

        private void leaveGame (String authToken, Integer gameID, Session session) throws
        DataAccessException, IOException {
            if (!checkForNullValues(authToken, gameID, session)) {
                return;
            }
            String username = webSocketService.getUsername(authToken);
            try {
                String result = webSocketService.leaveGame(gameID, username);
                String leaveGame = "You have left the game";
                Set<Session> sessions = sessionsMap.get(gameID);
                sessions.remove(session);
                Notification notification = new Notification(leaveGame);
                var message = String.format("%s has left the game", username);
                Notification broadcastMessage = new Notification(message);
                LoadGameMessage loadedGame = new LoadGameMessage(webSocketService.connect(gameID));
  //              notify(session, notification);
                broadcast(session, broadcastMessage);
   //             broadcast(session, loadedGame);
            } catch (IOException e) {
                var error = new ErrorMessage("unable to leave game");
                notify(session, error);
            }
        }

        private void resign (String authToken, Integer gameID, Session session) throws DataAccessException, IOException
        {
            if (!checkForNullValues(authToken, gameID, session)) {
                return;
            }
            ChessGame game = webSocketService.connect(gameID);
            if (game.getState() == ChessGame.GameState.GAME_OVER) {
                String error = "Game is already over";
                ErrorMessage errorMessage = new ErrorMessage(error);
                notify(session, errorMessage);
            }
            game.setState(ChessGame.GameState.GAME_OVER);
            webSocketService.resign(gameID);
            ChessGame loadedGame = webSocketService.connect(gameID);
            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            String username = webSocketService.getUsername(authToken);
            String message = String.format("%s has resigned", username);
            Notification broadcastMessage = new Notification(message);
            String individiual = "You have resigned";
            Notification individualMessage = new Notification(individiual);
            broadcast(session, broadcastMessage);
            notify(session, individualMessage);
         //   broadcast(session, loadGameMessage);
         //   sessionsMap.remove(gameID);
        }

        private boolean checkForNullValues (String authToken,int gameID, Session session) throws IOException {
            if (!webSocketService.getAuth(authToken) || !webSocketService.getGameID(gameID)) {
                String error = "Unable to connect to game";
                ErrorMessage errorMessage = new ErrorMessage(error);
                notify(session, errorMessage);
                return false;
            }
            return true;
        }


        private <T extends ServerMessage > void notify (Session session, T message) throws IOException {
            Gson gson = new Gson();
            String messageJson = gson.toJson(message);
            if (session.isOpen()) {
                session.getRemote().sendString(messageJson);
            }
        }


        private <T extends ServerMessage > void broadcast (Session exceptThisSession, T serverMessage) throws
        IOException {
            Gson gson = new Gson();
            String messageJson = gson.toJson(serverMessage);
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
