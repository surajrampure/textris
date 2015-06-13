/*

GameFrame.java
Suraj Rampure

Contains GamePanel, which holds all of the game logic and drawing.

*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameFrame extends JFrame {

    private int app_width = 1024, app_height = 768;
    private GamePane gamePane;

    public GameFrame (Point p, String mode) {
        super("Textris");
        setSize (app_width, app_height + 22);
        setLocation(p.x, p.y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gamePane = new GamePane(this, mode);
        add(gamePane);

        setVisible(true);
    }

}