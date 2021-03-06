package view.project;

import controller.project.ProjectTableController;
import model.project.Project;
import view.CloseableComponent;
import view.UIFactory;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * ProjectTable displays the list of projects which belong to a certain team or which are related to
 * a certain user. By double clicking on the title of the project in the table, a new ProjectFrame
 * is opened, where the user can view all the details of the selected project.
 *
 * @author Beata Keresztes
 */
public class ProjectTable extends JTable implements CloseableComponent {

  private DefaultTableModel tableModel;
  private ProjectTableController controller;
  private JFrame frame;
  private static final String[] columnNames = {"Name", "Deadline", "Status", "Importance"};

  public ProjectTable(JFrame frame, ProjectListModel projectListModel) {
    this.controller = new ProjectTableController(this, projectListModel);
    this.frame = frame;
    initTableDesign();
    this.addMouseListener(new TableMouseListener());
    this.setDefaultRenderer(Object.class, new ImportanceRenderer());
    // initialize the model
    initTableModel();
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
            String.valueOf(project.getStatus()),
            String.valueOf(project.getImportance())
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

  class ImportanceRenderer implements TableCellRenderer {

    Color getColor(Project.Importance importance) {
      switch (importance) {
        case HIGH:
          return Color.decode("#fcddd7");
        case MEDIUM:
          return Color.decode("#fbfcd7");
        case LOW:
          return Color.decode("#cfe6d2");
      }
      return Color.decode("#cfe6d2");
    }

    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      JTextField editor = new JTextField();
      if (value != null) editor.setText(value.toString());
      if (isSelected) {
        editor.setBackground(Color.LIGHT_GRAY);
      } else {
        // get color from controller
        editor.setBackground(getColor(controller.getProjectImportance(row)));
      }
      return editor;
    }
  }

  @Override
  public void onClose() {
    controller.close();
  }
}
