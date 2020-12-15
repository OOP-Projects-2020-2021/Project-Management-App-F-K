package view.project;
import view.UIFactory;
import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Displays the projects of the user.
 *
 * @author Beata Keresztes
 */
public class TeamHomeFrame extends JFrame {

    private static final Dimension DIMENSION = new Dimension(800, 500);

    JLabel titleLabel;
    JLabel deadlineLabel;
    JLabel statusLabel;
    JLabel importanceLabel;
    JButton openProjectButton;
    /*
    JTextArea descriptionArea;
    JComboBox statusPane; // drop-down list
    JComboBox importancePane;
*/
    public TeamHomeFrame() {
        super("Team Name"); // name of the team todo set it each time
        this.setSize(DIMENSION);
        this.setResizable(false);
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        initComponents();
        addComponents();
        this.addWindowListener(new ProjectWindowAdapter());
    }
    private void initComponents() {
        // todo add project details
        titleLabel = UIFactory.createLabel("Title",null);
        deadlineLabel = UIFactory.createLabel("Deadline",null);
        statusLabel = UIFactory.createLabel("Status",null);
        importanceLabel = UIFactory.createLabel("Importance",null);
        openProjectButton = new BasicArrowButton(BasicArrowButton.EAST);
    }

    private void addComponents() {

        JPanel projectPanel = new JPanel();
        projectPanel.setPreferredSize(new Dimension(800,50));
        GridLayout projectLayout = new GridLayout(1,5);
        projectPanel.setLayout(projectLayout);
        projectPanel.add(titleLabel);
        projectPanel.add(deadlineLabel);
        projectPanel.add(statusLabel);
        projectPanel.add(importanceLabel);
        projectPanel.add(openProjectButton);
        this.add(projectPanel,BorderLayout.CENTER);

        this.pack();
    }

    private class ProjectWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
