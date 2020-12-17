package view.team.single_team;

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

public class TeamProjectsPanel extends JPanel implements ActionListener {

  // todo add sorting options
  private JComboBox<String> statusFilterComboBox;
  private JComboBox<String> importanceFilterComboBox;
  private JCheckBox assignedProjects;
  private JCheckBox supervisedProjects;

  public TeamProjectsPanel(Dimension parentFrameDimension) {
    this.setPreferredSize(parentFrameDimension);
    this.setLayout(new BorderLayout());
    initProjectsPane(); // todo get form controller
  }

  private void initProjectsPane() {
    initProjectsHeader();
    initProjectsList();
  }

  private void initComponents() {
    String[] status = {"TO DO","IN PROGRESS","MARKED AS DONE","FINISHED"}; // todo get this from controller
    statusFilterComboBox = new JComboBox<>(status);
    String[] importance = {"LOW","HIGH","MEDIUM"};
    importanceFilterComboBox = new JComboBox<>(importance);
    assignedProjects = new JCheckBox();
    supervisedProjects = new JCheckBox();
  }
  private void initProjectsHeader() {
    // todo add filtering options here
    initComponents();
    JPanel header = new JPanel();
    GroupLayout headerLayout = new GroupLayout(header);
    headerLayout.setAutoCreateGaps(true);
    headerLayout.setAutoCreateContainerGaps(true);
    header.setLayout(headerLayout);

    JLabel filterLabel = new JLabel("Filter projects by:");
    JLabel statusFilterLabel = new JLabel("Status:");
    JLabel importanceFilterLabel = new JLabel("Importance:");
    JLabel assignedProjectsLabel = new JLabel("Assigned to me:");
    JLabel supervisedProjectsLabel = new JLabel("Supervised by me:");

    headerLayout.setHorizontalGroup(headerLayout.createSequentialGroup()
            .addGroup(headerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(filterLabel)
                    .addGroup(headerLayout.createSequentialGroup()
                          .addComponent(statusFilterLabel)
                          .addComponent(statusFilterComboBox))
                    .addGroup(headerLayout.createSequentialGroup()
                            .addComponent(assignedProjectsLabel)
                            .addComponent(assignedProjects)))
            .addGroup(headerLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(headerLayout.createSequentialGroup()
                            .addComponent(importanceFilterLabel)
                            .addComponent(importanceFilterComboBox))
                    .addGroup(headerLayout.createSequentialGroup()
                          .addComponent(supervisedProjectsLabel)
                          .addComponent(supervisedProjects))));

    headerLayout.setVerticalGroup(headerLayout.createSequentialGroup()
            .addComponent(filterLabel)
            .addGroup(headerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(statusFilterLabel)
                    .addComponent(statusFilterComboBox)
                    .addComponent(importanceFilterLabel)
                    .addComponent(importanceFilterComboBox))
            .addGroup(headerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(assignedProjectsLabel)
                            .addComponent(assignedProjects)
                            .addComponent(supervisedProjectsLabel)
                            .addComponent(supervisedProjects)));

    this.add(header,BorderLayout.NORTH);
  }

  private void initProjectsList() {
    // todo get data from controller

    String[] columnNames = {"Name", "Deadline", "Status", "Importance"};
    int noColumn = columnNames.length;
    int noProjects = 20;
    String[][] projectsData = new String[noProjects][noColumn];
    for (int i = 0; i < noProjects; i++) {
      String status = (i%2 == 0) ? "TO DO":"IN PROGRESS";
      projectsData[i] = new String[] {"Name" + i, "2020-12-" + i % 30,status , "LOW"};
    }
    JTable projectsTable =
        new JTable(projectsData, columnNames) {
          public boolean editCellAt(int row, int column, java.util.EventObject e) {
            return false;
          }
        };
    projectsTable.setFillsViewportHeight(true);
    projectsTable.setCellSelectionEnabled(true);
    projectsTable.getTableHeader().setReorderingAllowed(false); // doesnt allow rearranging the columns

    TableRowSorter<TableModel> sorter = new TableRowSorter<>(projectsTable.getModel());
    sorter.setComparator(1, new DateComparator());
    projectsTable.setRowSorter(sorter);

    RowFilter<Object,Object> statusFilter = new RowFilter<>() {
      public boolean include(Entry<?, ?> entry) {
        return entry.getStringValue(2).equals("TO DO");
      }
    };
    sorter.setRowFilter(statusFilter);
    JScrollPane scrollPane = new JScrollPane(projectsTable);
    scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Projects List", TitledBorder.CENTER,
            TitledBorder.TOP));
    this.add(scrollPane, BorderLayout.CENTER);
  }

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
