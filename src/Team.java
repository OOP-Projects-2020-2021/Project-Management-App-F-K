import org.jetbrains.annotations.NotNull;
import java.util.Random;
import java.io.Serializable;
import java.util.*;

/**
 * This represents a Team and holds data related to its members, projects and other components.
 *
 * @author Beata Keresztes
 */
public class Team implements Serializable {

    /** The name of the team. */
    private @NotNull String name;
    /** The code that uniquely identifies the team. */
    private int code;
    /** The team manager. */
    private @NotNull User manager;
    /** A set of the members of the team. */
    private HashSet<User> members = new HashSet<>();
    /** A list of the projects belonging to the team. */
    private List<Project> projects = new ArrayList<>();

    public Team(@NotNull String name,@NotNull User manager) {
        this.name = name;
        this.manager = manager;
        this.code = generateCode();
    }

    /**
     * Generate a unique identification code for the team.
     * @return int generated code
     */
    private int generateCode() {
        return (new Random().nextInt());
    }

    /**
     * Add a new member to the team.
     * @param member the member to be added
     */
    public void addMember(User member) {
        this.members.add(member);
    }

    /**
     * Add a new project on the team.
     * @param project the project to be added
     */
    public void addProject(Project project) {
        this.projects.add(project);
    }

    /**
     * Remove a member from the team.
     * @param member the member to be removed
     */
    public void removeMember(User member) {
        this.members.remove(member);
    }

    /**
     * Remove a project from the team.
     * @param project the project to be removed
     */
    public void removeProject(Project project) {
        this.projects.remove(project);
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public @NotNull User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = Objects.requireNonNull(manager);
    }

    public HashSet<User> getMembers() {
        return members;
    }

    public List<Project> getProjects() {
        return Collections.unmodifiableList(projects);
    }
}
