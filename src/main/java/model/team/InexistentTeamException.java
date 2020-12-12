package main.java.model.team;

public class InexistentTeamException extends Exception {
    InexistentTeamException(String code) {
        super("This operation is illegal because the requested team with code " + code + " does " +
                "not exist");
    }
}
