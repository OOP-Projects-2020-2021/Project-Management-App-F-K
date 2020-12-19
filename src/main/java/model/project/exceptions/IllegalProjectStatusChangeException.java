package model.project.exceptions;

import model.project.Project;

/**
 * IllegalProjectStatusChange is thrown when someone tries to change a project's status but the
 * change is illegal.
 *
 * @author Bori Fazakas
 */
public class IllegalProjectStatusChangeException extends Exception {
  public IllegalProjectStatusChangeException(
      Project.ProjectStatus oldStatus, Project.ProjectStatus newStatus) {
    super(
        "You are not allowed to change the project's status from "
            + oldStatus
            + " to "
            + newStatus);
  }
}
