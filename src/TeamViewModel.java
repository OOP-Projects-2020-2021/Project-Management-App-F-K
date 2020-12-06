public class TeamViewModel {
  private String name;
  private String code;
  private String managerName;

  public TeamViewModel(String name, String code, String managerName) {
    this.name = name;
    this.code = code;
    this.managerName = managerName;
  }

  public String getName() {
    return name;
  }

  public String getCode() {
    return code;
  }

  public String getManagerName() {
    return managerName;
  }
}
