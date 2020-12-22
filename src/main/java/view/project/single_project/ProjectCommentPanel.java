package view.project.single_project;

import controller.project.single_project.ProjectCommentController;
import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This panel allows the user to view the comments added to the given project and leave their own
 * comments.
 *
 * @author Beata Keresztes
 */
public class ProjectCommentPanel extends JPanel {

  private JTextArea commentTextArea;
  private JButton sendButton;
  private JPanel commentListPanel;
  private ProjectCommentController controller;
  private static final String LEAVE_COMMENT_MESSAGE = "Leave a comment";

  public ProjectCommentPanel(int projectId) {
    controller = new ProjectCommentController(projectId);
    initCommentPanel();
  }

  private JTextArea createUneditableCommentArea(String comment) {
    JTextArea commentArea = new JTextArea(comment);
    commentArea.setLineWrap(true);
    commentArea.setWrapStyleWord(true);
    commentArea.setEditable(false);
    return commentArea;
  }

  private void initCommentList() {
    commentListPanel = new JPanel(new GridLayout(3, 1)); // todo
    for (int i = 0; i < 3; i++) {
      JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      JLabel senderName = UIFactory.createLabel("user" + i, null);
      JLabel sendingDate = UIFactory.createLabel("2020-12-22", null);
      headerPanel.add(senderName);
      headerPanel.add(new JLabel("-"));
      headerPanel.add(sendingDate);

      JTextArea commentArea = createUneditableCommentArea("comment" + i);
      JScrollPane commentScrollPane = new JScrollPane(commentArea);

      JPanel rowPanel = new JPanel(new BorderLayout());
      rowPanel.add(headerPanel, BorderLayout.NORTH);
      rowPanel.add(commentScrollPane, BorderLayout.CENTER);

      commentListPanel.add(rowPanel);
    }
  }

  private void initCommentArea() {
    commentTextArea = createUneditableCommentArea(LEAVE_COMMENT_MESSAGE);
    commentTextArea.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyTyped(KeyEvent e) {
            if (commentTextArea.getText().equals(LEAVE_COMMENT_MESSAGE)) {
              commentTextArea.setText("");
              commentTextArea.setEditable(true);
            } else if (commentTextArea.getText().isEmpty()) {
              commentTextArea.setText(LEAVE_COMMENT_MESSAGE);
              commentTextArea.setEditable(false);
            }
          }
        });
  }

  private void initCommentPanel() {
    GroupLayout commentLayout = new GroupLayout(this);
    commentLayout.setAutoCreateContainerGaps(true);
    commentLayout.setAutoCreateGaps(true);
    this.setLayout(commentLayout);

    JLabel commentsLabel = UIFactory.createLabel("Comments:", null);
    initCommentList();
    initCommentArea();
    JScrollPane commentScrollPane = new JScrollPane(commentTextArea);
    sendButton = UIFactory.createButton("Send");
    sendButton.addActionListener(new ButtonListener());

    commentLayout.setHorizontalGroup(
        commentLayout
            .createSequentialGroup()
            .addGroup(
                commentLayout
                    .createParallelGroup()
                    .addComponent(commentsLabel)
                    .addComponent(commentListPanel)
                    .addGroup(
                        commentLayout
                            .createSequentialGroup()
                            .addComponent(commentScrollPane)
                            .addComponent(sendButton))));
    commentLayout.setVerticalGroup(
        commentLayout
            .createSequentialGroup()
            .addComponent(commentsLabel)
            .addComponent(commentListPanel)
            .addGroup(
                commentLayout
                    .createParallelGroup()
                    .addComponent(commentScrollPane)
                    .addComponent(sendButton)));
  }

  class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      if (actionEvent.getSource() == sendButton) {
        // todo
      }
    }
  }
}
