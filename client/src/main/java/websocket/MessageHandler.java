package websocket;

import websocket.messages.Notification;

public interface MessageHandler {
    void notify(Notification notification);
}
