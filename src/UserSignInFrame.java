import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Represents a view of the sign in page.
 */
public class UserSignInFrame extends JFrame{

    JLabel usernameLabel;
    JLabel passwordLabel;
    JTextField usernameTextField;
    JPasswordField passwordField;
    JButton signInButton;
    JButton createAccountButton;
    JLabel createAccountLabel;

    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 300;
    private static final int DEFAULT_GAP_SIZE = 20;
    private static final int LABEL_WIDTH = 30;
    private static final int LABEL_HEIGHT = 10;
    private static final int TEXT_FIELD_COLUMNS = 30;

    public UserSignInFrame() {
        // main Frame
        this.setTitle("Sign in");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.GRAY);

        JPanel mainPanel = new JPanel();    // main panel
        mainPanel.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT/2));

        Border centerAlignmentPadding = BorderFactory.createEmptyBorder(DEFAULT_GAP_SIZE,2*DEFAULT_GAP_SIZE,DEFAULT_GAP_SIZE,2*DEFAULT_GAP_SIZE);

        mainPanel.setBorder(centerAlignmentPadding);  // add padding

        GridLayout mainPanelLayout = new GridLayout();
        mainPanelLayout.setColumns(2);
        mainPanelLayout.setRows(2);
        mainPanelLayout.setVgap(DEFAULT_GAP_SIZE*2);
        mainPanel.setLayout(mainPanelLayout);


        usernameLabel = new JLabel();
        usernameLabel.setText("Username:");
        usernameLabel.setSize(LABEL_WIDTH,LABEL_HEIGHT);
        usernameLabel.setLabelFor(usernameTextField);

        usernameTextField = new JTextField();
        usernameTextField.setColumns(TEXT_FIELD_COLUMNS);
        usernameTextField.setEditable(true);

        passwordLabel = new JLabel();
        passwordLabel.setText("Password:");
        passwordLabel.setSize(LABEL_WIDTH,LABEL_HEIGHT);
        passwordLabel.setLabelFor(passwordField);

        passwordField = new JPasswordField();
        passwordField.setColumns(TEXT_FIELD_COLUMNS);
        passwordField.setEditable(true);

        mainPanel.add(usernameLabel);
        mainPanel.add(usernameTextField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);

        this.add(mainPanel,BorderLayout.NORTH); // add the main panel to the frame

        Border buttonBorder = BorderFactory.createEmptyBorder(10,20,10,20); // button border

        signInButton = new JButton();
        signInButton.setText("Sign in");
        signInButton.setBackground(Color.GRAY);
        signInButton.setBorder(buttonBorder);
        signInButton.setAlignmentX(CENTER_ALIGNMENT);
        signInButton.setFocusable(false);

        JPanel signInButtonPanel = new JPanel();
        signInButtonPanel.add(signInButton);    // add sign-in button to a separate panel

        createAccountLabel = new JLabel();
        createAccountLabel.setSize(LABEL_WIDTH,LABEL_HEIGHT);
        createAccountLabel.setText("Don't have an account yet?");
        createAccountLabel.setHorizontalAlignment((int)CENTER_ALIGNMENT);
        createAccountLabel.setLabelFor(createAccountButton);

        createAccountButton = new JButton();
        createAccountButton.setText("Create account");
        createAccountButton.setBackground(Color.GRAY);
        createAccountButton.setFocusable(false);
        createAccountButton.setAlignmentX(CENTER_ALIGNMENT);
        createAccountButton.setBorder(buttonBorder);

        // add create account label and button to a separate panel
        JPanel createAccountPanel = new JPanel();
        createAccountPanel.add(createAccountLabel);
        createAccountPanel.add(createAccountButton);

        this.add(signInButtonPanel,BorderLayout.CENTER); // add button panel to the frame

        this.add(createAccountPanel,BorderLayout.SOUTH); // add create account option panel to the frame

        // pack components
        this.pack();
        this.setResizable(false);   // doesn't allow window resizing
        this.setVisible(true);      // make frame visible

    }
    public static void main(String[] args) {
        UserSignInFrame myFrame = new UserSignInFrame();
    }
}
