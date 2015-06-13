/*

OptionsPane.java
Suraj Rampure

*/

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OptionsPane extends JLayeredPane implements ActionListener {

    Image background,
            musicOn, musicOff,
            ghostOn, ghostOff,
            gridOn, gridOff;
    OptionsFrame optionsFrame;
    JButton musicButton, ghostButton, gridButton;
    CustomButton backButton;
    javax.swing.Timer myTimer;

    boolean music, ghost, grid;

    public OptionsPane (OptionsFrame optionsFrame) {
        this.optionsFrame = optionsFrame;

        // Image loading for the background and the three toggles
        background = new ImageIcon("Images/Options Background.png").getImage();
        musicOn = new ImageIcon("Images/Buttons/Music On.png").getImage();
        musicOff = new ImageIcon("Images/Buttons/Music Off.png").getImage();
        ghostOn = new ImageIcon("Images/Buttons/Ghost Piece On.png").getImage();
        ghostOff = new ImageIcon("Images/Buttons/Ghost Piece Off.png").getImage();
        gridOn = new ImageIcon("Images/Buttons/Grid On.png").getImage();
        gridOff = new ImageIcon("Images/Buttons/Grid Off.png").getImage();

        musicButton = new JButton();
        musicButton.setBounds(75, 228, 162, 44);
        // A method in CustomButton makes any JButton invisible; it'll seem as if the user is clicking the picture
        CustomButton.makeInvisible(musicButton);
        musicButton.addActionListener(this);
        add(musicButton, 0);

        ghostButton = new JButton();
        ghostButton.setBounds(363, 228, 298, 44);
        CustomButton.makeInvisible(ghostButton);
        ghostButton.addActionListener(this);
        add(ghostButton, 0);

        gridButton = new JButton();
        gridButton.setBounds(760, 228, 171, 44);
        CustomButton.makeInvisible(gridButton);
        gridButton.addActionListener(this);
        add(gridButton, 0);

        backButton = new CustomButton("Back1");
        backButton.setBounds(40, 80, 124, 53);
        backButton.addActionListener(this);
        add(backButton, 0);

        music = Textris.data.flags[0];
        ghost = Textris.data.flags[1];
        grid = Textris.data.flags[2];

        myTimer = new javax.swing.Timer(10, this);
        myTimer.start();

        setVisible(true);
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public void actionPerformed (ActionEvent evt) {
        Object src = evt.getSource();

        // Toggles for the music, ghost and grid switches
        if (src == musicButton) {
            if (music) {
                music = false;
            }
            else {
                music = true;
            }
        }

        else if (src == ghostButton) {
            if (ghost) {
                ghost = false;
            }
            else {
                ghost = true;
            }
        }

        else if (src == gridButton) {
            if (grid) {
                grid = false;
            }
            else {
                grid = true;
            }
        }

        // Updates the user option settings (probably should've used a text file, I know)
        else if (src == backButton) {
            boolean [] flags = {music, ghost, grid};
            Textris.data.updateFlags(flags);
            Point p = optionsFrame.getLocationOnScreen();
            optionsFrame.setVisible(false);
            new IntroFrame(p);
        }

        repaint();

    }

    @Override
    public void paintComponent (Graphics g) {
        g.drawImage(background, 0, 0, this);

        // Draws either the ON or OFF state of each option
        if (music) {
            g.drawImage(musicOn, 75, 228, this);
        }
        else {
            g.drawImage(musicOff, 75, 228, this);
        }


        if (ghost) {
            g.drawImage(ghostOn, 363, 228, this);
        }
        else {
            g.drawImage(ghostOff, 363, 228, this);
        }

        if (grid) {
            g.drawImage(gridOn, 760, 228, this);
        }
        else {
            g.drawImage(gridOff, 760, 228, this);
        }


    }

}