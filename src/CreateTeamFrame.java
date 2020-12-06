import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateTeamFrame extends JFrame implements ActionListener {
    JLabel teamNameLabel;
    JTextField teamNameTextField;
    JButton submitBtn;

    CreateTeamFrame() {
        teamNameLabel = new JLabel("Enter the team name:");
        teamNameTextField = new JTextField();
        teamNameTextField.setPreferredSize(new Dimension(140, 20));

        JPanel dataPanel = new JPanel(new GridLayout(1, 2));
        dataPanel.add(teamNameLabel);
        dataPanel.add(teamNameTextField);
        dataPanel.setPreferredSize(new Dimension(300, 50));

        submitBtn = new JButton("Submit");
        submitBtn.addActionListener(this);

        JPanel contentPannel = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        dataPanel.setBorder(padding);
        this.setContentPane(contentPannel);

        this.setTitle("Create a new team");
        this.setLayout(new BorderLayout(0, 0));
        this.setSize(new Dimension(380, 120));
        this.add(dataPanel, BorderLayout.CENTER);
        this.add(submitBtn, BorderLayout.SOUTH);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == submitBtn) {
            // todo: create new team
        }
    }
}
