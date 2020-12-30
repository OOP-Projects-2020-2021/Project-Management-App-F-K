package view.project.single_project;

import controller.project.single_project.ProjectCommentController;
import model.comment.Comment;
import model.project.Project;
import view.UIFactory;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

/**
 * The ProjectCommentPanel allows the user to view the comments added to the given project and leave
 * their own comments. This functionality is only allowed to the members of the team to which the
 * project belongs.
 *
 * @author Beata Keresztes
 */
public class ProjectCommentPanel extends JPanel {

  private JTextArea commentTextArea;
  private JButton sendButton;
  private JScrollPane commentListScrollPanel;
  private JPanel commentListPanel;
  private AdjustmentListener adjustmentListener;

  private ProjectCommentController controller;
  private static final String LEAVE_COMMENT_MESSAGE = "Leave a comment";

  private static final Dimension COMMENT_PANEL_DIMENSION = new Dimension(200, 200);
  private static final Dimension COMMENT_AREA_DIMENSION = new Dimension(80, 80);

  public ProjectCommentPanel(Project project) {
    controller = new ProjectCommentController(this, project);
    initCommentPanel();
  }

  private JTextArea createUneditableCommentArea(String comment) {
    JTextArea commentArea = new JTextArea(comment);
    commentArea.setLineWrap(true);
    commentArea.setWrapStyleWord(true);
    commentArea.setEditable(false);
    return commentArea;
  }

  private void clearCommentList() {
    commentListPanel.removeAll();
  }

  private void addCommentToPanel(Comment comment) {
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel senderName = UIFactory.createLabel(controller.getSenderName(comment), null);
    JLabel sendingDate =
        UIFactory.createLabel(
            comment.getDateTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
            null);
    headerPanel.add(senderName);
    headerPanel.add(new JLabel("-"));
    headerPanel.add(sendingDate);

    JTextArea commentArea = createUneditableCommentArea(comment.getText());
    JScrollPane commentScrollPane = new JScrollPane(commentArea);
    commentScrollPane.setPreferredSize(COMMENT_AREA_DIMENSION);
    commentScrollPane.setWheelScrollingEnabled(true);

    JPanel rowPanel = new JPanel(new BorderLayout());
    rowPanel.add(headerPanel, BorderLayout.NORTH);
    rowPanel.add(commentScrollPane, BorderLayout.CENTER);

    commentListPanel.add(rowPanel);
  }

  private void fillCommentList() {
    List<Comment> commentList = controller.getComments();
    commentListPanel.setLayout(new GridLayout(0, 1));
    if (commentList != null) {
      for (Comment comment : commentList) {
        addCommentToPanel(comment);
      }
    }
  }
  private void initAdjustmentListener() {
    adjustmentListener = e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum());
  }

  private void initCommentList() {
    commentListPanel = new JPanel();
    commentListScrollPanel = new JScrollPane(commentListPanel);
    fillCommentList();
    commentListScrollPanel.setPreferredSize(COMMENT_PANEL_DIMENSION);
    initAdjustmentListener();
    commentListScrollPanel.addMouseWheelListener(e->commentListScrollPanel.getVerticalScrollBar().removeAdjustmentListener(adjustmentListener));
  }

  private void initCommentArea() {
    commentTextArea = createUneditableCommentArea(LEAVE_COMMENT_MESSAGE);
    commentTextArea.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (!commentTextArea.isEditable()) {
              commentTextArea.setText("");
              commentTextArea.setEditable(true);
            }
          }
        });
    clearCommentArea();
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
    sendButton.setMnemonic(KeyEvent.VK_ENTER);
    sendButton.addActionListener(
        e -> {
          if (e.getSource() == sendButton) {
            controller.addComment(commentTextArea.getText());
          }
        });

    commentLayout.setHorizontalGroup(
        commentLayout
            .createSequentialGroup()
            .addGroup(
                commentLayout
                    .createParallelGroup()
                    .addComponent(commentsLabel)
                    .addComponent(commentListScrollPanel)
                    .addGroup(
                        commentLayout
                            .createSequentialGroup()
                            .addComponent(commentScrollPane)
                            .addComponent(sendButton))));
    commentLayout.setVerticalGroup(
        commentLayout
            .createSequentialGroup()
            .addComponent(commentsLabel)
            .addComponent(commentListScrollPanel)
            .addGroup(
                commentLayout
                    .createParallelGroup()
                    .addComponent(commentScrollPane)
                    .addComponent(sendButton)));
  }

  private void clearCommentArea() {
    commentTextArea.setText(LEAVE_COMMENT_MESSAGE);
    commentTextArea.setEditable(false);
  }

  public void updateCommentPanel() {
    clearCommentArea();
    clearCommentList();
    fillCommentList();
    revalidate();
    scrollToBottom();
  }

  private void scrollToBottom() {
    commentListScrollPanel
            .getVerticalScrollBar()
            .addAdjustmentListener(adjustmentListener);
  }
}
