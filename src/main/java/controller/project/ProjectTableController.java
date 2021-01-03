package controller.project;

import controller.CloseablePropertyChangeListener;
import model.PropertyChangeObservable;
import model.project.Project;
import view.project.ProjectListModel;
import view.project.ProjectTable;
import view.project.single_project.ProjectFrame;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.List;

/**
 * ProjectTableController manages the project table, and it is responsible for displaying and
 * updating the projects in the table, with the list of projects received from the ProjectListModel.
 * I followed the MVC pattern, such that the ProjectTableController listens to any change that
 * occurs in the properties of the ProjectListModel, more specifically if the list of projects to be
 * displayed changes after applying some filters on it, the ProjectTableController gets notified and
 * it updates the corresponding table.
 *
 * @author Beata Keresztes, Bori Fazakas
 */
public class ProjectTableController implements CloseablePropertyChangeListener {

  private ProjectListModel projectListModel;
  private ProjectTable projectTable;
  private List<PropertyChangeObservable> propertyChangeObservables;

  public ProjectTableController(ProjectTable projectTable, ProjectListModel projectListModel) {
    this.projectListModel = projectListModel;
    this.projectTable = projectTable;
    propertyChangeObservables = List.of(projectListModel);
    this.setObservables();
  }

  public void initializeTableModel() {
    projectTable.fillTableModel(projectListModel.getProjectList());
  }

  private void updateTableModel() {
    projectTable.clearTableModel();
    projectTable.fillTableModel(projectListModel.getProjectList());
  }

  public void openProject(JFrame frame, int rowNr) {
    Project project = projectListModel.getProjectList().get(rowNr);
    if (project != null) {
      new ProjectFrame(frame, project);
      frame.setEnabled(false);
    }
  }

  public Project.Importance getProjectImportance(int projectIndex) {
    return projectListModel.getProjectList().get(projectIndex).getImportance();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(ProjectListModel.PROJECT_LIST)) {
      updateTableModel();
    }
  }

  @Override
  public List<PropertyChangeObservable> getPropertyChangeObservables() {
    return Collections.unmodifiableList(propertyChangeObservables);
  }
}
