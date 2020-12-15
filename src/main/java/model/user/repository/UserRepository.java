package model.user.repository;

import model.InexistentDatabaseEntityException;
import model.user.User;
import org.jetbrains.annotations.Nullable;
import java.sql.SQLException;

/** Interface to manage the user data in the database. */
public interface UserRepository {

  /**
   * When saving the user, only the username and the password are specified. The user's id is
   * automatically generated when the record is added to the User table.
   *
   * @param user = the user to be saved
   * @throws SQLException if the user could not be saved
   */
  void saveUser(User user) throws SQLException;
  /**
   * Gets the user's id when validating the sign-in operation.
   *
   * @param username = introduced by the user at sign-in
   * @param password = password introduced by the user at sign-in
   * @return teh user's id when the sign-in was successful, otherwise a negative integer.
   * @throws SQLException if the data could not be rea from the database.
   */
  int getUserId(String username, String password) throws SQLException;
  /**
   * Finds the user in the database based on its id.
   *
   * @param id = the id of the user
   * @return the User who has that id
   * @throws SQLException if the data could not be accessed in the database.
   */
  @Nullable
  User getUserById(int id) throws SQLException;
  /**
   * Finds the user in the database based on its username, used when managing the members of a team.
   *
   * @param username = username of the user
   * @return User who has that username
   * @throws SQLException if the data could not be accessed in the database.
   */
  @Nullable
  User getUserByUsername(String username) throws SQLException;

  /**
   * Updates the fields of an existing user.
   *
   * @param user = the user with the new attributes
   * @throws SQLException if an error occurs at reading from the database
   */
  void updateUser(User user) throws SQLException, InexistentDatabaseEntityException;
}
