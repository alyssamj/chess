package websocket.messages;

public class Notification extends ServerMessage {
    private String notification;

    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.notification = message;
    }

    public String getMessage() {
        return notification;
    }
}
