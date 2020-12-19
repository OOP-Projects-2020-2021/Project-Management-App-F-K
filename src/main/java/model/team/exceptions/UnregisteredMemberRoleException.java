package model.team.exceptions;

/**
 * UnregisteredMemberRoleException is thrown if a role in a project/team is assigned to a user who
 * is not a member of the team/the team in which the project exists.
 *
 * @author Bori Fazakas
 */
public class UnregisteredMemberRoleException extends Exception{
    public UnregisteredMemberRoleException(String userName, int teamId, String operation) {
        super("User " + userName + " cannot " + operation + " because they are not the member of " +
                "the team with id " + teamId);
    }
}
