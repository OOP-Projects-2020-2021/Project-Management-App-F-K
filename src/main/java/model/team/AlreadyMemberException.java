package model.team;

/**
 * AlreadyMemberException is thrown when a request is sent for a user to join a team, but the
 * user is already the member of the team.
 *
 * @author Bori Fazakas
 */
public class AlreadyMemberException extends Exception {
  AlreadyMemberException(String userName, String teamName) {
    super(
        "The user "
            + userName
            + " can't join "
            + teamName
            + " team, because the user is "
            + "already a member");
  }
}
