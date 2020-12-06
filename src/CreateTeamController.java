import java.awt.event.WindowEvent;

public class CreateTeamController {
    CreateTeamFrame frame;

    CreateTeamController(CreateTeamFrame frame) {
        this.frame = frame;
    }

    public void createTeam(String teamName) {
        // todo: pass data to modell, check if request is valid, handle exceptions
        System.out.println("Create team with name: " + teamName);
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }
}
