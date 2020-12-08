import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

class UserFrame extends JFrame {

  final String FRAME_TITLE;
  final int FRAME_WIDTH;
  final int FRAME_HEIGHT;
  final int SIDE_PADDING;
  final int TOP_PADDING;
  final Dimension FRAME_DIMENSION;
  final Border CENTER_ALIGNMENT_PADDING;
  static final Font TEXT_FONT = new Font("Courier", Font.BOLD, 15);
  static final Dimension TEXT_FIELD_DIMENSION = new Dimension(30, 10);
  static final Dimension LABEL_DIMENSION = new Dimension(30, 10);
  static final Border BUTTON_BORDER = BorderFactory.createEmptyBorder(10, 20, 10, 20);

  public UserFrame(String frameTitle, int frameWidth, int frameHeight) {
    FRAME_TITLE = frameTitle;
    FRAME_WIDTH = frameWidth;
    FRAME_HEIGHT = frameHeight;
    FRAME_DIMENSION = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
    TOP_PADDING = FRAME_HEIGHT / 10;
    SIDE_PADDING = FRAME_WIDTH / 20;
    CENTER_ALIGNMENT_PADDING =
        BorderFactory.createEmptyBorder(TOP_PADDING, SIDE_PADDING, TOP_PADDING, SIDE_PADDING);
    this.setTitle(frameTitle);
    this.setSize(FRAME_DIMENSION);
    this.setResizable(false);
    this.setVisible(true);
  }

  public JLabel createLabel(String text) {
    JLabel label = new JLabel();
    label.setText(text);
    label.setPreferredSize(LABEL_DIMENSION);
    label.setFont(TEXT_FONT);
    return label;
  }

  public JLabel createErrorLabel(String text) {
    JLabel label = new JLabel();
    label.setText(text);
    label.setFont(TEXT_FONT);
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
