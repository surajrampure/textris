/*

IntroFrame.java
Suraj Rampure

Frame that contains the IntroPane, which draws and controls the intro screen.

*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class IntroFrame extends JFrame {

    private int app_width = 1024, app_height = 768;
    IntroPane pane;

    public IntroFrame (Point p) {
        super("Textris");
        setSize (app_width, app_height + 22);
        setLocation(p.x, p.y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        pane = new IntroPane(this);
        add(pane);

        setVisible(true);
    }

}