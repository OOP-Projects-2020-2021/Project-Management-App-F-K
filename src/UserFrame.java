import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

class UserFrame extends JFrame {

  int FRAME_WIDTH;
  int FRAME_HEIGHT;
  int SIDE_PADDING;
  int TOP_PADDING;
  int GAP_SIZE;
  Dimension FRAME_DIMENSION;
  Font TEXT_FONT;
  Dimension TEXT_FIELD_DIMENSION;
  Border BUTTON_BORDER;
  Border CENTER_ALIGNMENT_PADDING;

  public UserFrame(int frameWidth, int frameHeight, int gapSize) {
    FRAME_WIDTH = frameWidth;
    FRAME_HEIGHT = frameHeight;
    TOP_PADDING = FRAME_HEIGHT / 10;
    SIDE_PADDING = FRAME_WIDTH / 20;
    GAP_SIZE = gapSize;
    FRAME_DIMENSION = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
    TEXT_FONT = new Font("Courier", Font.BOLD, 15);
    TEXT_FIELD_DIMENSION = new Dimension(30, 10);
    BUTTON_BORDER = BorderFactory.createEmptyBorder(10, 20, 10, 20);
    CENTER_ALIGNMENT_PADDING =
        BorderFactory.createEmptyBorder(TOP_PADDING, SIDE_PADDING, TOP_PADDING, SIDE_PADDING);
  }

  public void setPadding(int topPadding, int sidePadding) {
    TOP_PADDING = topPadding;
    SIDE_PADDING = sidePadding;
  }

  public JLabel createLabel(String text) {
    JLabel label = new JLabel();
    label.setText(text);
    label.setPreferredSize(TEXT_FIELD_DIMENSION);
    label.setFont(TEXT_FONT);
    return label;
  }

  public JLabel createErrorLabel(String text) {
    JLabel label = createLabel(text);
    label.setForeground(Color.RED);
    return label;
  }

  public JTextField createTextField(String text) {
    JTextField textField = new JTextField();
    textField.setText(text);
    textField.setPreferredSize(TEXT_FIELD_DIMENSION);
    textField.setFont(TEXT_FONT);
    textField.setEditable(true);
    return textField;
  }

  public JPasswordField createPasswordField() {
    JPasswordField passwordField = new JPasswordField();
    passwordField.setPreferredSize(TEXT_FIELD_DIMENSION);
    passwordField.setFont(TEXT_FONT);
    passwordField.setEditable(true);
    return passwordField;
  }

  public JButton createButton(String text) {
    JButton button = new JButton();
    button.setText(text);
    button.setBorder(BUTTON_BORDER);
    button.setAlignmentX(CENTER_ALIGNMENT);
    button.setFocusable(false);
    return button;
  }
}
