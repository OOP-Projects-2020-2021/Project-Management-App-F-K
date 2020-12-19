package model.project.repository.exceptions;


public class InexistentProjectException extends Exception {
    public InexistentProjectException(int projectId) {
        super(
                "This operation is illegal because the requested project with id "
                        + projectId
                        + " does "
                        + "not exist");
    }
}
