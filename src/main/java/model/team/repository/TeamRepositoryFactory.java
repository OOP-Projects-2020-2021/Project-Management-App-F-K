package model.team.repository;

import model.team.repository.impl.SqliteTeamRepository;

public class TeamRepositoryFactory {
    public enum RepositoryType {
        SQLITE
    }

    public static TeamRepository getTeamRepository(RepositoryType type) {
        switch (type) {
            case SQLITE: return new SqliteTeamRepository();
            default: throw new IllegalArgumentException("RepositoryType " + type + " is not " +
                    "implemented");
        }
    }
}
