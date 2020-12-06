import javax.swing.*;
import java.awt.event.WindowEvent;

public abstract class FrameController {
    JFrame frame;

    public FrameController(JFrame frame) {
        this.frame = frame;
    }

    protected void closeFrame() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
