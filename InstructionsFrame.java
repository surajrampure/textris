/*

InstructionsFrame.java
Suraj Rampure

Shows the user how to use the new text mode.

*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InstructionsFrame extends JFrame implements ActionListener {

    private int app_width = 1024, app_height = 768;
    CustomButton backButton;
    JLabel image;

    public InstructionsFrame (Point p) {
        super("Textris");
        setSize (app_width, app_height + 22);
        setLocation(p.x, p.y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setLayout(null);

        image = new JLabel (new ImageIcon("Images/Instructions Page.png"));
        image.setSize(1024, 768);
        add(image);

        backButton = new CustomButton("Back1");
        backButton.setBounds(20, 60, 124, 53);
        backButton.addActionListener(this);
        add(backButton);

        setVisible(true);
    }

    public void actionPerformed (ActionEvent evt) {
        Object src = evt.getSource();

        // Goes back to the intro screen if clicked
        if (src == backButton) {
            Point p = getLocationOnScreen();
            setVisible(false);
            new IntroFrame(p);
        }
    }

}