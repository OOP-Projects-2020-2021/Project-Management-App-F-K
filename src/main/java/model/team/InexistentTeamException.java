package model.team;

/**
 * Exception thrown is the team with the specified id or code does not exist in the database.
 *
 * @author Bori Fazakas
 */
public class InexistentTeamException extends Exception {
  public InexistentTeamException(String code) {
    super(
        "This operation is illegal because the requested team with code "
            + code
            + " does "
            + "not exist");
  }

  public InexistentTeamException(int teamId) {
    super(
        "This operation is illegal because the requested team with id "
            + teamId
            + " does "
            + "not exist");
  }
}
