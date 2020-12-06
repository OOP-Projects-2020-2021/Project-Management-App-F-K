import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamListController {
    public List<TeamViewModel> getUsersTeams() {
        // todo: get list of users teams, exract data for ViewModels and then pass to Panel
        // dummy data
        List<TeamViewModel> teams = new ArrayList<>(
                Arrays.asList(new TeamViewModel("IT Team", "456238", "Andrew Smith"),
                                new TeamViewModel("HR Team", "654236", "Maria Spencer"),
                                new TeamViewModel("Entire Team", "656325", "Margaret Grant"),
                                new TeamViewModel("IT Team2", "456238", "Andrew Smith"),
                                new TeamViewModel("HR Team2", "654236", "Maria Spencer"),
                                new TeamViewModel("Entire Team2", "656325", "Margaret Grant"),
                                new TeamViewModel("IT Team3", "456238", "Andrew Smith"),
                                new TeamViewModel("HR Team3", "654236", "Maria Spencer"),
                                new TeamViewModel("Entire Team3", "656325", "Margaret Grant"))
        );
        return teams;
    }
}
