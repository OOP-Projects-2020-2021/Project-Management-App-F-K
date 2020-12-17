package view.team.single_team;

import view.UIFactory;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class TeamProjectsPanel extends JPanel implements ActionListener {

  // todo add sorting options

  public TeamProjectsPanel(Dimension parentFrameDimension) {
    this.setPreferredSize(parentFrameDimension);
    this.setLayout(new BorderLayout());
    initProjectsPane(); // todo get form controller
  }

  private void initProjectsPane() {
    initProjectsHeader();
    initProjectsList();
  }

  private void initProjectsHeader() {
    JLabel projectsLabel = UIFactory.createLabel("List of projects:", null);
    projectsLabel.setHorizontalAlignment(JLabel.LEFT);
    JPanel headerPanel = new JPanel(new FlowLayout());
    headerPanel.add(projectsLabel);
    this.add(headerPanel, BorderLayout.NORTH);
  }

  private void initProjectsList() {
    // todo get data from controller
    String[] columnNames = {"Name", "Deadline", "Status", "Importance"};
    int noColumn = columnNames.length;
    int noProjects = 20;
    String[][] projectsData = new String[noProjects][noColumn];
    for (int i = 0; i < noProjects; i++) {
      projectsData[i] = new String[] {"Name" + i, "2020-12-" + i % 30, "TO DO", "LOW"};
    }
    JTable projectsTable =
        new JTable(projectsData, columnNames) {
          public boolean editCellAt(int row, int column, java.util.EventObject e) {
            return false;
          }
        };
    JScrollPane scrollPane = new JScrollPane(projectsTable);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    projectsTable.setFillsViewportHeight(true);
    projectsTable.setCellSelectionEnabled(true);
    this.add(scrollPane, BorderLayout.CENTER);

    TableRowSorter<TableModel> sorter = new TableRowSorter<>(projectsTable.getModel());
    sorter.setComparator(1, new DateComparator());
    projectsTable.setRowSorter(sorter);
  }

  class DateComparator implements Comparator<String> {

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
