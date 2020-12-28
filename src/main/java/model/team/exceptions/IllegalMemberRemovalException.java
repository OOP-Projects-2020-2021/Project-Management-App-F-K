package model.team.exceptions;

/**
 * IllegalMemberRemovalException is thrown when the user attempts to remove a member from a team,
 * while that member still has unfinished projects, either assigned to or supervised by them.
 *
 * @author Beata Keresztes
 */
public class IllegalMemberRemovalException extends Exception{
    public IllegalMemberRemovalException(
            String memberName) {
        super(
                "You are not allowed to remove member "
                        + memberName
                        + ", because he/she still has unfinished projects");
    }
}
