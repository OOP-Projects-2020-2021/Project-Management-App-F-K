import javax.swing.*;
import java.awt.event.WindowEvent;

public class JoinTeamController extends FrameController{

    public JoinTeamController(JFrame frame) {
        super(frame);
    }

    public void joinTeam(String teamCode) {
        // todo: pass data to modell, check if request is valid, handle exceptions
        System.out.println("Join team with code: " + teamCode);
        closeFrame();
    }
}
