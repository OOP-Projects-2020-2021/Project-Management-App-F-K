package view;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * UIFactory provides utility functions for creating generic UI components anywhere in the
 * application, with the predefined attributes.
 *
 * @author Bori Fazakas
 */
public class UIFactory {
  public static final double SIDE_PADDING_WIDTH_RATIO = 0.1;
  public static final double TOP_PADDING_HEIGHT_RATIO = 0.05;

  public static Font NORMAL_TEXT_FONT;
  public static Font MEDIUM_HIGHLIGHT_TEXT_FONT;
  public static Font HIGHLIGHT_TEXT_FONT;

  public static final Dimension LABEL_DIMENSION = new Dimension(100, 50);

  static {
    GraphicsEnvironment ge;
    try {
      ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      InputStream is = UIFactory.class.getResourceAsStream("/arial.ttf");
      Font font = Font.createFont(Font.TRUETYPE_FONT, is);
      ge.registerFont(font);
      NORMAL_TEXT_FONT = font.deriveFont(15f);
      MEDIUM_HIGHLIGHT_TEXT_FONT = font.deriveFont(Font.BOLD, 15);
      HIGHLIGHT_TEXT_FONT = font.deriveFont(Font.BOLD, 18);
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  public static JLabel createLabel(String text, @Nullable Dimension dimension) {
    JLabel label = createGenericLabel(text, dimension);
    label.setFont(NORMAL_TEXT_FONT);
    return label;
  }

  public static JLabel createHighlightedLabel(String text, @Nullable Dimension dimension) {
    JLabel label = createGenericLabel(text, dimension);
    label.setFont(HIGHLIGHT_TEXT_FONT);
    return label;
  }

  public static JLabel createMediumHighlightedLabel(String text, @Nullable Dimension dimension) {
    JLabel label = createGenericLabel(text, dimension);
    label.setFont(MEDIUM_HIGHLIGHT_TEXT_FONT);
    return label;
  }

  private static JLabel createGenericLabel(String text, @Nullable Dimension dimension) {
    JLabel label = new JLabel();
    label.setText(text);
    label.setSize(Objects.requireNonNullElse(dimension, LABEL_DIMENSION));
    label.setHorizontalAlignment(SwingConstants.RIGHT);
    label.setBorder(new EmptyBorder(0, 0, 0, 10)); // top,left,bottom,right
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

  public static JTextField createTextField(@Nullable String text) {
    JTextField textField = new JTextField();
    if (text != null) {
      textField.setText(text);
    }
    textField.setEditable(true);
    textField.setFont(NORMAL_TEXT_FONT);
    return textField;
  }

  public static JPasswordField createPasswordField() {
    JPasswordField passwordField = new JPasswordField();
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
