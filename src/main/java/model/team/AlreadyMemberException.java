package model.team;

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
