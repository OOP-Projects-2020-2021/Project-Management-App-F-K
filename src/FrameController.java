import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * FrameController defines a generic contract for all the controllers of frames.
 *
 * @author Bori Fazakas
 */
public abstract class FrameController {
    JFrame frame;

    public FrameController(JFrame frame) {
        this.frame = frame;
    }

    protected void closeFrame() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}