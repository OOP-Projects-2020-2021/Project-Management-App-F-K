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
  public enum Status {
    TO_DO,
    IN_PROGRESS,
    TURNED_IN,
    FINISHED
  }

  public enum Importance {
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    private int value;

    Importance(int value) {
      this.value = value;
    }

    public static class ImportanceComparator implements Comparator<Importance> {

      @Override
      public int compare(Importance i1, Importance i2) {
        return i1.value - i2.value;
      }
    }
  }

  /**
   * DeadlineStatus is used for database queries and specifies the required status of the project
   * with respect to its deadline.
   */
  public enum DeadlineStatus {
    IN_TIME_TO_FINISH, // the deadline is later than the current date (or today) and the project is
    // not finished yet
    OVERDUE, // the deadline is earlier than today and the project is not finished yet.
    FINISHED_IN_TIME, // the project was finished until the deadline.
    FINISHED_LATE, // the project was finished later than the deadline.
  }

  /**
   * PorjectSorter is used for the database and specifies by which attribute the projects must be
   * sorted when returned from the database.
   */
  public enum SorterType {
    NONE("true"),
    DEADLINE("Deadline"),
    STATUS("StatusId"),
    IMPORTANCE("ImportanceId");

    private String columnName; //specifies the name of the column in the database on which
    // sorting is
    // applied.

    SorterType(String columnName) {
      this.columnName = columnName;
    }

    public String getColumnName() {
      return this.columnName;
    }
  }

  /**
   * This class is used only when the project instance is created to be saved in the database, but
   * does not have a valid id yet.
   */
  public static class SavableProject extends Project {
    public SavableProject(
        String title,
        int teamId,
        LocalDate deadline,
        int supervisorId,
        int assigneeId,
        Importance importance) {
      super(-1, title, teamId, deadline, supervisorId, assigneeId, importance);
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
  /** Status of the project. */
  private Status status;
  /** Turn-in date of the project, if the project is turned in or finished. Otherwise, null. */
  private @Nullable LocalDate turnInDate;
  /** The importance of the project. */
  private Importance importance;

  /** Conrtuctor which sets the projects status to TO_DO. */
  public Project(
      int id,
      String title,
      int teamId,
      LocalDate deadline,
      int supervisorId,
      int assigneeId,
      Importance importance) {
    this(id, title, teamId, deadline, Status.TO_DO, supervisorId, assigneeId, null, importance);
  }

  public Project(
      int id,
      String title,
      int teamId,
      LocalDate deadline,
      Status status,
      int supervisorId,
      int assigneeId,
      @Nullable LocalDate turnInDate,
      Importance importance) {
    this.id = id;
    this.title = title;
    this.teamId = teamId;
    this.deadline = deadline;
    this.status = status;
    this.supervisorId = supervisorId;
    this.assigneeId = assigneeId;
    this.turnInDate = turnInDate;
    this.importance = importance;
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
    this.deadline = Objects.requireNonNull(deadline);
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

  public void setStatus(Status status) {
    this.status = Objects.requireNonNull(status);
  }

  public Status getStatus() {
    return status;
  }

  public Optional<LocalDate> getTurnInDate() {
    return Optional.ofNullable(turnInDate);
  }

  public void setTurnInDate(@Nullable LocalDate turnInDate) {
    this.turnInDate = turnInDate;
  }

  public Importance getImportance() {
    return importance;
  }

  public void setImportance(Importance importance) {
    this.importance = importance;
  }
}
