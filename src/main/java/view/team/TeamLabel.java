package view.team;

import controller.team.TeamLabelController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * TeamLabel is a label representing the most important information of a team, as displayed in the
 * TeamListPanel.
 *
 * @author Bori Fazakas
 */
public class TeamLabel extends JLabel {
  private static final String HTML_FORMAT =
      "<html>"
          + "<div style=\"text-align:center;width:125px\""
          + "<span style=\"font-size:48px\">%c</span>"
          + "<br>"
          + "<span style=\"font-size:18px\">%s</span>"
          + "<br>"
          + "<span style=\"font-size:10px\">%s</span>"
          + "</div>"
          + "</html>";
  private static final Dimension LABEL_DIMENSION = new Dimension(160, 160);

  private TeamLabelController controller;

  public TeamLabel(TeamViewModel teamData) {
    super(
        String.format(
            HTML_FORMAT,
            teamData.getName().charAt(0),
            teamData.getName(),
            teamData.getManagerName()));
    this.controller = new TeamLabelController(teamData.getId());
    this.setPreferredSize(LABEL_DIMENSION);
    this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    this.addMouseListener(new TeamLabelMouseListener());
  }


  private class TeamLabelMouseListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
      controller.onLabelClicked();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
  }
}
