package model;

import model.project.Project;

public class InexistentDatabaseEntityException extends Exception {
  public InexistentDatabaseEntityException(Project.SavableProject p) {
    super("SaveableProjects do not exist in the database yet, so id access is illegal");
  }
}
