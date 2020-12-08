import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Objects;
import java.util.Optional;

public class UIFactory {
    public static final double SIDE_PADDING_WIDTH_RATIO = 0.1;
    public static final double TOP_PADDING_HEIGHT_RATIO = 0.05;

    public static final Border BUTTON_BORDER = BorderFactory.createEmptyBorder(10, 20, 10, 20);

    public static final Font NORMAL_TEXT_FONT = new Font("Courier", Font.PLAIN, 15);
    public static final Font HIGHLIGHT_TEXT_FONT = new Font("Courier", Font.BOLD, 15);

    public static final Dimension TEXT_FIELD_DIMENSION = new Dimension(30, 10);
    public static final Dimension PASSWORD_FIELD_DIMENSION = new Dimension(30, 10);
    public static final Dimension LABEL_DIMENSION = new Dimension(30, 10);


    public static JLabel createLabel(String text, @Nullable Dimension dimension) {
        JLabel label = new JLabel();
        label.setText(text);
        label.setPreferredSize(Objects.requireNonNullElse(dimension, LABEL_DIMENSION));
        label.setFont(NORMAL_TEXT_FONT);
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

    public static JTextField createTextField(String text, @Nullable Dimension dimension) {
        JTextField textField = new JTextField();
        textField.setText(text);
        textField.setPreferredSize(Objects.requireNonNullElse(dimension, TEXT_FIELD_DIMENSION));
        textField.setFont(NORMAL_TEXT_FONT);
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

    public static JButton createButton(String text, @Nullable Dimension dimension) {
        JButton button = new JButton();
        button.setText(text);
        button.setBorder(BUTTON_BORDER);
        button.setPreferredSize(Objects.requireNonNullElse(dimension, LABEL_DIMENSION));
        // todo
        // button.setAlignmentX();
        button.setFocusable(false);
        return button;
    }
}