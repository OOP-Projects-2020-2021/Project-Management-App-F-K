package model.project;

/**
 * DuplicateProjectNameException is thrown when someone tries to create a project inside a team
 * where a project with the same name already exists. Remark that the database indexes would not
 * allow saving a second project with the same name.
 *
 * @author Bori Fazakas
 */
public class DuplicateProjectNameException extends Exception {
    public DuplicateProjectNameException(String projectName, String teamName) {
        super("Invalid project name: a project with name " + projectName + " already exists in " +
                "team " + teamName);
    }
}
