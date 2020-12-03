import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Project {
    public enum ProjectStatus {
        TO_DO,
        IN_PROGRESS,
        MARKED_AS_DONE,
        FINISHED
    }

    private String title;
    private @Nullable String description;
    private @Nullable User assignee;
    private @Nullable User supervisor;
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

    public void addContributor(User contributor) {
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

        public Builder addDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder addAssignee(User assignee) {
            this.assignee = assignee;
            return this;
        }

        public Builder addSupervisor(User supervisor) {
            this.supervisor = supervisor;
            return this;
        }

        public Project build() {
            return new Project(this);
        }
    }
}
