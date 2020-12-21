package view.team.single_team;

import view.UIFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Displays the list of projects, which can be sorted based on the deadline or filtered based on the
 * project's status, importance, assignee or supervisor.
 *
 * @author Beata Keresztes
 */
public class TeamProjectsPanel extends JPanel implements ActionListener {

  private JComboBox<String> statusFilterComboBox;
  private JComboBox<String> importanceFilterComboBox;
  private JCheckBox assignedProjectsCheckBox;
  private JCheckBox supervisedProjectsCheckBox;

  public TeamProjectsPanel(Dimension parentFrameDimension) {
    this.setPreferredSize(parentFrameDimension);
    this.setLayout(new BorderLayout());
    initProjectsPane();
  }

  private void initProjectsPane() {
    initProjectsHeader();
    initProjectsList();
  }

  private void initHeaderComponents() {
    // todo get this from controller
    String[] status = {"TO DO", "IN PROGRESS", "MARKED AS DONE", "FINISHED"};
    statusFilterComboBox = new JComboBox<>(status);
    String[] importance = {"LOW", "HIGH", "MEDIUM"};
    importanceFilterComboBox = new JComboBox<>(importance);
    assignedProjectsCheckBox = new JCheckBox();
    supervisedProjectsCheckBox = new JCheckBox();
  }

  private void initProjectsHeader() {
    initHeaderComponents();

    JPanel header = new JPanel();
    GroupLayout headerLayout = new GroupLayout(header);
    headerLayout.setAutoCreateGaps(true);
    headerLayout.setAutoCreateContainerGaps(true);
    header.setLayout(headerLayout);

    JLabel filterLabel = UIFactory.createLabel("Filter projects by:", null);
    JLabel statusFilterLabel = UIFactory.createLabel("Status:", null);
    JLabel importanceFilterLabel = UIFactory.createLabel("Importance:", null);
    JLabel assignedProjectsLabel = UIFactory.createLabel("Assigned to me:", null);
    JLabel supervisedProjectsLabel = UIFactory.createLabel("Supervised by me:", null);

    headerLayout.setHorizontalGroup(
        headerLayout
            .createSequentialGroup()
            .addGroup(
                headerLayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(filterLabel)
                    .addGroup(
                        headerLayout
                            .createSequentialGroup()
                            .addComponent(statusFilterLabel)
                            .addComponent(statusFilterComboBox))
                    .addGroup(
                        headerLayout
                            .createSequentialGroup()
                            .addComponent(assignedProjectsLabel)
                            .addComponent(assignedProjectsCheckBox)))
            .addGroup(
                headerLayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(
                        headerLayout
                            .createSequentialGroup()
                            .addComponent(importanceFilterLabel)
                            .addComponent(importanceFilterComboBox))
                    .addGroup(
                        headerLayout
                            .createSequentialGroup()
                            .addComponent(supervisedProjectsLabel)
                            .addComponent(supervisedProjectsCheckBox))));

    headerLayout.setVerticalGroup(
        headerLayout
            .createSequentialGroup()
            .addComponent(filterLabel)
            .addGroup(
                headerLayout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(statusFilterLabel)
                    .addComponent(statusFilterComboBox)
                    .addComponent(importanceFilterLabel)
                    .addComponent(importanceFilterComboBox))
            .addGroup(
                headerLayout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(assignedProjectsLabel)
                    .addComponent(assignedProjectsCheckBox)
                    .addComponent(supervisedProjectsLabel)
                    .addComponent(supervisedProjectsCheckBox)));

    this.add(header, BorderLayout.NORTH);
  }

  private void initProjectsList() {
    // todo get data from controller

    String[] columnNames = {"Name", "Deadline", "Status", "Importance"};
    int noColumn = columnNames.length;
    int noProjects = 20;
    String[][] projectsData = new String[noProjects][noColumn];
    for (int i = 0; i < noProjects; i++) {
      String status = (i % 2 == 0) ? "TO DO" : "IN PROGRESS";
      projectsData[i] = new String[] {"Name" + i, "2020-12-" + i % 30, status, "LOW"};
    }
    // the data in the tables cannot be edited only viewed
    JTable projectsTable =
        new JTable(projectsData, columnNames) {
          public boolean editCellAt(int row, int column, java.util.EventObject e) {
            return false;
          }
        };
    projectsTable.setFillsViewportHeight(true);
    projectsTable.setCellSelectionEnabled(true);
    // the columns cannot be rearranged
    projectsTable.getTableHeader().setReorderingAllowed(false);

    // the projects can be sorted by deadlines when clicking on the column's header
    TableRowSorter<TableModel> sorter = new TableRowSorter<>(projectsTable.getModel());
    sorter.setComparator(1, new DateComparator());
    projectsTable.setRowSorter(sorter);

    JScrollPane scrollPane = new JScrollPane(projectsTable);
    // title of the table
    scrollPane.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Projects List",
            TitledBorder.CENTER,
            TitledBorder.TOP));
    this.add(scrollPane, BorderLayout.CENTER);
  }

  /** Compare the dates, used for sorting the column containing the deadlines. */
  static class DateComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
      try {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(s1);
        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
        return date1.compareTo(date2);
      } catch (ParseException e) {
        e.printStackTrace();
        return 0;
      }
    }
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {}
}
