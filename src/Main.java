import java.util.List;

public class Main {
  public static void main(String[] args) {
    // test Project class
    User assignee = new User();
    User supervisor = new User();
    Project project = new Project.Builder("washing the dishes")
                          .addAssignee(assignee)
                          .addSupervisor(supervisor)
                          .build();
  }
}
