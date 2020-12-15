package model.project;

import model.InexistentDatabaseEntityException;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
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
   * This class is used only when the project instance is created to be saved in the database, but
   * does not have a valid id yet.
   */
  public static class SavableProject extends Project {
    public SavableProject(String title, int teamId, LocalDate deadline, int supervisorId,
                          int assigneeId) {
      super (-1, title, teamId, deadline, supervisorId, assigneeId);
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
  /** Id of the team. */
  private int teamId;
  /** The deadline of the project until which it should be finished. */
  private LocalDate deadline;
  /** Optional. */
  private @Nullable String description;
  /** The id of the person who is responsible for the project. */
  private int assigneeId;
  /** The id of the person who checks whether the project is properly finished. */
  private int supervisorId;
  private ProjectStatus status;

  private Project(int id, String title, int teamId, LocalDate deadline, int supervisorId,
                  int assigneeId) {
    this(id, title, teamId, deadline, ProjectStatus.TO_DO, supervisorId, assigneeId);
  }

  private Project(int id, String title, int teamId, LocalDate deadline, ProjectStatus status,
                  int supervisorId, int assigneeId) {
    this.id = id;
    this.title = title;
    this.teamId = teamId;
    this.deadline = deadline;
    this.status = status;
    this.supervisorId = supervisorId;
    this.assigneeId = assigneeId;
  }

  public int getId() throws InexistentDatabaseEntityException {
    return this.id;
  }

  public int getTeamId() {
    return teamId;
  }

  public void setTeamId(int teamId) {
    this.teamId = teamId;
  }

  public LocalDate getDeadline() {
    return deadline;
  }

  public void setDeadline(LocalDate deadline) {
    this.deadline = deadline;
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

  public int getAssigneeId() {
    return assigneeId;
  }

  public void setAssigneeId(int assigneeId) {
    this.assigneeId = assigneeId;
  }

  public int getSupervisorId() {
    return supervisorId;
  }

  public void setSupervisorId(int supervisorId) {
    this.supervisorId = supervisorId;
  }

  public void setStatus(ProjectStatus status) {
    this.status = status;
  }

  public ProjectStatus getStatus() {
    return status;
  }
}
