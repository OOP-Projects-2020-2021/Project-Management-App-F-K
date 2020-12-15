package model;

import model.project.Project;

/**
 * Exception used when a savable object's id is accessed.
 * Remark that savable objects should be used only befroe saving them in the database. Thus, they
 * don't have an id.
 *
 * @author Bori Fazakas
 */
public class InexistentDatabaseEntityException extends Exception {
  public InexistentDatabaseEntityException(Project.SavableProject p) {
    super("SaveableProjects do not exist in the database yet, so id access is illegal");
  }
}
