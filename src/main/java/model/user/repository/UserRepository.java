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
    /** Access a user's data based on the username and password. */
    @Nullable User getUser(String username,String password) throws SQLException;
}
