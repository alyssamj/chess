package websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;

public interface MessageHandler {

    void printMessage(String message);

    void loadGame(LoadGameMessage loadGameMessage);
}
