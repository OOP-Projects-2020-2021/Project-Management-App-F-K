import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private static final int TEXT_WIDTH = 30;
    private static final int TEXT_HEIGHT = 10;

    public UserSignInFrame() {
        // main Frame
        this.setTitle("Sign in");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.GRAY);

        JPanel mainPanel = new JPanel();    // main panel
        mainPanel.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT/2));

        // set borders for the main panel
        Border centerAlignmentPadding = BorderFactory.createEmptyBorder(DEFAULT_GAP_SIZE,2*DEFAULT_GAP_SIZE,DEFAULT_GAP_SIZE,2*DEFAULT_GAP_SIZE);

        mainPanel.setBorder(centerAlignmentPadding);  // add padding

        GridLayout mainPanelLayout = new GridLayout();
        mainPanelLayout.setColumns(1);
        mainPanelLayout.setRows(2);
        mainPanelLayout.setVgap(DEFAULT_GAP_SIZE*2);
        mainPanel.setLayout(mainPanelLayout);

        // set font and dimension of the labels&text-fields
        Font textFont = new Font("Courier", Font.BOLD,15);
        Dimension textFieldDimension = new Dimension(TEXT_WIDTH,TEXT_HEIGHT);

        usernameLabel = new JLabel();
        usernameLabel.setText("Username:");
        usernameLabel.setPreferredSize(textFieldDimension);
        usernameLabel.setLabelFor(usernameTextField);
        usernameLabel.setFont(textFont);

        usernameTextField = new JTextField();
        usernameTextField.setPreferredSize(textFieldDimension);
        usernameTextField.setEditable(true);
        usernameTextField.setFont(textFont);

        passwordLabel = new JLabel();
        passwordLabel.setText("Password:");
        passwordLabel.setPreferredSize(textFieldDimension);
        passwordLabel.setLabelFor(passwordField);
        passwordLabel.setFont(textFont);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(textFieldDimension);
        passwordField.setEditable(true);
        passwordField.setFont(textFont);

        mainPanel.add(usernameLabel);
        mainPanel.add(usernameTextField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);

        this.add(mainPanel,BorderLayout.NORTH); // add the main panel to the frame

        // set borders for the buttons
        Border buttonBorder = BorderFactory.createEmptyBorder(10,20,10,20);
        // create an action listener for the buttons
        ButtonListener buttonListener = new ButtonListener();

        signInButton = new JButton();
        signInButton.setText("Sign in");
        signInButton.setBackground(Color.GRAY);
        signInButton.setBorder(buttonBorder);
        signInButton.setAlignmentX(CENTER_ALIGNMENT);
        signInButton.setFocusable(false);
        signInButton.addActionListener(buttonListener);

        JPanel signInButtonPanel = new JPanel();
        signInButtonPanel.add(signInButton);    // add sign-in button to a separate panel

        createAccountLabel = new JLabel();
        createAccountLabel.setText("Don't have an account yet?");
        createAccountLabel.setLabelFor(createAccountButton);

        createAccountButton = new JButton();
        createAccountButton.setText("Create account");
        createAccountButton.setBackground(Color.GRAY);
        createAccountButton.setFocusable(false);
        createAccountButton.setBorder(buttonBorder);
        createAccountButton.addActionListener(buttonListener);

        JPanel createAccountPanel = new JPanel(new FlowLayout()); // add create account label and button to a separate panel
        createAccountPanel.add(createAccountLabel);
        createAccountPanel.add(createAccountButton);

        this.add(signInButtonPanel,BorderLayout.CENTER); // add button panel to the frame

        this.add(createAccountPanel,BorderLayout.SOUTH); // add create account option panel to the frame

        this.pack();                // pack components
        this.setResizable(false);   // doesn't allow window resizing
        this.setVisible(true);      // make frame visible
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JButton source = (JButton) actionEvent.getSource();
            if(source == signInButton) {
                JOptionPane.showMessageDialog(signInButton,"You have signed in successfully","Successful log-in",JOptionPane.INFORMATION_MESSAGE);
            }
            else if(source == createAccountButton) {
                System.out.println("create account");
            }
        }

    }
}