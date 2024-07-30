package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//import static dataaccess.DatabaseManager.getConnection;

public class MySQLGameAccess implements GameDAO {

    public MySQLGameAccess() throws DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private final String[] createStatements = {
            """
        CREATE TABLE IF NOT EXISTS games (
            `gameID` INTEGER NOT NULL,
            `whiteUsername` varchar(256) DEFAULT NULL,
            `blackUsername` varchar(256) DEFAULT NULL,
            `gameName` varchar(256) NOT NULL,
            `game` longtext NOT NULL,
            PRIMARY KEY (`gameID`),
            INDEX(gameName)
            )
"""
    };

    @Override
    public GameData[] listGames() throws DataAccessException {
        int sizeOfList = gameListSize();
        GameData[] games = new GameData[sizeOfList];
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    int i = 0;
                    while (rs.next() && i < sizeOfList) {
                        int gameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String game = rs.getString("game");
                        Gson gson = new Gson();
                        ChessGame myGame = gson.fromJson(game, ChessGame.class);
                        games[i] = new GameData(gameID, whiteUsername, blackUsername, gameName, myGame);
                        i++;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return games;
    }

    @Override
    public boolean clear() throws DataAccessException {
        String statement = "DELETE FROM games";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
            if (gameListSize() == 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGameWithGameName(String gameName) throws DataAccessException {
        String getUserSQL = "SELECT gameID, whiteUsername, blackUsername, game FROM games WHERE gameName=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(getUserSQL)) {
            preparedStatement.setString(1, gameName);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    int gameID = rs.getInt("gameID");
                    String game = rs.getString("game");
                    Gson gson = new Gson();
                    ChessGame myGame;
                    myGame = gson.fromJson(game, ChessGame.class);
                    return new GameData(gameID, whiteUsername, blackUsername, gameName, myGame);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void createGame(GameData newGame) throws DataAccessException {
        Integer gameID = newGame.gameID();
        String whiteUser = newGame.whiteUsername();
        String blackUser = newGame.blackUsername();
        String gameName = newGame.gameName();
        ChessGame game = newGame.game();
        var serializer = new Gson();
        String jsonGame = serializer.toJson(game);

        String insertSQL = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, gameID);
            preparedStatement.setString(2, whiteUser);
            preparedStatement.setString(3, blackUser);
            preparedStatement.setString(4, gameName);
            preparedStatement.setString(5, jsonGame);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGameWithID(Integer gameID) throws DataAccessException {
        String getUserSQL = "SELECT whiteUsername, blackUsername, gameName, game FROM games WHERE gameID=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(getUserSQL)) {
            preparedStatement.setInt(1, gameID);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    String game = rs.getString("game");
                    Gson gson = new Gson();
                    ChessGame myGame;
                    myGame = gson.fromJson(game, ChessGame.class);
                    return new GameData(gameID, whiteUsername, blackUsername, gameName, myGame);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public int gameListSize() throws DataAccessException {
        String sizeQuery = "SELECT COUNT(*) AS count FROM games";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sizeQuery)) {
            preparedStatement.setString(1, "games"); // Set your database name here
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return 0;
    }

    @Override
    public void addBlackUsername(Integer gameID, String username) throws DataAccessException {
        String getSQL = "UPDATE games SET blackUsername = ? WHERE gameID=? AND blackUsername IS NULL";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(getSQL)) {

            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, gameID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void addWhiteUsername(Integer gameID, String username) throws DataAccessException {
        String getSQL = "UPDATE games SET whiteUsername = ? WHERE gameID=? AND whiteUsername IS NULL";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(getSQL)) {

            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, gameID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
