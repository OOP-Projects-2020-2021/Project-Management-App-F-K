package view.project;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeamProjectsPanel extends JPanel implements ActionListener {

    //todo add sorting options

    public TeamProjectsPanel(Dimension parentFrameDimension) {
        this.setPreferredSize(parentFrameDimension);
        this.setLayout(new BorderLayout());
        initProjectsPane();  // todo get form controller
    }
    private void initProjectsPane() {
        initProjectsList();
    }
    private void initProjectsList() {
        String[] columnNames = {"Name","Deadline","Status","Importance"};
        int noColumn = columnNames.length;
        int noProjects = 9;
        String[][] projectsData = new String[noProjects][noColumn];
        for(int i=0;i<noProjects;i++) {
            projectsData[i] = new String[]{"Name" + i,"2020-12-1"+i,"TO DO","LOW"};
        }
        JTable projectsTable = new JTable(projectsData,columnNames){
                public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(projectsTable);
        projectsTable.setFillsViewportHeight(true);
        projectsTable.setCellSelectionEnabled(true);
        this.add(scrollPane);

    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
