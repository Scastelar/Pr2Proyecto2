package insta;

import javax.swing.*;
import java.awt.*;

public class SignupPanel extends JPanel {
    InstaLogin Log;
    public SignupPanel(InstaLogin Log) {
        this.Log=Log;
        setLayout(new BorderLayout());
        InstaSignup frame = new InstaSignup(Log);
        JPanel panel = frame.getJFrame();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.add(panel, BorderLayout.CENTER);
    }
}
