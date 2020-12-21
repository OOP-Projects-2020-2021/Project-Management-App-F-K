package view.team;
/**
 * TeamViewModel holds the data required for the View (TeamListPanel) to display an icon for a team.
 * It is an immutable class.
 *
 * @author Bori Fazakas
 */
public class TeamViewModel {
  private int teamId;
  private String name;
  private String managerName;

  public TeamViewModel(int teamId, String name, String managerName) {
    this.teamId = teamId;
    this.name = name;
    this.managerName = managerName;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return teamId;
  }

  public String getManagerName() {
    return managerName;
  }
}
