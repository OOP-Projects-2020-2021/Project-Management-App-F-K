import javax.swing.*;
import java.awt.event.WindowEvent;

public class SignInController{
    JFrame signInFrame;

    public SignInController(JFrame signInFrame) {
        this.signInFrame = signInFrame;
    }
    public void signIn(String username, String password) {
        // TODO!! validate user credentials: if correct close frame, otherwise leave frame (let user try again)
        closeFrame();
    }
    public void closeFrame() {
        signInFrame.dispatchEvent(new WindowEvent(signInFrame,WindowEvent.WINDOW_CLOSING));
    }
}
