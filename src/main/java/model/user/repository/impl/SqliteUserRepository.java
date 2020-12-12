package model.user.repository.impl;
import model.user.repository.UserRepository;
import model.user.User;
import org.jetbrains.annotations.Nullable;

import java.sql.*;

public class SqliteUserRepository implements UserRepository {

    @Nullable private Connection connection;
    private PreparedStatement saveUserStatement;
    private PreparedStatement getUserStatement;

    private static final String SAVE_USER_STATEMENT = "INSERT INTO User (UserName,Password) VALUES (?,?)";
    private static final String GET_USER_STATEMENT = "SELECT * FROM User WHERE (Username = ? and Password = ?);";

    private static final String DATABASE_URL = "jdbc:sqlite:E:/CTI_2nd year/OOP/project_management_app/project_database/project_management_app.db";

    public SqliteUserRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            //String url = "jdbc:sqlite:project_management_app.db";
            connection = DriverManager.getConnection(DATABASE_URL);
            prepareStatements();
        }catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void prepareStatements() throws SQLException {
        if(connection != null) {
            saveUserStatement = connection.prepareStatement(SAVE_USER_STATEMENT);
            getUserStatement = connection.prepareStatement(GET_USER_STATEMENT);
        }
    }

    public void saveUser(User user) throws SQLException{
        saveUserStatement.setString(1, user.getUsername());
        saveUserStatement.setString(2, user.getPassword());
        saveUserStatement.execute();
        // check if the user was saved
        User savedUser = getUser(user.getUsername(),user.getPassword());
        if (savedUser != null) {
            throw new SQLException("User could not be saved.");
        }
    }

    @Nullable public User getUser(String username,String password) throws SQLException{
        getUserStatement.setString(1, username);
        getUserStatement.setString(2,password);
        ResultSet result = getUserStatement.executeQuery();
        if (result.next()) {
            int id = result.getInt("UserId");
            String usernameFound = result.getString("UserName");
            String passwordFound = result.getString("Password");
            return new User(id, usernameFound,passwordFound);
        } else {
            return null;
        }
    }
}


