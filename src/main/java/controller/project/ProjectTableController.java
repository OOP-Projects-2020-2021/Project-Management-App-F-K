package controller.project;

import model.project.Project;
import model.project.ProjectManager;
import view.project.ProjectListModel;
import view.project.ProjectTable;
import view.project.single_project.ProjectFrame;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * ProjectTableController manages the project table, and it is responsible for displaying and
 * updating the projects in the table, with the list of projects received from the ProjectListModel.
 * I followed the MVC pattern, such that the ProjectTableController listens to any change that
 * occurs in the properties of the ProjectListModel, more specifically if the list of projects to be
 * displayed changes after applying some filters on it, the ProjectTableController gets notified and
 * it updates the corresponding table.
 *
 * @author Beata Keresztes
 */
public class ProjectTableController implements PropertyChangeListener {

  private ProjectListModel projectListModel;
  private ProjectManager projectManager;
  private ProjectTable projectTable;

  public ProjectTableController(ProjectTable projectTable) {
    this.projectListModel = ProjectListModel.getInstance();
    projectListModel.addPropertyChangeListener(this);
    projectManager = ProjectManager.getInstance();
    projectManager.addPropertyChangeListener(this);
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
    if (evt.getPropertyName().equals(ProjectListModel.PROJECT_LIST)
        || evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.UPDATE_PROJECT.toString())
        || evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.CREATE_PROJECT.toString())
        || evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.SET_PROJECT_STATUS.toString())
        || evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.DELETE_PROJECT.toString())) {
      updateTableModel();
    }
  }
}
