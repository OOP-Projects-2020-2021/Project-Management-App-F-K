import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class TeamLabel extends JLabel {

    public TeamLabel(TeamViewModel teamData) {
        super(String.format( "<html><font-size=\"6\"><span style=\"font-family:Arial;" +
                "font-size:13px\">Giraffe " +
                "says :</font></span>Hi there!</html>"));
        this.setPreferredSize(new Dimension(160, 160));
        this.setBackground(Color.RED);
        this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }
}
