package model.team.exceptions;

/**
 * IllegalTeamRemovalException is thrown when the user attempts to delete a team, which still has unfinished projects.
 *
 * @author Beata Keresztes
 */
public class IllegalTeamRemovalException extends Exception{
    public IllegalTeamRemovalException(String teamName) {
        super(
                "You are not allowed to delete the team "
                        + teamName
                        + ", because it still has unfinished projects.");
    }
}
