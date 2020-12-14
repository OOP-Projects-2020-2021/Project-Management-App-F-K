package model.project;

import model.InexistentDatabaseEntityException;
import model.user.User;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Represents a Project and holds its data.
 *
 * @author Bori Fazakas
 */
public class Project {
  public enum ProjectStatus {
    TO_DO,
    IN_PROGRESS,
    MARKED_AS_DONE,
    FINISHED
  }

  /**
   * This class is used only when the project instance is created to be saved in the database,
   * but does not have a valid id yet.
   */
  public class SavableProject extends Project {
    public SavableProject(String title) {
      super (-1, title);
    }

    @Override
    public int getId() throws InexistentDatabaseEntityException {
      throw new InexistentDatabaseEntityException(this);
    }
  }

  /** Unique identifier of the project in the database. */
  private int id;
  /** The title must be unique inside the team, but not overall. */
  private String title;
  /** Optional. */
  private @Nullable String description;
  /** Optional. The person who is responsible for the project. */
  private @Nullable User assignee;
  /** Optional. The person who checks whether the project is properly finished. */
  private @Nullable User supervisor;
  private ProjectStatus status;

  private Project(int id, String title) {
    this.id = id;
    this.title = title;
    this.status = ProjectStatus.TO_DO;
  }

  private Project(int id, String title, ProjectStatus status) {
    this.id = id;
    this.title = title;
    this.status = status;
  }

  public int getId() throws InexistentDatabaseEntityException {
    return this.id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = Objects.requireNonNull(title);
  }

  public Optional<String> getDescription() {
    return Optional.ofNullable(description);
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public Optional<User> getAssignee() {
    return Optional.ofNullable(assignee);
  }

  public void setAssignee(@Nullable User assignee) {
    this.assignee = assignee;
  }

  public Optional<User> getSupervisor() {
    return Optional.ofNullable(supervisor);
  }

  public void setSupervisor(@Nullable User supervisor) {
    this.supervisor = supervisor;
  }

  public void setStatus(ProjectStatus status) {
    this.status = status;
  }

  public ProjectStatus getStatus() {
    return status;
  }
}
