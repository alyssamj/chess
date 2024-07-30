package dataaccess;

import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MySQLAuthAccess implements AuthDAO {

    public MySQLAuthAccess() throws DataAccessException{
        configureDatabase();
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {
        String deleteAuthSQL = "DELETE FROM auths WHERE authToken=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(deleteAuthSQL)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData verifyToken(String authToken) throws DataAccessException {
        String verifyAuthSQL = "SELECT username FROM auths WHERE authToken=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(verifyAuthSQL)) {
            preparedStatement.setString(1, authToken);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    return new AuthData(authToken, username);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void addAuthToken(String authToken, String username) throws DataAccessException {
        String insertSQL = "INSERT INTO auths (authToken, username) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String returnUserName(String authToken) throws DataAccessException {
        String getUserSQL = "SELECT username FROM auths WHERE authToken=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(getUserSQL)) {
            preparedStatement.setString(1, authToken);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean clear() throws DataAccessException {
        String statement = "TRUNICATE auths";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
            if (authTokensSize() == 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("HELISLIGN:SLDNGISDGSIDHGSIDLGHSDLGH");
            throw new DataAccessException("CLEAR AUTH ERROR MESSAGE");
        }
    }

    @Override
    public int authTokensSize() throws DataAccessException {
        String sizeQuery = "SELECT COUNT(*) AS count FROM auths";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sizeQuery)) {
            preparedStatement.setString(1, "auths"); // Set your database name here
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

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final String[] createStatements = {
            """
        CREATE TABLE IF NOT EXISTS auths (
            `authToken` varchar(256) NOT NULL,
            `username` varchar(256) DEFAULT NULL,
            PRIMARY KEY (`authToken`),
            INDEX(`username`)
            )
"""
    };
}
