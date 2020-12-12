package main.java.model.team.repository;

import main.java.model.User;
import main.java.model.team.Team;

public interface TeamRepository {
    void createTeam(Team team);

    void getTeam(String code);

    void deleteTeam(Team team);

    void joinTeam(User user, String teamCode);

    void leaveTeam(User user, Team team);
}
