import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoinTeamFrame extends JFrame implements ActionListener {
    JLabel teamCodeLabel;
    JTextField teamCodeTextField;
    JButton submitBtn;

    JoinTeamFrame() {
        teamCodeLabel = new JLabel("Enter the team code:");
        teamCodeTextField = new JTextField();
        teamCodeTextField.setPreferredSize(new Dimension(80, 20));

        JPanel dataPanel = new JPanel(new GridLayout(1, 2));
        dataPanel.add(teamCodeLabel);
        dataPanel.add(teamCodeTextField);
        dataPanel.setPreferredSize(new Dimension(240, 50));

        submitBtn = new JButton("Submit");
        submitBtn.addActionListener(this);

        JPanel contentPannel = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        dataPanel.setBorder(padding);
        this.setContentPane(contentPannel);

        this.setTitle("Join a new team");
        this.setLayout(new BorderLayout(0, 10));
        this.setSize(new Dimension(320, 120));
        this.add(dataPanel, BorderLayout.CENTER);
        this.add(submitBtn, BorderLayout.SOUTH);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == submitBtn) {
            // todo: join new team
        }
    }
}
