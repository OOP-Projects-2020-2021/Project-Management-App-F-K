import org.jetbrains.annotations.Nullable;

import java.util.List;

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
}
