package model.user.repository;
import model.user.User;
import org.jetbrains.annotations.Nullable;
import java.sql.SQLException;

/**
 * Interface to manage the user data in the database.
 */
public interface UserRepository {

    /** Saves a new user in the database. */
    void saveUser(User user) throws SQLException;
    /** Get the user's id based on the username and password, used for validating the sign-in. */
    int getUserId(String username,String password) throws SQLException;
    /** Access the user's data based on the id of the user. */
    @Nullable User getUserById(int id) throws SQLException;
    /** Access the user's data based on the username of the user, used when managing the members of a team */
    @Nullable User getUserByUsername(String username) throws SQLException;
}
