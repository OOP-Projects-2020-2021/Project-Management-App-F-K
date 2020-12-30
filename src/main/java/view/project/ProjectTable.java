package view.project;

import controller.project.ProjectTableController;
import model.project.Project;
import view.UIFactory;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ProjectTable displays the list of projects which belong to a certain team or which are related to
 * a certain user. By double clicking on the title of the project in the table, a new ProjectFrame
 * is opened, where the user can view all the details of the selected project.
 *
 * @author Beata Keresztes
 */
public class ProjectTable extends JTable {

  private DefaultTableModel tableModel;
  private ProjectTableController controller;
  private JFrame frame;
  private static final String[] columnNames = {"Name", "Deadline", "Status"};

  public ProjectTable(JFrame frame, ProjectListModel projectListModel) {
    this.controller = new ProjectTableController(this, projectListModel);
    this.frame = frame;
    initTableDesign();
    this.addMouseListener(new TableMouseListener());
    // initialize the model
    initTableModel();
    // sort the rows by the deadline
    addSorter();
  }

  private void initTableDesign() {
    setFillsViewportHeight(true);
    setRowSelectionAllowed(true);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setFont(UIFactory.NORMAL_TEXT_FONT);
    getTableHeader().setFont(UIFactory.HIGHLIGHT_TEXT_FONT);
    setRowHeight(30);
    // the columns cannot be rearranged
    getTableHeader().setReorderingAllowed(false);
  }

  private void addSorter() {
    // the projects can be sorted by deadlines when clicking on the column's header
    TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
    sorter.setComparator(
        1,
        (s1, s2) -> {
          try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse((String) s1);
            Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse((String) s2);
            return date1.compareTo(date2);
          } catch (ParseException e) {
            e.printStackTrace();
            return 0;
          }
        });
    this.setRowSorter(sorter);
  }

  private void initTableModel() {
    tableModel = (DefaultTableModel) this.getModel();
    tableModel.setColumnIdentifiers(columnNames);
    controller.initializeTableModel();
  }

  public void clearTableModel() {
    while (tableModel.getRowCount() > 0) {
      tableModel.removeRow(0);
    }
  }

  public void fillTableModel(List<Project> projectsList) {
    for (Project project : projectsList) {
      String[] rowData =
          new String[] {
            project.getTitle(),
            String.valueOf(project.getDeadline()),
            String.valueOf(project.getStatus())
          };
      tableModel.addRow(rowData);
    }
  }
  // the data in the tables cannot be edited only viewed
  @Override
  public boolean isCellEditable(int row, int column) {
    return false;
  }

  class TableMouseListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent evt) {
      int row = getSelectedRow();
      if (evt.getClickCount() > 1) {
        // on double click on the name of the project, the project frame is opened
        controller.openProject(frame, row);
      }
    }
  }
}
