package model.team.repository;

import model.team.repository.impl.SqliteTeamRepository;

/**
 * teamRepositoryFactory creates and returns an implementation of TeamRepository corresponding to
 * the specified RepositoryType.
 *
 * @author Bori Fazakas
 */
public class TeamRepositoryFactory {
  public enum RepositoryType {
    SQLITE
  }

  public static TeamRepository getTeamRepository(RepositoryType type) {
    switch (type) {
      case SQLITE:
        return new SqliteTeamRepository();
      default:
        throw new IllegalArgumentException("RepositoryType " + type + " is not " + "implemented");
    }
  }
}
