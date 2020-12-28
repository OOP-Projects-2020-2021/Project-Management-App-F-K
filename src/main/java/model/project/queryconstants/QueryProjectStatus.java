package model.project.queryconstants;

import model.project.Project;

/**
 * An instance of QueryProjectStatus is used to specify the status of the projects that should be
 * retrieved from the database.
 */
public enum QueryProjectStatus {
  ALL(null) {
    @Override
    public Project.Status getCorrespondingStatus() {
      throw new IllegalStateException(
          "All QueryProjectStatus is not bound to a single " + "status");
    }
  },
  TO_DO(Project.Status.TO_DO),
  IN_PROGRESS(Project.Status.IN_PROGRESS),
  TURNED_IN(Project.Status.TURNED_IN),
  FINISHED(Project.Status.FINISHED);

  private final Project.Status correspondingStatus;

  QueryProjectStatus(Project.Status correspondingStatus) {
    this.correspondingStatus = correspondingStatus;
  }

  public Project.Status getCorrespondingStatus() {
    return correspondingStatus;
  }
}
