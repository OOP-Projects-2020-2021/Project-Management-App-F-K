import javax.swing.*;
import java.awt.event.WindowEvent;

public class SignUpController {

    JFrame signUpFrame;

    public SignUpController(JFrame signUpFrame) {
            this.signUpFrame = signUpFrame;
    }
    public void signUp(String username, String password) {
        // TODO!! check if user input has a correct format ex.min_length,alphanumeric_characters
       // closeFrame();
    }

    public void closeFrame() {
        signUpFrame.dispatchEvent(new WindowEvent(signUpFrame,WindowEvent.WINDOW_CLOSING));
    }
}
