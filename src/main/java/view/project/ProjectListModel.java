package view.project;

import model.PropertyChangeObservable;
import model.project.Project;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ProjectListMode represents the underlying model containing the list of projects to be displayed.
 * One instance is shared among the ProjectFilterPanel (and its controller) and the corresponding
 * ProjectTable (and its controller). When a user applies a filter on the projects list in the
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

  /**
   * Sorts the list of projects.
   * @param sortByComponent specifies the component after which the list should be sorted.
   * @param order specifies the order of the sorting: ASC or DESC
   * <p>For value 0, it will sort the projects based on the title.</p>
   * <p>For value 1, it will sort the projects based on the deadline.</p>
   * <p>For value 2, it will sort the projects based on the status.</p>
   */
  public void sortProjects(int sortByComponent, SortOrder order) {
    List<Project> oldProjectList = projectList;
    Comparator<Project> comparator = getComparator(sortByComponent);
    projectList.sort(comparator);
    if(order.equals(SortOrder.DESCENDING)) {
      Collections.reverse(projectList);
      System.out.println("reversed");
    }
    support.firePropertyChange(
            PROJECT_LIST, oldProjectList, Collections.unmodifiableCollection(projectList));
  }

  /**
   * Gets the appropriate comparator for sorting the list.
   * @param compareByComponent specifies the component after which the list should be sorted
   * For value 0, it will sort the projects based on the title.
   * For value 1, it will sort the projects based on the deadline.
   * For value 2, it will sort the projects based on the status.
   * @return the chosen comparator
   */
  public Comparator<Project> getComparator(int compareByComponent) {
    switch (compareByComponent) {
      case 0: return Comparator.comparing(Project::getTitle);
      case 1: return Comparator.comparing(Project::getDeadline);
      case 2: return Comparator.comparing(Project::getStatus);
      default: return null;
    }
  }

  public void setProjectList(List<Project> newProjectList) {
    List<Project> oldProjectList = projectList;
    projectList = newProjectList;
    support.firePropertyChange(
        PROJECT_LIST, oldProjectList, Collections.unmodifiableCollection(projectList));
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
