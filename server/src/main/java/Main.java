import chess.*;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import server.Server;
import service.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        // I need a new service
        Server server = new Server();
        server.run(8080);
    }
}