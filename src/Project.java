import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Project {
  public enum ProjectStatus {
    TO_DO,
    IN_PROGRESS,
    MARKED_AS_DONE,
    FINISHED
  }

  private String name;
  private @Nullable String description;
  private @Nullable User assignee;
  private @Nullable User supervisor;
  private List<User> contributors;
  private ProjectStatus status;

  private Project(Builder builder) {
    this.name = builder.name;
    this.description = builder.description;
    this.assignee = builder.assignee;
    this.supervisor = builder.supervisor;
    this.contributors = builder.contributors;
    this.status = builder.status;
  }

  public static final class Builder {
    private String name;
    private @Nullable String description;
    private @Nullable User assignee;
    private @Nullable User supervisor;
    private List<User> contributors;
    private ProjectStatus status;

    public Builder(String name) {
      this.name = Objects.requireNonNull(name);
      contributors = new ArrayList<>();
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

    public Builder addContributor(User contributor) {
      this.contributors.add(contributor);
      return this;
    }

    public Builder setStatus(ProjectStatus status) {
      this.status = status;
      return this;
    }

    public Project build() {
      return new Project(this);
    }
  }
}
