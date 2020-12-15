package model.database;

import model.project.repository.impl.SqliteProjectRepository;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Repository {
    protected Connection c;

    protected Repository() {
        try {
            c = SqliteDatabaseConnectionFactory.getConnection();
            prepareStatements();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    protected abstract void prepareStatements() throws SQLException;
}
