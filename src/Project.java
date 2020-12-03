import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Represents a Project and holds its data.
 *
 * @author Bori Fazakas
 */
public class Project {
  public enum ProjectStatus {
    TO_DO,
    IN_PROGRESS,
    MARKED_AS_DONE,
    FINISHED
  }

    private String title;
    private @Nullable String description; /** Optional. */
    private @Nullable User assignee; /** Optional. The person who is responsible for the project. */
    /** Optional. The person who checks whether the project is properly finished.*/
    private @Nullable User supervisor;
    /** The list of users who contributed in any way to the project. */
    private List<User> contributors;
    private ProjectStatus status;

    private Project(Builder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.assignee = builder.assignee;
        this.supervisor = builder.supervisor;
        this.contributors = new ArrayList<>();
        this.status = ProjectStatus.TO_DO;
    }

    /**
     * Marks the given user as a contributor, and also sets the project's status to IN_PROGRESS.
     * The IN_PROGRESS status should never be manually set.
     */
    public void addContributor(User contributor) {
        if (this.status == ProjectStatus.TO_DO) {
            this.status = ProjectStatus.IN_PROGRESS;
        }
        this.contributors.add(contributor);
    }

    public void removeContributor(User user) {
        this.contributors.remove(user);
    }

    public boolean isContributor(User user) {
        return contributors.contains(user);
    }

    public List<User> getContributors() {
        return Collections.unmodifiableList(contributors);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public Optional<User> getAssignee() {
        return Optional.ofNullable(assignee);
    }

    public void setAssignee(@Nullable User assignee) {
        this.assignee = assignee;
    }

    public Optional<User> getSupervisor() {
        return Optional.ofNullable(supervisor);
    }

    public void setSupervisor(@Nullable User supervisor) {
        this.supervisor = supervisor;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public static final class Builder {
        private String title;
        private @Nullable String description;
        private @Nullable User assignee;
        private @Nullable User supervisor;

        public Builder(String title) {
            this.title = Objects.requireNonNull(title);
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setAssignee(User assignee) {
            this.assignee = assignee;
            return this;
        }

        public Builder setSupervisor(User supervisor) {
            this.supervisor = supervisor;
            return this;
        }

        public Project build() {
            return new Project(this);
        }
    }
}
