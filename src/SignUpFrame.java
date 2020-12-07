import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class SignUpFrame extends UserFrame {

  private JLabel usernameLabel;
  private JLabel passwordLabel;
  private JTextField usernameTextField;
  private JPasswordField passwordField;
  private JButton signUpButton;

  private SignUpController signUpController;

  public SignUpFrame() {

    super(400,300,40);

    this.signUpController = new SignUpController(this);

    this.setTitle("Sign up");
    this.setSize(FRAME_DIMENSION);
    this.setLayout(new BorderLayout());

    JPanel mainPanel = new JPanel();
    mainPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT / 2));

    mainPanel.setBorder(CENTER_ALIGNMENT_PADDING);

    GridLayout mainPanelLayout = new GridLayout();
    mainPanelLayout.setColumns(1);
    mainPanelLayout.setRows(2);
    mainPanelLayout.setVgap(GAP_SIZE);
    mainPanel.setLayout(mainPanelLayout);

    usernameLabel = createLabel("Username:");
    usernameLabel.setLabelFor(usernameTextField);

    usernameTextField = createTextField(null);

    passwordLabel = createLabel("Password:");
    passwordLabel.setLabelFor(passwordField);

    passwordField = createPasswordField();

    mainPanel.add(usernameLabel);
    mainPanel.add(usernameTextField);
    mainPanel.add(passwordLabel);
    mainPanel.add(passwordField);

    this.add(mainPanel, BorderLayout.NORTH);

    signUpButton = createButton("Sign up");
    signUpButton.setAlignmentX(CENTER_ALIGNMENT);

    signUpButton.addActionListener(e -> {
      String username = usernameTextField.getName();
      String password = Arrays.toString(passwordField.getPassword());
      signUpController.signUp(username,password);
    });

    JPanel signUpButtonPanel = new JPanel();
    signUpButtonPanel.add(signUpButton);

    this.add(signUpButtonPanel, BorderLayout.CENTER);

    this.pack();
    this.setResizable(false);
    this.setVisible(true);
    // todo!! solve this
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public static void main(String[] args) {
    new SignUpFrame();
  }
}
