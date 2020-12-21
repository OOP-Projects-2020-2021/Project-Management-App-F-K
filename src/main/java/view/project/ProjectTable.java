package view.project;

import model.project.Project;
import view.UIFactory;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Creates a table containing to display the projects.
 *
 * @author Beata Keresztes
 */
public class ProjectTable extends JTable {

    private DefaultTableModel tableModel;
    private static final String[] columnNames = {"Name", "Deadline", "Status"};

    public ProjectTable() {
        setFillsViewportHeight(true);
        setCellSelectionEnabled(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setFont(UIFactory.NORMAL_TEXT_FONT);
        getTableHeader().setFont(UIFactory.HIGHLIGHT_TEXT_FONT);
        setRowHeight(30);
        // the columns cannot be rearranged
        getTableHeader().setReorderingAllowed(false);
        addMouseListener(new TableMouseListener());
        // initialize the model
        initTableModel();
        // the projects can be sorted by deadlines when clicking on the column's header
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        sorter.setComparator(1, new DateComparator());
        setRowSorter(sorter);
    }

    private void initTableModel() {
        tableModel = (DefaultTableModel) this.getModel();
        tableModel.setColumnIdentifiers(columnNames);
    }

    public void clearTableModel() {
        while (tableModel.getRowCount() != 0) {
            tableModel.removeRow(0);
        }
    }

    public void fillTableModel(List<Project> projectsList) {
        for (Project project : projectsList) {
                tableModel.addRow(
                        new String[] {
                                project.getTitle(),
                                String.valueOf(project.getDeadline()),
                                String.valueOf(project.getStatus())
                        });
        }
    }

    /** Compare Strings which represent dates. */
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
    // the data in the tables cannot be edited only viewed
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    class TableMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = getSelectedRow();
            int column = getSelectedColumn();
            if (column == 0 && evt.getClickCount() > 1) {
                // on double click
                String projectName = (String) tableModel.getValueAt(row, column);
                // todo open the project frame based on the name
                JOptionPane.showMessageDialog(
                        null, "you selected " + projectName, "title", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
