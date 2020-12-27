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

/**
 * Creates a panel containing all the buttons required for changing the status of a project.
 *
 * @author Bori Fazakas
 */
public class ProjectStatusButtonsPanel extends JPanel implements ActionListener {
  // Status buttons.
  // The progress can be marked by anyone if the current status is TO_DO.
  private JButton markProgressButton = UIFactory.createButton("Mark progress");
  // The assignee can set the project back to TO_DO if the current status is IN_PROGRESS.
  private JButton setBackToToDoButtton = UIFactory.createButton("Set back to 'To Do'");
  // The assignee can turn in the project if its status is TO_DO or IN_PROGRESS.
  private JButton turnInButton = UIFactory.createButton("Turn In");
  // The assignee can undo the turn in if the current status is TURNED_IN.
  private JButton undoTurnInButton = UIFactory.createButton("Undo turn-in");
  // The supervisor can review a TURNED_IN project.
  private JButton reviewButton = UIFactory.createButton("Review");

  private ProjectStatusController controller;

  /** The controller specified using the enum the set of necessary buttons. */
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

  /**
   * Adds the necessary buttons specified by the controller.
   */
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
