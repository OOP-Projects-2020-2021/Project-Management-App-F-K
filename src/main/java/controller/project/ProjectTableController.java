package controller.project;

import model.project.Project;
import view.project.ProjectListModel;
import view.project.ProjectTable;
import view.project.single_project.ProjectFrame;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Manages the project table, and it is responsible for displaying and updating the projects in the
 * table. When a user selects a project, it will open a separate frame for viewing the details of
 * that project.
 *
 * @author Beata Keresztes
 */
public class ProjectTableController implements PropertyChangeListener {

  private ProjectListModel projectListModel;
  private ProjectTable projectTable;

  public ProjectTableController(ProjectTable projectTable) {
    this.projectListModel = ProjectListModel.getInstance();
    projectListModel.addPropertyChangeListener(this);
    this.projectTable = projectTable;
  }

  public void initializeTableModel() {
    projectTable.fillTableModel(projectListModel.getProjectList());
  }

  private void updateTableModel() {
    projectTable.clearTableModel();
    if (!projectListModel.isEmptyProjectList()) {
      projectTable.fillTableModel(projectListModel.getProjectList());
    }
  }

  public void openProject(JFrame frame, int rowNr) {
    Project project = projectListModel.getProjectList().get(rowNr);
    if (project != null) {
      new ProjectFrame(frame, project);
      frame.setEnabled(false);
      frame.setVisible(false);
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(ProjectListModel.PROJECT_LIST)) {
      updateTableModel();
    }
  }
}
