package model.project.exceptions;

import model.project.Project;

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
