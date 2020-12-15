package model.team;

import model.InexistentDatabaseEntityException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This represents a Team and holds data related to its members, projects and other components.
 *
 * @author Beata Keresztes, Bori Fazakas
 */
public class Team {

  /** The id that identifies the team in the database. */
  private int id;
  /** The name of the team. Doesn't need to be unique. */
  private @NotNull String name;
  /** The code that uniquely identifies the team, but it can be modified. */
  private @NotNull String code;
  /** The number of characters the code must consist of. */
  public static final int CODE_LENGTH = 6;
  /** The team manager. */
  private int managerId;

  /**
   * This class is used only when the team instance is created to be saved in the database, but does
   * not have a valid id yet.
   */
  public static class SavableTeam extends Team {
    public SavableTeam(@NotNull String name, int managerId, @NotNull String code) {
      super(-1, name, managerId, code);
    }

    @Override
    public int getId() throws InexistentDatabaseEntityException {
      throw new InexistentDatabaseEntityException(this);
    }
  }

  public Team(int id, @NotNull String name, int managerId, @NotNull String code) {
    this.id = id;
    this.name = name;
    this.managerId = managerId;
    this.code = code;
  }

  public int getId() throws InexistentDatabaseEntityException {
    return id;
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
