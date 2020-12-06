import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.AncestorListener;
import java.awt.*;

public class TeamLabel extends JLabel {

    public TeamLabel(TeamViewModel teamData) {
        super(String.format( "<html>" +
                            "<div style=\"text-align:center;width:125px\"" +
                                "<span style=\"font-size:48px\">%c</span>" +
                                "<br>" +
                                "<span style=\"font-size:18px\">%s</span>" +
                                "<br>" +
                                "<span style=\"font-size:10px\">%s</span>" +
                            "</div>" +
                        "</html>",
                teamData.getName().charAt(0), teamData.getName(), teamData.getManagerName()));
        this.setPreferredSize(new Dimension(160, 160));
        this.setBackground(Color.RED);
        this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }
}