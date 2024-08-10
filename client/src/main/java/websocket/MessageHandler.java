package websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.Notification;

public interface MessageHandler {
    void notify(Notification notification);

    void error(ErrorMessage errorMessage);

    void loadGame(LoadGameMessage loadGameMessage);
}
