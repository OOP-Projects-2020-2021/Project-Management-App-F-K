package model.team;

/**
 * UnregisteredMemberRemoval is thrown when a request is sent to remove a user from the members
 * of a team, but the user is not a member of the team.
 *
 * @author Bori Fazakas
 */
public class UnregisteredMemberRemovalException extends Exception {
    UnregisteredMemberRemovalException(String teamName, String userName) {
        super (userName + " cannot leave team " + teamName + " because they have not been " +
                "registered as a member.");
    }
}
