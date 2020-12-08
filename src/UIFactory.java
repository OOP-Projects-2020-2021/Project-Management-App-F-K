import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class UIFactory {
    public static final double SIDE_PADDING_WIDTH_RATIO = 0.1;
    public static final double TOP_PADDING_HEIGHT_RATIO = 0.05;

    public static final Border BUTTON_BORDER = BorderFactory.createEmptyBorder(10, 20, 10, 20);

    public static final Font NORMAL_TEXT_FONT = new Font("Courier", Font.PLAIN, 15);
    public static final Font HIGHLIGHT_TEXT_FONT = new Font("Courier", Font.BOLD, 15);
}
