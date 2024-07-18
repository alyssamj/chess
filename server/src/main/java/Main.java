import chess.*;
import dataaccess.DataAccess;
import dataaccess.*;
import server.Server;
import service.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        // I need a new service
        DataAccess dataAccess = new DataAccess(new UserMemoryAccess(), new AuthMemoryAccess());
        var clearService = new Clear(dataAccess);
        var userService = new UserService(new UserMemoryAccess(), new AuthMemoryAccess());
        Server server = new Server(clearService, userService);
        server.run(8080);
    }
}