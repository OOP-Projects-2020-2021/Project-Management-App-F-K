package view.project.single_project;

import controller.project.single_project.ProjectStatusController;
import model.project.Project;
import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;
import java.util.Optional;

public class ProjectStatusButtonsPanel extends JPanel implements ActionListener {
  // Status buttons.
  private JButton markProgressButton = UIFactory.createButton("Mark progress");
  private JButton setBackToToDoButtton = UIFactory.createButton("Set back to 'To Do'");
  private JButton turnInButton = UIFactory.createButton("Turn In");
  private JButton undoTurnInButton = UIFactory.createButton("Undo turn-in");
  private JButton reviewButton = UIFactory.createButton("Review");

  private ProjectStatusController controller;

  public enum ButtonType {
    MARK_PROGRESS,
    SET_BACK_TO_TO_DO,
    TURN_IN,
    UNDO_TURN_IN,
    REVIEW,
  }

  private JButton getButton(ButtonType buttonType) {
    switch (buttonType) {
      case MARK_PROGRESS:
        return markProgressButton;
      case SET_BACK_TO_TO_DO:
        return setBackToToDoButtton;
      case TURN_IN:
        return turnInButton;
      case UNDO_TURN_IN:
        return undoTurnInButton;
      case REVIEW:
        return reviewButton;
    }
    return new JButton();
  }

  ProjectStatusButtonsPanel(JFrame frame, Project project) {
    controller = new ProjectStatusController(frame, project, this);
    initButtons();
  }

  public void updateButtons() {
    this.removeAll();
    EnumSet<ButtonType> neededButtonTypes = controller.getNecessaryButtonTypes();
    for (ButtonType type : neededButtonTypes) {
      this.add(getButton(type));
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
