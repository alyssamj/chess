package dataaccess;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.mindrot.jbcrypt.BCrypt;

public class MySQLUserAccess implements UserDAO {

    public MySQLUserAccess() throws DataAccessException {
        configureDatabase();
    }



    private void configureDatabase() throws DataAccessException {
//        DatabaseManager.createDatabase();
//        Connection connection = null;
//        String connectionURL = "jdbc:mysql://localhost:3306";
//        try (Connection c = DatabaseManager.getConnection("jdbc:mysql://localhost:3306")) {
//            Connection f = c;
//        } catch (SQLException ex) {
//            if (connection != null) {
//                connection.rollback();
//            }
//            throw new DataAccessException("");
//        }
//        try (var conn = DatabaseManager.getConnection()) {
//            for (var statement : createStatements) {
//                try (var preparedStatement = conn.prepareStatement(statement)) {
//                    preparedStatement.executeUpdate();
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException(String.format("unable to configure database"));
//        }
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
        CREATE TABLE IF NOT EXISTS users (
            `username` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL,
            PRIMARY KEY (`username`),
            INDEX(password),
            INDEX(email)
            )
"""
    };

    @Override
    public boolean clear() throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String getUserSQL = "SELECT password, email FROM users WHERE username=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(getUserSQL)) {
            preparedStatement.setString(1, username);
            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(username, password, email);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public void addUser(UserData user) throws DataAccessException {
        String username = user.username();
        String password = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        String email = user.email();
        String insertSQL = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to insert data into users table", e);
        }
    }

    @Override
    public int returnUsersSize() throws DataAccessException {
        return 0;
    }
}
