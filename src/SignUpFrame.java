import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class SignUpFrame  extends JFrame {

    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton signUpButton;

    private SignUpController signUpController;

    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 300;
    private static final int DEFAULT_GAP_SIZE = 20;
    private static final int TEXT_WIDTH = 30;
    private static final int TEXT_HEIGHT = 10;

    public SignUpFrame() {
        // add controller
        this.signUpController = new SignUpController(this);
        // main Frame
        this.setTitle("Sign up");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        this.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();    // main panel
        mainPanel.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT/2));

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

        Border buttonBorder = BorderFactory.createEmptyBorder(10,20,10,20); // button border
        ButtonListener buttonListener = new ButtonListener();

        signUpButton = new JButton();
        signUpButton.setText("Sign up");
        signUpButton.setBackground(Color.GRAY);
        signUpButton.setBorder(buttonBorder);
        signUpButton.setAlignmentX(CENTER_ALIGNMENT);
        signUpButton.setFocusable(false);
        signUpButton.addActionListener(buttonListener);

        JPanel signUpButtonPanel = new JPanel();
        signUpButtonPanel.add(signUpButton);    // add sign-in button to a separate panel

        this.add(signUpButtonPanel,BorderLayout.CENTER); // add button panel to the frame

        this.pack();                // pack components
        this.setResizable(false);   // doesn't allow window resizing
        this.setVisible(true);      // make frame visible
    }

    // testing method
    public static void main(String[] args) {
        new SignUpFrame();
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JButton source = (JButton) actionEvent.getSource();
            if(source == signUpButton) {
                JOptionPane.showMessageDialog(signUpButton,"You have signed up successfully","Successful sign-up",JOptionPane.INFORMATION_MESSAGE);
                signUpController.signUp(usernameTextField.getName(), Arrays.toString(passwordField.getPassword()));
            }
        }
    }
}

