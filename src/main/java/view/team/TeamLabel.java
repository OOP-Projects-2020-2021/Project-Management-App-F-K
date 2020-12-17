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

  private static final int THIN_BORDER_THICKNESS = 1;
  private static final int THICKER_BORDER_THICKNESS = 2;
  private static final int VERY_THICK_BORDER_THICKNESS = 3;

  public TeamLabel(JFrame frame, TeamViewModel teamData) {
    super(
        String.format(
            HTML_FORMAT,
            teamData.getName().charAt(0),
            teamData.getName(),
            teamData.getManagerName()));
    this.controller = new TeamLabelController(teamData.getId(), frame);
    this.setPreferredSize(LABEL_DIMENSION);
    this.setThinBorder();
    this.addMouseListener(new TeamLabelMouseListener());
  }

  private void setThinBorder() {
    this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, THIN_BORDER_THICKNESS));
    this.revalidate();
  }

  private void setThickerBorder() {
    this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, THICKER_BORDER_THICKNESS));
    this.revalidate();
  }

  private void setVeryThickBorder() {
    this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, VERY_THICK_BORDER_THICKNESS));
    this.revalidate();
  }

  private class TeamLabelMouseListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
      controller.onLabelClicked();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
      setVeryThickBorder();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
      setThickerBorder();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
      setThickerBorder();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
      setThinBorder();
    }
  }
}
