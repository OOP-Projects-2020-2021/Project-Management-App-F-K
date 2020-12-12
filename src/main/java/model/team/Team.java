package model.team;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This represents a Team and holds data related to its members, projects and other components.
 *
 * @author Beata Keresztes, Bori Fazakas
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
}
