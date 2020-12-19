package model.project.queryconstants;

import model.project.Project;

/**
 * An instance of QueryProjectStatus is used to specify the status of the projects that should be
 * retrieved from the database.
 */
public enum QueryProjectStatus {
    ALL(null) {
        @Override
        public
        Project.ProjectStatus getCorrespondingStatus() {
            throw new IllegalStateException("All QueryProjectStatus is not bound to a single " +
                    "status");
        }
    },
    TO_DO(Project.ProjectStatus.TO_DO),
    IN_PROGRESS(Project.ProjectStatus.IN_PROGRESS),
    MARKED_AS_DONE(Project.ProjectStatus.MARKED_AS_DONE),
    FINISHED(Project.ProjectStatus.FINISHED);

    private final Project.ProjectStatus correspondingStatus;

    QueryProjectStatus(Project.ProjectStatus correspondingStatus) {
        this.correspondingStatus = correspondingStatus;
    }

    public Project.ProjectStatus getCorrespondingStatus() {
        return correspondingStatus;
    }
}