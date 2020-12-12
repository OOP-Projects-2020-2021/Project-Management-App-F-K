package main.java.model.team;

import main.java.model.Project;
import main.java.model.User;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This represents a Team and holds data related to its members, projects and other components.
 *
 * @author Beata Keresztes
 */
public class Team {

  /** The id that identifies the team in the database. */
  private Integer id;
  /** The name of the team. */
  private @NotNull String name;
  /** The code that uniquely identifies the team, but it can be modified. */
  private @NotNull String code;
  /** The number of characters the code must consist of. */
  public static final int CODE_LENGTH = 6;
  /** The team manager. */
  private int managerId;
  /** A set of the members of the team. */
  private HashSet<User> members = new HashSet<>();
  /** A list of the projects belonging to the team. */
  private List<Project> projects = new ArrayList<>();

  public Team(@NotNull String name, int managerId, @NotNull String code) {
    this.id = null;
    this.name = name;
    this.managerId = managerId;
    this.code = code;
  }

  public Team(int id, @NotNull String name, int managerId, @NotNull String code) {
    this.id = id;
    this.name = name;
    this.managerId = managerId;
    this.code = code;
  }

  public Optional<Integer> getId() {
    return Optional.of(id);
  }

  /**
   * Add a new member to the team.
   *
   * @param member the member to be added
   */
  public void addMember(User member) {
    this.members.add(member);
  }

  /**
   * Add a new project on the team.
   *
   * @param project the project to be added
   */
  public void addProject(Project project) {
    this.projects.add(project);
  }

  /**
   * Remove a member from the team.
   *
   * @param member the member to be removed
   */
  public void removeMember(User member) {
    this.members.remove(member);
  }

  /**
   * Remove a project from the team.
   *
   * @param project the project to be removed
   */
  public void removeProject(Project project) {
    this.projects.remove(project);
  }

  public @NotNull String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = Objects.requireNonNull(name);
  }

  public @NotNull String getCode() {
    return code;
  }

  public void setCode(@NotNull String code) {
    this.code = code;
  }

  public int getManagerId() {
    return managerId;
  }

  public void setManagerId(int managerId) {
    this.managerId = managerId;
  }

  public Set<User> getMembers() {
    return Collections.unmodifiableSet(members);
  }

  public List<Project> getProjects() {
    return Collections.unmodifiableList(projects);
  }
}
