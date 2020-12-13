package model.user.repository.impl;
import model.user.repository.UserRepository;
import model.user.User;
import org.jetbrains.annotations.Nullable;

import java.sql.*;

public class SqliteUserRepository implements UserRepository {

    private Connection connection;
    private PreparedStatement saveUserStatement;
    private PreparedStatement getUserIdStatement;
    private PreparedStatement getUserByIdStatement;
    private PreparedStatement getUserByUsernameStatement;

    private static final String SAVE_USER_STATEMENT = "INSERT INTO User (UserName,Password) VALUES (?,?)";
    private static final String GET_USER_ID_STATEMENT = "SELECT UserId FROM User WHERE (Username = ? and Password = ?);";
    private static final String GET_USER_BY_ID_STATEMENT = "SELECT * FROM User WHERE UserId = ?;";
    private static final String GET_USER_BY_USERNAME_STATEMENT = "SELECT * FROM User WHERE Username = ?;";


    public SqliteUserRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:project_management_app.db");
            prepareStatements();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareStatements() throws SQLException {
        saveUserStatement = connection.prepareStatement(SAVE_USER_STATEMENT);
        getUserIdStatement = connection.prepareStatement(GET_USER_ID_STATEMENT);
        getUserByIdStatement = connection.prepareStatement(GET_USER_BY_ID_STATEMENT);
        getUserByUsernameStatement = connection.prepareStatement(GET_USER_BY_USERNAME_STATEMENT);
    }

    /**
     * When saving the user, only the username and the password are specified.
     * The user's id is automatically generated when the record is added to the User table.
     * @param user = the user to be saved
     * @throws SQLException if the user could not be saved
     */
    public void saveUser(User user) throws SQLException{
        saveUserStatement.setString(1, user.getUsername());
        saveUserStatement.setString(2, user.getPassword());
        saveUserStatement.execute();
        // check if the user was saved
        User savedUser = getUserByUsername(user.getUsername());
        if(savedUser == null) {
            throw new SQLException("User could not be saved.");
        }
    }
    /**
     * Gets the user's id when validating the sign-in operation.
     * @param username = introduced by the user at sign-in
     * @param password = password introduced by the user at sign-in
     * @return teh user's id when the sign-in was successful, otherwise a negative integer.
     * @throws SQLException if the data could not be rea from the database.
     */
    public int getUserId(String username,String password) throws SQLException {
        getUserIdStatement.setString(1, username);
        getUserIdStatement.setString(2,password);
        ResultSet result = getUserIdStatement.executeQuery();
        if (result.next()) {
            return result.getInt("UserId");
        } else {
            return -1;
        }
    }

    /**
     * Finds the user in the database based on its id.
     * @param id = the id of the user
     * @return the User who has that id
     * @throws SQLException if the data could not be accessed in the database.
     */
    @Nullable public User getUserById(int id) throws SQLException{
        getUserByIdStatement.setInt(1, id);
        ResultSet result = getUserByIdStatement.executeQuery();
        if (result.next()) {
            String username = result.getString("UserName");
            String password = result.getString("Password");
            return new User(id, username,password);
        } else {
            return null;
        }
    }

    /**
     * Finds the user in the database based on its username, used when managing the members of a team.
     * @param username = username of the user
     * @return User who has that username
     * @throws SQLException if the data could not be accessed in the database.
     */
    @Nullable public User getUserByUsername(String username) throws SQLException{
        getUserByUsernameStatement.setString(1, username);
        ResultSet result = getUserByUsernameStatement.executeQuery();
        if (result.next()) {
            int id = result.getInt("UserId");
            String password = result.getString("Password");
            return new User(id, username, password);
        } else {
            return null;
        }
    }


}


