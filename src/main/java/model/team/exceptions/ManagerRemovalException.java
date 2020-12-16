package model.team.exceptions;

/**
 * Exception thrown when the manager is to be removed from a team.
 *
 * @author Bori Fazakas
 */
public class ManagerRemovalException extends Exception {
  public ManagerRemovalException(String teamName, String managerName) {
    super(managerName + " cannot leave team " + teamName + " because they are the manager.");
  }
}
