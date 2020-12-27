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
    private static JButton markProgressButton = UIFactory.createButton("Mark progress");; //cell 1
    private static JButton setBackToToDoButtton = UIFactory.createButton("Set back to 'To Do'");
    //cell 1
    private static JButton turnInButton = UIFactory.createButton("Turn In"); //cell 2
    private static JButton undoTurnInButton = UIFactory.createButton("Undo turn-in"); //cell 2
    private static JButton reviewButton = UIFactory.createButton("Review"); //cell 3

    private ProjectStatusController controller;

    public enum Cell1Button {
        MARK_PROGRESS(markProgressButton),
        SET_BACK_TO_TO_DO(setBackToToDoButtton),
        NONE(null);

        private JButton button;

        Cell1Button(JButton button) {
            this.button = button;
        }

        Optional<JButton> getButton() {
            return Optional.ofNullable(button);
        }
    }

    public enum Cell2Button {
        TURN_IN(turnInButton),
        UNDO_TURN_IN(undoTurnInButton),
        NONE(null);

        private JButton button;

        Cell2Button(JButton button) {
            this.button = button;
        }

        Optional<JButton> getButton() {
            return Optional.ofNullable(button);
        }
    }

    public enum Cell3Button {
        REVIEW(reviewButton),
        NONE(null);

        private JButton button;

        Cell3Button(JButton button) {
            this.button = button;
        }

        Optional<JButton> getButton() {
            return Optional.ofNullable(button);
        }
    }

    ProjectStatusButtonsPanel(JFrame frame, Project project) {
        controller = new ProjectStatusController(frame, project, this);
        initButtons();
    }

    public void updateButtons() {
        this.removeAll();
        Cell1Button cell1Button = controller.getCell1Button();
        if (cell1Button.getButton().isPresent()) {
            this.add(cell1Button.getButton().get());
        }
        Cell2Button cell2Button = controller.getCell2Button();
        if (cell2Button.getButton().isPresent()) {
            this.add(cell2Button.getButton().get());
        }
        Cell3Button cell3Button = controller.getCell3Button();
        if (cell3Button.getButton().isPresent()) {
            this.add(cell3Button.getButton().get());
        }
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

        } else if (actionEvent.getSource() == setBackToToDoButtton) {

        } else if (actionEvent.getSource() == turnInButton) {

        } else if (actionEvent.getSource() == undoTurnInButton) {

        } else if (actionEvent.getSource() == reviewButton) {

        }
    }
}
