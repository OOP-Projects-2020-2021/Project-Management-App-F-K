package model.project;

import model.project.repository.ProjectRepository;
import model.project.repository.impl.SqliteProjectRepository;

public class ProjectManager {
  private static ProjectManager instance = new ProjectManager();
  private ProjectRepository projectRepository = new SqliteProjectRepository();

  private ProjectManager() {}

  public static ProjectManager getInstance() {
    return instance;
  }
}
