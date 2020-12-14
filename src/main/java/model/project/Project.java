package model.project;

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

  /** The title must be unique inside the team, but not overall. */
  private String title;
  /** Optional. */
  private @Nullable String description;
  /** Optional. The person who is responsible for the project. */
  private @Nullable User assignee;
  /** Optional. The person who checks whether the project is properly finished. */
  private @Nullable User supervisor;

  private ProjectStatus status;

  private Project(Builder builder) {
    this.title = builder.title;
    this.description = builder.description;
    this.assignee = builder.assignee;
    this.supervisor = builder.supervisor;
    this.status = ProjectStatus.TO_DO;
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
