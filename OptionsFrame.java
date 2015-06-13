/*

OptionsFrame.java
Suraj Rampure

Contains the OptionsPane, which allows the user to choose
between a few (VERY FEW) options.

*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OptionsFrame extends JFrame {

    private int app_width = 1024, app_height = 768;
    OptionsPane pane;

    public OptionsFrame (Point p) {
        super("Textris");
        setSize (app_width, app_height + 22);
        setLocation(p.x, p.y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        pane = new OptionsPane(this);
        add(pane);

        setVisible(true);
    }

}