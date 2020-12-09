import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class UIFactory {
  public static final double SIDE_PADDING_WIDTH_RATIO = 0.1;
  public static final double TOP_PADDING_HEIGHT_RATIO = 0.05;

  public static final Border BUTTON_BORDER = BorderFactory.createEmptyBorder(10, 20, 10, 20);

  public static final Font NORMAL_TEXT_FONT = new Font("Courier", Font.PLAIN, 15);
  public static final Font HIGHLIGHT_TEXT_FONT = new Font("Courier", Font.BOLD, 15);

  // todo remove: texfields size should be controlled by layout manager
  public static final Dimension TEXT_FIELD_DIMENSION = new Dimension(100, 50);
  public static final Dimension PASSWORD_FIELD_DIMENSION = new Dimension(100, 50);
  public static final Dimension LABEL_DIMENSION = new Dimension(100, 50);

  public static JLabel createLabel(String text, @Nullable Dimension dimension) {
    JLabel label = new JLabel();
    label.setText(text);
    label.setSize(Objects.requireNonNullElse(LABEL_DIMENSION, LABEL_DIMENSION));
    label.setFont(NORMAL_TEXT_FONT);
    label.setHorizontalAlignment(SwingConstants.RIGHT);
    label.setBorder(new EmptyBorder(0,0,0,10));//top,left,bottom,right
    return label;
  }

  public static JLabel createErrorLabel(String text, @Nullable Dimension dimension) {
    JLabel label = new JLabel();
    label.setText(text);
    label.setFont(NORMAL_TEXT_FONT);
    label.setForeground(Color.RED);
    label.setPreferredSize(Objects.requireNonNullElse(dimension, LABEL_DIMENSION));
    return label;
  }

    public static JTextField createTextField(@Nullable String text, @Nullable Dimension dimension) {
        JTextField textField = new JTextField();
        if (text != null) {
            textField.setText(text);
        }
        // textField.setSize(Objects.requireNonNullElse(dimension, TEXT_FIELD_DIMENSION));
        textField.setFont(NORMAL_TEXT_FONT);
        textField.setColumns(100);
        textField.setEditable(true);
        return textField;
    }

    public static JPasswordField createPasswordField(@Nullable Dimension dimension) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(Objects.requireNonNullElse(dimension, PASSWORD_FIELD_DIMENSION));
        passwordField.setFont(NORMAL_TEXT_FONT);
        passwordField.setEditable(true);
        return passwordField;
    }

    public static JButton createButton(String text) {
        JButton button = new JButton();
        button.setText(text);
        button.setFocusable(false);
        return button;
    }

    public static Border createCenterAlignmentPadding(JFrame frame) {
        int top_padding = getTopPadding(frame);
        int side_padding = getSidePadding(frame);
        return BorderFactory.createEmptyBorder(top_padding, side_padding, top_padding, side_padding);
    }

    public static int getTopPadding(Frame frame) {
      return (int) (frame.getHeight() * UIFactory.TOP_PADDING_HEIGHT_RATIO);
    }

    public static int getSidePadding(Frame frame) {
        return (int) (frame.getWidth() * UIFactory.SIDE_PADDING_WIDTH_RATIO);
    }
}
