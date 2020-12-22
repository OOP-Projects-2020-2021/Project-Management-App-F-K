package controller.project;

import model.InexistentDatabaseEntityException;
import view.ErrorDialogFactory;
import view.project.ProjectListModel;
import view.project.ProjectTable;
import view.project.single_project.ProjectFrame;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ProjectTableController implements PropertyChangeListener {

    private ProjectListModel projectListModel;
    private ProjectTable projectTable;

    public ProjectTableController(ProjectTable projectTable) {
        this.projectListModel = ProjectListModel.getInstance();
        projectListModel.addPropertyChangeListener(this);
        this.projectTable = projectTable;
    }

    public void initializeTableModel() {
        projectTable.fillTableModel(projectListModel.getProjectList());
    }

    private void updateTableModel() {
        projectTable.clearTableModel();
        if (!projectListModel.isEmptyProjectList()) {
            projectTable.fillTableModel(projectListModel.getProjectList());
        }
    }

    public void openProject(JFrame frame,String title,int rowNr) {
        try {
            int projectId = projectListModel.getProjectList().get(rowNr).getId();
            if(projectId != -1) {
                new ProjectFrame(frame, projectId, title);
                frame.setEnabled(false);
                frame.setVisible(false);
            }
        }catch(InexistentDatabaseEntityException e) {
            ErrorDialogFactory.createErrorDialog(e,null,"The project with title \"" + title + "\" was not found.");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(ProjectListModel.PROJECT_LIST)) {
            updateTableModel();
        }
    }
}
