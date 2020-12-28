package view.project;

import model.PropertyChangeObservable;
import model.project.Project;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ProjectListMode represents the underlying model containing the list of projects to be displayed.
 * One instance is shared among the ProjectFilterPanel (and its controller) and the corresponding
 * ProjectTable (and its controller).
 * When a user applies a filter on the projects list in the
 * ProjectFilterPanel, the model will be updated by the ProjectFilterPanel's controller. The
 * controller which displays the actual projects in a table, listens to the changes made in the
 * model, and updates the view.
 *
 * @author Beata Keresztes, Bori Fazakas
 */
public class ProjectListModel implements PropertyChangeObservable {

  private List<Project> projectList;
  public static final String PROJECT_LIST = "Project list";

  private PropertyChangeSupport support = new PropertyChangeSupport(this);
  private static ProjectListModel instance = new ProjectListModel();

  public ProjectListModel() {
    projectList = new ArrayList<>();
  }

  public List<Project> getProjectList() {
    return Collections.unmodifiableList(projectList);
  }

  public void setProjectList(List<Project> newProjectList) {
    List<Project> oldProjectList = projectList;
    projectList = newProjectList;
    support.firePropertyChange(PROJECT_LIST, Collections.unmodifiableList(oldProjectList),
            Collections.unmodifiableCollection(projectList));
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
