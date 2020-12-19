package model.project.exceptions;

/**
 * IllegalProjectStatusChange is thrown when a project needed in an operation is not found in the
 * database.
 *
 * @author Bori Fazakas
 */
public class InexistentProjectException extends Exception {
  public InexistentProjectException(int projectId) {
    super(
        "This operation is illegal because the requested project with id "
            + projectId
            + " does not exist");
  }
}
