package view.project.single_project;

import view.UIFactory;

import javax.swing.*;
import java.awt.*;

public class ProjectStatusButtonsPanel extends JPanel {
    // Status buttons.
    private JButton markProgressButton; //cell 1
    private JButton setBackToToDoButtton; //cell 1
    private JButton turnInButton; //cell 2
    private JButton undoTurnInButton; //cell 2
    private JButton reviewButton; //cell 3

    public enum Cell1Button {
        MARK_PROGRESS,
        SET_BACK_TO_TO_DO,
        NONE
    }

    public enum Cell2Button {
        TURN_IN,
        UNDO_TURN_IN,
        NONE
    }

    public enum Cell3Button {
        REVIEW,
        NONE
    }

    ProjectStatusButtonsPanel() {
        this.setLayout(new GridLayout(3, 1));
        initStatusButtons();
    }

    private void initStatusButtons() {
        markProgressButton = UIFactory.createButton("Mark progress");
        setBackToToDoButtton = UIFactory.createButton("Set back to 'To Do'");
        turnInButton = UIFactory.createButton("Turn In");
        undoTurnInButton = UIFactory.createButton("Undo turn-in");
        reviewButton = UIFactory.createButton("Review");
    }
}
