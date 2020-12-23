package view.project;

import model.PropertyChangeObservable;
import model.project.Project;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * ProjectListMode represents the underlying model containing the list of projects to be displayed.
 * It is implemented using the singleton pattern, so that when the data in the model changes, all of its
 * instances would be updated.
 * When a user applies a filter on the projects list in the ProjectFilterPanel, the model will be updated by the ProjectFilterPanel's
 * controller.
 * The controller which displays the actual projects in a table, listens to the changes made in the model, and updates the view.
 *
 * @author Beata Keresztes
 */
public class ProjectListModel implements PropertyChangeObservable {

  private List<Project> projectList;
  private PropertyChangeSupport support = new PropertyChangeSupport(this);
  public static final String PROJECT_LIST = "Project list";
  private static ProjectListModel instance = new ProjectListModel();

  private ProjectListModel() {
    projectList = new ArrayList<>();
  }

  public static ProjectListModel getInstance() {
    return instance;
  }

  public List<Project> getProjectList() {
    return projectList;
  }

  public boolean isEmptyProjectList() {
    return projectList == null || projectList.isEmpty();
  }

  public void setProjectList(List<Project> newProjectList) {
    List<Project> oldProjectList = projectList;
    projectList = newProjectList;
    support.firePropertyChange(PROJECT_LIST, oldProjectList, projectList);
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }
}
