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
        String deleteAuthSQL = "DELETE authToken username FROM auths WHERE authToken=?";
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
            throw new RuntimeException("Unable to insert data into auth table", e);
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
                    String username = rs.getString("username");
                    return username;
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
        String statement = "DELETE FROM auths";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
            if (authTokensSize() == 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public int authTokensSize() throws DataAccessException {
        String sizeQuery = "SELECT table_schema AS database_name, " +
                "SUM(data_length + index_length) AS database_size " +
                "FROM information_schema.tables " +
                "WHERE table_schema = ? " +
                "GROUP BY table_schema";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sizeQuery)) {
            preparedStatement.setString(1, "auths"); // Set your database name here
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("database_size");
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
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
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
