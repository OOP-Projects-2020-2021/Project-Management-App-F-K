import java.awt.event.WindowEvent;

public class JoinTeamController {
    JoinTeamFrame frame;

    JoinTeamController(JoinTeamFrame frame) {
        this.frame = frame;
    }

    public void joinTeam(String teamCode) {
        // todo: pass data to modell, check if request is valid, handle exceptions
        System.out.println("Join team with code: " + teamCode);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
