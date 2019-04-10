import java.awt.*;  
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.awt.Color;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
class JLabelCleaner {

    private JLabel label;
    private int waitSeconds;

    public JLabelCleaner(int waitSeconds, JLabel label) {
        this.label = label;
        this.waitSeconds = waitSeconds;
    }

    public void startCountdownFromNow() {
        Timer timer = new Timer(waitSeconds * 1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("");
            }
        });
        timer.start();
    }
}