import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Arrays;

/**
 * Using this frame the user can set a new password.
 */

public class ChangePasswordFrame extends UserFrame{

    private JLabel newPasswordLabel;
    private JPasswordField newPasswordField;
    private JLabel newPasswordAgainLabel;
    private JPasswordField newPasswordAgainField;
    private JButton saveButton;

    private ChangePasswordController changePasswordController;

    public ChangePasswordFrame() {

        super(500,300,0);
        setPadding(25,20);

        changePasswordController = new ChangePasswordController(this);

        this.setTitle("Change password");
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT * 3 / 4));
        mainPanel.setBorder(CENTER_ALIGNMENT_PADDING);

        this.setContentPane(mainPanel);

        JPanel dataInputPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        dataInputPanel.setPreferredSize(FRAME_DIMENSION);
        dataInputPanel.setBorder(CENTER_ALIGNMENT_PADDING);

        BorderLayout dialogPanelLayout = new BorderLayout();
        mainPanel.setLayout(dialogPanelLayout);

        newPasswordLabel = createLabel("Enter new password:");
        newPasswordLabel.setLabelFor(newPasswordField);

        newPasswordField = createPasswordField();

        newPasswordAgainLabel = createLabel("Reenter new password:");
        newPasswordAgainLabel.setLabelFor(newPasswordAgainField);

        newPasswordAgainField = createPasswordField();

        dataInputPanel.add(newPasswordLabel);
        dataInputPanel.add(newPasswordField);
        dataInputPanel.add(newPasswordAgainLabel);
        dataInputPanel.add(newPasswordAgainField);

        JPanel saveButtonPanel = new JPanel(new FlowLayout());

        saveButton = createButton("Save");

        saveButton.addActionListener(e -> {
            String newPassword1 = Arrays.toString(newPasswordField.getPassword());
            String newPassword2 = Arrays.toString(newPasswordAgainField.getPassword());
            changePasswordController.changePassword(newPassword1, newPassword2);
            // todo print a message on screen
        });
        saveButtonPanel.add(saveButton);

        mainPanel.add(dataInputPanel, BorderLayout.CENTER);
        mainPanel.add(saveButtonPanel, BorderLayout.SOUTH);

        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new ChangePasswordFrame();
    }

}
