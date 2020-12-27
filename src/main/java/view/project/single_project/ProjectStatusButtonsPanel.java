package view.project.single_project;

import controller.project.single_project.ProjectStatusController;
import model.project.Project;
import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class ProjectStatusButtonsPanel extends JPanel implements ActionListener {
    // Status buttons.
    private JButton markProgressButton = UIFactory.createButton("Mark progress");; //cell 1
    private JButton setBackToToDoButtton = UIFactory.createButton("Set back to 'To Do'");
    //cell 1
    private JButton turnInButton = UIFactory.createButton("Turn In"); //cell 2
    private JButton undoTurnInButton = UIFactory.createButton("Undo turn-in"); //cell 2
    private JButton reviewButton = UIFactory.createButton("Review"); //cell 3

    private ProjectStatusController controller;

    public enum Cell1ButtonType {
        MARK_PROGRESS,
        SET_BACK_TO_TO_DO,
        NONE
    }

    private Optional<JButton> getCell1Button(Cell1ButtonType buttonType) {
        switch (buttonType) {
            case MARK_PROGRESS: return Optional.of(markProgressButton);
            case SET_BACK_TO_TO_DO: return Optional.of(setBackToToDoButtton);
            case NONE: return Optional.empty();
        }
        return Optional.empty();
    }

    public enum Cell2ButtonType {
        TURN_IN,
        UNDO_TURN_IN,
        NONE
    }

    private Optional<JButton> getCell2Button(Cell2ButtonType buttonType) {
        switch (buttonType) {
            case TURN_IN: return Optional.of(turnInButton);
            case UNDO_TURN_IN: return Optional.of(undoTurnInButton);
            case NONE: return Optional.empty();
        }
        return Optional.empty();
    }

    public enum Cell3ButtonType {
        REVIEW,
        NONE
    }

    private Optional<JButton> getCell3Button(Cell3ButtonType buttonType) {
        switch (buttonType) {
            case REVIEW: return Optional.of(reviewButton);
            case NONE: return Optional.empty();
        }
        return Optional.empty();
    }

    ProjectStatusButtonsPanel(JFrame frame, Project project) {
        controller = new ProjectStatusController(frame, project, this);
        initButtons();
    }

    public void updateButtons() {
        this.removeAll();
        Optional<JButton> cell1Button = getCell1Button(controller.getCell1ButtonType());
        cell1Button.ifPresent(this::add);
        Optional<JButton> cell2Button = getCell2Button(controller.getCell2ButtonType());
        cell2Button.ifPresent(this::add);
        Optional<JButton> cell3Button = getCell3Button(controller.getCell3ButtonType());
        cell3Button.ifPresent(this::add);
        this.revalidate();
        this.repaint();
    }

    private void initButtons() {
        markProgressButton.addActionListener(this);
        setBackToToDoButtton.addActionListener(this);
        undoTurnInButton.addActionListener(this);
        turnInButton.addActionListener(this);
        reviewButton.addActionListener(this);
        updateButtons();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("Button clicked");
        if (actionEvent.getSource() == markProgressButton) {
            controller.markProgress();
        } else if (actionEvent.getSource() == setBackToToDoButtton) {
            controller.setBackToToDo();
        } else if (actionEvent.getSource() == turnInButton) {
            controller.turnIn();
        } else if (actionEvent.getSource() == undoTurnInButton) {
            controller.undoTurnIn();
        } else if (actionEvent.getSource() == reviewButton) {
            controller.review();
        }
    }
}
