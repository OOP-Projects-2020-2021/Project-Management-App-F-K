package main.java.model.team;

public class TeamManager {
    private static TeamManager instance = new TeamManager();

    public static TeamManager getInstance() {
        return instance;
    }
}
