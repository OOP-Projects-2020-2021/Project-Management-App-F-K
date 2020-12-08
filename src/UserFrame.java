import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

class MFrame extends JFrame {

  final int SIDE_PADDING;
  final int TOP_PADDING;
  final Border CENTER_ALIGNMENT_PADDING;

  public MFrame(String frameTitle, int frameWidth, int frameHeight) {
    TOP_PADDING = (int) (frameHeight * UIFactory.TOP_PADDING_HEIGHT_RATIO);
    SIDE_PADDING = (int) (frameWidth *UIFactory.SIDE_PADDING_WIDTH_RATIO);
    CENTER_ALIGNMENT_PADDING =
        BorderFactory.createEmptyBorder(TOP_PADDING, SIDE_PADDING, TOP_PADDING, SIDE_PADDING);
    this.setTitle(frameTitle);
    this.setSize(new Dimension(frameWidth, frameHeight));
    this.setResizable(false);
    this.setVisible(true);
  }
}
