package controller.team;

import view.team.TeamViewModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TeamListController controls the actions related to TeamListPanel.
 *
 * @author Bori Fazakas
 */
public class TeamListController {
  public List<TeamViewModel> getUsersTeams() {
    // todo: get list of users teams, exract data for ViewModels and then pass to Panel
    // dummy data
    List<TeamViewModel> teams =
        new ArrayList<>(
            Arrays.asList(
                new TeamViewModel("IT Team", "456238", "Andrew Smith"),
                new TeamViewModel("HR Team", "654236", "Maria Spencer"),
                new TeamViewModel("Entire Team", "656325", "Margaret Grant"),
                new TeamViewModel("Football team", "452238", "Greta Williams"),
                new TeamViewModel("Basketball Team", "123236", "Adrian Brown"),
                new TeamViewModel("Street Dance Team", "965525", "Abigail Johnson"),
                new TeamViewModel("Ballet team", "456628", "Bailey Miller"),
                new TeamViewModel("Handball team", "634236", "Casey Lewis"),
                new TeamViewModel("Volleyball team", "614125", "Edmund Robinson"),
                new TeamViewModel("Kayaking Team", "695625", "Howard Young"),
                new TeamViewModel("Rugby Team", "236325", "Katherine Phillips")));
    return teams;
  }
}
